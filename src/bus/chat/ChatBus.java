package bus.chat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.net.Socket;

public class ChatBus {
    private Socket connection;

    public ChatBus(Socket connection) {
        this.connection = connection;
    }

    public void transmit(Message message) throws IOException {
        DataOutputStream output = new DataOutputStream(connection.getOutputStream());
        byte[] data = serializeMessage(message);
        output.writeInt(data.length);
        output.write(data);
    }

    public Message receive() throws IOException {
        DataInputStream input = new DataInputStream(connection.getInputStream());
        int length = input.readInt();
        byte[] data = new byte[length];
        input.readFully(data);
        return deserializeMessage(data);
    }

    public Socket getConnection() {
        return this.connection;
    }

    private byte[] serializeMessage(Message message) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException("Error serializing payload", e);
        }
        return bos.toByteArray();
    }

    private Message deserializeMessage(byte[] data) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        try (ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error deserializing message", e);
        }
    }
}
