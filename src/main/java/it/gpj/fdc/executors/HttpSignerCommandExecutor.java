/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.gpj.fdc.executors;

import it.gpj.fdc.exceptions.PostSignOperationException;
import it.gpj.fdc.utils.HttpUtils;
import it.gpj.fdl.documents.HttpDocumentRetriever;
import it.gpj.fdl.documents.IDocumentRetriever;
import it.gpj.fdl.exceptions.DocumentRetrievingException;
import it.gpj.fdl.models.SignResult;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Giovanni
 */
public class HttpSignerCommandExecutor extends AbstractSignCommandExecutor {

    public static String MANAGED_COMMAND = "sign";
    
    public enum Parameters {
        INPUT_URL("INPUT_URL"),
        OUTPUT_URL("OUTPUT_URL");

        public final String key;

        private Parameters(String key) {
            this.key = key;
        }
    }
    
    @Override
    public String getManagedCommand() {
        return MANAGED_COMMAND;
    }

    @Override
    protected IDocumentRetriever getDocumentRetriever(Map parameters) throws DocumentRetrievingException{
        // Se non indicato nei parametri, apro dialog per scelta del file da firmare
        String inputUrl;
        if (parameters.containsKey(HttpSignerCommandExecutor.Parameters.INPUT_URL.key)){
            inputUrl = parameters.get(HttpSignerCommandExecutor.Parameters.INPUT_URL.key).toString();
        } else {
            throw new DocumentRetrievingException("Url da cui recuperare il file non specificato");
        }
        // Uso un FileSystemDocumentRetriever assocato al path del file indicato
        return new HttpDocumentRetriever(inputUrl);
    }

    @Override
    protected void postSignOperations(SignResult signResult, Map parameters) throws PostSignOperationException {
        // Se non indicato nei parametri, apro dialog per scelta del file da firmare
        String outputUrl;
        if (parameters.containsKey(HttpSignerCommandExecutor.Parameters.OUTPUT_URL.key))
            outputUrl = parameters.get(HttpSignerCommandExecutor.Parameters.OUTPUT_URL.key).toString();
        else 
            return;
            //throw new PostSignOperationException("Url a cui inviare il file firmato non specificato");
        try {
            HttpUtils.SendFile(signResult.getSignedDocumentStream(), outputUrl);
        } catch (IOException ex) {
            throw new PostSignOperationException(ex.getMessage(), ex);
        }
    }
    
}
