package it.gpj.fdc.exceptions;

/**
 * Eccezione che indica che è avvenuto un errore durante lo operazioni post firma.
 * @author Giovanni
 */
public class PostSignOperationException extends Exception{

    public PostSignOperationException(String message) {
        super(message);
    }

    public PostSignOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostSignOperationException(Throwable cause) {
        super(cause);
    }
    
}
