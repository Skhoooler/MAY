package com.sklr.MAY;

import com.sklr.MAY.obj.MAYRequest;
import com.sklr.MAY.util.DummyBuilder;
import com.sklr.MAY.util.Logger;
import com.sklr.MAY.util.RequestType;
import com.sklr.MAY.util.enumerations.HTTP_Method;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class HTTPRequestParserTest {
    private final String HTTP_11 = "HTTP/1.1";
    private final String dummyURI = "\\home\\dummy\\address";
    private final SocketAddress dummyAddress = new InetSocketAddress("localhost", 12345);



    @Test
    public void parseGETRequest() {
        Logger.test("Parse valid GET request");
        InputStream dummyRequestStream = DummyBuilder.createDummyHTTPRequestInputStream(HTTP_Method.GET, dummyURI, RequestType.TEXT);
        MAYRequest dummy = DummyBuilder.createDummyMAYRequest(dummyAddress, RequestType.TEXT);

        dummy.parseRequest(dummyRequestStream);

        assertEquals("The URI of the request line was not parsed correctly.", dummyURI, dummy.getURI());
        assertEquals("The method of the request line was not parsed correctly.", HTTP_Method.GET, dummy.getMethod());
        assertEquals("The protocol of the request line was not parsed correctly", HTTP_11, dummy.getProtocol());
        assertNotNull("The request headers have not been set/are null.", dummy.getHeaders());
    }

    @Test
    public void parseGETRequestWithUndefinedMethod() {
        Logger.test("Parse invalid GET Request with Undefined HTTP Method");

        String[] undefinedMethods = {"FUNKY", "Funky", "ALERT", "ZERO", "funky", "FunKy"};

        for (String undefinedMethod : undefinedMethods) {
            Logger.subtest(undefinedMethod);

            InputStream dummyRequestStreamUndefinedMethod = DummyBuilder.createDummyHTTPRequestInputStream(undefinedMethod, dummyURI, RequestType.TEXT);
            MAYRequest dummy = DummyBuilder.createDummyMAYRequest(dummyAddress, RequestType.TEXT);

            dummy.parseRequest(dummyRequestStreamUndefinedMethod);

            assertEquals(undefinedMethod + ": " + "HTTP method was not set to \"ERROR\"", HTTP_Method.ERROR, dummy.getMethod());
            assertEquals(undefinedMethod + ": " + "Requested URI for undefined method is not \"ERROR\"", "ERROR", dummy.getURI());
            assertEquals(undefinedMethod + ": " + "Protocol for undefined method was not set to \"ERROR\"", "ERROR", dummy.getProtocol());
        }
    }

    @Test
    public void parseGETRequestWithErrorMethod() {
        Logger.test("Parse invalid GET request with \"Error\" as it's HTTP Method");

        String[] errors = {"ERROR", "error", "Error", "Errors", "ErRoR", "ERRORS"};

        for (String error : errors) {
            Logger.subtest(error);

            InputStream dummyRequestStreamErrorMethod = DummyBuilder.createDummyHTTPRequestInputStream(error, dummyURI, RequestType.TEXT);
            MAYRequest dummy = DummyBuilder.createDummyMAYRequest(dummyAddress, RequestType.TEXT);

            dummy.parseRequest(dummyRequestStreamErrorMethod);

            assertEquals(error + ": " + "HTTP method was not set to \"ERROR\"", HTTP_Method.ERROR, dummy.getMethod());
            assertEquals(error + ": " + "Requested URI for error method is not \"ERROR\"", "ERROR", dummy.getURI());
            assertEquals(error + ": " + "Protocol for undefined error was not set to \"ERROR\"", "ERROR", dummy.getProtocol());
        }
    }

    @Test
    public void parseGETRequestWithSymbolMethod() {
        Logger.test("Parse invalid GET request with a symbol as it's HTTP Method");

        String[] symbols = {"\\", "#", "$", "@", "\"", "@POST", "P@ST", "G#T", "GET \\ HTTP/1.1"};

        for (String symbol: symbols) {
            Logger.subtest(symbol);

            InputStream dummyRequestStreamSymbolMethod = DummyBuilder.createDummyHTTPRequestInputStream(symbol, dummyURI, RequestType.TEXT);
            MAYRequest dummy = DummyBuilder.createDummyMAYRequest(dummyAddress, RequestType.TEXT);

            dummy.parseRequest(dummyRequestStreamSymbolMethod);

            assertEquals(symbol + ": " + "HTTP method was not set to \"ERROR\"", HTTP_Method.ERROR, dummy.getMethod());
            assertEquals(symbol + ": " + "Requested URI for symbol method is not \"ERROR\"", "ERROR", dummy.getURI());
            assertEquals(symbol + ": " + "Protocol for symbol method was not set to \"ERROR\"", "ERROR", dummy.getProtocol());
        }

    }

    @Test
    public void parseGETRequestWithNumericMethod() {
        Logger.test("Parse invalid GET request with a number as it's HTTP Method");

        String[] numbers = {"1", "-1", "0", "105262", "2.6", "1 5", "6 7 1"};

        for (String number : numbers) {
            Logger.subtest(number);

            InputStream dummyRequestStreamNumericMethod = DummyBuilder.createDummyHTTPRequestInputStream(number, dummyURI, RequestType.TEXT);
            MAYRequest dummy = DummyBuilder.createDummyMAYRequest(dummyAddress, RequestType.TEXT);

            dummy.parseRequest(dummyRequestStreamNumericMethod);

            assertEquals(number + ": " + "HTTP method was not set to \"ERROR\"", HTTP_Method.ERROR, dummy.getMethod());
            assertEquals(number + ": " + "Requested URI for numeric method is not \"ERROR\"", "ERROR", dummy.getURI());
            assertEquals(number + ": " + "Protocol for numeric method was not set to \"ERROR\"", "ERROR", dummy.getProtocol());
        }
    }

    @Test
    public void parseDELETERequest() {
        Logger.test("Parse valid DELETE request");

        InputStream dummyRequestStream = DummyBuilder.createDummyHTTPRequestInputStream(HTTP_Method.DELETE, dummyURI, RequestType.TEXT);
        MAYRequest dummy = DummyBuilder.createDummyMAYRequest(dummyAddress, RequestType.TEXT);

        dummy.parseRequest(dummyRequestStream);

        assertEquals("The URI of the request line was not parsed correctly.", dummyURI, dummy.getURI());
        assertEquals("The method of the request line was not parsed correctly.", HTTP_Method.DELETE, dummy.getMethod());
        assertEquals("The protocol of the request line was not parsed correctly", HTTP_11, dummy.getProtocol());
        assertNotNull("The request headers have not been set/are null.", dummy.getHeaders());
    }

    @Test
    public void parsePOSTRequest() {
        Logger.test("Parse valid POST request");

        InputStream dummyRequestStream = DummyBuilder.createDummyHTTPRequestInputStream(HTTP_Method.POST, dummyURI, RequestType.TEXT);
        MAYRequest dummy = DummyBuilder.createDummyMAYRequest(dummyAddress, RequestType.TEXT);

        dummy.parseRequest(dummyRequestStream);

        assertEquals("The URI of the request line was not parsed correctly.", dummyURI, dummy.getURI());
        assertEquals("The method of the request line was not parsed correctly.", HTTP_Method.POST, dummy.getMethod());
        assertEquals("The protocol of the request line was not parsed correctly", HTTP_11, dummy.getProtocol());
        assertNotNull("The request headers have not been set/are null.", dummy.getHeaders());
    }
}