package gui.chat;

import javax.swing.*;

import bus.chat.ChatBus;
import bus.common.CommonBus;
import gui.client.ClientPanel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class MainChatPanel extends JPanel{
private JLabel connectionsLabel;
    private JMenuBar menuBar;
    private JPopupMenu popupMenu;

    private ArrayList<ChatPanel> chatPanels;

    private CommonBus commonBus;

    private int count;

    public MainChatPanel(){
        this.chatPanels = new ArrayList<>();

        initUI();
    }

    public MainChatPanel(CommonBus commonBus) {
        this.commonBus = commonBus;
        this.chatPanels = new ArrayList<>();

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.decode(ClientPanel.BACKGROUND_COLOR));

        menuBar = new JMenuBar();
        connectionsLabel = new JLabel();
        popupMenu = new JPopupMenu();

        connectionsLabel.setText(getConnectionsLabelText(0));
        connectionsLabel.setFont(new Font("segoe ui", Font.PLAIN, 13));
        connectionsLabel.addMouseListener(new ConnectionLabelMouseListener());

        menuBar.add(connectionsLabel);
        menuBar.setLayout(new GridBagLayout());
        add(menuBar, BorderLayout.NORTH);
    }

    private String getConnectionsLabelText(int count) {
        return "<html>All connections <font color='red'>(" + count + ")</font></html>";
    }

    public void addCount(int n) {
        count += n;
        connectionsLabel.setText(getConnectionsLabelText(count));
    }

    private class ConnectionLabelMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                popupMenu.show(connectionsLabel, 130, 5);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            connectionsLabel.setFont(new Font("segoe ui", Font.BOLD, 13));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            connectionsLabel.setFont(new Font("segoe ui", Font.PLAIN, 13));
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    public ArrayList<ChatPanel> getChatPanels() {
        return chatPanels;
    }

    public void addNewConnection(ChatBus chatBus) {
        ChatPanel chatPanel = new ChatPanel(this, commonBus, chatBus);
        ConnectionItem item = new ConnectionItem(chatPanel, chatPanels);
        chatPanel.setConnectionItem(item);
        add(chatPanel, BorderLayout.CENTER);
        chatPanels.add(chatPanel);
        popupMenu.add(item);

        addCount(1);
        revalidate();
        repaint();
    }
}
