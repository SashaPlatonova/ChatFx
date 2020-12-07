package chat;

public interface AuthService {
    void addUser(String name, String pass);

    boolean auth(String name, String pass);
}
