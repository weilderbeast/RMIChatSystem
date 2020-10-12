package dk.via.client.core;

import dk.via.client.view.login.LoginViewController;
import dk.via.client.view.main.MainViewController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class ViewHandler {
    private Scene mainScene;
    private ViewModelFactory viewModelFactory;
    private Stage stage;
    private Scene scene;

    public ViewHandler(ViewModelFactory viewModelFactory) {
        this.viewModelFactory = viewModelFactory;
    }

    public void start() throws IOException {
        stage = new Stage();
        openView("Login");
    }

    public void openView(String view) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;

        switch (view) {
            case "Login": {
                loader.setLocation(
                        getClass().getResource("..\\view\\login\\LoginView.fxml")
                );
                root = loader.load();
                LoginViewController loginViewController = loader.getController();
                loginViewController.init(viewModelFactory.getLoginViewModel(), this);
                stage.setTitle("Temperature View");
                break;
            }
            case "Main": {
                loader.setLocation(
                        getClass().getResource("..\\view\\main\\MainView.fxml")
                );
                root = loader.load();
                MainViewController mainViewController = loader.getController();
                mainViewController.init(viewModelFactory.getMainViewModel(), this);
                stage.setTitle("Log View");
                break;
            }
        }

        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("..\\view\\style\\style.css").toExternalForm());
        Font.loadFont(getClass().getResourceAsStream("..\\view\\style\\Roboto-Regular.ttf"),16);
        stage.setScene(scene);
        stage.show();
    }
}
