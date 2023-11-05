package com.sklr.MAY;

import com.sklr.MAY.obj.MAYRequest;
import com.sklr.MAY.obj.MAYResource;
import com.sklr.MAY.obj.MAYResponse;
import com.sklr.MAY.util.Logger;
import com.sklr.MAY.util.enumerations.Content_Type;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * APIGateway is responsible for routing incoming HTTP requests to their proper API endpoints. It must always return
 * a resource, if not the requested resource, then a 404 or error page.
 */
public class APIGateway {
    private final Path HOME_PAGE_PATH = Paths.get("src/com/sklr/MAY/res/home.html");
    private final Path NOT_FOUND_PATH = Paths.get("src/com/sklr/MAY/res/404 - Not Found.html");

    public MAYResponse fulfillRequest(MAYRequest request) {
        //return notFoundResponse();
        return null;
    }

    // ERROR RESPONSES

    /**
     * Creates a MayResponse Object for an error 404 - Not Found page.
     * @return A MayResponse Object that contains everything to display a 404 - Not Found page to the user as HTML
     */
    private MAYResponse notFoundResponse() {
        try {
            File file = gatherResourceFromPath(NOT_FOUND_PATH);

            MAYResource res = new MAYResource(file, Content_Type.HTML);

            return new MAYResponse("HTTP/1.1", "404", "Not Found", res);
        } catch(Exception e) {
            Logger.error("Problem creating a 404 - Not Found response.", e);

            return null;
        }
    }

    // UTILITY METHODS

    /**
     * Returns a File object when given a Path to that Object
     * @param path A Path object that points to the desired file
     * @return A File object
     */
    private File gatherResourceFromPath(Path path) {
        return new File(path.toUri());
    }
}
