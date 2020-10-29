package dk.via.shared.networking;

import dk.via.client.network.RMIClient;
import dk.via.shared.transfer.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerInterface extends Remote {
    void login(String nickname) throws RemoteException;
    void registerClient(ClientInterface client, String nickname) throws RemoteException;
    void broadcast(Message message) throws RemoteException;
    void disconnect(String nickname) throws RemoteException;
    void getUserList(String nickname) throws RemoteException;
}
