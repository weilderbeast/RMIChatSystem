package dk.via.client.view.login;

import dk.via.client.core.ViewHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginViewController
{
  private ViewHandler viewHandler;
  private LoginViewModel viewModel;
  @FXML private TextField textField;

  public void init(LoginViewModel loginViewModel, ViewHandler viewHandler)
  {
    this.viewHandler = viewHandler;
    viewModel = loginViewModel;
    textField.textProperty().bindBidirectional(viewModel.getNickname());
  }

  public void openMain(ActionEvent e) throws IOException
  {
    viewModel.setNickname();
    viewHandler.openView("Main");
  }
}
