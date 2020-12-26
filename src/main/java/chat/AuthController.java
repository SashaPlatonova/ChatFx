package chat;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthController {
    public TextField login;
    public TextField password;

    public void enter(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        Network.getInstance().write(Message.of(login.getText(),"/auth "
                + login.getText() + " " + password.getText()));
        Message msg = (Message) Network.getInstance().read();
        System.out.println(msg.toString());
        if(msg.getMessage().equals("connected")){
//        int countUser = 0;
//        if(!login.getText().equals("") && !password.getText().equals("")){
//            countUser = authUser(login.getText(), password.getText());
//        }
//
//        if (countUser!=0) {
            Parent chat = FXMLLoader.load(getClass().getResource("chat.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Сетевой чат");
            stage.setScene(new Scene(chat));
            stage.setResizable(false);
            stage.show();
            login.getScene().getWindow().hide();

        } else {
            login.setText("WRONG LOGIN OR PASSWORD");
            password.clear();
        }
    }

    public void reg(ActionEvent actionEvent) throws IOException {
        Parent reg = FXMLLoader.load(getClass().getResource("registration.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Регистрация");
        stage.setScene(new Scene(reg));
        stage.setResizable(false);
        stage.show();
        login.getScene().getWindow().hide();
    }
}
