package dk.via.client.view.main;

import dk.via.client.model.ChatSystem;
import dk.via.shared.transfer.Message;
import dk.via.shared.transfer.Request;
import dk.via.shared.utils.Subject;
import dk.via.shared.utils.UserAction;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class MainViewModel implements Subject {
    private ChatSystem chatSystem;
    private StringProperty sentText;
    private ObservableList<String> users;
    private PropertyChangeSupport support;
    private String lastSender = "";

    public MainViewModel(ChatSystem chatSystem) {
        this.chatSystem = chatSystem;

        sentText = new SimpleStringProperty();

        this.chatSystem.addListener(UserAction.RECEIVE_ALL.toString(), this::onGroupMessage);
        this.chatSystem.addListener(UserAction.RECEIVE.toString(), this::onPrivateMessage);

        this.chatSystem.addListener(UserAction.USER_LIST.toString(), this::getUserList);

        support = new PropertyChangeSupport(this);
    }

    private void getUserList(PropertyChangeEvent propertyChangeEvent) {

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
        VBox vBox = createMessage(message);
        support.firePropertyChange(UserAction.TEXT.toString(), null, vBox);
        lastSender = message.getMessageSender();
    }

    public void sendGroupMessage() {
        chatSystem.sendGroupMessage(sentText.get());
        VBox vBox = createMessage();
        support.firePropertyChange(UserAction.TEXT.toString(), null, vBox);
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
}
