package dk.via.client.network;

import dk.via.shared.transfer.Request;
import dk.via.shared.utils.UserAction;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class SocketClient implements Client {
    private final PropertyChangeSupport support;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Socket socket;
    //private String ID;

    public SocketClient() {
        support = new PropertyChangeSupport(this);
        System.out.println("Created client.");
    }

    @Override
    public void startClient() {
        try {
            socket = new Socket("localhost", 6969);
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            new Thread(() -> listenToServer()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenToServer() {
        try {
            while (true) {
                Request request = (Request) inputStream.readObject();
                System.out.println("received request " + request.getType());
                support.firePropertyChange(request.getType().toString(), null, request);
            }
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
        }
    }

    public void sendToServer(Request request) {
        try {
            System.out.println("sent request " + request.getType());
            outputStream.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        try {
            sendToServer(new Request(UserAction.DISCONNECT, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addListener(String eventName,
                            PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName,
                               PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }
}
