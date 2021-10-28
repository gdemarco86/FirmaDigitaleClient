package it.gpj.fdc.executors;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import it.gpj.fdc.forms.ProgressBar;
import it.gpj.fdl.documents.FileSystemDocumentRetriever;
import it.gpj.fdl.exceptions.DocumentRetrievingException;
import it.gpj.fdl.exceptions.IncorrectPinException;
import it.gpj.fdl.exceptions.NotValidDLLException;
import it.gpj.fdl.exceptions.PKCSException;
import it.gpj.fdl.models.PKCS11Parameters;
import it.gpj.fdl.models.SignParameters;
import it.gpj.fdl.models.SignResult;
import it.gpj.fdl.sign.CadesPKCS11Signer;
import it.gpj.fdc.utils.FormsUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Specifico executor che risponde al comando 'testsign' e si occupa di effettuare
 * un'operazione di firma di test, chiedendo all'utente di indicare un file da
 * file system da firmare e chiedendo poi di salvare il file firmato ottenuto.
 * @author Giovanni
 */
public class TestSignerCommandExecutor implements ICommandExecutor {

    public static String MANAGED_COMMAND = "testsign";
    /**
     * Chiavi dei possibili parametri accettati da questo Executor
     */
    public enum Parameters {
        PIN("PIN"),
        INPUT_FILE("INPUT_FILE"),
        PIN_MESSAGE("PIN"),
        OUTPUT_FILE("OUTPUT_FILE");

        public final String key;

        private Parameters(String key) {
            this.key = key;
        }
    }
    
    /**
     * Crae un Executor per l'operazione di firma di test
     */
    public TestSignerCommandExecutor(){}
    
    /**
     * Torna il comando gestito da questo executor, ossia 'testsign'
     * @return la stringa 'testsign' rappresentante il nome del comando gestito
     */
    @Override
    public String getManagedCommand() {
        return MANAGED_COMMAND;
    }

    /**
     * Esegue l'operazione di firma di test, chiedendo all'utente di indicare un file da
     * file system da firmare e chiedendo poi di salvare il file firmato ottenuto.
     * @param parameters parametri dell'operazione di firma; vedere le chiavi accettate nell'enum Parameters
     * @return Oggetto SignResult della librearia FDL rappresentante l'esito di firma
     * @throws IncorrectPinException in caso di pin della smart card errato
     * @throws NotValidDLLException in caso di dll pkcs11 fornita non valida
     * @throws PKCSException in caso di problemi nell'accesso alla smart card
     * @throws IOException in caso di errori nella lettura/salvataggio dei file
     * @throws DocumentRetrievingException in caso di problemi nel recupero del documento da firmare
     */
    @Override
    public SignResult execute(Map parameters) throws IncorrectPinException, PKCSException,
            NotValidDLLException, IOException, DocumentRetrievingException {
        // Istanzio una progress bar di attesa
        ProgressBar progressBar = new ProgressBar(true, "Attendi...");
        
        // Se non indicato nei parametri, apro dialog per scelta del file da firmare
        String inputFile = parameters.containsKey(Parameters.INPUT_FILE.key) 
                ? parameters.get(Parameters.INPUT_FILE.key).toString() 
                : FormsUtils.chooseFileFromFileSystem(FormsUtils.FileChooserMode.CHOOSE, "Scegli il file da firmare");
        
        if (inputFile != null){
            System.out.println("Selected file: " + inputFile);
            try{
                // Se non presente nei parametri, chiedo il pin all'utente
                String pin = null;
                if (parameters.containsKey(Parameters.PIN.key))
                    pin = parameters.get(Parameters.PIN.key).toString();
                else
                {
                    String pinMessage = parameters.containsKey(Parameters.PIN_MESSAGE.key) 
                        ? parameters.get(Parameters.PIN_MESSAGE.key).toString() 
                        : "Indicare il PIN della smart card: ";
                    String askedPin = FormsUtils.getPasswordFromPanel("Richiesta PIN", pinMessage);

                    if (askedPin != null)
                        pin = askedPin;
                    else 
                        return null;
                }
                
                // Visualizzo barra di attesa
                progressBar.setString("Firma in corso...");
                progressBar.setVisible(true);
                
                // Effettuo l'operazione di firma
                SignResult signResult = signDocument(inputFile, pin);
                
                progressBar.setVisible(false);
                
                // Se non presente nei parametri, apro dialog per far scegliere dove salvare il file firmato
                String outputFile = 
                    parameters.containsKey(Parameters.OUTPUT_FILE.key) 
                    ? parameters.get(Parameters.OUTPUT_FILE.key).toString() 
                    : FormsUtils.chooseFileFromFileSystem(
                        FormsUtils.FileChooserMode.SAVE,  "Scegli dove salvare il file firmato",
                            new FileNameExtensionFilter("P7M signed files", "p7m"));
                
                if (outputFile != null){
                    // Salvo documento firmato in p7m
                    if (!outputFile.endsWith(".p7m"))
                        outputFile += ".p7m";
                    signResult.saveSignedDocument(outputFile);
                }
                
                return signResult;
            } catch (IncorrectPinException ex) {
                // In caso di pin errato, ripeto l'operazione passando già l'input file in modo che non 
                // venga richiesto all'utente
                parameters.put(Parameters.INPUT_FILE.key, inputFile);
                parameters.put(Parameters.PIN_MESSAGE.key, "PIN Errato. Indicare il PIN della smart card: ");
                this.execute(parameters);
            }
        }
        else {
            throw new IOException("Non è stato indicato il file da firmare.");
        }
        return null;
    }
    
    // Effettua la firma del file indicato
    private SignResult signDocument(String fileToSign, String pin) throws NotValidDLLException, PKCSException, IncorrectPinException, DocumentRetrievingException{
        // Uso un FileSystemDocumentRetriever assocato al path del file indicato
        FileSystemDocumentRetriever docRetriever = 
                new FileSystemDocumentRetriever(fileToSign);
        
        // Istanzio i parametri per accesso a smart card (passando il pin)
        PKCS11Parameters pcksParams = new PKCS11Parameters(pin);
        // Istanzio i parametri per la firma
        SignParameters signParams = 
                new SignParameters(SignatureLevel.CAdES_BASELINE_B, SignaturePackaging.ENVELOPING, DigestAlgorithm.SHA256);
        signParams.addAdditionalParameter(SignParameters.KnownAdditionalParameters.SignWithExpiredCertificate, true);
        
        // Istanzio il signer per la firma Cades con PCKS11
        CadesPKCS11Signer signer = new CadesPKCS11Signer(docRetriever,pcksParams);
        
        // Firmo
        return signer.signDocument(signParams);
    }
    
}
