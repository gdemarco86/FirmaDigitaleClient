package it.gpj.fdc.test;

import it.gpj.fdc.executors.CommandExecutionManager;
import it.gpj.fdc.executors.TestSignerCommandExecutor;
import it.gpj.fdc.documents.Command;
import it.gpj.fdc.executors.HttpSignerCommandExecutor;
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
            String url = Command.APP_PROTOCOL + "://"+HttpSignerCommandExecutor.MANAGED_COMMAND+"?INPUT_URL=https%3A%2F%2Fwww.orimi.com%2Fpdf-test.pdf";
            CommandExecutionManager commandExecutionManager = new CommandExecutionManager();
            Command command = new Command(url);
            commandExecutionManager.executeCommand(command);
            
            JOptionPane.showMessageDialog(null,"Firma avvenuta correttamente."
                    ,"Successo",JOptionPane.PLAIN_MESSAGE);
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
