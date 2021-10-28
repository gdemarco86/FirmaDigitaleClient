package it.gpj.fdc.documents;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe modello che rappresenta un comando da eseguire
 * @author Giovanni
 */
public class Command {
    
    /**
     * Stringa rappresentante il "protocollo" dell'applicazione, ossia il nome
     * con cui è richiamabile da browser tramite URL (sign://COMANDO?params).
     */
    public static final String APP_PROTOCOL = "sign";
    
    /**
     * Nome del comando
     */
    public String command;

    /**
     * Mappa di parametri associati al comando
     */
    public Map parameters;
    
    /**
     * Costruisce un comando con il nome e i parametri indicati
     * @param command Nome del comando
     * @param params Parametri del omando
     */
    public Command(String command, Map params){
        this.command = command;
        this.parameters = params;
    }
    
    /**
     * Genera un comando a partire dalla sintassi ad "URL" cui cui è richiamabile
     * La sintassi prevede: PROTOCOLLO://COMANDO?param1?val1&param2=val2
     * @param commandUrl Url del comando secondo la sintassi prevista
     * @throws MalformedURLException in caso di URL malformato o non attinente alla sintassi previstas
     */
    public Command(String commandUrl) throws MalformedURLException{
        URL url = new URL(commandUrl.replaceFirst(APP_PROTOCOL, "http"));
        this.command = url.getHost();
        this.parameters = getParamsFromQueryUrl(url.getQuery());
    }
    
    /**
     * Torna una mappa chiave valore a partire dalla query string dell'url associato
     * al comando
     * @param query query string ottenuta dall'URL del comando
     * @return mappa chiave valore con i parametri
     */
    protected static Map<String, String> getParamsFromQueryUrl(String query) {  
        Map<String, String> map = new HashMap<String, String>();
        if (query != null){
            String[] params = query.split("&");  

            for (String param : params) {  
                String name = param.split("=")[0];  
                String value = param.split("=")[1];  
                map.put(name, value); 
            }  
        }
        return map;
    }
}
