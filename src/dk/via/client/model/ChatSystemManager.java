package dk.via.client.model;

import dk.via.client.network.Client;
import dk.via.shared.transfer.Message;
import dk.via.shared.transfer.Request;
import dk.via.shared.utils.UserAction;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class ChatSystemManager implements ChatSystem {
    private final PropertyChangeSupport support;
    private final Client client;
    private StringProperty nickname;
    private ArrayList<String> userList;

    public ChatSystemManager(Client client) {
        this.client = client;

        support = new PropertyChangeSupport(this);
        nickname = new SimpleStringProperty();

        userList = new ArrayList<>();

        this.client.addListener(UserAction.RECEIVE_ALL.toString(), this::onReceiveRequest);
        this.client.addListener(UserAction.RECEIVE.toString(), this::onReceiveRequest);
        this.client.addListener(UserAction.USER_LIST.toString(), this::onReceiveRequest);
        this.client.addListener(UserAction.LOGIN_SUCCESS.toString(), this::onReceiveRequest);
        this.client.addListener(UserAction.LOGIN_FAILED.toString(), this::onReceiveRequest);
        this.client.startClient();

        System.out.println("Created chatSystemManager");
    }

    @Override
    public void startClient(String nickname) {
        client.sendToServer(new Request(UserAction.LOGIN, nickname));
        this.nickname.setValue(nickname);
    }

    private void onReceiveRequest(PropertyChangeEvent propertyChangeEvent) {
        Request request = (Request) propertyChangeEvent.getNewValue();
        System.out.println(request.getType() + " event fired with " + request.getObject());

        if(request.getType().equals(UserAction.USER_LIST)){
            userList = (ArrayList<String>) request.getObject();
        }
        support.firePropertyChange(propertyChangeEvent);
    }

    @Override
    public StringProperty getNickname(){
        return nickname;
    }

    @Override
    public void getUserList() {
        client.sendToServer(new Request(UserAction.USER_LIST, "nothing to see here move along"));
    }

    @Override
    public void disconnect() {
        client.sendToServer(new Request(UserAction.DISCONNECT, null));
        System.exit(0);
    }

    @Override
    public void sendMessage(Message message) {
        client.sendToServer(new Request(UserAction.SEND, message));
    }

    @Override
    public void addListener(String eventName,
                            PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName,
                               PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }
}
