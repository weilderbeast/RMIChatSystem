package dk.via.client.view.main;

import dk.via.client.model.ChatSystem;
import dk.via.shared.transfer.Message;
import dk.via.shared.transfer.Request;
import dk.via.shared.utils.Subject;
import dk.via.shared.utils.UserAction;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class MainViewModel implements Subject {
    private final ChatSystem chatSystem;
    private final StringProperty sentText;
    private final ArrayList<String> onlineUsers;
    private final PropertyChangeSupport support;

    public MainViewModel(ChatSystem chatSystem) {
        this.chatSystem = chatSystem;

        sentText = new SimpleStringProperty();
        onlineUsers = new ArrayList<>();

        this.chatSystem.addListener(UserAction.RECEIVE_ALL.toString(), this::onGroupMessage);
        this.chatSystem.addListener(UserAction.RECEIVE.toString(), this::onPrivateMessage);
        this.chatSystem.addListener(UserAction.USER_LIST.toString(), this::createUserList);

        support = new PropertyChangeSupport(this);
    }

    private void createUserList(PropertyChangeEvent propertyChangeEvent) {
        Request request = (Request) propertyChangeEvent.getNewValue();
        ArrayList<String> users = (ArrayList<String>) request.getObject();
        System.out.println("Received user list: " + users.toString());
        onlineUsers.clear();
        for (String user : users) {
            onlineUsers.add(user);
        }
        support.firePropertyChange(UserAction.USER_LIST.toString(), null, onlineUsers);
    }

    public StringProperty getSentText() {
        return sentText;
    }

    private void onPrivateMessage(PropertyChangeEvent propertyChangeEvent) {
        Request request = (Request) propertyChangeEvent.getNewValue();
        Message message = (Message) request.getObject();

    }

    private void onGroupMessage(PropertyChangeEvent propertyChangeEvent) {
        Request request = (Request) propertyChangeEvent.getNewValue();
        Message message = (Message) request.getObject();
        support.firePropertyChange(UserAction.TEXT.toString(), null, message);
    }

    public void sendGroupMessage() {
        chatSystem.sendGroupMessage(sentText.get());
    }

    public void disconnect() {
        chatSystem.disconnect();
    }

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }
}
