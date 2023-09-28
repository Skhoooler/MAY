package com.sklr.MAY.obj;

import com.sklr.MAY.util.Logger;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketAddress;

/**
 * Holds all data for fulfilling a request to M.A.Y
 */
public class MAYObject {
    // Request
    private final SocketAddress foreignAddress;
    private MAYRequest request;
    private MAYResponse response;


    public MAYObject(SocketAddress foreignAddress) {
        this.foreignAddress = foreignAddress;
        Logger.pop("Connected to " + this.foreignAddress + ".");
    }

    public SocketAddress getForeignAddress() {
        return foreignAddress;
    }

    public MAYRequest getRequest() {
        if (request == null) {
            request = new MAYRequest();
        }
        return request;
    }

    public void parseRequest(InputStream in) {
        this.request = new MAYRequest(in);
    }

    public MAYResponse getResponse() {
        return response;
    }

    public void setResource(String statusCode, String reasonPhrase, MAYResource resource) {
        response = new MAYResponse(response.getProtocol(), statusCode, reasonPhrase, resource);
    }

    public byte[] buildByteResponse() {
        return response.buildResponse();
    }
}
