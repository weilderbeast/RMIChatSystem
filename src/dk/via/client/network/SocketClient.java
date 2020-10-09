package dk.via.client.network;

import dk.via.shared.transfer.Request;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClient implements Client
{
  private PropertyChangeSupport support;
  private ObjectInputStream inputStream;
  private ObjectOutputStream outputStream;
  private Socket socket;
  private String nickname;

  public SocketClient()
  {
    support = new PropertyChangeSupport(this);
  }

  @Override public void startClient()
  {
    try
    {
      socket = new Socket("localhost",6969);
      inputStream = new ObjectInputStream(socket.getInputStream());
      outputStream = new ObjectOutputStream(socket.getOutputStream());

      new Thread(() -> listenToServer()).start();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void listenToServer()
  {
    try
    {
      while(true){
        Request request = (Request) inputStream.readObject();
        support.firePropertyChange(request.getType().toString(),null,request.getMessage());
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  public void sendToServer(Request request)
  {
    try
    {
      outputStream.writeObject(request);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  @Override public void addListener(String eventName,
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(eventName,listener);
  }

  @Override public void removeListener(String eventName,
      PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(eventName,listener);
  }
}
