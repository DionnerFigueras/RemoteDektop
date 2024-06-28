package gui.server;
import javax.swing.*;
import java.awt.*;

public class ShowImage {
    public static void mostrarImagen(String rutaImagen) {
        // Creamos un JFrame para mostrar la imagen
        JFrame frame = new JFrame("Imagen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creamos un JLabel para mostrar la imagen
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(rutaImagen));

        // Agregamos el JLabel al JFrame
        frame.getContentPane().add(label, BorderLayout.CENTER);

        // Establecemos el tamaño del JFrame
        frame.setSize(400, 300);

        // Mostramos el JFrame
        frame.setVisible(true);
    }

    public static void main(String[] args){
        mostrarImagen("src/images/dialog_icon.png");
    }
}