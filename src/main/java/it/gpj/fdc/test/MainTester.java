package it.gpj.fdc.test;

import it.gpj.fdc.executors.CommandExecutionManager;
import it.gpj.fdc.executors.TestSignerCommandExecutor;
import it.gpj.fdc.documents.Command;
import it.gpj.fdl.exceptions.DocumentRetrievingException;
import it.gpj.fdl.exceptions.IncorrectPinException;
import it.gpj.fdl.exceptions.PKCSException;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * Classe con main per i test
 * @author Giovanni
 */
public class MainTester {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            String url = Command.APP_PROTOCOL + "://"+TestSignerCommandExecutor.MANAGED_COMMAND;
            CommandExecutionManager commandExecutionManager = new CommandExecutionManager();
            Command command = new Command(url);
            commandExecutionManager.executeCommand(command);
            
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
                    + ex.getMessage()
                    ,"Errore",JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Si è verificato un errore. Firma non eseguita.\n\n"
                    + "Dettagli Tecnici:\n\n"
                    + ex.getMessage()
                    ,"Errore",JOptionPane.ERROR_MESSAGE);
        } finally {
            System.exit(0);
        }
    }
}
