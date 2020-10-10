package dk.via.client.model;

import dk.via.client.network.Client;
import dk.via.shared.transfer.Message;
import dk.via.shared.transfer.Request;
import dk.via.shared.transfer.UserList;
import dk.via.shared.utils.UserAction;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ChatSystemManager implements ChatSystem {
    private PropertyChangeSupport support;
    private Client client;
    private String nickname;
    private UserList userList;

    public ChatSystemManager(Client client) {
        this.client = client;

        client.addListener(UserAction.RECEIVE_ALL.toString(), this::onReceiveMessage);
        client.addListener(UserAction.RECEIVE.toString(), this::onReceiveDirectMessage);

        client.addListener(UserAction.USER_LIST.toString(), this::onReceiveUserList);

        userList = new UserList();

        support = new PropertyChangeSupport(this);
    }

    public void startClient(String nickname)
    {
        client.startClient();
        client.sendToServer(new Request(UserAction.LOGIN,nickname));
        this.nickname = nickname;
    }

    @Override
    public void disconnect() {
        client.disconnect();
    }

    private void onReceiveUserList(PropertyChangeEvent propertyChangeEvent) {
        Request request = (Request) propertyChangeEvent.getNewValue();
        userList = (UserList) request.getObject();
        support.firePropertyChange(propertyChangeEvent);
    }

    //TODO have these checks in the server side (create your own propertyChangeEvent in server)
    private void onReceiveDirectMessage(PropertyChangeEvent propertyChangeEvent) {
        Request request = (Request) propertyChangeEvent.getNewValue();
        Message message = (Message) request.getObject();
        if (message.getMessageReceiver().equals(nickname))
            support.firePropertyChange(propertyChangeEvent);
    }

    //TODO have these checks in the server side (create your own propertyChangeEvent in server)
    private void onReceiveMessage(PropertyChangeEvent propertyChangeEvent) {
        Request request = (Request) propertyChangeEvent.getNewValue();
        Message message = (Message) request.getObject();
        if (!message.getMessageSender().equals(nickname))
            support.firePropertyChange(propertyChangeEvent);
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
    public void sendPrivateMessage() {

    }
}
