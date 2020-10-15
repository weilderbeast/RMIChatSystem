package dk.via.server.networking;

import dk.via.server.model.ConnectionPool;
import dk.via.shared.transfer.Message;
import dk.via.shared.transfer.Request;
import dk.via.shared.utils.UserAction;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class SocketHandler implements Runnable {
    private final Socket socket;
    private final ConnectionPool connectionPool;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private String nickname;

    public SocketHandler(Socket socket, ConnectionPool connectionPool) {
        this.socket = socket;
        this.connectionPool = connectionPool;

        connectionPool.addListener(UserAction.SEND.toString(), this::sendToClient);
        connectionPool.addListener(UserAction.RECEIVE_ALL.toString(), this::sendToClient);
        connectionPool.addListener(UserAction.RECEIVE.toString(), this::sendToClient);
        connectionPool.addListener(UserAction.USER_LIST.toString(), this::sendToClient);

        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Request request = (Request) inputStream.readObject();
                switch (request.getType()) {
                    case SEND:
                        Message privateMessage = (Message) request.getObject();
                        System.out.println(privateMessage.getMessageSender() +
                                " sends to " + privateMessage.getMessageReceiver() +
                                " : "+privateMessage.getMessageBody());
                        connectionPool.broadcast(request);
                        break;
                    case LOGIN:
                        nickname = (String) request.getObject();
                        System.out.println(nickname + " connected.");
                        connectionPool.addUser(nickname);
                        connectionPool.getUserList();
                        break;
                    case DISCONNECT:
                        connectionPool.removeUser(nickname);
                        connectionPool.removeListener(UserAction.SEND.toString(), this::sendToClient);
                        connectionPool.removeListener(UserAction.RECEIVE_ALL.toString(), this::sendToClient);
                        connectionPool.removeListener(UserAction.RECEIVE.toString(), this::sendToClient);
                        connectionPool.removeListener(UserAction.USER_LIST.toString(), this::sendToClient);
                        System.out.println(nickname + " disconnected.");
                        connectionPool.getUserList();
                        //socket.close();
                        break;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendToClient(PropertyChangeEvent evt) {
        try {
            Request request = (Request) evt.getNewValue();
            //ugly, i know, the only way i could check who to send the messages to on the server side, because
            //i am using firePropertyChange
            switch (request.getType()) {
                case RECEIVE_ALL:
                    Message message = (Message) request.getObject();
                    if (!message.getMessageSender().equals(nickname))
                        outputStream.writeObject(request);
                    break;
                case RECEIVE:
                    Message privateMessage = (Message) request.getObject();
                    if (privateMessage.getMessageReceiver().equals(nickname))
                        outputStream.writeObject(request);
                    break;
                case USER_LIST:
                    outputStream.writeUnshared(request);
                    break;
                default:
                    outputStream.writeObject(request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
