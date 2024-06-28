package gui.chat;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ConnectionItem extends JMenuItem{
    private ChatPanel chatPanel;
    private ArrayList<ChatPanel> chatPanels;

    public ConnectionItem(ChatPanel chatPanel, ArrayList<ChatPanel> chatPanels) { 
        this.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/online.png")));
        this.setText(chatPanel.getHostName());
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                connectionItemMousePressed(e);
            }
        });

        this.chatPanel = chatPanel;
        this.chatPanels = chatPanels;
    }

    private void connectionItemMousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            chatPanels.forEach(panel -> panel.setVisible(panel == chatPanel));
        }
    }

}
