package chat;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.ObjectInputStream;
public class ChatReader extends Thread{
        private TextArea outp;
        private TextArea myOutput;
        private final ObjectInputStream is;
        private boolean running;

        public ChatReader(TextArea out, TextArea myOutput, ObjectInputStream is) {
            this.outp = out;
            this.is = is;
            this.myOutput = myOutput;
            running = true;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    Message message = (Message) is.readObject();
                    if(message.getAuthor().equals("/my")){
                        outp.appendText("\n");
                        continue;
                    }
                    outp.appendText(message.toString());
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        public void setRunning(boolean running) {
            this.running = running;
        }
}
