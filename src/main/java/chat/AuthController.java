package chat;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthController {
    public TextField login;
    public TextField password;

    public void enter(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        int countUser = 0;
        if(!login.getText().equals("") && !password.getText().equals("")){
            countUser = authUser(login.getText(), password.getText());
        }
//        boolean auth = MockAuthServiceImpl.getInstance()
//                .auth(login.getText(), password.getText());
        if (countUser!=0) {
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

    private int authUser(String text, String text1) throws SQLException, ClassNotFoundException {
        int count = 0;
        DBHandler db = new DBHandler();
        ResultSet res = db.getUser(text, text1);
        while (res.next()){
            count++;
        }
        return count;
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
