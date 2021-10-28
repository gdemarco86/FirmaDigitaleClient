/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.gpj.fdc.run;

import it.gpj.fdc.executors.CommandExecutionManager;
import it.gpj.fdc.documents.Command;
import it.gpj.fdc.executors.CommandExecutorNotFoundException;
import it.gpj.fdl.exceptions.DocumentRetrievingException;
import it.gpj.fdl.exceptions.IncorrectPinException;
import it.gpj.fdl.exceptions.PKCSException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.Provider;
import java.security.Security;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * Main di lancio dell'applicazione. Sarà richiamato a partire da un URL che conterrà
 * il protocollo di questa applicazione e il comando da eseguire (con eventuali parametri).
 * La sintassi prevede: PROTOCOLLO://COMANDO?param1?val1&param2=val2
 * @author Giovanni
 */
public class MainLauncher {
    public static void main(String[] args) {
        try {
            if (args.length == 0)
                throw new MalformedURLException("Comando di lancio non definito.");
            else {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                // Ottengo il Command associato all'URL letto (nel primo parametro degli args)
                String url = (String)args[0];
                CommandExecutionManager commandExecutionManager = new CommandExecutionManager();
                Command command = new Command(url);
                // Chiedo all'execution manager di eseguire il comando rilevato
                commandExecutionManager.executeCommand(command);
                
                JOptionPane.showMessageDialog(null,"Firma avvenuta correttamente."
                    ,"Successo",JOptionPane.PLAIN_MESSAGE);
            }
        } catch (CommandExecutorNotFoundException ex) {
            JOptionPane.showMessageDialog(null,"Si è verificato un errore. Comando non supportato.\n\n"
                    + "Dettagli Tecnici:\n\n"
                    + ex.getMessage()
                    ,"Errore",JOptionPane.ERROR_MESSAGE);
        } catch (MalformedURLException ex) {
            JOptionPane.showMessageDialog(null,"Si è verificato un errore. Comando di esecuzione malformato.\n\n"
                    + "Dettagli Tecnici:\n\n"
                    + ex.getMessage()
                    ,"Errore",JOptionPane.ERROR_MESSAGE);
        }catch (IncorrectPinException ex) {
            JOptionPane.showMessageDialog(null,"Si è verificato un errore. Troppi tentativi di PIN errato.\n\n"
                    + "Dettagli Tecnici:\n\n"
                    + ex.getMessage()
                    ,"Errore",JOptionPane.ERROR_MESSAGE);
        } catch (PKCSException ex) {
            JOptionPane.showMessageDialog(null,"Si è verificato un errore: sembrano esserci problemi con i Driver della Smart Card. Firma non eseguita.\n\n"
                    + "Dettagli Tecnici:\n\n"
                    + ex.getMessage()
                    ,"Errore",JOptionPane.ERROR_MESSAGE);
        }catch (DocumentRetrievingException | InstantiationException | IllegalAccessException | ClassNotFoundException | IOException  ex) {
            JOptionPane.showMessageDialog(null,"Si è verificato un errore. Firma non eseguita.\n\n"
                    + "Dettagli Tecnici:\n\n"
                    + ex.getClass().getCanonicalName()
                    + "\n"
                    + ex.getMessage()
                    ,"Errore",JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Si è verificato un errore. Firma non eseguita.\n\n"
                    + "Dettagli Tecnici:\n\n"
                    + ex.getClass().getCanonicalName()
                    + "\n"
                    + ex.getMessage()
                    ,"Errore",JOptionPane.ERROR_MESSAGE);
        } finally {
            System.exit(0);
        }
    }
}
