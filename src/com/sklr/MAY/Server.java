package com.sklr.MAY;

import com.sklr.MAY.obj.MAYRequest;
import com.sklr.MAY.obj.MAYResponse;
import com.sklr.MAY.util.Logger;

import java.io.*;
import java.net.*;

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
            Logger.error("Cannot listen on port " + serverSocket.getLocalPort() + ".");
            e.printStackTrace();
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
            Logger.error("Response Object was null. Could not respond to request.");
            e.printStackTrace();

        } catch (IOException e) {
            Logger.error("Error responding to the client.");
            e.printStackTrace();
        }
    }

    /**
     * Disconnects a server socket. Any streams (or objects built from those streams) associated with the server socket
     * must not be closed unless done through this method. Closing a stream that comes from the server socket also closes
     * the socket. Closing the server here should close everything all at once.
     * @param server A connected socket
     */
    private void disconnect(Socket server) {
        try {
            server.close();
            Logger.log("Disconnected from " + server.getRemoteSocketAddress() + ".\n");
        } catch (IOException e) {
            Logger.error("Error disconnecting from address " + server.getRemoteSocketAddress() + ".");
        }
    }
}
