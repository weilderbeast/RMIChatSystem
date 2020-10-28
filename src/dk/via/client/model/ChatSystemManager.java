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
        this.client.addListener(UserAction.USER_LIST.toString(), this::onReceiveUserList);
        this.client.addListener(UserAction.LOGIN_SUCCESS.toString(), this::onReceiveRequest);
        this.client.addListener(UserAction.LOGIN_FAILED.toString(), this::onReceiveRequest);
        this.client.startClient();

        System.out.println("Created chatSystemManager");
    }

    //do i really need this?
    private void onReceiveUserList(PropertyChangeEvent propertyChangeEvent) {
        userList = (ArrayList<String>) propertyChangeEvent.getNewValue();
        support.firePropertyChange(propertyChangeEvent);
    }

    private void onReceiveRequest(PropertyChangeEvent propertyChangeEvent) {
        support.firePropertyChange(propertyChangeEvent);
    }

    @Override
    public void startClient(String nickname) {
        client.login(nickname);
        this.nickname.setValue(nickname);
    }

    @Override
    public StringProperty getNickname(){
        return nickname;
    }

    @Override
    public void getUserList() {
        client.getUserList();
    }

    @Override
    public void disconnect() {
        client.disconnect(nickname.getValue());
        System.exit(0);
    }

    @Override
    public void sendMessage(Message message) {
        client.sendMessage(message);
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
