package com.sklr.MAY;

import com.sklr.MAY.obj.MAYObject;
import com.sklr.MAY.obj.MAYRequest;
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

                MAYObject MAYObj = new MAYObject(server.getRemoteSocketAddress());

                MAYObj.parseRequest(server.getInputStream());

                gateway.fulfillRequest(MAYObj);

                respond(server, MAYObj);

                disconnect(server, MAYObj);
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
     * @param mayObj An input stream that contains the response to the client
     */
    private void respond(Socket server, MAYObject mayObj) {
        try {
            if (mayObj == null) {
                throw new NullPointerException();
            }

            OutputStream out = server.getOutputStream();
            out.write(mayObj.buildByteResponse());
            out.flush();

        } catch (NullPointerException e) {
            Logger.error("May Object was null. Could not respond to request.");
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
     * @param request The MAYRequest object that holds the foreign IP Address and Port
     */
    private void disconnect(Socket server, MAYObject request) {
        try {
            server.close();
            Logger.log("Disconnected from " + request.getForeignAddress() + ".\n");
        } catch (IOException e) {
            Logger.error("Error disconnecting from address " + request.getForeignAddress() + ".");
        }
    }
}
