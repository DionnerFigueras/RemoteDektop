package gui.chat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import bus.chat.FileMessage;

import java.awt.event.MouseEvent;

public class FieldLabel extends JLabel{
 private FileMessage file_message;

    public FieldLabel(FileMessage file_message) {
        this.file_message = file_message;

        // Estilo de la etiqueta
        this.setText(file_message.getAuthor() + " sent a file: " + file_message.getFilename());
        this.setFont(new Font("Segoe UI", Font.BOLD, 16));
        this.setForeground(Color.decode("#3498db")); // Azul claro
        this.setBackground(Color.decode("#f7f7f7")); // Blanco claro
        this.setOpaque(true);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#ccc"), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Manejador de eventos
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                fileLabelMouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                fileLabelMouseExited(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    fileLabelMousePressed(e);
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(null, "Can't save file:\n" + exception.getMessage());
                }
            }
        });
    }

    private void fileLabelMouseEntered(MouseEvent e) {
        if (isEnabled()) {
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
            this.setForeground(Color.decode("#2ecc71")); // Verde claro
            this.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 16));
        }
    }

    private void fileLabelMouseExited(MouseEvent e) {
        if (isEnabled()) {
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            this.setForeground(Color.decode("#3498db")); // Azul claro
            this.setFont(new Font("Segoe UI", Font.BOLD, 16));
        }
    }

    private void fileLabelMousePressed(MouseEvent e) throws IOException {
        if (e.getButton() == MouseEvent.BUTTON1) {
            JFileChooser file_chooser = new JFileChooser();
            file_chooser.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
            file_chooser.setSelectedFile(new File(file_message.getFilename()));
            file_chooser.showSaveDialog(this);

            File dir = file_chooser.getSelectedFile();
            if (dir != null) {
                FileOutputStream fos = new FileOutputStream(dir);
                fos.write(file_message.getContent());
                fos.close();
            }
        }
    }
}
