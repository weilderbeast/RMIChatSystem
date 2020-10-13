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
    private final ObservableList<HBox> onlineUsers;
    private final ObservableList<HBox> currentUserList;
    private final PropertyChangeSupport support;
    private String lastSender = "";

    public MainViewModel(ChatSystem chatSystem) {
        this.chatSystem = chatSystem;

        sentText = new SimpleStringProperty();
        onlineUsers = FXCollections.observableArrayList();
        currentUserList = FXCollections.observableArrayList();

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
            createUserListing(user);
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
        support.firePropertyChange(UserAction.TEXT.toString(), null, createMessage(message));
        lastSender = message.getMessageSender();
    }

    public void sendGroupMessage() {
        chatSystem.sendGroupMessage(sentText.get());
        lastSender = "";
        support.firePropertyChange(UserAction.TEXT.toString(), null, createMessage());
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

    //TODO these should be made in the controller
    //TODO ask troels about this
    private VBox createMessage() {
        Message message = new Message("Me", sentText.get());

        Label messageLabel = new Label(message.getMessageBody());
        messageLabel.getStyleClass().add("chat-message-label-sent");
        messageLabel.setWrapText(true);

        VBox messageBox = new VBox(messageLabel);
        messageBox.setAlignment(Pos.BASELINE_RIGHT);

        return messageBox;
    }

    private VBox createMessage(Message message) {
        Label messageLabel = new Label(message.getMessageBody());
        Label senderLabel = new Label(message.getMessageSender());

        messageLabel.getStyleClass().add("chat-message-label-received");
        senderLabel.getStyleClass().add("chat-sender");
        messageLabel.setWrapText(true);

        VBox messageBox;

        if (!lastSender.equals(message.getMessageSender())) {
            messageBox = new VBox(senderLabel, messageLabel);
        } else {
            messageBox = new VBox(messageLabel);
        }

        lastSender = message.getMessageSender();
        messageBox.setAlignment(Pos.BASELINE_LEFT);
        return messageBox;
    }

    private void createUserListing(String name) {
        Label userName = new Label(name);
        Label lastSentText = new Label("placeholder");
        Label timeStamp = new Label("1:47PM");

        timeStamp.setAlignment(Pos.BASELINE_RIGHT);

        VBox vBox = new VBox(userName, lastSentText);
        vBox.setStyle("-fx-padding: 10px");

        HBox hBox = new HBox(vBox, timeStamp);
        hBox.setSpacing(1);
        hBox.setAlignment(Pos.CENTER_LEFT);

        onlineUsers.add(hBox);
    }
}
