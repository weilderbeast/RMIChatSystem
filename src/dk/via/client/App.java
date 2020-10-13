package dk.via.client;

import dk.via.client.core.ClientFactory;
import dk.via.client.core.ModelFactory;
import dk.via.client.core.ViewHandler;
import dk.via.client.core.ViewModelFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        ClientFactory clientFactory = new ClientFactory();
        ModelFactory modelFactory = new ModelFactory(clientFactory);
        ViewModelFactory viewModelFactory = new ViewModelFactory(modelFactory);
        ViewHandler viewHandler = new ViewHandler(viewModelFactory);
        viewHandler.start();
    }
}
