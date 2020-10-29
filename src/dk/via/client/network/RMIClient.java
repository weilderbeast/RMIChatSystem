package dk.via.client.network;

import dk.via.server.networking.RMIServer;
import dk.via.shared.networking.ClientInterface;
import dk.via.shared.networking.ServerInterface;
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

public class RMIClient implements Client, ClientInterface {

    private ServerInterface server;
    private PropertyChangeSupport support;

    public RMIClient(){
        support = new PropertyChangeSupport(this);
    }

    @Override
    public void startClient() {
        try {
            UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            server = (ServerInterface) registry.lookup("RMIServer");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void login(String nickname) {
        try {
            server.registerClient(this, nickname);
            server.login(nickname);
        } catch (RemoteException e) {
            throw new RuntimeException("Could not contact server");
        }
    }

    @Override
    public void loginResult(UserAction userAction) {
        support.firePropertyChange(userAction.toString(), null, null);
    }

    @Override
    public void sendMessage(Message message) {
        try {
            server.broadcast(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void broadcast(Message message) {
        support.firePropertyChange(UserAction.RECEIVE_ALL.toString(), null, message);
    }

    @Override
    public void userList(ArrayList<String> newValue) {
        support.firePropertyChange(UserAction.USER_LIST.toString(), null, newValue);
    }

    @Override
    public void getUserList(String nickname) {
        try {
            server.getUserList(nickname);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect(String nickname) throws RemoteException {
        server.disconnect(nickname);
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
