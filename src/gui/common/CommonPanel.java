package gui.common;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import gui.MainFrame;
import gui.client.ClientPanel;

public class CommonPanel  extends JPanel{
 private JLabel serverLabel;
    private JTextField serverField;
    private JLabel portLabel;
    private JTextField portField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JLabel helpLabel;

    public CommonPanel() {
        this.setBorder(BorderFactory.createTitledBorder(null, "Connection Settings",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("segoe ui", Font.BOLD, 16),
            Color.decode(ClientPanel.FOREGROUND_COLOR))
        );
        this.setBounds(50, 30, MainFrame.WIDTH_FRAME - 100, 230);
        this.setBackground(Color.decode(ClientPanel.BACKGROUND_COLOR));
        this.setForeground(Color.decode(ClientPanel.FOREGROUND_COLOR));
        this.setLayout(null);

        initComponents();
    }

    private void initComponents() {
        serverLabel = new JLabel("Server IP:");
        serverLabel.setBounds(30, 30, 100, 30);
        serverLabel.setFont(new Font("segoe ui", Font.BOLD, 14));
        add(serverLabel);

        serverField = new JTextField(20);
        serverField.setBounds(140, 35, 130, 20);
        add(serverField);

        portLabel = new JLabel("Server Port:");
        portLabel.setBounds(30, 60, 100, 30);
        portLabel.setFont(new Font("segoe ui", Font.BOLD, 14));
        add(portLabel);

        portField = new JTextField("9999");
        portField.setBounds(140, 65, 130, 20);
        add(portField);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 90, 100, 30);
        passwordLabel.setFont(new Font("segoe ui", Font.BOLD, 14));
        add(passwordLabel);

        passwordField = new JPasswordField("123456");
        passwordField.setBounds(140, 95, 130, 20);
        add(passwordField);

        helpLabel = new JLabel("<html>>>Help: enter server IP/Name and port to connect.<br>>>Example: 192.168.0.1:9999</html>");
        helpLabel.setBounds(40, 130, 230, 70);
        helpLabel.setForeground(Color.decode(ClientPanel.FOREGROUND_COLOR));
        helpLabel.setFont(new Font("segoe ui", Font.BOLD, 13));
        add(helpLabel);
    }

    @Override
    public void setEnabled(boolean b) {
        serverField.setEnabled(b);
        portField.setEnabled(b);
        passwordField.setEnabled(b);
    }

    public JLabel getServerLabel() {
        return serverLabel;
    }

    public JTextField getServerField() {
        return serverField;
    }

    public JLabel getPortLabel() {
        return portLabel;
    }

    public JTextField getPortField() {
        return portField;
    }

    public JLabel getPasswordLabel() {
        return passwordLabel;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JLabel getHelpLabel() {
        return helpLabel;
    }
}
