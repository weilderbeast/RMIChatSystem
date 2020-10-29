package dk.via.shared.networking;

import dk.via.shared.transfer.Message;
import dk.via.shared.utils.UserAction;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientInterface extends Remote {
    void loginResult(UserAction userAction) throws RemoteException;
    void broadcast(Message message) throws RemoteException;
    void userList(ArrayList<String> newValue) throws RemoteException;
}
