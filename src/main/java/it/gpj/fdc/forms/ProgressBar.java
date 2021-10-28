package it.gpj.fdc.forms;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;
import javax.swing.JDialog;

/**
 * Barra di attesa
 * @author Giovanni
 */
public class ProgressBar extends JFrame {

    private JProgressBar current;
    
    /**
     * Crae una barra di attesa
     */
    public ProgressBar(){
        this(false,null);
    }

    /**
     * Crea una barra di attesa indeterminata (senza percentuale di avanzamento)
     * e che mostra il testo indicato
     * @param indeterminate
     * @param text
     */
    public ProgressBar(boolean indeterminate, String text) {
        super("Attendi...");
        current = new JProgressBar(0, 100);
        if (indeterminate)
            current.setIndeterminate(true);
        else
            current.setValue(0);

        setAlwaysOnTop(true);
        setResizable(false);

//        setEnabled(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Premuto X");
                System.exit(1);
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
        JPanel pane = new JPanel();
        pane.setLayout(null);

        current.setSize(300, 30);
        // Se è indeterminato setto il testo fornito, altrimenti verrà msotrata la percentuale
        if (current.isIndeterminate()){
            current.setStringPainted(true);
            current.setString(text);
        } else {
            current.setStringPainted(true);
        }
            
        
        pane.add(current);
        setContentPane(pane);
//        pack();
        setLocationByPlatform(true);
        setSize(306, 60);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int)(((screenSize.width - getWidth()) / 2) * .7),(int)(((screenSize.height - getHeight()) / 2) * .7));
        
    }

    /**
     * Torna il valore di avanzamento corrente
     * @return valore di avanzamento corrente
     */
    public int getCurrentValue() {
        return current.getValue();
    }

    /**
     * Valore massimo
     * @return valore massimo
     */
    public int getMaxValue() {
        return current.getMaximum();
    }

    /**
     * Incrementa lo step dell'ammontare indicato
     * @param amount indica di quanto incrementare gli step
     */
    public void addStep(int amount) {

        if (current.getValue() + amount >= getMaxValue())
            current.setValue(getMaxValue());
        else
            current.setValue(getCurrentValue() + amount);
        update(getGraphics());
    }

    /**
     * Setta il valore indicato
     * @param value valore da settare
     */
    public void setValue(int value) {
        current.setValue(value);
        update(getGraphics());
    }
    
    /**
     * Indica se deve essere una barra indeterminata, ossia che non mostra percentuale di avanzamento
     * @param indeterminate se true, la barra non mostrerà la percentuale di avanzamento
     */
    public void setIndeterminate(boolean indeterminate) {
        current.setIndeterminate(indeterminate);        
        update(getGraphics());
    }
    
    /**
     * Setta la stringa da mostrare
     * @param string testo di attesa da mostrare
     */
    public void setString(String string) {
        current.setString(string);        
        update(getGraphics());
    }
    
    /**
     * Indica se si tratta di barra di attesa indeterminata (senza percentuale avanzamento)
     * @return true se la barra è indeterminata
     */
    public boolean isIndeterminate() {
        return current.isIndeterminate();
    }

    /**
     * Resetta il progresso della barra
     */
    public void reset() {
        current.setValue(0);
        update(getGraphics());
        System.err.println("barra del progresso settata a " + getCurrentValue());
    }

    /**
     * Cambia il colore del font
     * @param color colore da settare
     */
    public void setColor(Color color) {
        current.setForeground(color);
    }

    /**
     * Restituisce il colore attuale del font
     * @return colore attuale
     */
    public Color getColor() {
        return current.getForeground();
    }

    /**
     * termina la barra
     */
    public void terminate() {
        setTitle("Terminato");
        current.setValue(getMaxValue());
        setVisible(false);
        dispose();
    }
}
