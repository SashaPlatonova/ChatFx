package chat;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ChatController implements Initializable{
    public TextArea output;
    public TextField input;
    public TextArea myOutput;
    private String userName;
    private ObjectInputStream is;
    private ChatReader read;

    public void send(ActionEvent actionEvent) throws IOException {
        Network.getInstance().write(Message.of(userName, input.getText()));
        myOutput.appendText(input.getText() + "\n");
        input.clear();
    }

    public void quit(ActionEvent actionEvent) throws IOException {
        Network.getInstance().write(Message.of(userName, "/left"));
        Parent chat = FXMLLoader.load(getClass().getResource("auth.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Авторизация");
        stage.setScene(new Scene(chat));
        stage.setResizable(false);
        stage.show();
        input.getScene().getWindow().hide();
    }
    public void group(ActionEvent actionEvent) throws IOException {
        Network.getInstance().write(Message.of(userName, "/group " + input.getText()));
        input.clear();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userName = Network.getInstance().getUserName();
        ArrayList<Message> history = FileHistoryService.getInstance().load(userName + ".txt");
        for (Message msg : history) {
            if(msg.getAuthor().equals("/my")){
                myOutput.appendText(msg.toString());
                output.appendText("\n");
            } else {
                output.appendText(msg.toString());
                myOutput.appendText("\n");
            }
        }
        read = new ChatReader(output, myOutput, Network.getInstance().getInputStream());
        read.start();
    }


}
