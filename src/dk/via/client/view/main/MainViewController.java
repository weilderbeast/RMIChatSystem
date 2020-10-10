package dk.via.client.view.main;

import dk.via.client.core.ViewHandler;
import dk.via.shared.transfer.Message;
import dk.via.shared.transfer.Request;
import dk.via.shared.utils.UserAction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class MainViewController {
    private MainViewModel viewModel;
    private ViewHandler viewHandler;

    @FXML
    private TextField textField;

    public void init(MainViewModel mainViewModel, ViewHandler viewHandler) {
        viewModel = mainViewModel;
        this.viewHandler = viewHandler;
        textField.textProperty().bindBidirectional(viewModel.getSentText());
    }

    public void sendGroupMessage(ActionEvent actionEvent) {
        viewModel.sendGroupMessage();
    }

    public void onReceiveMessage() {

    }
}
