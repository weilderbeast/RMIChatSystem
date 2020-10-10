package dk.via.client.core;

import dk.via.client.model.ChatSystemManager;
import dk.via.client.view.login.LoginViewModel;
import dk.via.client.view.main.MainViewModel;

public class ViewModelFactory {
    private final ModelFactory modelFactory;
    private MainViewModel mainViewModel;
    private LoginViewModel loginViewModel;

    public ViewModelFactory(ModelFactory modelFactory) {
        this.modelFactory = modelFactory;
    }

    public LoginViewModel getLoginViewModel() {
        if (loginViewModel == null) {
            loginViewModel = new LoginViewModel(modelFactory.getChatSystem());
        }
        return loginViewModel;
    }

    public MainViewModel getMainViewModel() {
        if (mainViewModel == null) {
            mainViewModel = new MainViewModel(modelFactory.getChatSystem());
        }
        return mainViewModel;
    }
}
