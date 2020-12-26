package chat;
import java.io.*;
import java.util.ArrayList;


public class FileHistoryService implements HistoryService{

    private static FileHistoryService instanse;
    private String path = "C:\\Users\\User\\Desktop\\geekbrains\\ChatFx\\src\\main\\resources\\chat\\history.txt";
    private ObjectOutputStream out;
    private ObjectInputStream in;


    private FileHistoryService() {

    }

    public static FileHistoryService getInstance() {
        return instanse == null ?
                new FileHistoryService() : instanse;
    }
    @Override
    public void save(ArrayList<Message> chat, String fileName) {
        try {
            out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(chat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Message> load(String fileName) {
        try {
            in = new ObjectInputStream(new FileInputStream(fileName));
            ArrayList<Message> chatHistory;
           chatHistory = (ArrayList<Message>) in.readObject();
            return chatHistory;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("File not found");
        }
    }
}
