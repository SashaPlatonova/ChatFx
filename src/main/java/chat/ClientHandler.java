package chat;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable, Closeable {

    private ChatServer chatServer;
    private Socket socket;
    private static int cnt = 0;
    private String userName;
    private ObjectInputStream is;
    private ObjectOutputStream os;
    private boolean running;
    private byte [] buffer;

    public ClientHandler(Socket socket, ChatServer chatServer) throws IOException {
        this.chatServer = chatServer;
        this.socket = socket;
        this.os = new ObjectOutputStream(socket.getOutputStream());
        this.is = new ObjectInputStream(socket.getInputStream());
        userName = "user#" + cnt;
        running = true;
        buffer = new byte[256];
    }

    @Override
    public void close() throws IOException {
        os.close();
        is.close();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Message message = (Message) is.readObject();
                System.out.println(message);
                chatServer.broadCast(message);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Exception while read");
                break;
            }
        }
    }

    public boolean isRunning() {

        return running;
    }

    public void setRunning(boolean running) {

        this.running = running;
    }

    public void sendMessage(Message message) throws IOException {
        os.writeObject(message);
        os.flush();
    }
}
