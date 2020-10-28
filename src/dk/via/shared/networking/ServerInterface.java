package dk.via.shared.networking;

import dk.via.shared.transfer.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerInterface extends Remote {
    void login(String nickname) throws RemoteException;
    void disconnect(String nickname) throws RemoteException;
    void sendMessage(Message message) throws RemoteException;
    ArrayList<String> getUserList() throws RemoteException;
    Message getMessage() throws RemoteException;
}
