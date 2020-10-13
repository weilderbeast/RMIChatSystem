package dk.via.client.view.main;

import com.sun.webkit.dom.KeyboardEventImpl;
import dk.via.client.core.ViewHandler;
import dk.via.shared.transfer.Message;
import dk.via.shared.transfer.Request;
import dk.via.shared.utils.Date;
import dk.via.shared.utils.UserAction;
import javafx.application.Platform;
import javafx.beans.Observable;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class MainViewController {
    private MainViewModel viewModel;
    private ViewHandler viewHandler;
    private String lastSender = "";

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

    public void init(MainViewModel mainViewModel, ViewHandler viewHandler) {
        viewModel = mainViewModel;
        this.viewHandler = viewHandler;
        textField.textProperty().bindBidirectional(viewModel.getSentText());
        chatVBox.setSpacing(10);
        connectedUsersVBox.setSpacing(10);
        viewModel.addListener(UserAction.TEXT.toString(), event -> updateGeneralChat(event));
        viewModel.addListener(UserAction.USER_LIST.toString(), event -> updateUsersList(event));
        viewModel.addListener(UserAction.TEXT.toString(), event -> createMessage(event));

        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode().equals(KeyCode.ENTER)){
                    sendGroupMessage();
                }
            }
        });
    }

    public void sendGroupMessage() {
        if (!textField.getText().equals(null) && !textField.getText().equals("")) {
            viewModel.sendGroupMessage();
            chatVBox.getChildren().add(createMessage());
            textField.clear();
        } else {
            System.out.println("it's empty no send duh");
        }
    }

    public void disconnect() {
        viewModel.disconnect();
    }

    public void updateGeneralChat(PropertyChangeEvent event){
        Platform.runLater(() -> {
            chatVBox.getChildren().add(createMessage(event));
        });
    }

    public void updateUsersList(PropertyChangeEvent event) {
        Platform.runLater(() -> {
            ArrayList<String> userList = (ArrayList<String>)event.getNewValue();
            connectedUsersVBox.getChildren().clear();
            for(int i=0;i<userList.size();i++)
            {
                HBox hBox = createUserListing(userList.get(i));
                hBox.setStyle("-fx-background-color: #009687;-fx-padding: 5px;-fx-text-fill: white;-fx-cursor: hand");
                connectedUsersVBox.getChildren().add(hBox);
            }
            connectedUsersVBox.setStyle("-fx-text-fill: white");
        });
    }

    public void updateCurrentTime(){
        Platform.runLater(() -> {

        });
    }

    private VBox createMessage(PropertyChangeEvent event){

        Message message = (Message) event.getNewValue();

        Label messageLabel = new Label(message.getMessageBody());
        Label senderLabel = new Label(message.getMessageSender());
        Label timeStamp = new Label(message.getTimeStamp());

        messageLabel.getStyleClass().add("chat-message-label-received");
        senderLabel.getStyleClass().add("chat-sender");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(410);

        VBox messageBox;
        HBox secondaryMessageBox = new HBox(messageLabel,timeStamp);
        secondaryMessageBox.setMaxWidth(450);
        secondaryMessageBox.setAlignment(Pos.BOTTOM_LEFT);
        if (!lastSender.equals(message.getMessageSender())) {
            messageBox = new VBox(senderLabel, secondaryMessageBox);
            messageBox.setSpacing(5);
        } else {
            messageBox = new VBox(secondaryMessageBox);
            messageBox.setSpacing(0);
        }

        lastSender = message.getMessageSender();
        messageBox.setAlignment(Pos.BASELINE_LEFT);

        return messageBox;
    }

    private VBox createMessage(){
        Message message = new Message("Me", textField.getText());

        Label messageLabel = new Label(message.getMessageBody());
        Label timeStamp = new Label(message.getTimeStamp());

        messageLabel.getStyleClass().add("chat-message-label-sent");
        messageLabel.setMaxWidth(400);
        messageLabel.setWrapText(true);

        HBox secondaryMessageBox = new HBox(timeStamp,messageLabel);
        secondaryMessageBox.setAlignment(Pos.BOTTOM_RIGHT);
        VBox messageBox = new VBox(secondaryMessageBox);
        messageBox.setAlignment(Pos.BASELINE_RIGHT);
        messageBox.setSpacing(5);

        return messageBox;
    }

    private HBox createUserListing(String name) {
        Label userName = new Label(name);
        Label lastSentText = new Label("placeholder");
        Label timeStamp = new Label("1:47PM");

        timeStamp.setAlignment(Pos.BASELINE_RIGHT);

        VBox vBox = new VBox(userName, lastSentText);
        vBox.setStyle("-fx-padding: 10px");

        HBox hBox = new HBox(vBox, timeStamp);
        hBox.setSpacing(1);
        hBox.setAlignment(Pos.CENTER_LEFT);

        return hBox;
    }



}
