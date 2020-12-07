package chat;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ChatController implements Initializable{
    public TextField output;
    public TextField input;
    public TextField myOutput;
    public AnchorPane chatBox;
    private ObjectOutputStream out;
    private ObjectInputStream is;

    public ChatController(){
        try {
            Socket socket = new Socket("localhost", 8189);
            out = new ObjectOutputStream(socket.getOutputStream());
            is = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Connection Error");;
        }
        Thread readThread = new Thread(() -> {
            try {
                while (true) {
                    Message message = (Message) is.readObject();
                    output.appendText(message.getMessage());
//                    update(message);

                }
            } catch (Exception e) {
                System.out.println("Reading Error");
            }
        });
        readThread.start();
    }

    public void send(ActionEvent actionEvent) throws IOException {

            String msg = input.getText();
            myOutput.appendText(input.getText() + "\n");
            out.writeObject(Message.of("User", input.getText()));
            out.flush();
//            update(Message.of("User", input.getText()));
            input.clear();

    }

    public void quit(ActionEvent actionEvent) throws IOException {
        Parent chat = FXMLLoader.load(getClass().getResource("auth.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Авторизация");
        stage.setScene(new Scene(chat));
        stage.setResizable(false);
        stage.show();
        input.getScene().getWindow().hide();
        FileHistoryService.getInstance().save(
                Arrays.asList(output.getText().split("\n").clone()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FileHistoryService.getInstance().load().forEach(historyLine -> {
            output.appendText(historyLine + "\n");
        });
    }

//    public void update (Message message){
//    Здесь должны отрисовываться блоки с сообщениями, но что-то пошло не так((
//        TextField txt = new TextField(message.getMessage());
////        txt.setStyle("-fx-background-radius: 10");
////        txt.setStyle("-fx-background-color: #ff7a00");
////        txt.setStyle("-fx-text-inner-color: #000");
////        txt.minHeight(20);
////        txt.setStyle("-fx-padding: 20 20");
//        txt.getStyleClass().add("textField");
//        txt.setText(message.getMessage());
//        chatBox.getChildren().addAll(txt);
//
//    }
}
