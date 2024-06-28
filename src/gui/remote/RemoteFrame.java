package gui.remote;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
import javax.imageio.ImageIO;
import javax.swing.*;

import bus.common.CommonBus;
import bus.rmi.RemoteDesktopInterface;
import gui.client.ClientPanel;

public class RemoteFrame extends JFrame implements Runnable{
    private ClientPanel clientPanel;
    private CommonBus commonBus;
    private RemoteDesktopInterface remoteObject;

    private JLabel screenLabel;
    private JMenuBar menuBar;
    private JMenu menuMonitor;
    private HardwareDialog hardwareDialog;

    private Dimension screenSize;
    private Insets frameInsets;
    private Insets taskbarInsets;
    private String quality;

    private float dx;
    private float dy;

    private volatile Thread screenThread;

    public RemoteFrame(ClientPanel clientPanel, CommonBus commonBus, String quality) throws Exception {
        this.setTitle("You are remoting " + commonBus.getTcpClient().getClient().getLocalAddress().getHostName());
        this.setIconImage(
                new ImageIcon(this.getClass().getClassLoader().getResource("images/window_icon.png")).getImage());
        this.getContentPane().setBackground(Color.BLACK);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    remoteFrameWindowClosing(e);
                } catch (Exception exception) {
                    dispose();
                    JOptionPane.showMessageDialog(null, "Can't close connection");
                }
            }

            @Override
            public void windowOpened(WindowEvent e) {
                remoteFrameWindowOpened(e);
            }
        });
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    remoteFrameKeyPressed(e);
                } catch (RemoteException remoteException) {
                    dispose();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    remoteFrameKeyReleased(e);
                } catch (RemoteException remoteException) {
                    dispose();
                }
            }
        });
        this.setVisible(true);

        this.quality = quality;
        this.clientPanel = clientPanel;
        this.commonBus = commonBus;
        this.remoteObject = this.commonBus.getRmiClient().getRemoteObject();
        this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.frameInsets = this.getInsets();
        this.taskbarInsets = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());

        initComponents();
        setupWindow();

        this.screenThread = new Thread(this);
        this.screenThread.setDaemon(true);
        this.screenThread.start();
    }

    private void initComponents() throws RemoteException {
        screenLabel = new JLabel();
        menuBar = new JMenuBar();
        menuMonitor = new JMenu();
        hardwareDialog = new HardwareDialog(this, remoteObject);

        this.setMinimumSize(hardwareDialog.getPreferredSize());

        screenLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    screenLabelMousePressed(e);
                } catch (RemoteException remoteException) {
                    dispose();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                try {
                    screenLabelMouseReleased(e);
                } catch (RemoteException remoteException) {
                    dispose();
                }
            }

        });

        screenLabel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                try {
                    screenLabelMouseWheelMoved(e);
                } catch (RemoteException remoteException) {
                    dispose();
                }
            }
        });

        screenLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                try {
                    screenLabelMouseMoved(e);
                } catch (RemoteException remoteException) {
                    dispose();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                try {
                    screenLabelMouseDragged(e);
                } catch (RemoteException remoteException) {
                    dispose();
                }
            }
        });
        this.add(screenLabel, BorderLayout.CENTER);

        menuBar.setLayout(new GridBagLayout());
        menuBar.setBackground(Color.decode("0x9A9A9A"));
        menuBar.setPreferredSize(new Dimension(0, 25));
        this.setJMenuBar(menuBar);

        menuMonitor.setText("Show monitor");
        menuMonitor.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                menuMonitorMousePressed(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                menuMonitorMouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menuMonitorMouseExited(e);
            }
        });
        menuBar.add(menuMonitor);
    }

    private void setupWindow() throws Exception {
        ImageIO.setUseCache(false);
        byte[] dgram = remoteObject.takeScreenshotServer(quality);
        ByteArrayInputStream bis = new ByteArrayInputStream(dgram);
        BufferedImage screenshot = ImageIO.read(bis);

        screenSize.width -= (taskbarInsets.left + taskbarInsets.right);
        screenSize.height -= (taskbarInsets.top + taskbarInsets.bottom + frameInsets.top
                + menuBar.getPreferredSize().height);

        if (screenSize.width > screenshot.getWidth() && screenSize.height > screenshot.getHeight()) {
            int hGap = (screenSize.width - screenshot.getWidth()) / 2;
            int vGap = (screenSize.height - screenshot.getHeight()) / 2;

            dx = 1;
            dy = 1;
            screenLabel.setBounds(hGap, vGap, screenshot.getWidth(), screenshot.getHeight());
        } else {
            float ratio = (float) screenshot.getWidth() / screenshot.getHeight();
            int tmpWidth = screenSize.width;
            screenSize.width = (int) (ratio * screenSize.height);

            int hGap = (tmpWidth - screenSize.width) / 2;

            dx = (float) screenshot.getWidth() / screenSize.width;
            dy = (float) screenshot.getHeight() / screenSize.height;
            screenLabel.setBounds(hGap, 0, screenSize.width, screenSize.height);
        }
    }

    @Override
    public void run() {
        int width = screenLabel.getWidth();
        int height = screenLabel.getHeight();
        try {
            while (commonBus.getTcpClient().isConnected()) {
                byte[] dgram = remoteObject.takeScreenshotServer(quality);
                ByteArrayInputStream bis = new ByteArrayInputStream(dgram);
                Image screenshot = ImageIO.read(bis).getScaledInstance(width, height, Image.SCALE_SMOOTH);
                screenLabel.setIcon(new ImageIcon(screenshot));
            }
            dispose();
        } catch (Exception e) {
            dispose();
        }
    }

    @Override
    public void dispose() {
        try {
            super.setVisible(false);
            super.dispose();
            clientPanel.setEnabled(true);
            commonBus.getRmiClient().setRemoteServer(false);
            commonBus.getTcpClient().setConnected(false);
            commonBus.getTcpClient().getClient().close();
            if (!screenThread.isInterrupted())
                screenThread.interrupt();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, "Can't close connection");
        }
    }

    private void remoteFrameWindowClosing(WindowEvent e) {
        dispose();
    }

    private void remoteFrameWindowOpened(WindowEvent e) {
        clientPanel.setEnabled(false);
    }

    private void remoteFrameKeyPressed(KeyEvent e) throws RemoteException {
        remoteObject.keyPress(e.getKeyCode());
    }

    private void remoteFrameKeyReleased(KeyEvent e) throws RemoteException {
        remoteObject.keyRelease(e.getKeyCode());
    }

    private void screenLabelMousePressed(MouseEvent e) throws RemoteException {
        remoteObject.mousePress(InputEvent.getMaskForButton(e.getButton()));
    }

    private void screenLabelMouseReleased(MouseEvent e) throws RemoteException {
        remoteObject.mouseRelease(InputEvent.getMaskForButton(e.getButton()));
    }

    private void screenLabelMouseMoved(MouseEvent e) throws RemoteException {
        float x = e.getX() * dx;
        float y = e.getY() * dy;
        remoteObject.mouseMove((int) x, (int) y);
    }

    private void screenLabelMouseDragged(MouseEvent e) throws RemoteException {
        float x = e.getX() * dx;
        float y = e.getY() * dy;
        remoteObject.mouseMove((int) x, (int) y);
    }

    private void screenLabelMouseWheelMoved(MouseWheelEvent e) throws RemoteException {
        remoteObject.mouseWheelServer(e.getWheelRotation());
    }

    private void menuMonitorMousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            hardwareDialog.setVisible(true);
        }
    }

    private void menuMonitorMouseEntered(MouseEvent e) {
        menuMonitor.setFont(new Font("segoe ui", Font.BOLD, 16));
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void menuMonitorMouseExited(MouseEvent e) {
        menuMonitor.setFont(new Font("segoe ui", Font.PLAIN, 14));
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
}
