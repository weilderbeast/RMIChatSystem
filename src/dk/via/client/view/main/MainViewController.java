package dk.via.client.view.main;

import com.sun.webkit.dom.KeyboardEventImpl;
import dk.via.client.core.ViewHandler;
import dk.via.shared.transfer.Message;
import dk.via.shared.transfer.Request;
import dk.via.shared.utils.Conversation;
import dk.via.shared.utils.Date;
import dk.via.shared.utils.UserAction;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class MainViewController {
    private MainViewModel viewModel;
    private ViewHandler viewHandler;
    private String lastSender = "";
    private StringProperty SOURCE;
    private StringProperty nickname;

    @FXML
    private TextField textField;
    @FXML
    private VBox chatVBox;
    @FXML
    private VBox connectedUsersVBox;
    @FXML
    private Label currentTime;
    @FXML
    private Button sendButton;
    @FXML
    private Label chatName;
    @FXML
    private Label generalChat;
    @FXML
    private ScrollPane chatScrollPane;

    public void init(MainViewModel mainViewModel, ViewHandler viewHandler) {
        viewModel = mainViewModel;
        this.viewHandler = viewHandler;

        SOURCE = new SimpleStringProperty();
        SOURCE.bindBidirectional(viewModel.getSOURCE());
        nickname = new SimpleStringProperty();
        nickname.bind(viewModel.getNickname());

        textField.setText("");
        textField.textProperty().bindBidirectional(viewModel.getSentText());

        chatVBox.setSpacing(10);
        connectedUsersVBox.setSpacing(10);

        viewModel.addListener(UserAction.TEXT.toString(), event -> updateChat((Message) event.getNewValue()));
        viewModel.addListener(UserAction.USER_LIST.toString(), event -> updateUsersList(event));
        //viewModel.addListener(UserAction.TEXT.toString(), event -> createMessage(event));
        viewModel.addListener(UserAction.LOAD_LOGS.toString(), event -> loadChat(event));
        viewModel.addListener(UserAction.NOTIFICATION.toString(), event -> ping(event));

        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    sendMessage();
                }
            }
        });

        generalChat.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            SOURCE.setValue("General");
            chatName.setText("General");
            chatVBox.getChildren().clear();
            viewModel.loadLogs();
        });

        System.out.println("Created controller.");
        viewModel.loadUsers();
    }

    private void ping(PropertyChangeEvent event) {
        System.out.println("got a message from someone else");
        Message message = (Message) event.getNewValue();
        Platform.runLater(() -> {
            for(int i=0;i<connectedUsersVBox.getChildren().size();i++){
                HBox hBox = (HBox)connectedUsersVBox.getChildren().get(i);
                VBox vBox = (VBox)hBox.getChildren().get(0);
                Label timeStamp = (Label)hBox.getChildren().get(1);
                Label lastSent = (Label)vBox.getChildren().get(1);

                Date date = new Date();
                //TODO the last message sent and the timestamp
                timeStamp.setText(date.getTimestamp());
                //lastSent.set
            }
        });
    }

    private void loadChat(PropertyChangeEvent event) {
        if (event.getNewValue() != null) {
            chatVBox.getChildren().clear();
            Conversation conversation = (Conversation) event.getNewValue();
            ArrayList<Message> messages = conversation.getMessages();
            for (Message message : messages) {
                updateChat(message);
            }
        }
    }

    public void sendMessage() {
        System.out.println("current source: "+SOURCE.getValue());
        if (!textField.getText().equals(null) && !textField.getText().equals("")) {
            viewModel.sendMessage();
            updateChat(new Message(nickname.getValue(), textField.getText(), SOURCE.getValue()));
            textField.clear();
        } else {
            System.out.println("empty field, did not send");
        }
    }

    public void disconnect() {
        viewModel.disconnect();
    }

    public void updateChat(Message message) {
        Platform.runLater(() -> {
            if (message.getMessageSender().equals(nickname.getValue()))
                chatVBox.getChildren().add(createPersonalMessage(message));
            else chatVBox.getChildren().add(createReceivedMessage(message));

            //scrolls the last message into view
            chatVBox.heightProperty().addListener((observable -> {
                chatScrollPane.setVvalue(1.0d);
            }));
        });
    }

    public void updateUsersList(PropertyChangeEvent event) {
        Platform.runLater(() -> {
            ArrayList<String> userList = (ArrayList<String>) event.getNewValue();
            connectedUsersVBox.getChildren().clear();
            for (int i = 0; i < userList.size(); i++) {
                int other_i = i;
                HBox hBox = createUserListing(userList.get(i));
                hBox.setStyle("-fx-background-color: #009687;-fx-padding: 5px;-fx-text-fill: white;-fx-cursor: hand");

                hBox.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
                    SOURCE.setValue(userList.get(other_i));
                    chatName.setText(SOURCE.getValue());
                    chatVBox.getChildren().clear();
                    viewModel.loadLogs();
                });

                connectedUsersVBox.getChildren().add(hBox);
            }
            connectedUsersVBox.setStyle("-fx-text-fill: white");
        });
    }

    public void updateCurrentTime() {
        Platform.runLater(() -> {

        });
    }

    private VBox createPersonalMessage(Message message) {

        Label messageLabel = new Label(message.getMessageBody());
        Label timeStamp = new Label(message.getTimeStamp());

        messageLabel.getStyleClass().add("chat-message-label-sent");
        messageLabel.setMaxWidth(400);
        messageLabel.setWrapText(true);

        HBox secondaryMessageBox = new HBox(timeStamp, messageLabel);
        secondaryMessageBox.setAlignment(Pos.BOTTOM_RIGHT);
        VBox messageBox = new VBox(secondaryMessageBox);
        messageBox.setAlignment(Pos.BASELINE_RIGHT);
        messageBox.setSpacing(5);

        return messageBox;
    }

    private VBox createReceivedMessage(Message message) {

        Label messageLabel = new Label(message.getMessageBody());
        Label senderLabel = new Label(message.getMessageSender());
        Label timeStamp = new Label(message.getTimeStamp());

        messageLabel.getStyleClass().add("chat-message-label-received");
        senderLabel.getStyleClass().add("chat-sender");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(410);

        VBox messageBox;
        HBox secondaryMessageBox = new HBox(messageLabel, timeStamp);
        secondaryMessageBox.setMaxWidth(450);
        secondaryMessageBox.setAlignment(Pos.BOTTOM_LEFT);
        if (SOURCE.getValue().equals("General")) {
            if (!lastSender.equals(message.getMessageSender())) {
                messageBox = new VBox(senderLabel, secondaryMessageBox);
                messageBox.setSpacing(5);
            } else {
                messageBox = new VBox(secondaryMessageBox);
                messageBox.setSpacing(0);
            }
            lastSender = message.getMessageSender();
        } else {
            messageBox = new VBox(secondaryMessageBox);
            messageBox.setSpacing(0);
        }
        messageBox.setAlignment(Pos.BASELINE_LEFT);
        return messageBox;
    }

    private HBox createUserListing(String name) {
        Label userName = new Label(name);
        Label lastSentText = new Label("");
        Label timeStamp = new Label("");

        timeStamp.setAlignment(Pos.BASELINE_RIGHT);

        VBox vBox = new VBox(userName, lastSentText);
        vBox.setStyle("-fx-padding: 10px");

        HBox hBox = new HBox(vBox, timeStamp);
        hBox.setSpacing(1);
        hBox.setAlignment(Pos.CENTER_LEFT);

        return hBox;
    }


}
