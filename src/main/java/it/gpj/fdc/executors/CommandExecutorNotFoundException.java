package it.gpj.fdc.executors;

/**
 * Eccezione generata in caso non sian presenti CommandExecutor che eseguano
 * un determinato comando
 * @author Giovanni
 */
public class CommandExecutorNotFoundException extends Exception{

    public CommandExecutorNotFoundException(String message) {
        super(message);
    }

    public CommandExecutorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandExecutorNotFoundException(Throwable cause) {
        super(cause);
    }
}
