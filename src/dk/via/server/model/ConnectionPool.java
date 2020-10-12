package dk.via.server.model;

import dk.via.shared.transfer.Request;
import dk.via.shared.utils.Subject;
import dk.via.shared.utils.UserAction;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class ConnectionPool implements Subject {
    private PropertyChangeSupport support;
    private ArrayList<String> userList;

    public ConnectionPool() {
        support = new PropertyChangeSupport(this);
        userList = new ArrayList<>();
    }

    public void broadcast(Request request) {
        support.firePropertyChange(UserAction.RECEIVE_ALL.toString(), null, new Request(UserAction.RECEIVE_ALL,request.getObject()));
    }

    public void sendPrivate(Request request) {
        support.firePropertyChange(UserAction.RECEIVE.toString(), null, new Request(UserAction.RECEIVE,request.getObject()));
    }

    public void addUser(String nickname) {
        userList.add(nickname);
    }

    public void removeUser(String nickname) {
        userList.remove(nickname);
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

    public void getUserList() {
        support.firePropertyChange(UserAction.USER_LIST.toString(),null, new Request(UserAction.USER_LIST,new ArrayList<>(userList)));
    }
}
