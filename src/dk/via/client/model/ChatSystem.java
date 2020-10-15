package dk.via.client.model;

import dk.via.shared.transfer.Message;
import dk.via.shared.transfer.Request;
import dk.via.shared.utils.Subject;
import javafx.beans.property.StringProperty;

import java.beans.PropertyChangeEvent;

public interface ChatSystem extends Subject {
    void startClient(String nickname);
    void disconnect();
    void sendMessage(Message message);
    StringProperty getNickname();
}
