package dk.via.client.network;

import dk.via.shared.networking.ClientInterface;
import dk.via.shared.transfer.Message;
import dk.via.shared.utils.Subject;
import dk.via.shared.utils.UserAction;

import java.lang.reflect.Member;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Client extends Subject {
    void startClient() throws RemoteException;
    void sendMessage(Message message) throws RemoteException;
    void login(String nickname) throws RemoteException;
    void getUserList(String nickname) throws RemoteException;
    void disconnect(String nickname) throws RemoteException;
}
