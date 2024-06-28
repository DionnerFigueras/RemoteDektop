import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.JOptionPane;

import gui.MainFrame;

public class App {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new MainFrame();
            }catch(IOException e) {
                JOptionPane.showMessageDialog(null, "Application Error \n" + e.getMessage());
            }
        });
    }
}
