package dk.via.client.view.login;

import dk.via.client.model.ChatSystem;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoginViewModel {
    private final ChatSystem chatSystem;
    private final StringProperty nickname;
    private StringProperty error;

    public LoginViewModel(ChatSystem chatSystem) {
        this.chatSystem = chatSystem;
        nickname = new SimpleStringProperty();
        error = new SimpleStringProperty();
    }

    public StringProperty getNickname() {
        return nickname;
    }
    public StringProperty getError() { return error; }

    public void startClient() {
        chatSystem.startClient(nickname.get());
    }
}
