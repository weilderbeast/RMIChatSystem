package dk.via.client.view.login;

import dk.via.client.model.ChatSystem;
import dk.via.shared.utils.Subject;
import dk.via.shared.utils.UserAction;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class LoginViewModel implements Subject {
    private final ChatSystem chatSystem;
    private final StringProperty nickname;
    private StringProperty error;
    private PropertyChangeSupport support;

    public LoginViewModel(ChatSystem chatSystem) {
        this.chatSystem = chatSystem;
        nickname = new SimpleStringProperty();
        error = new SimpleStringProperty();
        error.setValue("");
        support = new PropertyChangeSupport(this);
        this.chatSystem.addListener(UserAction.LOGIN_FAILED.toString(), this::onLoginFail);
        this.chatSystem.addListener(UserAction.LOGIN_SUCCESS.toString(), this::onLoginOK);

        System.out.println("Created loginViewModel.");
    }

    private void onLoginOK(PropertyChangeEvent event) {
        support.firePropertyChange(UserAction.LOGIN_SUCCESS.toString(), null, null);
    }

    private void onLoginFail(PropertyChangeEvent event) {
        Platform.runLater(() ->{
            error.setValue("Username already taken.");
        });
        System.out.println("failed to login");
    }

    public StringProperty getNickname() {
        return nickname;
    }

    public StringProperty getError() {
        return error;
    }

    public void startClient() {
        chatSystem.startClient(nickname.get());
    }

    public void disconnect() {
        chatSystem.disconnect();
    }

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }


}
