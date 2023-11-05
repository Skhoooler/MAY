package com.sklr.MAY;

import com.sklr.MAY.obj.MAYRequest;
import com.sklr.MAY.obj.MAYResponse;
import com.sklr.MAY.util.Formatter;
import com.sklr.MAY.util.Logger;
import com.sklr.MAY.util.PropertyAccess;
import com.sklr.MAY.util.enumerations.Content_Type;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class Server {
    private ServerSocket serverSocket;

    /**
     * Server entryway. This initiates the server
     */
    public void run(int port) {
        try {
            serverSocket = new ServerSocket(port);
            Logger.pop("Listening on port " + serverSocket.getLocalPort() + ".\n");

            APIGateway gateway = new APIGateway();

            while (true) {
                // Block until a connection is made
                Socket server = serverSocket.accept();

                MAYRequest request = new MAYRequest(server.getRemoteSocketAddress());
                request.parseRequest(server.getInputStream());

                MAYResponse response = gateway.fulfillRequest(request);

                respond(server, response);

                disconnect(server);
            }

        } catch (BindException e) {
            e.printStackTrace();
            Logger.warn("The local port selected " + port + " is already in use");
            run(0); // 0 has the system pick its own port.

        } catch (IOException e) {
            Logger.error("Cannot listen on port " + serverSocket.getLocalPort() + ".", e);
        }
    }

    /**
     * Returns a response to the requesting client
     * @param server The connected socket
     * @param response Contains all information required to send a response back to the client.
     * */
    private void respond(Socket server, MAYResponse response) {
        try {
            if (response == null) {
                throw new NullPointerException();
            }

            OutputStream out = server.getOutputStream();
            out.write(response.buildResponse());
            out.flush();

        } catch (NullPointerException e) {
            Logger.error("Response Object was null, sending last-ditch response.", e);
            sendLastDitchResponse(server);

        } catch (IOException e) {
            Logger.error("Something went wrong while responding to the client.", e);
        }
    }

    /**
     * Disconnects a server socket. Any streams (or objects built from those streams) associated with the server socket
     * must not be closed unless done through this method. Closing a stream that comes from the server socket also closes
     * the socket. Closing the server here should close everything all at once.
     * @param server A socket connected to the client
     */
    private void disconnect(Socket server) {
        try {
            server.close();
            Logger.log("Disconnected from " + server.getRemoteSocketAddress() + ".");
        } catch (IOException e) {
            Logger.error("Error disconnecting from address " + server.getRemoteSocketAddress() + ".", e);
        }
    }

    /**
     * A last-ditch effort to send something to the client. This should only trigger under the most dire circumstances.
     * @param server A socket connected to the client
     */
    private void sendLastDitchResponse(Socket server) {
        try {
            String lastDitchHTML = """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <title>500 - Internal Server Error</title>
                    </head>
                    <body>
                        <p1>500 - Internal Server Error.</p1>
                    </body>
                    </html>
                    """;

            String lastDitchResponse =
                    "HTTP/1.1 500 Internal Server Error" +
                    "\nContent-Length:" + lastDitchHTML.length() +
                    "\nContent-Type:" + Content_Type.HTML.toString() +
                    "\nContent-Language:en" +
                    "\nDate:" + Formatter.formatHTTPDateTime(LocalDateTime.now()) +
                    "\nServer:" + PropertyAccess.getInstance().getVersionedName() +
                    "\n\n" + lastDitchHTML;

            OutputStream out = server.getOutputStream();
            out.write(lastDitchResponse.getBytes(StandardCharsets.UTF_8));
            out.flush();
            Logger.log("Successfully sent last-ditch response to " + server.getRemoteSocketAddress() + ".");

        } catch (Exception e) {
            Logger.error("Error sending last ditch response.", e);
        }
    }
}
