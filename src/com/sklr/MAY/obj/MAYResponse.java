package com.sklr.MAY.obj;

import com.sklr.MAY.util.Formatter;
import com.sklr.MAY.util.PropertyAccess;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MAYResponse {
    private String protocol;
    private String statusCode;
    private String reasonPhrase;

    private HashMap<String, Object> headers;

    private MAYResource resource;

    public MAYResponse(String protocol, String statusCode, String reasonPhrase, MAYResource resource) {
        this.protocol = protocol;
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;

        this.headers = new HashMap<>();

        this.resource = resource;
    }

    /**
     * Responsible for putting together a valid HTTP/1.1 response to be sent to the client.
     * An HTTP Response has the following aspects:
     * Response      = Status-Line                ; Section 6.1
     *                 *(( general-header         ; Section 4.5
     *                   | response-header        ; Section 6.2
     *                   | entity-header ) CRLF)  ; Section 7.1
     *                 CRLF
     *                 [ message-body ]           ; Section 7.2
     *
     * The HTTP 1.1 specification can be found here (Ch 6 deals with the HTTP/1.1 response):
     * https://www.ietf.org/rfc/rfc2616.txt
     * @return A byte array that has the status line, headers, and resource in the format prescribed by RFC2616
     */
    public byte[] buildResponse() {
        byte[] statusLine = getStatusLine().getBytes(StandardCharsets.UTF_8);

        StringBuilder headerBuilder = new StringBuilder();
        for (String key : headers.keySet()) {
            headerBuilder.append(key)
                         .append(":")
                         .append(headers.get(key));
        }
        headerBuilder.append("\r\n");

        byte[] headers = headerBuilder.toString().getBytes(StandardCharsets.UTF_8);

        byte[] file = resource.getBytes();

        return new byte[statusLine.length + headers.length + file.length];
    }

    /**
     * The status-line consists of the following:
     *      [protocol version] [status code] [reason phrase][CRLF]
     * The status code is meant for use by the client, while the reason phrase is meant for use by the user. Example:
     *      HTTP/1.1 404 Not Found\r\n
     */
    public String getStatusLine() {
        return protocol + " " + statusCode + " " + reasonPhrase;
    }

    /**
     * Puts together all the response headers for the HTTP/1.1 response
     */
    private void buildHeaders() {
        headers.put("Content-Length", resource.getContentLength());
        headers.put("Content-Type", resource.getContentType().toString());
        headers.put("Content-Language", "en"); // RFC 2616 3.10
        headers.put("Date", Formatter.formatHTTPDateTime(LocalDateTime.now()));
        headers.put("Server", PropertyAccess.getInstance().getVersionedName());
        headers.put("Last-Modified", resource.getLastModified());
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public MAYResource getResource() {
        return resource;
    }

    public void setResource(MAYResource resource) {
        this.resource = resource;
    }



    @Override
    public String toString() {
        String str = getStatusLine();

        boolean logHeaders = Boolean.parseBoolean(PropertyAccess.getInstance().getValue("LOG_HEADERS"));
        if (logHeaders) {
            str += Formatter.formatHTTPHeaders(headers) + resource.toString();
        }

        return str;
    }
}
