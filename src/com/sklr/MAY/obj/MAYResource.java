package com.sklr.MAY.obj;

import com.sklr.MAY.util.enumerations.Content_Type;
import com.sklr.MAY.util.Formatter;

import java.io.File;
import java.util.Objects;
import java.time.LocalDateTime;

public class MAYResource {
    private final File resource;
    private final String path;
    private final long contentLength;
    private final Content_Type contentType;
    private final String lastModified;

    // CONSTRUCTORS
    MAYResource(File resource, Content_Type contentType) {
        this.resource = resource;
        this.path = resource.getPath();
        this.contentLength = resource.length();
        this.contentType = contentType;
        this.lastModified = Formatter.formatHTTPDateTime(LocalDateTime.now());
    }

    MAYResource(File resource, Content_Type contentType, String lastModified) {
        this.resource = resource;
        this.path = resource.getPath();
        this.contentLength = resource.length();
        this.contentType = contentType;
        this.lastModified = lastModified;
    }

    /**
     * Returns the resource as a byte array, regardless of the type of file. Images, text, etc. should
     * all be returned as a byte array.
     * @return The resource as a byte array
     */
    public byte[] getBytes() {
        return null;
    }

    // GETTERS AND SETTERS
    public String getPath() {
        return path;
    }

    public long getContentLength() {
        return contentLength;
    }

    public Content_Type getContentType() {
        return contentType;
    }

    public File getResource() {
        return resource;
    }

    public String getLastModified() {
        return lastModified;
    }



    @Override
    public String toString() {
        return "Resource: " + path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MAYResource that = (MAYResource) o;
        return path.equals(that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
