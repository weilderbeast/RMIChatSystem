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

public class SocketHandler implements Runnable
{
  private Socket socket;
  private ConnectionPool connectionPool;

  private ObjectOutputStream outputStream;
  private ObjectInputStream inputStream;

  private String username;

  public SocketHandler(Socket socket, ConnectionPool connectionPool)
  {
    this.socket = socket;
    this.connectionPool = connectionPool;
    connectionPool.addListener(UserAction.SEND_ALL.toString(),this::sendAll);
    connectionPool.addListener(UserAction.SEND.toString(),this::send);
    try
    {
      outputStream = new ObjectOutputStream(socket.getOutputStream());
      inputStream = new ObjectInputStream(socket.getInputStream());
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  @Override public void run()
  {
    try
    {
      Request request = (Request) inputStream.readObject();
      System.out.println(request.getMessage().getMessageSender()+" :"+request.getMessage().getMessageBody());
      switch (request.getType())
      {
        case SEND:
            connectionPool.sendPrivate(request);
          break;
        case SEND_ALL:
            connectionPool.broadcast(request);
          break;
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

  public String getUsername() { return username; }

  private void send(PropertyChangeEvent evt)
  {
    try
    {
      Request request = (Request) evt.getNewValue();
      outputStream.writeObject(new Request(request.getType(), request.getMessage()));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void sendAll(PropertyChangeEvent evt)
  {
    try
    {
      Request request = (Request) evt.getNewValue();
      outputStream.writeObject(new Request(request.getType(),request.getMessage()));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
