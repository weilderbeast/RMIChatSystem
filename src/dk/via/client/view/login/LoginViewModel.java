package dk.via.client.view.login;

import dk.via.client.model.ChatSystem;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoginViewModel {
    private ChatSystem chatSystem;
    private StringProperty nickname;

    public LoginViewModel(ChatSystem chatSystem) {
        this.chatSystem = chatSystem;
        nickname = new SimpleStringProperty();
    }

    public StringProperty getNickname() {
        return nickname;
    }

    public void startClient() {
        chatSystem.startClient(nickname.get());
    }
}
