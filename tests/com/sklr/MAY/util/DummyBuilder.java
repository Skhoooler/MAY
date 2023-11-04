package com.sklr.MAY.util;

import com.sklr.MAY.obj.MAYRequest;
import com.sklr.MAY.util.enumerations.HTTP_Method;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class DummyBuilder {

    private final static int dummyPort = 56976;
    private final static String CRLF = "\n";

    /**
     * These methods return an Input Stream that represents an incoming HTTP request
     * @return InputStream of an HTTP Request
     */
    public static InputStream createDummyHTTPRequestInputStream() {
        return createDummyHTTPRequestInputStream(HTTP_Method.GET.name(), "\\", RequestType.TEXT, null);
    }

    public static InputStream createDummyHTTPRequestInputStream(RequestType requestType) {
        return createDummyHTTPRequestInputStream(HTTP_Method.GET.name(), "\\", requestType, null);
    }

    public static InputStream createDummyHTTPRequestInputStream(RequestType requestType, ArrayList<String> message) {
        return createDummyHTTPRequestInputStream(HTTP_Method.GET.name(), "\\", requestType, message);
    }

    public static InputStream createDummyHTTPRequestInputStream(HTTP_Method method, String URI, RequestType requestType) {
        return createDummyHTTPRequestInputStream(method.name(), URI, requestType, null);
    }

    public static InputStream createDummyHTTPRequestInputStream(String method, String URI, RequestType requestType) {
        return createDummyHTTPRequestInputStream(method, URI, requestType, null);
    }

    public static InputStream createDummyHTTPRequestInputStream(String method, String URI, RequestType requestType, ArrayList<String> message) {
        try {
            String dummyRequestLine = method + " " + URI + " HTTP/1.1" + CRLF;
            System.out.println(dummyRequestLine);
            HashMap<String, Object> dummyHeaders = createDummyHTTPRequestHeaders(requestType);

            // Put together a ByteArrayOutputStream to turn into an inputStream to be read by HTTPRequestParser
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // Write the request line to the stream
            out.write(dummyRequestLine.getBytes(StandardCharsets.UTF_8));

            // Write headers to the stream
            for (String key : dummyHeaders.keySet()) {
                String dummyHeader = key + dummyHeaders.get(key);
                System.out.println(dummyHeader);
                out.write(dummyHeader.getBytes(StandardCharsets.UTF_8));
            }

            // At the end of HTTP request headers, there are two extra empty lines before the message.
            // These are those two lines.
            out.write(CRLF.getBytes(StandardCharsets.UTF_8));
            out.write(CRLF.getBytes(StandardCharsets.UTF_8));

            // Write the message to the stream
            if (message != null) {
                for (String line : message) {
                    out.write(line.getBytes(StandardCharsets.UTF_8));
                }
            }

            // Create the Byte array
            byte[] bytes = out.toByteArray();
            out.close();

            return new ByteArrayInputStream(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Fill out a dummy MAYRequest with realistic, but fake request headers. This dummy MAYRequest
     * does not include a method (GET, DELETE, POST, etc.) or a resource URI.
     * @param requestType An enum for describing what type of request it is (image, document, json, etc.)
     * @return a Dummy MAY request
     */
    public static MAYRequest createDummyMAYRequest(SocketAddress dummyAddress, RequestType requestType) {
        MAYRequest dummy = new MAYRequest(dummyAddress);
        dummy.setProtocol("HTTP/1.1");
        dummy.setHeaders(createDummyHTTPRequestHeaders(requestType));

        return dummy;
    }

    /**
     * Returns a Hashmap full of dummy HTTP request headers
     * @param requestType The type of request headers needed
     * @return a hashmap full of request headers
     */
    public static HashMap<String, Object> createDummyHTTPRequestHeaders(RequestType requestType) {
        HashMap<String, Object> dummyHeaders = new HashMap<>();

        if (requestType == RequestType.TEXT) {
            dummyHeaders.put("Accept:", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            dummyHeaders.put("Sec-Fetch-Dest:", "document");
        }
        else if (requestType == RequestType.IMAGE) {
            dummyHeaders.put("Accept:", "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8");
            dummyHeaders.put("Sec-Fetch-Dest:", "image");
        }
        else if (requestType == RequestType.JSON) {
            dummyHeaders.put("Accept:", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            dummyHeaders.put("Sec-Fetch-Dest:", "document");
        }

        dummyHeaders.put("Connection:", "keep-alive");
        dummyHeaders.put("User-Agent:", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 OPR/100.0.0.0");
        dummyHeaders.put("Sec-Fetch-Site:", "None");
        dummyHeaders.put("Host:", "localhost:57492");
        dummyHeaders.put("Accept-Encoding:", "gzip. deflate, br");
        dummyHeaders.put("Sec-Fetch-Mode:", "navigate");
        dummyHeaders.put("sec-ch-ua:", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Opera GX\";v=\"100\"");
        dummyHeaders.put("sec-ch-ua-mobile:", "?0");
        dummyHeaders.put("Cache-Control:", "max-age=0");
        dummyHeaders.put("Upgrade-Insecure-Requests:", "1");
        dummyHeaders.put("sec-ch-ua-platform:", "\"Windows\"");
        dummyHeaders.put("Accept-Language:", "en-US,en;q=0.9");

        return dummyHeaders;
    }

    //public static MAYRequest createEmptyDummyMAYObject() { return new MAYRequest(new InetSocketAddress(dummyPort));}

    public static int getDummyPort() { return dummyPort; }
}
// References:
// https://stackoverflow.com/questions/9837754/java-accessing-a-list-of-strings-as-an-inputstream