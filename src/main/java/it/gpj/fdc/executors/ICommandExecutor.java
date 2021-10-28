package it.gpj.fdc.executors;

import java.util.Map;

/**
 * Interfaccia generica che rappresenta un esecutore di comandi.
 * Dovrà essere iplementata se si vuole una classe che esegua una certa
 * operazione corrispondente ad un comando
 * @author Giovanni
 */
public interface ICommandExecutor {

    /**
     * Torna il nome del comando eseguito da questa classe
     * @return
     */
    public String getManagedCommand();

    /**
     * Esegue l'operazione associata al comando
     * @param <T> Tipo del dato di ritorno
     * @param parameters parametri associati all'operazione
     * @return Risultato dell'operazione
     * @throws Exception in caso di errori nell'esecuzione dell'operazione
     */
    public <T> T execute(Map parameters) throws Exception;
}
