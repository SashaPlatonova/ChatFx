package chat;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable, Closeable {
//команда: /left отправляется при нажатии на кнопку выхода пользователем;
//команда: /private nick - отправка сообщения только пользователю с именем nick;
//команда: /changeNick - смена имени пользователя;
//команда: /group - генерируется при нажатии пользователем на кнопку формирования группы,
// при этом в строке ввода указывается название создаваемой группы и имена пользователей,
// которые туда будут добавлены;
// команда: /toGroup name - отправка сообщения всем учатникам группы с названием name;
// команда: /my - генерируется сервером для отображения сообщения у его автора в правой части экрана.
// команда: /auth - генерируется при регистрации для установки имени пользователя согласно данным,
// введенным в поле "логин".
    private final ChatServer chatServer;
    private final Socket socket;
    private static int cnt = 0;
    private String userName;
    private final ObjectInputStream is;
    private final ObjectOutputStream out;
    private boolean running;
    private byte [] buffer;

    public ClientHandler(Socket socket, ChatServer chatServer) throws IOException, ClassNotFoundException {
        this.chatServer = chatServer;
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.is = new ObjectInputStream(socket.getInputStream());
        Message message = (Message) is.readObject();
        userName = message.getAuthor();
        cnt++;
        running = true;
        buffer = new byte[256];
        out.writeObject(Message.of(userName, "OK"));
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
        is.close();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Message message = (Message) is.readObject();
                String[] data = message.getMessage().split(" ");
                String command = data[0];
                switch (command){
                    case "/auth":
                        chatServer.broadCast(Message.of(userName, " connected"));
                        continue;
                    case "/left": chatServer.broadCast(Message.of(userName, " Left chat\n"));
                                chatServer.kickClient(this);
                                continue;
                    case "/changeNick":  String oldName = userName;
                        userName = data[1];
                        String msg = String.format("User %s change name to %s", oldName, userName);
                        sendMessage(Message.of(userName, msg));
                        continue;
                    case "/private": String privateMsg = data[2];
                        for (int i = 3; i <data.length ; i++) {
                            privateMsg += data[i];
                        }
                        chatServer.sendMessageTo(data[1], privateMsg, userName);
                        continue;
                    case "/group": String nameOfGroup = data[1];
                        ArrayList<String> members = new ArrayList<>();
                        for (int i = 2; i< data.length; i++){
                            members.add(data[i]);
                        }
                        members.add(userName);
                        chatServer.newGroup(nameOfGroup, members);
                        chatServer.messageToGroup(nameOfGroup,
                                Message.of(" ", "You were added to the group " + nameOfGroup));
                            continue;
                    case "/toGroup": String name = data[1];
                        if(!chatServer.getGroups().containsKey(name)){
                            continue;
                        }
                        String msgToGroup = data[2];
                            for (int i = 3; i <data.length ; i++) {
                                msgToGroup += data[i];
                            }
                            chatServer.messageToGroup(name,Message.of(userName, msgToGroup));
                            continue;

                    default:
                        message.setAuthor(userName);
                        System.out.println(message);
                        chatServer.broadCast(message);
                }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void sendMessage(Message message) throws IOException {
        out.writeObject(message);
        out.flush();
    }
}
