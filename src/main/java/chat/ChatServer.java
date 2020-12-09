package chat;

import chat.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ChatServer {

    private final int PORT = 8189;
    private boolean running;
    private ConcurrentLinkedDeque<ClientHandler> clients = new ConcurrentLinkedDeque<>();
    private int counter = 0;


    public ChatServer(){
        running = true;
        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Server started!");
            while (running){
                System.out.println("Server is waiting connection");
                Socket socket = serverSocket.accept();
                System.out.println("Client accepted!");
                counter++;
                ClientHandler handler = new ClientHandler(socket, this);
                clients.add(handler);
                new Thread(handler).start();
                System.out.println("Connected user: " + counter);
            }
        } catch (IOException e) {
            System.out.println("Server was broken");
        }
    }

    public boolean isRunning() {

        return running;
    }

    public void setRunning(boolean running) {

        this.running = running;
    }

    public void broadCast(Message message) throws IOException {
        for (ClientHandler client: clients) {
            client.sendMessage(message);
        }
    }

    public void kickClient(ClientHandler client){

        clients.remove(client);
    }

    public static void main(String[] args) {

        new ChatServer();
    }
}
