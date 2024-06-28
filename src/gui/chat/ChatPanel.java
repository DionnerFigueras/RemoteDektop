package gui.chat;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import bus.chat.ChatBus;
import bus.chat.FileMessage;
import bus.chat.Message;
import bus.chat.StringMessage;
import bus.common.CommonBus;
import gui.MainFrame;
import gui.client.ClientPanel;
import gui.common.CommonLabel;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;

public class ChatPanel extends JPanel implements Runnable{
public final static String BACKGROUND_COLOR = "0xFFFFFF";
    public final static int MAX_LENGTH_LINE = 20;

    private JPanel contentPanel;
    private JScrollPane scrollPanel;
    private JTextArea messageText;
    private JScrollPane scrollText;
    private CommonLabel sendLabel;
    private CommonLabel fileLabel;
    private JLabel loaderLabel;

    private GroupLayout layout;
    private GroupLayout.ParallelGroup hParallel;
    private GroupLayout.SequentialGroup vSequential;

    private MainChatPanel root;

    private CommonBus commonBus;
    private ChatBus chatBus;

    private Thread receiveThread;

    public ChatPanel(MainChatPanel root, CommonBus commonBus, ChatBus chatBus) {
      
        this.setLocation(0, MainFrame.HEIGHT_TASKBAR - 42);
        this.setSize(MainFrame.WIDTH_FRAME, MainFrame.HEIGHT_FRAME - MainFrame.HEIGHT_TASKBAR);
        this.setBackground(Color.decode(ClientPanel.BACKGROUND_COLOR));
        this.setLayout(null);

        
        this.commonBus = commonBus;
        this.chatBus = chatBus;
        this.root = root;

       
        this.initComponents();

        
        this.receiveThread = new Thread(this);
        this.receiveThread.setDaemon(true);
        this.receiveThread.start();
    }

    private void initComponents() {
        
        this.contentPanel = new JPanel();
        this.scrollPanel = new JScrollPane();
        this.messageText = new JTextArea();
        this.scrollText = new JScrollPane();
        this.sendLabel = new CommonLabel();
        this.fileLabel = new CommonLabel();
        this.loaderLabel = new JLabel();

        
        this.layout = new GroupLayout(this.contentPanel);
        this.hParallel = this.layout.createParallelGroup();
        this.vSequential = this.layout.createSequentialGroup();
        this.layout.setHorizontalGroup(hParallel);
        this.layout.setVerticalGroup(vSequential);

        
        this.contentPanel.setBackground(Color.decode(ChatPanel.BACKGROUND_COLOR));
        this.contentPanel.setLayout(layout);

        
        this.scrollPanel.setViewportView(this.contentPanel);
        this.scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.scrollPanel.setBounds(10, 15, MainFrame.WIDTH_FRAME - 20, MainFrame.HEIGHT_FRAME - 150);
        this.add(this.scrollPanel);

        
        this.messageText.setFont(new Font("consolas", Font.PLAIN, 14));
        this.messageText.setLineWrap(true);
        this.messageText.getDocument().putProperty("filterNewlines", Boolean.TRUE);
        this.messageText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                messageTextKeyPressed(e);
            }
        });

        
        this.scrollText.setViewportView(this.messageText);
        this.scrollText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.scrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.scrollText = new JScrollPane(this.messageText);
        this.scrollText.setBounds(30, scrollPanel.getHeight() + 20, MainFrame.WIDTH_FRAME - 150,
                MainFrame.HEIGHT_FRAME - MainFrame.HEIGHT_TASKBAR - scrollPanel.getHeight() - 30);
        this.add(this.scrollText);

        
        this.sendLabel.setText("Send");
        this.sendLabel.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/send_icon.png")));
        this.sendLabel.setForeground(Color.decode(ClientPanel.FOREGROUND_COLOR));
        this.sendLabel.setFont(new Font("segoe ui", Font.PLAIN, 13));
        this.sendLabel.setBounds(this.scrollText.getWidth() + 50, this.scrollText.getY(), 80, 30);
        this.sendLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                sendLabelMousePressed(e);
            }
        });
        this.add(this.sendLabel);

        
        this.fileLabel.setText("File");
        this.fileLabel.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/file_icon.png")));
        this.fileLabel.setForeground(Color.decode(ClientPanel.FOREGROUND_COLOR));
        this.fileLabel.setFont(new Font("segoe ui", Font.PLAIN, 13));
        this.fileLabel.setBounds(this.scrollText.getWidth() + 50, this.scrollText.getY() + 40, 60, 30);
        this.fileLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    fileLabelMousePressed(e);
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(null, "Can't send message:\n" + exception.getMessage());
                }
            }
        });
        this.add(this.fileLabel);

       
        this.loaderLabel.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/loader_icon2.gif")));
        this.loaderLabel.setBounds(this.fileLabel.getX() + 60, this.fileLabel.getY() + 7, 16, 16);
        this.loaderLabel.setVisible(false);
        this.add(this.loaderLabel);
    }

    public String getHostName() {
        return this.chatBus.getConnection().getInetAddress().getHostName();
    }

    @Override
    public void setEnabled(boolean b) {
        this.messageText.setEnabled(b);
        this.sendLabel.setEnabled(b);
        this.fileLabel.setEnabled(b);
    }

    @Override
    public void setVisible(boolean b) {
        
        this.scrollPanel.getViewport()
                .setViewPosition(new Point(0, this.scrollPanel.getVerticalScrollBar().getMaximum()));
        this.scrollPanel.getViewport()
                .setViewPosition(new Point(0, this.scrollPanel.getVerticalScrollBar().getMaximum()));
        super.setVisible(b);
    }

    private void messageTextKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            this.sendMessage();
        }
    }

    private void sendLabelMousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && this.sendLabel.isEnabled()) {
            this.sendMessage();
        }
    }

    
    private void sendMessage() {
        try {
            String content = this.messageText.getText();
            if (!content.trim().equals("")) {
                StringMessage strMessage = new StringMessage(InetAddress.getLocalHost().getHostName(), content);
                this.chatBus.transmit(strMessage);
                int gap = this.scrollPanel.getWidth() - 180;

                JLabel label = new JLabel("You send a message:" + content);
                label.setFont(new Font("consolas", Font.PLAIN, 14));
                label.setForeground(Color.BLACK);
                this.addMessageToPanel(label, gap, "green");
            }
            this.messageText.setText("");
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Can't send message:\n" + exception.getMessage());
        }
    }

    
    private void fileLabelMousePressed(MouseEvent e) throws IOException {
        if (e.getButton() == MouseEvent.BUTTON1 && this.fileLabel.isEnabled()) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
            fileChooser.showDialog(this, "Send");

            File dir = fileChooser.getSelectedFile();
            if (dir != null) {
                if (dir.length() > 30 * 1024 * 1024)
                    throw new IOException("File too large, please send file < 30MB");

                this.loaderLabel.setVisible(true);
                this.setEnabled(false);
                Thread sendThread = new Thread(() -> {
                    try {
                        
                        FileInputStream fis = new FileInputStream(dir);
                        FileMessage fileMessage = new FileMessage(InetAddress.getLocalHost().getHostName(),
                                dir.getName(), dir.length(), fis.readAllBytes());
                        this.chatBus.transmit(fileMessage);

                        int gap = this.scrollPanel.getWidth() - 180;
                        fileMessage.setAuthor("You");
                        FieldLabel fileLabel = new FieldLabel(fileMessage);
                        this.addMessageToPanel(fileLabel, gap, "green");

                        this.setEnabled(true);
                        this.loaderLabel.setVisible(false);
                        fis.close();
                    } catch (IOException exception) {
                        this.setEnabled(false);
                        this.loaderLabel.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Can't send file:\n" + exception.getMessage());
                    }
                });
                sendThread.setDaemon(true);
                sendThread.start();
            }
        }
    }

    
    public void addMessageToPanel(JLabel label, int gap, String colorHeader) {
        EventQueue.invokeLater(() -> {
            label.setText(this.handleMessage(label.getText(), colorHeader));

            this.hParallel.addGroup(
                    this.layout.createSequentialGroup()
                            .addContainerGap(gap, gap)
                            .addComponent(label));
            this.vSequential.addComponent(label).addGap(10, 10, 10);
            this.scrollPanel.revalidate();

            
            this.scrollPanel.getViewport()
                    .setViewPosition(new Point(0, this.scrollPanel.getVerticalScrollBar().getMaximum()));
            this.scrollPanel.getViewport()
                    .setViewPosition(new Point(0, this.scrollPanel.getVerticalScrollBar().getMaximum()));
        });
    }

    
    private String handleMessage(String message, String colorHeader) {
        String formattedMessage = "<html>";
        formattedMessage += this.getHeaderName(message, colorHeader);
        formattedMessage += this.multiLinesString(message);
        formattedMessage += "</html>";
        return formattedMessage;
    }

    private String getHeaderName(String message, String colorHeader) {
        if (message.contains(":")) {
            String headerName = "<font color='" + colorHeader + "'>";
            headerName += message.substring(0, message.indexOf(':') + 1) + "</font><br>";
            return headerName;
        }
        return "";
    }

    private String multiLinesString(String message) {
        message = message.substring(message.indexOf(':') + 1);
        if (message.length() > ChatPanel.MAX_LENGTH_LINE) {
            int loops = message.length() / ChatPanel.MAX_LENGTH_LINE;
            int index = 0;
            String content = "";
            for (int i = 0; i < loops; ++i) {
                content += message.substring(index, index + ChatPanel.MAX_LENGTH_LINE) + "<br>";
                index += ChatPanel.MAX_LENGTH_LINE;
            }
            content += message.substring(index);
            return content;
        }
        return message;
    }

    
    @Override
    public void run() {
        while (true) {
            try {
                if (this.commonBus.getTcpServer().hasPartner() || this.commonBus.getTcpClient().isConnected()) {
                    Message objMessage = this.chatBus.receive();
                    if (objMessage != null) {
                        if (objMessage.getContentType() == Message.TEXT_MESSAGE) {
                            StringMessage strMessage = (StringMessage) objMessage;

                            JLabel label = new JLabel(
                                    strMessage.getAuthor() + " send a message:" + strMessage.getContentType());
                            label.setFont(new Font("consolas", Font.PLAIN, 14));
                            label.setForeground(Color.BLACK);
                            this.addMessageToPanel(label, 0, "blue");
                        } else if (objMessage.getContentType() == Message.FILE_MESSAGE) {
                            FileMessage fileMessage = (FileMessage) objMessage;
                            FieldLabel fileLabel = new FieldLabel(fileMessage);
                            this.addMessageToPanel(fileLabel, 0, "blue");
                        }
                    }
                    continue; 
                }
                Thread.sleep(1000); 
            } catch (Exception e) {
                this.commonBus.getTcpServer().setHasPartner(false);
                this.commonBus.getTcpClient().setConnected(false);

                this.root.remove(this);
                this.root.getPopupMenu().remove(item);
                this.root.addCount(-1);
                this.root.getChatPanels().remove(this);
                this.root.validate();
                this.root.revalidate();
                this.root.repaint();

                try {
                    this.chatBus.getConnection().close();
                } catch (Exception exception) {
                }

                break;
            }
        }
    }

    private ConnectionItem item;

    public void setConnectionItem(ConnectionItem item) {
        this.item = item;
    }
}
