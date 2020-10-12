package dk.via.client.view.main;

import dk.via.client.model.ChatSystem;
import dk.via.shared.transfer.Message;
import dk.via.shared.transfer.Request;
import dk.via.shared.transfer.UserList;
import dk.via.shared.utils.Subject;
import dk.via.shared.utils.UserAction;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class MainViewModel implements Subject {
    private ChatSystem chatSystem;
    private StringProperty sentText;
    private ObservableList<String> users;
    private PropertyChangeSupport support;

    public MainViewModel(ChatSystem chatSystem) {
        this.chatSystem = chatSystem;

        sentText = new SimpleStringProperty();

        this.chatSystem.addListener(UserAction.RECEIVE_ALL.toString(), this::onGroupMessage);
        this.chatSystem.addListener(UserAction.RECEIVE.toString(), this::onPrivateMessage);

        this.chatSystem.addListener(UserAction.USER_LIST.toString(), this::getUserList);

        support = new PropertyChangeSupport(this);
    }

    private void getUserList(PropertyChangeEvent propertyChangeEvent) {
        Request request = (Request) propertyChangeEvent.getNewValue();
        UserList requestUserList = (UserList) request.getObject();
        ArrayList<String> userList = requestUserList.getUsers();
        System.out.println("UserList:");
        for (int i = 0; i < userList.size(); i++) {
            System.out.println(userList.get(i));
        }
//        users = (ObservableList<String>) propertyChangeEvent.getNewValue();
    }

    public StringProperty getSentText() {
        return sentText;
    }

    private void onPrivateMessage(PropertyChangeEvent propertyChangeEvent) {
        Request request = (Request) propertyChangeEvent.getNewValue();
        Message message = (Message) request.getObject();
        System.out.println(message.getMessageSender() + ":" + message.getMessageBody() + " to " + message.getMessageReceiver());
    }

    private void onGroupMessage(PropertyChangeEvent propertyChangeEvent) {
        Request request = (Request) propertyChangeEvent.getNewValue();
        Message message = (Message) request.getObject();
        System.out.println(message.getMessageSender() + ":" + message.getMessageBody());

        Label messageLabel = new Label(message.getMessageBody());
        Label senderLabel = new Label(message.getMessageSender() + ": ");
        messageLabel.setStyle("-fx-text-fill: white");
        senderLabel.setStyle("-fx-text-fill: white");
        messageLabel.getStyleClass().add("chat-message-label-received");
        messageLabel.setWrapText(true);
        HBox messageBox = new HBox(senderLabel, messageLabel);
        messageBox.setAlignment(Pos.BASELINE_LEFT);


        support.firePropertyChange(UserAction.TEXT.toString(), null, messageBox);
    }

    public void sendGroupMessage() {
        chatSystem.sendGroupMessage(sentText.get());

        Message message = new Message("", sentText.get());

        Label messageLabel = new Label(message.getMessageBody());
        messageLabel.setStyle("-fx-text-fill: white");
        messageLabel.getStyleClass().add("chat-message-label-sent");
        messageLabel.setWrapText(true);
        HBox messageBox = new HBox(messageLabel);
        messageBox.setAlignment(Pos.BASELINE_RIGHT);


        support.firePropertyChange(UserAction.TEXT.toString(), null, messageBox);
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
