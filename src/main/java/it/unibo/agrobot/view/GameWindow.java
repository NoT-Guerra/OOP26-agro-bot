package it.unibo.agrobot.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.CardLayout;

/**
 * gestione del jframe principale del gioco
 */
public class GameWindow {

    private final JFrame frame;
    private final JPanel cards;
    private final CardLayout cardLayout;

    /**
     * inizializza la finestra di gioco.
     * 
     * @param title titolo della finestra
     */
    public GameWindow(String title) {
        this.frame = new JFrame(title);
        this.cardLayout = new CardLayout();
        this.cards = new JPanel(this.cardLayout);
        
        this.setupWindow();
    }

    private void setupWindow() {
        // chiude l'applicazione quando la finestra viene chiusa
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.frame.setResizable(true);

        // aggiunge il pannello principale al frame
        this.frame.add(this.cards);
    }

    /**
     * Aggiunge un pannello con un nome specifico.
     *
     * @param panel il pannello da aggiungere
     * @param name il nome identificativo del pannello
     */
    public void addPanel(JPanel panel, String name) {
        this.cards.add(panel, name);
    }

    /**
     * Mostra il pannello associato al nome specificato.
     *
     * @param name il nome del pannello da mostrare
     */
    public void showPanel(String name) {
        this.cardLayout.show(this.cards, name);
    }

    /**
     * rende visibile la finestra di gioco.
     * permette di eseguire il rendering della griglia e del drone.
     */
    public void show() {
        SwingUtilities.invokeLater(() -> {
            // adatta le dimensioni della finestra
            this.frame.pack();
            
            // centra la finestra sullo schermo
            this.frame.setLocationRelativeTo(null);
            
            this.frame.setVisible(true);
        });
    }
}
