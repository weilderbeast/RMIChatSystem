package dk.via.client.view.main;

import dk.via.client.core.ViewHandler;
import dk.via.shared.transfer.Message;
import dk.via.shared.transfer.Request;
import dk.via.shared.utils.UserAction;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

public class MainViewController {
    private MainViewModel viewModel;
    private ViewHandler viewHandler;

    @FXML
    private TextField textField;
    @FXML
    private VBox chatVBox;
    @FXML
    private VBox connectedUsersVBox;

    public void init(MainViewModel mainViewModel, ViewHandler viewHandler) {
        viewModel = mainViewModel;
        this.viewHandler = viewHandler;
        textField.textProperty().bindBidirectional(viewModel.getSentText());
        chatVBox.setSpacing(10);
        connectedUsersVBox.setSpacing(10);
        viewModel.addListener(UserAction.TEXT.toString(), event -> updateGeneralChat(event));
        viewModel.addListener(UserAction.USER_LIST.toString(), event -> updateUsersList(event));
    }

    public void sendGroupMessage(ActionEvent actionEvent) {
        viewModel.sendGroupMessage();
        textField.clear();
    }

    public void disconnect() {
        viewModel.disconnect();
    }

    public void updateGeneralChat(PropertyChangeEvent event){
        Platform.runLater(() -> {
            chatVBox.getChildren().add((VBox) event.getNewValue());
        });
    }

    public void updateUsersList(PropertyChangeEvent event) {
        Platform.runLater(() -> {
            ObservableList<HBox> userList = (ObservableList<HBox>)event.getNewValue();
            connectedUsersVBox.getChildren().clear();
            for(int i=0;i<userList.size();i++)
            {
                userList.get(i).setStyle("-fx-cursor: hand");
                userList.get(i).setStyle("-fx-background-color: #009687;-fx-padding: 5px;-fx-text-fill: white");
                connectedUsersVBox.getChildren().add(userList.get(i));
            }
        });
    }
}
