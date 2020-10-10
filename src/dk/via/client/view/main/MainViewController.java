package dk.via.client.view.main;

import dk.via.client.core.ViewHandler;
import dk.via.shared.transfer.Message;
import dk.via.shared.transfer.Request;
import dk.via.shared.utils.UserAction;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

public class MainViewController {
    private MainViewModel viewModel;
    private ViewHandler viewHandler;
    private ObservableList<HBox> userList;

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

//        connectedUsersVBox.accessibleTextProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
//                if(t1 == null) return;
//
//            }
//        });
    }

    public void sendGroupMessage(ActionEvent actionEvent) {
        viewModel.sendGroupMessage();
    }

    public void disconnect() {
        viewModel.disconnect();
    }

    public void updateUsersList(PropertyChangeEvent event) {
        Platform.runLater(() -> {

        });
    }

    public void onReceiveMessage() {

    }
}
