/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.gpj.fdc.executors;

import it.gpj.fdc.exceptions.PostSignOperationException;
import it.gpj.fdc.utils.FormsUtils;
import it.gpj.fdl.documents.FileSystemDocumentRetriever;
import it.gpj.fdl.documents.IDocumentRetriever;
import it.gpj.fdl.exceptions.DocumentRetrievingException;
import it.gpj.fdl.models.SignResult;
import java.io.IOException;
import java.util.Map;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Giovanni
 */
public class FileSystemSignerCommandExecutor extends AbstractSignCommandExecutor {

    public static String MANAGED_COMMAND = "fssign";
    
    public enum Parameters {
        INPUT_FILE("INPUT_FILE"),
        OUTPUT_FILE("OUTPUT_FILE");

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
        String inputFile;
        if (parameters.containsKey(FileSystemSignerCommandExecutor.Parameters.INPUT_FILE.key)){
            inputFile = parameters.get(FileSystemSignerCommandExecutor.Parameters.INPUT_FILE.key).toString();
        } else {
            inputFile = FormsUtils.chooseFileFromFileSystem(FormsUtils.FileChooserMode.CHOOSE, "Scegli il file da firmare");
            parameters.put(FileSystemSignerCommandExecutor.Parameters.INPUT_FILE.key, inputFile);
        }
        // Uso un FileSystemDocumentRetriever assocato al path del file indicato
        return new FileSystemDocumentRetriever(inputFile);
    }

    @Override
    protected void postSignOperations(SignResult signResult, Map parameters) throws PostSignOperationException{
        try {
            // Se non presente nei parametri, apro dialog per far scegliere dove salvare il file firmato
            String outputFile = 
                parameters.containsKey(FileSystemSignerCommandExecutor.Parameters.OUTPUT_FILE.key) 
                ? parameters.get(FileSystemSignerCommandExecutor.Parameters.OUTPUT_FILE.key).toString() 
                : FormsUtils.chooseFileFromFileSystem(
                    FormsUtils.FileChooserMode.SAVE,  "Scegli dove salvare il file firmato",
                        new FileNameExtensionFilter("P7M signed files", "p7m"));

            if (outputFile != null){
                // Salvo documento firmato in p7m
                if (!outputFile.endsWith(".p7m"))
                    outputFile += ".p7m";
                signResult.saveSignedDocument(outputFile);
            }
        } catch (IOException e){
            throw new PostSignOperationException(e.getMessage(), e);
        }
    }
    
}
