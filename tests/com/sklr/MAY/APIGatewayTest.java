package com.sklr.MAY;

import com.sklr.MAY.obj.MAYObject;
import com.sklr.MAY.util.DummyBuilder;
import com.sklr.MAY.util.Logger;
import com.sklr.MAY.util.RequestType;
import org.junit.Test;
import static org.junit.Assert.*;

import com.sklr.MAY.util.enumerations.HTTP_Method;
import com.sklr.MAY.util.enumerations.Content_Type;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class APIGatewayTest {
    APIGateway gateway = new APIGateway();

    @Test
    public void routeHome() {

    }

    @Test
    public void routeNotFound() {

    }

    @Test
    public void routeError() {
        Logger.test("Fulfil a request for an invalid URI");

    }

    @Test
    public void routeJSON() {
        Logger.test("Fulfil a valid request for JSON");

    }

    @Test
    public void routeFavicon() {
        Logger.test("Fulfil a valid request for a Favicon");
    }

    @Test
    public void routeWebpages() {
        Logger.test("Fulfil a valid request for an external API webpage");

    }

}


