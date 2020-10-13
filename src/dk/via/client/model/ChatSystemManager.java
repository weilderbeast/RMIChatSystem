package dk.via.client.model;

import dk.via.client.network.Client;
import dk.via.shared.transfer.Message;
import dk.via.shared.transfer.Request;
import dk.via.shared.utils.UserAction;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ChatSystemManager implements ChatSystem {
    private final PropertyChangeSupport support;
    private final Client client;
    private String nickname;

    public ChatSystemManager(Client client) {
        this.client = client;

        client.addListener(UserAction.RECEIVE_ALL.toString(), this::onReceiveMessage);
        client.addListener(UserAction.RECEIVE.toString(), this::onReceiveDirectMessage);
        client.addListener(UserAction.USER_LIST.toString(), this::onReceiveUserList);

        support = new PropertyChangeSupport(this);
    }

    public void startClient(String nickname) {
        client.startClient();
        client.sendToServer(new Request(UserAction.LOGIN, nickname));
        this.nickname = nickname;
    }

    private void onReceiveUserList(PropertyChangeEvent propertyChangeEvent) {
        Request request = (Request) propertyChangeEvent.getNewValue();
        System.out.println(request.getType()+" event fired with "+request.getObject());
        support.firePropertyChange(propertyChangeEvent);
    }

    private void onReceiveDirectMessage(PropertyChangeEvent propertyChangeEvent) {
        support.firePropertyChange(propertyChangeEvent);
    }

    private void onReceiveMessage(PropertyChangeEvent propertyChangeEvent) {
        support.firePropertyChange(propertyChangeEvent);
    }

    @Override
    public void disconnect() {
        client.disconnect();
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

    @Override
    public void sendGroupMessage(String text) {
        Message message = new Message(nickname, text);
        client.sendToServer(new Request(UserAction.SEND_ALL, message));
    }

    @Override
    public void sendPrivateMessage(String text, String destination) {
        Message message = new Message(nickname, text, destination);
        client.sendToServer(new Request(UserAction.SEND, message));
    }
}
