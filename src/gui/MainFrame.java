package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.concurrent.atomic.AtomicInteger;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;

import bus.common.CommonBus;
import gui.chat.MainChatPanel;
import gui.client.ClientPanel;
import gui.common.CommonLabel;
import gui.server.ServerPanel;

public class MainFrame extends JFrame{
    public final static int WIDTH_FRAME = 400;
    public final static int HEIGHT_FRAME = 420;
    public final static int HEIGHT_TASKBAR = 60;
    public final static String TASKBAR_BACKGROUND = "0xE5E5EA";

    private CommonBus commonBus;
    private JPanel taskbarPanel;
    private CommonLabel clientLabel;
    private CommonLabel serverLabel;
    private CommonLabel chatLabel;
    private ClientPanel clientPanel;
    private ServerPanel serverPanel;
    private MainChatPanel mainChatPanel;
    private int focusKey;
    private JPanel cardPanel; // nuevo panel con CardLayout

    public MainFrame() throws IOException {
        ImageIO.setUseCache(false);

        UIManager.put("Label.disabledForeground", Color.decode("0xD3D3D3"));
        UIManager.put("RadioButton.disabledText", Color.decode("0xD3D3D3"));

        initUI();
    }

    private void initUI() {
        setPreferredSize(new Dimension(WIDTH_FRAME, HEIGHT_FRAME));
        pack();
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Remote Desktop Software");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("images/window_icon.png")).getImage());
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    mainFrameWindowClosing(e);
                } catch (Exception exception) {
                }
            }
        });

        setVisible(true);

        initComponents();
    }

    private void initComponents() {
        commonBus = new CommonBus();
        taskbarPanel = new JPanel();
        clientLabel = new CommonLabel();
        serverLabel = new CommonLabel();
        chatLabel = new CommonLabel();
        clientPanel = new ClientPanel(commonBus);
        serverPanel = new ServerPanel(commonBus);
        mainChatPanel = new MainChatPanel(commonBus);

        commonBus.setMainChatPanel(mainChatPanel);

        focusKey = 1;

        taskbarPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        taskbarPanel.setBackground(Color.decode(TASKBAR_BACKGROUND));
        taskbarPanel.setBounds(0, 0, WIDTH_FRAME, HEIGHT_TASKBAR);
        add(taskbarPanel, BorderLayout.SOUTH);

        clientLabel.setText("Client");
        clientLabel.setHighlightFont();
        clientLabel.addMouseListener(new TabLabelMouseListener(clientLabel, 1));
        taskbarPanel.add(clientLabel, gbc);

        serverLabel.setText("Server");
        serverLabel.addMouseListener(new TabLabelMouseListener(serverLabel, 2));
        taskbarPanel.add(serverLabel, gbc);

        chatLabel.setText("Chat");
        chatLabel.addMouseListener(new TabLabelMouseListener(chatLabel, 3));
        taskbarPanel.add(chatLabel, gbc);

        // Crear un contenedor con CardLayout
        JPanel contentPane = new JPanel(new CardLayout());
        add(contentPane, BorderLayout.CENTER);

        cardPanel = new JPanel(new CardLayout()); // nuevo panel con CardLayout para los paneles que se muestran y ocultan
        contentPane.add(cardPanel); // agregar el panel cardPanel al contenido principal

        cardPanel.add(clientPanel, "client");
        cardPanel.add(serverPanel, "server");
        cardPanel.add(mainChatPanel, "chat");
        
        // Mostrar el panel de cliente por defecto
        CardLayout cardLayout = (CardLayout) contentPane.getLayout();
        cardLayout.show(contentPane, "client");
    }

    private void mainFrameWindowClosing(WindowEvent e) throws IOException, NotBoundException {
        commonBus.stopServer();
    }

    private class TabLabelMouseListener extends MouseAdapter {
        //private CommonLabel label;
        private int key;

        public TabLabelMouseListener(CommonLabel label, int key) {
            //this.label = label;
            this.key = key;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (key == focusKey)
                    return;
                JPanel showPanel = getPanel(key);
                JPanel hidePanel = getPanel(focusKey);
                boolean isLeft = key > focusKey;
                showPanelsSlider(showPanel, hidePanel, isLeft);
                //label.setText("");
                focusKey = key;
                updateLabels();
            }
        }
    }

    private JPanel getPanel(int key) {
        switch (key) {
            case 1:
                return clientPanel;
            case 2:
                return serverPanel;
            case 3:
                return mainChatPanel;
            default:
                return null;
        }
    }

    private void updateLabels() {
        clientLabel.setNormalFont();
        serverLabel.setNormalFont();
        chatLabel.setNormalFont();
        switch (focusKey) {
            case 1:
                clientLabel.setHighlightFont();
                break;
            case 2:
                serverLabel.setHighlightFont();
                break;
            case 3:
                chatLabel.setHighlightFont();
                break;
        }
    }

    private void showPanelsSlider(JPanel show_panel, JPanel hide_panel, boolean isLeft) {
        show_panel.setVisible(true);

        AtomicInteger x_hide_location = new AtomicInteger(0);
        AtomicInteger x_show_location = new AtomicInteger(0);
        AtomicInteger value = new AtomicInteger(0);

        if (isLeft) {
            x_show_location.set(MainFrame.WIDTH_FRAME);
            value.set(-50);
        } else {
            x_show_location.set(-MainFrame.WIDTH_FRAME);
            value.set(50);
        }

        Timer timer = new Timer(10, (e) -> {
            hide_panel.setLocation(x_hide_location.get(), MainFrame.HEIGHT_TASKBAR);
            show_panel.setLocation(x_show_location.get(), MainFrame.HEIGHT_TASKBAR);
            if (x_show_location.get() == 0) {
                ((Timer) e.getSource()).stop();
                hide_panel.setVisible(false);
            }
            x_show_location.addAndGet(value.get());
            x_hide_location.addAndGet(value.get());
        });
        timer.start();
        
        CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
        cardLayout.show(cardPanel, show_panel.getName());

    }
}
