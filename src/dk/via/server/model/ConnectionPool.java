package dk.via.server.model;

import dk.via.shared.transfer.Message;
import dk.via.shared.transfer.Request;
import dk.via.shared.utils.Subject;
import dk.via.shared.utils.UserAction;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class ConnectionPool implements Subject {
    private final PropertyChangeSupport support;
    private final ArrayList<String> userList;


    /*TODO implement your own propertyChangeListener,
        have here an arrayList of listeners,
        have the socket handler implement the interface,
        have a "getUsername" method in the listener interface,
        loop through the list when you want to send something,
        implement methods in the listener for every action
    * */
    public ConnectionPool() {
        support = new PropertyChangeSupport(this);
        userList = new ArrayList<>();
    }

    public void broadcast(Message message) {
        if (message.getMessageReceiver().equals("General"))
            support.firePropertyChange(UserAction.RECEIVE_ALL.toString(), null, new Request(UserAction.RECEIVE_ALL, message));
        else
            support.firePropertyChange(UserAction.RECEIVE.toString(), null, new Request(UserAction.RECEIVE, message));
    }

    public void addUser(String nickname) {
        boolean flag = false;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).equals(nickname))
                flag = true;
        }
        if (!flag) {
            userList.add(nickname);
            System.out.println(nickname + " connected.");
            support.firePropertyChange(UserAction.LOGIN_SUCCESS.toString(), null, new Request(UserAction.LOGIN_SUCCESS, nickname));
        }
        else
            support.firePropertyChange(UserAction.LOGIN_FAILED.toString(), null, new Request(UserAction.LOGIN_FAILED, nickname));
    }

    public void removeUser(String nickname) {
        userList.remove(nickname);
    }

    public ArrayList<String> getUserList() {
        support.firePropertyChange(UserAction.USER_LIST.toString(), null, new Request(UserAction.USER_LIST, new ArrayList<>(userList)));
        return null;
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
