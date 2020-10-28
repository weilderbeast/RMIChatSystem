package dk.via.server.networking;

import dk.via.server.model.ConnectionPool;
import dk.via.shared.networking.ServerInterface;
import dk.via.shared.transfer.Message;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RMIServer implements ServerInterface {

    private final ConnectionPool connectionPool;
    private String nickname;

    public RMIServer(ConnectionPool connectionPool) throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
        this.connectionPool = connectionPool;
    }

    public void startServer() throws RemoteException, AlreadyBoundException {
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind("RMIServer", this);
    }

    @Override
    public void login(String nickname) throws RemoteException {
        connectionPool.addUser(nickname);
        this.nickname = nickname;
    }

    @Override
    public void disconnect(String nickname) throws RemoteException {
        connectionPool.removeUser(nickname);
    }

    @Override
    public void sendMessage(Message message) throws RemoteException {
        connectionPool.broadcast(message);
    }

    @Override
    public ArrayList<String> getUserList() throws RemoteException {
        return connectionPool.getUserList();
    }

    @Override
    public Message getMessage() throws RemoteException {
        return null;
    }
}
