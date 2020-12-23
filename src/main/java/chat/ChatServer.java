package chat;

import chat.ClientHandler;
import chat.Message;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ChatServer {

    private final int PORT = 8080;
    private boolean running;
    private ConcurrentLinkedDeque<ClientHandler> clients = new ConcurrentLinkedDeque<>();
    private int counter = 0;
    private HashMap<String, ArrayList<String>> groups = new HashMap<>();

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
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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
            if (client.getUserName().equals(message.getAuthor())){
                client.sendMessage(Message.of("/my",message.getMessage() ));
                continue;
            }
            client.sendMessage(message);
        }
    }

    public void kickClient(ClientHandler client){
        clients.remove(client);
    }

    public void sendMessageTo(String nick, String msg, String from) throws IOException {
        for (ClientHandler client:clients) {
            if(client.getUserName().equals(nick)){
                client.sendMessage(Message.of(from, msg));
            }
        }
    }

    public HashMap<String, ArrayList<String>> getGroups() {
        return groups;
    }

    public void newGroup(String name, ArrayList<String> members){
        if(!groups.containsKey(name)) {
            groups.put(name, members);
        }
    }

    public void dropGroup(String name){
       if(groups.containsKey(name)) {
           groups.remove(name);
       }
    }
    public void messageToGroup(String name, Message message) throws IOException {
        if(groups.containsKey(name)){
            ArrayList<String> nicks = groups.get(name);
            for (ClientHandler client:clients) {
                for (String nick: nicks){
                    if(client.getUserName().equals(nick)){
                        client.sendMessage(message);
                    }
                }
            }
        }
    }

    public int authUser(String login, String pass) throws SQLException, ClassNotFoundException {
        int count = 0;
        DBHandler db = new DBHandler();
        ResultSet res = db.getUser(login, pass);
        while (res.next()){
            count++;
        }
        return count;
    }

    public void regUser(String login, String pass) throws SQLException, ClassNotFoundException {
        DBHandler db = new DBHandler();
        db.addUser(login, pass);
    }

    public void changeNick(String oldLog, String newLog) throws SQLException, ClassNotFoundException {
        DBHandler db = new DBHandler();
        db.changeNick(oldLog, newLog);
    }
    public static void main(String[] args) {
        new ChatServer();
    }
}
