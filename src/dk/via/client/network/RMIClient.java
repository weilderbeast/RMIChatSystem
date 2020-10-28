package dk.via.client.network;

import dk.via.server.networking.RMIServer;
import dk.via.shared.transfer.Message;
import dk.via.shared.utils.UserAction;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RMIClient implements Client{

    private RMIServer server;
    private PropertyChangeSupport support;

    public RMIClient(){
        support = new PropertyChangeSupport(this);
    }

    @Override
    public void startClient() {
        try {
            UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            server = (RMIServer) registry.lookup("UppercaseServer");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(Message message) {
        try {
            server.sendMessage(message);
        } catch (RemoteException e) {
            throw new RuntimeException("Could not contact server");
        }
    }

    @Override
    public void login(String nickname) {
        try {
            server.login(nickname);
        } catch (RemoteException e) {
            throw new RuntimeException("Could not contact server");
        }
    }

    @Override
    public void disconnect(String nickname) {
        try {
            server.disconnect(nickname);
        } catch (RemoteException e) {
            throw new RuntimeException("Could not contact server");
        }
    }

    @Override
    public void receivePrivateMessage() {
        try {
            Message message = server.getMessage();
            support.firePropertyChange(UserAction.RECEIVE.toString(), null, message);
        } catch (RemoteException e) {
            throw new RuntimeException("Could not contact server");
        }
    }

    @Override
    public void receiveGeneralMessage() {
        try {
            Message message = server.getMessage();
            support.firePropertyChange(UserAction.RECEIVE_ALL.toString(), null, message);
        } catch (RemoteException e) {
            throw new RuntimeException("Could not contact server");
        }
    }

    @Override
    public void getUserList() {
        try {
            ArrayList<String> userList = server.getUserList();
            support.firePropertyChange(UserAction.USER_LIST.toString(), null, userList);
        } catch (RemoteException e) {
            throw new RuntimeException("Could not contact server");
        }
    }

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }
}
