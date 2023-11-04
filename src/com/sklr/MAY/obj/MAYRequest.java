package com.sklr.MAY.obj;

import com.sklr.MAY.util.Formatter;
import com.sklr.MAY.util.enumerations.HTTP_Method;
import com.sklr.MAY.util.Logger;
import com.sklr.MAY.util.PropertyAccess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Java bean pertaining to all relevant data in a request to MAY.
 */
public class MAYRequest {
    private final SocketAddress foreignAddress;
    private String protocol;
    private HTTP_Method method;
    private String URI;

    private Map<String, Object> headers;
    private List<String> body;

    /**
     * Parse the incoming InputStream into the rest of the MAYRequest Object.
     * @param foreignAddress The IP Address & Port number of the request, as an InetSocketAddress
     */
    public MAYRequest(SocketAddress foreignAddress) {
        this.foreignAddress = foreignAddress;

        method = HTTP_Method.ERROR;
        URI = "ERROR";
        protocol = "ERROR";

        headers = new HashMap<>();
        body = new ArrayList<>();
    }


    public void parseRequest(InputStream in) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            // Parse the Input Stream
            parseRequestLine(reader.readLine());
            parseHeaders(reader);
            parseBody(reader);

        } catch (IOException e) {
            Logger.error("Invalid HTTP request, could not parse.");
        }
    }

    /**
     * Iterate through the rest of the InputStream lines to gather the body, if there is one.
     * @param reader BufferedReader that already read out the HTTP request line & headers
     */
    private void parseBody(BufferedReader reader) {
        try {
            while (reader.ready()) {
                body.add(reader.readLine());
            }

        } catch(IOException e) {
            Logger.error("Could not parse HTTP body");
        }
    }

    /**
     * Read all of the headers from the request
     * @param reader Buffered Reader that has already read out the HTTP request line
     */
    private void parseHeaders(BufferedReader reader) {
        try {
            if (headers == null) {
                headers = new HashMap<>();
            }

            String header = reader.readLine();
            while (header.length() > 0) {
                mapHeader(header);
                header = reader.readLine();
            }

        } catch (IOException e) {
            Logger.error("Could not parse HTTP headers.");
        }
    }

    /**
     * Splits a line from the HTTP headers into a key-value pair and appends it to the request hashmap
     * @param header The header and its value separated by a colon (:) as a String
     */
    private void mapHeader(String header) {
        try {
            int index = header.indexOf(":");

            if (index == -1) {
                throw new IllegalArgumentException();
            }

            headers.put(header.substring(0, index), header.substring(index + 1));
        } catch (IllegalArgumentException e) {
            Logger.error("Could not read the header: " + header + ".");
        }
    }

    /**
     * Parses the request line of the HTTP 1.1 Request. RFC 2626 5.1 specifies that the request line
     * provides the method request (GET, POST, etc.), as well as the resource (/home, /about, etc.)
     * @param requestLine The first line of the HTTP request
     */
    private void parseRequestLine(String requestLine) {
        try {
            if (requestLine == null || requestLine.length() == 0) {
                throw new IOException();
            }

            // Split the request line into a three element array -
            // [METHOD, URI, PROTOCOL (Should be HTTP/1.1)]
            String[] requestElements = requestLine.split(" ");

            if (requestElements.length != 3) {
                throw new IOException();
            }

            if (requestElements[0].equals("ERROR")) {
                throw new IllegalArgumentException();
            }

            // If this is not a valid HTTP Method, it will throw an IllegalArgumentException
            method = HTTP_Method.valueOf(requestElements[0]);
            URI = requestElements[1];
            protocol = requestElements[2];

            return;

        } catch (IllegalArgumentException e) {
            Logger.error("Invalid HTTP method.");
        } catch (Exception e) {
            Logger.error("Invalid request line: " + requestLine + ".");
        }

        // If there was an issue parsing the request line, set each element to error
        method = HTTP_Method.ERROR;
        URI = "ERROR";
        protocol = "ERROR";

    }

    // GETTERS & SETTERS

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public HTTP_Method getMethod() {
        return method;
    }

    public void setMethod(HTTP_Method method) {
        this.method = method;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public List<String> getBody() {
        return body;
    }

    public void setBody(String body) {
        ArrayList<String> bodyList = new ArrayList<>();
        bodyList.add(body);
        this.body = bodyList;
    }

    public void setBody(List<String> body) {
        this.body = body;
    }

    public SocketAddress getForeignAddress() {
        return foreignAddress;
    }

    // OTHER METHODS

    public String getRequestLine() {
        return method + " " + URI + " " + protocol + "\n";
    }

    @Override
    public String toString() {
        String str = getRequestLine();

        boolean logHeaders = Boolean.parseBoolean(PropertyAccess.getInstance().getValue("LOG_HEADERS"));
        if (logHeaders) {
            str += Formatter.formatHTTPHeaders(headers) + Formatter.formatHTTPBody(body);
        }

        return str;
    }

}
// References
// https://stackoverflow.com/questions/13255622/parsing-raw-http-request
