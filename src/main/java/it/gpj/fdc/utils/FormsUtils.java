/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.gpj.fdc.utils;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Giovanni
 */
public class FormsUtils {

    /**
     * Mostra un pannello che richiede l'inserimento di una password all'utente
     * @param title Titolo del pannello
     * @param message Messaggio da mostrare nel pannello
     * @return la password inserita dall'utente
     */
    public static String getPasswordFromPanel(String title, String message){
        JPanel panel = new JPanel();
        JLabel label = new JLabel(message);
        JPasswordField pass = new JPasswordField(10);
        panel.add(label);
        panel.add(pass);
        String[] options = new String[]{"OK", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, panel, title,
                                 JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                                 null, options, options[1]);
        if(option == 0) // pressing OK button
        {
            char[] password = pass.getPassword();
            return new String(password);
        } else
            return null;
    }
    
    /**
     * Tipologia di dialog per la scelta del file
     */
    public enum FileChooserMode {CHOOSE, SAVE}

    /**
     * Apre una dialog per la scelta di un file o per indicare dove salvare un file
     * @param mode indica se chiedere di indicare un file da aprire o da salvare (enum FileChooserMode)
     * @param dialogTitle Titolo della finestra
     * @param fileFilters eventuali filtri sui file da mostrare
     * @return Percorso del file scelto
     */
    public static String chooseFileFromFileSystem(FileChooserMode mode, String dialogTitle, FileFilter fileFilters){
        
        JDialog dialog = new JDialog();
        dialog.setAlwaysOnTop(true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(dialogTitle);
        fileChooser.setApproveButtonText(mode == FileChooserMode.CHOOSE ? "Apri" : "Salva");
        //fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        if (fileFilters != null)
            fileChooser.setFileFilter(fileFilters);
        
        int result = fileChooser.showOpenDialog(dialog);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        } else 
            return null;
    }

    /**
     * Apre una dialog per la scelta di un file o per indicare dove salvare un file
     * @param mode indica se chiedere di indicare un file da aprire o da salvare (enum FileChooserMode)
     * @param dialogTitle Titolo della finestra
     * @return Percorso del file scelto
     */
    public static String chooseFileFromFileSystem(FileChooserMode mode, String dialogTitle){
        return chooseFileFromFileSystem(mode, dialogTitle, null);
    }
}
