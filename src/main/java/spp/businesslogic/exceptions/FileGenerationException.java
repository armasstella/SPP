package spp.businesslogic.exceptions;


public class FileGenerationException extends RuntimeException {

    public FileGenerationException(String message) {
        super(message);
    }

    public FileGenerationException(String message, Throwable e) {
        super(message, e);
    }

}
