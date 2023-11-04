package com.sklr.MAY;

import com.sklr.MAY.util.Logger;

/**
 * M.A.Y - Modular API gatewaY
 */
public class Main {
    private static final int DEFAULT_PORT = 8023;

    public static void main(String[] args) {
        Logger.title();

        // Grab port from the command line or use the default one
        int port = 0;
        try {
            port = args[0] != null ? Integer.parseInt(args[0]) : DEFAULT_PORT;


            if (port < 0 || port > 65535) {
                throw new IllegalArgumentException();
            } else if (port > 0 && port < 1024) {
                Logger.warn("Port numbers below 1024 are reserved for special use. Please reconsider using port " + args[0] + ".");
            }


        } catch (NumberFormatException e) {
            Logger.error("Could not parse the user defined port. Please ensure that it is a number.", e);
            System.exit(-1);
        } catch (IllegalArgumentException e) {
            Logger.error("The given port number " + args[0] + " is not a valid port number. Valid port numbers range from 1 to 65535", e);
            System.exit(-1);
        } catch (ArrayIndexOutOfBoundsException e) {
            Logger.warn("No port given. The port will be selected automatically.");
            port = 0;
        }


        new Server().run(port);
    }
}
