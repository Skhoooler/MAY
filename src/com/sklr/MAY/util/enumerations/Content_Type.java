package com.sklr.MAY.util.enumerations;

/**
 * HTTP Content Type enum
 *
 * HTTP Content Types are defined by the IANA, and can be found here:  https://www.iana.org/assignments/media-types/media-types.xhtml
 * The ones defined here are only the content types likely to be sent by MAY. More common
 * content types can be found here: https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
 *
 * The default content type for text files is "PLAINTEXT (text/plain)"
 * The default for all other cases is "OCTET_STREAM (application/octet-stream)"
 */
public enum Content_Type {
    PLAINTEXT("text/plain"),
    OCTET_STREAM("application/octet-stream"),
    CSS("text/css"),
    CSV("text/csv"),
    WORD_DOC("application/msword"),
    JAVASCRIPT("text/javascript"),
    PDF("application/pdf"),
    HTML("text/html");

    public final String contentType;

    private Content_Type(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return contentType;
    }
}
