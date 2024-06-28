package bus.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicBoolean;

import bus.chat.ChatBus;
import gui.chat.MainChatPanel;

public class TcpServer {
private MainChatPanel mainChatPanel;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private String password;

    private AtomicBoolean isListening;
    private AtomicBoolean hasPartner;

    public TcpServer(MainChatPanel mainChatPanel) {
        this.mainChatPanel = mainChatPanel;
        this.serverSocket = null;
        this.clientSocket = null;
        this.password = null;
        this.isListening = new AtomicBoolean(false);
        this.hasPartner = new AtomicBoolean(false);
    }

    public void startListening(String host, int port, String password) throws IOException {
        if (!isListening.get()) {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName(host));
            this.password = password;
            isListening.set(true);
            new Thread(this::acceptConnections).start();
        }
    }

    public void stopListening() throws IOException {
        if (isListening.get()) {
            serverSocket.close();
            if (clientSocket!= null) clientSocket.close();
            isListening.set(false);
            hasPartner.set(false);
        }
    }

    public void acceptConnections() {
        while (isListening.get()) {
            try {
                clientSocket = serverSocket.accept();
                handleClientConnection(clientSocket);
            } catch (IOException e) {
                // Log error
            }
        }
    }

    private void handleClientConnection(Socket clientSocket) throws IOException {
        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
        DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
        String clientPassword = dis.readUTF();
        if (password.equals(clientPassword)) {
            dos.writeUTF("true");
            ChatBus chatBus = new ChatBus(clientSocket);
            mainChatPanel.addNewConnection(chatBus);
            hasPartner.set(true);
        } else {
            dos.writeUTF("false");
        }
    }

    public ArrayList<String> getLocalIpv4Addresses() throws SocketException {
        ArrayList<String> ipv4Addresses = new ArrayList<>();
        Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
        while (networks.hasMoreElements()) {
            NetworkInterface networkInterface = networks.nextElement();
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                if (inetAddress instanceof Inet4Address) {
                    ipv4Addresses.add(inetAddress.getHostAddress());
                }
            }
        }
        return ipv4Addresses;
    }

    public boolean isListening() {
        return isListening.get();
    }

    public boolean hasPartner() {
        return hasPartner.get();
    }

    public void setHasPartner(boolean hasPartner) {
        this.hasPartner.set(hasPartner);
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
