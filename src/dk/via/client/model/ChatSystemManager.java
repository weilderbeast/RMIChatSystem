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
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ChatSystemManager implements ChatSystem {
    private final PropertyChangeSupport support;
    private final Client client;
    private StringProperty nickname;

    public ChatSystemManager(Client client) {
        this.client = client;

        support = new PropertyChangeSupport(this);
        nickname = new SimpleStringProperty();

        this.client.addListener(UserAction.RECEIVE_ALL.toString(), this::onReceiveRequest);
        this.client.addListener(UserAction.RECEIVE.toString(), this::onReceiveRequest);
        this.client.addListener(UserAction.USER_LIST.toString(), this::onReceiveRequest);
        this.client.addListener(UserAction.LOGIN_SUCCESS.toString(), this::onReceiveRequest);
        this.client.addListener(UserAction.LOGIN_FAILED.toString(), this::onReceiveRequest);
        try {
            this.client.startClient();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        System.out.println("Created chatSystemManager");
    }

    private void onReceiveRequest(PropertyChangeEvent propertyChangeEvent) {
        support.firePropertyChange(propertyChangeEvent);
    }

    @Override
    public void startClient(String nickname) {
        try {
            client.login(nickname);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        this.nickname.setValue(nickname);
    }

    @Override
    public StringProperty getNickname(){
        return nickname;
    }

    @Override
    public void getUserList() {
        try {
            client.getUserList(nickname.getValue());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        try {
            client.disconnect(nickname.getValue());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    @Override
    public void sendMessage(Message message) {
        try {
            client.sendMessage(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
