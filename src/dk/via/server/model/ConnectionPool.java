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

    public ConnectionPool() {
        support = new PropertyChangeSupport(this);
        userList = new ArrayList<>();
    }

    public void broadcast(Message message) {
        if (message.getMessageReceiver().equals("General")) {
            for (String user : userList) {
                if (!user.equals(message.getMessageSender())) {
                    support.firePropertyChange(UserAction.RECEIVE_ALL.toString() + user, null, message);
                }
            }
        } else
            support.firePropertyChange(UserAction.RECEIVE.toString() + message.getMessageReceiver(), null, message);
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
            support.firePropertyChange(UserAction.LOGIN.toString() + nickname, null, UserAction.LOGIN_SUCCESS);
            getUserList();
        } else
            support.firePropertyChange(UserAction.LOGIN.toString() + nickname, null, UserAction.LOGIN_FAILED);
    }

    public void removeUser(String nickname) {
        userList.remove(nickname);
    }

    public void getUserList(String nickname) {
        support.firePropertyChange(UserAction.USER_LIST.toString() + nickname, null, new ArrayList<>(userList));
    }

    public void getUserList() {
        for (String user : userList) {
            support.firePropertyChange(UserAction.USER_LIST.toString() + user, null, new ArrayList<>(userList));
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
