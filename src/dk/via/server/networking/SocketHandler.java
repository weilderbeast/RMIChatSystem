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

public class SocketHandler implements Runnable {
    private Socket socket;
    private ConnectionPool connectionPool;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private String nickname;

    public SocketHandler(Socket socket, ConnectionPool connectionPool) {
        this.socket = socket;
        this.connectionPool = connectionPool;

        connectionPool.addListener(UserAction.SEND_ALL.toString(), this::sendAll);
        connectionPool.addListener(UserAction.SEND.toString(), this::send);

        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true){
        try {
            Request request = (Request) inputStream.readObject();
            System.out.println("Request type: "+request.getType());
            switch (request.getType()) {
                case SEND:
                    connectionPool.sendPrivate(request);
                    break;
                case SEND_ALL:
                    Message message = (Message) request.getObject();
                    System.out.println(message.getMessageSender()+": "+message.getMessageBody());
                    connectionPool.broadcast(request);
                    break;
                case LOGIN:
                    nickname = (String)request.getObject();
                    System.out.println(nickname+" connected.");
                    //connectionPool.addUser(nickname);
                    break;
                case DISCONNECT:
                    connectionPool.removeUser(nickname);
                    connectionPool.removeListener(UserAction.SEND_ALL.toString(),this::sendAll);
                    connectionPool.removeListener(UserAction.SEND.toString(), this::send);
                    System.out.println(nickname+" disconnected.");
                    //socket.close();
                    break;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        }
    }

    private void send(PropertyChangeEvent evt) {
        try {
            Request request = (Request) evt.getNewValue();
            outputStream.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendAll(PropertyChangeEvent evt) {
        try {
            Request request = (Request) evt.getNewValue();
            outputStream.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
