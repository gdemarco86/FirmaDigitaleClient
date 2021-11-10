/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.gpj.fdc.executors;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import it.gpj.fdc.exceptions.PostSignOperationException;
import it.gpj.fdc.forms.ProgressBar;
import it.gpj.fdc.utils.FormsUtils;
import it.gpj.fdl.documents.IDocumentRetriever;
import it.gpj.fdl.exceptions.DocumentRetrievingException;
import it.gpj.fdl.exceptions.IncorrectPinException;
import it.gpj.fdl.exceptions.NotValidDLLException;
import it.gpj.fdl.exceptions.PKCSException;
import it.gpj.fdl.models.PKCS11Parameters;
import it.gpj.fdl.models.SignParameters;
import it.gpj.fdl.models.SignResult;
import it.gpj.fdl.sign.CadesPKCS11Signer;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author Giovanni
 */
public abstract class AbstractSignCommandExecutor implements ICommandExecutor {
    
    public enum Parameters {
        PIN("PIN"),
        PIN_MESSAGE("PIN_MESSAGE");

        public final String key;

        private Parameters(String key) {
            this.key = key;
        }
    }
    
    protected ProgressBar progressBar;
    
    /**
     * Torna il comando gestito da questo executor, ossia 'testsign'
     * @return la stringa 'testsign' rappresentante il nome del comando gestito
     */
    @Override
    public abstract String getManagedCommand();
    
    protected abstract IDocumentRetriever getDocumentRetriever(Map parameters) throws DocumentRetrievingException;
    
    protected abstract void postSignOperations(SignResult signResult, Map parameters) throws PostSignOperationException;
    
    public SignResult execute(Map parameters) throws IncorrectPinException, PKCSException,
            NotValidDLLException, IOException, DocumentRetrievingException, PostSignOperationException {
        // Istanzio una progress bar di attesa
        progressBar = new ProgressBar(true, "Attendi...");
        
        IDocumentRetriever docRetriever = getDocumentRetriever(parameters);
        
        if (docRetriever != null){
            try{
                // Se non presente nei parametri, chiedo il pin all'utente
                String pin = null;
                if (parameters.containsKey(AbstractSignCommandExecutor.Parameters.PIN.key))
                    pin = parameters.get(AbstractSignCommandExecutor.Parameters.PIN.key).toString();
                else
                {
                    String pinMessage = parameters.containsKey(AbstractSignCommandExecutor.Parameters.PIN_MESSAGE.key) 
                        ? parameters.get(AbstractSignCommandExecutor.Parameters.PIN_MESSAGE.key).toString() 
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
                SignResult signResult = signDocument(docRetriever, pin);
                
                progressBar.setVisible(false);
                
                postSignOperations(signResult, parameters);
                
                return signResult;
            } catch (IncorrectPinException ex) {
                // In caso di pin errato, ripeto l'operazione passando già l'input file in modo che non 
                // venga richiesto all'utente
                parameters.put(AbstractSignCommandExecutor.Parameters.PIN_MESSAGE.key, "PIN Errato. Indicare il PIN della smart card: ");
                this.execute(parameters);
            }
        }
        else {
            throw new IOException("Non è stato indicato il file da firmare.");
        }
        return null;
    }
    
    // Effettua la firma del file indicato
    private SignResult signDocument(IDocumentRetriever docRetriever, String pin) throws NotValidDLLException, PKCSException, IncorrectPinException, DocumentRetrievingException{
        // Istanzio i parametri per accesso a smart card (passando il pin)
        PKCS11Parameters pcksParams = new PKCS11Parameters(pin);
        // Istanzio i parametri per la firma
        SignParameters signParams = 
                new SignParameters(SignatureLevel.CAdES_BASELINE_B, SignaturePackaging.ENVELOPING, DigestAlgorithm.SHA256);
        signParams.addAdditionalParameter(SignParameters.KnownAdditionalParameters.SignWithExpiredCertificate, true);
        
        // Istanzio il signer per la firma Cades con PCKS11
        CadesPKCS11Signer signer = new CadesPKCS11Signer(docRetriever, pcksParams);
        
        // Firmo
        return signer.signDocument(signParams);
    }
    
}
