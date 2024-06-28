package bus.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import bus.chat.ChatBus;
import gui.chat.MainChatPanel;

public class TcpClient {
private MainChatPanel mainChatPanel;

    private Socket client;
    private AtomicBoolean isConnected;

    public TcpClient(MainChatPanel mainChatPanel) {
        this.mainChatPanel = mainChatPanel;
        this.client = null;
        this.isConnected = new AtomicBoolean(false);
    }

    public void connectToTcpServer(String host, int port, String password) throws IOException {
        if (!isConnected.get()) {
            client = new Socket(host, port);
            try (DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                 DataInputStream dis = new DataInputStream(client.getInputStream())) {
                dos.writeUTF(password);
                String result = dis.readUTF();

                if (result.equals("true")) {
                    ChatBus chatBus = new ChatBus(client);
                    mainChatPanel.addNewConnection(chatBus);
                    isConnected.set(true);
                } else if (result.equals("false")) {
                    client.close();
                    throw new IOException("Wrong password of server");
                }
            }
        }
    }

    public void disconnectFromTcpServer() throws IOException {
        if (isConnected.get()) {
            client.close();
            isConnected.set(false);
        }
    }

    public boolean isConnected() {
        return isConnected.get();
    }

    public void setConnected(boolean connected) {
        isConnected.set(connected);
    }

    public Socket getClient() {
        return client;
    }
}
