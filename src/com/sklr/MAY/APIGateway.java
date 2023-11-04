package com.sklr.MAY;

import com.sklr.MAY.obj.MAYRequest;
import com.sklr.MAY.obj.MAYResponse;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * APIGateway is responsible for routing incoming HTTP requests to their proper API endpoints. It must always return
 * a resource, if not the requested resource, then a 404 or error page.
 */
public class APIGateway {
    private final Path HOME_PAGE_PATH = Paths.get("src/com/sklr/MAY/res/home.html");

    public MAYResponse fulfillRequest(MAYRequest request) {
        return null;
    }
}
