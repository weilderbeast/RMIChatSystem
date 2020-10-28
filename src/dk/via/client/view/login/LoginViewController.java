package dk.via.client.view.login;

import dk.via.client.core.ViewHandler;
import dk.via.shared.utils.UserAction;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.beans.PropertyChangeEvent;
import java.io.IOException;

public class LoginViewController {
    private ViewHandler viewHandler;
    private LoginViewModel viewModel;
    @FXML
    private TextField textField;
    @FXML
    private Label errorLabel;

    public void init(LoginViewModel loginViewModel, ViewHandler viewHandler) {
        this.viewHandler = viewHandler;
        viewModel = loginViewModel;
        viewModel.addListener(UserAction.LOGIN_SUCCESS.toString(), this::openMain);
        errorLabel.textProperty().bindBidirectional(viewModel.getError());
        textField.textProperty().bindBidirectional(viewModel.getNickname());
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    try {
                        Login();
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    public void Login(){
        viewModel.startClient();
    }

    public void openMain(PropertyChangeEvent evt) {
        Platform.runLater(() ->{
            try {
                viewHandler.openView("Main");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void Disconnect(){
        viewModel.disconnect();
    }
}
