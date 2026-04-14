package spp.businesslogic.exceptions;

public class LogicLayerException extends Exception {
    public LogicLayerException(String message) {
        super(message);
    }

    public LogicLayerException(String message, Throwable cause) {
        super(message, cause);
    }
}
