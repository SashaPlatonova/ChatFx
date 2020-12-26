package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Network {

    private static Network instance;
    private final ObjectInputStream is;
    private final ObjectOutputStream out;

    private Network() {
        try {
            Socket socket = new Socket("localhost", 8080);
            out = new ObjectOutputStream(socket.getOutputStream());
            is = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException("Couldn't create network connection");
        }
    }

    public static Network getInstance() {
        if (instance == null) {
            instance = new Network();
        }
        return instance;
    }

    public Message read() throws IOException, ClassNotFoundException {
        return (Message) is.readObject();
    }

    public String getUserName() {
        try {
            return ((Message) is.readObject()).getAuthor();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void write(Message message) throws IOException {
        out.writeObject(message);
        out.flush();
    }

    public ObjectInputStream getInputStream() {
        return is;
    }
}
