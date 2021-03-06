package chat;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegistrationController {
    public TextField login;
    public TextField password;
    private ObjectInputStream is;

    public void enter(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {

        Network.getInstance().write(Message.of(login.getText(),"/registr " +
                login.getText() + " " + password.getText()));
        System.out.println("Пользователь отправил запрос о регистрации");
        Parent chat = FXMLLoader.load(getClass().getResource("chat.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Сетевой чат");
        stage.setScene(new Scene(chat));
        stage.setResizable(false);
        stage.show();
        login.getScene().getWindow().hide();

    }


}
