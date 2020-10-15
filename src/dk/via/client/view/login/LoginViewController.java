package dk.via.client.view.login;

import dk.via.client.core.ViewHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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
        //errorLabel.textProperty().bind(viewModel.getError());
        textField.textProperty().bindBidirectional(viewModel.getNickname());
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    try {
                        openMain();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void openMain() throws IOException {
        viewModel.startClient();
        viewHandler.openView("Main");
    }
}
