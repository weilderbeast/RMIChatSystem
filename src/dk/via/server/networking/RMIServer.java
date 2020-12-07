package dk.via.server.networking;

import dk.via.server.model.ConnectionPool;
import dk.via.shared.networking.ClientInterface;
import dk.via.shared.networking.ServerInterface;
import dk.via.shared.transfer.Message;
import dk.via.shared.utils.UserAction;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RMIServer implements ServerInterface {

    private final ConnectionPool connectionPool;
    private PropertyChangeListener loginListener;
    private PropertyChangeListener broadcastListener;
    private PropertyChangeListener userListListener;

    public RMIServer(ConnectionPool connectionPool) throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
        this.connectionPool = connectionPool;
    }

    public void startServer() throws RemoteException, AlreadyBoundException {
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind("RMIServer", this);
    }

    @Override
    public void registerClient(ClientInterface client, String nickname) throws RemoteException {
        loginListener = (event) -> {
            try {
                client.loginResult((UserAction) event.getNewValue());
            } catch (RemoteException e) {
                connectionPool.removeListener(UserAction.LOGIN.toString() + nickname, loginListener);
            }
        };
        broadcastListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                try {
                    client.broadcast((Message) event.getNewValue());
                } catch (RemoteException e) {
                    connectionPool.removeListener(UserAction.RECEIVE.toString() + nickname, broadcastListener);
                    connectionPool.removeListener(UserAction.RECEIVE_ALL.toString() + nickname, broadcastListener);}
            }
        };

        userListListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                try {
                    client.userList((ArrayList<String>) event.getNewValue());
                } catch (RemoteException e) {
                    connectionPool.removeListener(UserAction.USER_LIST.toString() + nickname, userListListener);
                }
            }
        };

        connectionPool.addListener(UserAction.LOGIN.toString() + nickname, loginListener);
        connectionPool.addListener(UserAction.RECEIVE_ALL.toString() + nickname, broadcastListener);
        connectionPool.addListener(UserAction.USER_LIST.toString() + nickname, userListListener);
        connectionPool.addListener(UserAction.RECEIVE.toString() + nickname, broadcastListener);
    }

    @Override
    public void login(String nickname) throws RemoteException {
        connectionPool.addUser(nickname);
    }

    @Override
    public void broadcast(Message message) throws RemoteException {
        connectionPool.broadcast(message);
    }

    @Override
    public void disconnect(String nickname) throws RemoteException {
        connectionPool.removeUser(nickname);
    }

    @Override
    public void getUserList(String nickname) throws RemoteException {
        connectionPool.getUserList(nickname);
    }

}
