package chat;

import java.util.ArrayList;
import java.util.List;

public interface HistoryService {
    void save(ArrayList<Message> chat, String fileName);

    List<Message> load(String fileName);
}
