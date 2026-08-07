package it.unibo.agrobot.view;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * gestione del jframe principale del gioco
 */
public class GameWindow {

    private final JFrame frame;
    private final GamePanel gamePanel;

    /**
     * inizializza la finestra di gioco.
     * 
     * @param title titolo della finestra
     * @param gamePanel pannello di gioco da inserire nella finestra
     */
    public GameWindow(String title, GamePanel gamePanel) {
        this.frame = new JFrame(title);
        this.gamePanel = gamePanel;
        
        this.setupWindow();
    }

    private void setupWindow() {
        // chiude l'applicazione quando la finestra viene chiusa
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.frame.setResizable(true);

        // aggiunge il pannello di gioco al frame
        this.frame.add(this.gamePanel);

        // adatta le dimensioni della finestra al pannnello di gioco
        this.frame.pack();
        
        // imposta la finestra a schermo intero
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // centra la finestra sullo schermo
        this.frame.setLocationRelativeTo(null);
    }

    /**
     * rende visibile la finestra di gioco.
     * permette di eseguire il rendering della griglia e del drone.
     */
    public void show() {
        SwingUtilities.invokeLater(() -> {
            this.frame.setVisible(true);
        });
    }
}
