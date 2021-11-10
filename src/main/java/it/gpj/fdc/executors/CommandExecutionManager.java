package it.gpj.fdc.executors;

import it.gpj.fdc.documents.Command;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Set;
import org.reflections.Reflections;

/**
 * Manager che si occupa di eseguire un comando ricevuto.
 * Cerca nell'apposito package tutti gli Handler (ICommandExecutor) implementati
 * e sceglie quello opportuno per l'esecuzione del comando
 * @author Giovanni
 */
public class CommandExecutionManager {

    /**
     * Genera un nuovo CommandExecutionManager
     */
    public CommandExecutionManager() {
    }
    
    /**
     * Esegue il comando fornito
     * @param <T> Tipo del dato di ritorno
     * @param command Comando da eseguire
     * @return Risultato dell'operazione legata al comando, specifica per il tipo di comando eseguito
     * @throws InstantiationException in caso di errore nell'istanziazione del Command Executor
     * @throws IllegalAccessException in caso il Command Executor non esponga i metodi necessari
     * @throws ClassNotFoundException in caso di errori generici nel recupero dell'executor
     * @throws CommandExecutorNotFoundException in caso non sia stato trovato un CommandExecutor che possa eseguire il comando
     * @throws Exception in caso di errore generico durante l'operazione
     */
    public <T> T executeCommand(Command command) throws InstantiationException, IllegalAccessException, ClassNotFoundException, CommandExecutorNotFoundException, Exception{
        // Trovo l'executor associato al comando indicato
        ICommandExecutor cmdExecutor = findExecutorByCommand(command);
        if (cmdExecutor == null)
            throw new CommandExecutorNotFoundException("No Execution command found for command "+command.command);
        // Chiedo all'executor di eseguire l'operazione associata al comando
        return cmdExecutor.execute(command.parameters);
    }
    
    /*
    Tramite Reflection vengono cercati e istanziate tutte le classi che implementano l'interfaccia 
    ICommandExecutor. Ad ognuna viene richiamto il metodo getManagedCommand() per valutare
    se il comando a cui è associato corrisponde con il comando da eseguire.
    */
    private ICommandExecutor findExecutorByCommand(Command command) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException{
        // Ottengo tutte le classi che implementano l'interfaccia ICommandExecutor. 
        Reflections reflections = new Reflections("it.gpj.fdc.executors");    
        Set<Class<? extends ICommandExecutor>> classes = reflections.getSubTypesOf(ICommandExecutor.class);
        for (Class classInfo : classes){
            System.out.println ("Found " + classInfo.getName());
             
            int modifiers = classInfo.getModifiers();
            if (!Modifier.isAbstract(modifiers)) {
                // Istanzio l'executor e valuto se esegue il comando richiesto
                ICommandExecutor cmdExecutor = (ICommandExecutor) classInfo.getDeclaredConstructor().newInstance();
                if (cmdExecutor.getManagedCommand().equals(command.command)){
                    return cmdExecutor;
                }
            }
        }
        return null;
    }
}
