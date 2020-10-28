package dk.via.client.network;

import dk.via.shared.transfer.Message;
import dk.via.shared.utils.Subject;
import dk.via.shared.utils.UserAction;

import java.lang.reflect.Member;
import java.rmi.Remote;
import java.util.ArrayList;

public interface Client extends Subject, Remote {
    void startClient();
    void sendMessage(Message message);
    void login(String nickname);
    void receivePrivateMessage();
    void receiveGeneralMessage();
    void getUserList();
    void disconnect(String nickname);
}
