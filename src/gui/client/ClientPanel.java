package gui.client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import bus.common.CommonBus;
import gui.MainFrame;
import gui.common.CommonLabel;
import gui.common.CommonPanel;
import gui.remote.RemoteFrame;

public class ClientPanel extends JPanel {
    public final static String BACKGROUND_COLOR = "0x00A571";
    public final static String FOREGROUND_COLOR = "0x003927";

    private CommonPanel mainPanel;
    private CommonLabel connectLabel;
    private ButtonGroup qualityGroup;
    private JRadioButton lowQualityRadio;
    private JRadioButton highQualityRadio;
    private JLabel loaderLabel;

    private CommonBus customBus;

    public ClientPanel(CommonBus customBus) {
        this.customBus = customBus;
        this.setSize(0, MainFrame.HEIGHT_TASKBAR);
        this.setBackground(Color.decode(ClientPanel.BACKGROUND_COLOR));
        this.initComponents();
    }

    private void initComponents() {
        mainPanel = new CommonPanel();
        connectLabel = new CommonLabel();
        qualityGroup = new ButtonGroup();
        lowQualityRadio = new JRadioButton("Low quality");
        highQualityRadio = new JRadioButton("High quality");
        loaderLabel = new JLabel();

        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(gridBagLayout);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);

        // Create input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(null, "Connect To Server",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("segoe ui", Font.BOLD, 16),
                Color.decode(ClientPanel.FOREGROUND_COLOR)));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        mainPanel.getServerLabel().setText("Remote IP");
        inputPanel.add(mainPanel.getServerLabel(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        mainPanel.getServerField().setVisible(true);
        inputPanel.add(mainPanel.getServerField(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        mainPanel.getPortLabel().setText("Remote Port:");
        inputPanel.add(mainPanel.getPortLabel(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        ;
        mainPanel.getPortField().setVisible(true);
        inputPanel.add(mainPanel.getPortField(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        mainPanel.getPasswordLabel().setText("Password:");
        inputPanel.add(mainPanel.getPasswordLabel(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        mainPanel.getPasswordField().setVisible(true);
        inputPanel.add(mainPanel.getPasswordField(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(new JLabel(
                "<html>>>Help: Enter a name or an IP address and port of server which you want to remote.<br>>>Example: 192.168.0.1:9999</html>"),
                gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(inputPanel, gbc);

        // Create quality panel
        JPanel qualityPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(qualityPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        lowQualityRadio.setFont(new Font("segoe ui", Font.PLAIN, 15));
        lowQualityRadio.setSelected(true);
        qualityPanel.add(lowQualityRadio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        highQualityRadio.setFont(new Font("segoe ui", Font.PLAIN, 15));
        qualityPanel.add(highQualityRadio, gbc);

        lowQualityRadio.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    highQualityRadio.setSelected(false);
                }
            }
        });

        highQualityRadio.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    lowQualityRadio.setSelected(false);
                }
            }
        });

        // Create connect button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        connectLabel.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/connect_icon.png")));
        connectLabel.setText("Connect now");
        connectLabel.setFont(new Font("segoe ui", Font.PLAIN, 15));
        connectLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                connectLabelMousePressed(e);
            }
        });
        this.add(connectLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        loaderLabel.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/loader_icon.gif")));
        loaderLabel.setVisible(false);
        this.add(loaderLabel, gbc);
    }

    @Override
    public void setEnabled(boolean b) {
        mainPanel.setEnabled(b);
        lowQualityRadio.setEnabled(b);
        highQualityRadio.setEnabled(b);
        connectLabel.setEnabled(b);
    }

    private boolean isValidIpv4(String host) {
        int count = 0;
        for (int i = 0; i < host.length(); ++i) {
            if (host.charAt(i) == '.')
                ++count;
        }
        return count == 3 || count == 0;
    }

    private void connectLabelMousePressed(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1 && connectLabel.isEnabled()) {
            this.setEnabled(false);
            loaderLabel.setVisible(true);

            

            Thread connectThread = new Thread(() -> {
                try {

                    String host = this.mainPanel.getServerField().getText().toString();
                    int port = Integer.parseInt(this.mainPanel.getPortField().getText().trim());
                    char[] passwordChar = this.mainPanel.getPasswordField().getPassword();
                    String password = new String(passwordChar);

                    if (!isValidIpv4(host)) throw new Exception("Wrong format IPV4");

                    customBus.connectToServer(host, port, password);

                    EventQueue.invokeLater(() -> {
                        try {
                            if (lowQualityRadio.isSelected()) {
                                new RemoteFrame(this, customBus, "jpg");
                            } else if (highQualityRadio.isSelected()) {
                                new RemoteFrame(this, customBus, "png");
                            }
                        } catch (Exception exception) {
                            JOptionPane.showMessageDialog(this, "Can't start remoting to server:\n" + exception.getMessage());
                        }
                    });
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(this, "Can't connect to server:\n" + exception.getMessage());
                    
                }
                this.setEnabled(true);
                loaderLabel.setVisible(false);
            });
            connectThread.setDaemon(true);
            connectThread.start();
        }
    }

    public static void main(String[] args) {
        // Crear un objeto CommonBus para pasar como parámetro
        CommonBus commonBus = new CommonBus();

        // Crear un objeto JFrame para contener el panel
        JFrame frame = new JFrame("Remote Desktop Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear un objeto ClientPanel y agregarlo al frame
        ClientPanel clientPanel = new ClientPanel(commonBus);
        frame.getContentPane().add(clientPanel);

        // Configurar el tamaño y la visibilidad del frame
        frame.setSize(400, 400);
        frame.setVisible(true);
    }
}
