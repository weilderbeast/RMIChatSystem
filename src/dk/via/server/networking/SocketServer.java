package dk.via.server.networking;

import dk.via.server.model.ConnectionPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer
{

  private ConnectionPool connectionPool;
  public SocketServer(ConnectionPool connectionPool)
  {
    this.connectionPool = connectionPool;
  }

  public void startServer()
  {
    try
    {
      ServerSocket welcomeSocket = new ServerSocket(6969);
      ConnectionPool connectionPool = new ConnectionPool();

      while(true)
      {
        Socket socket = welcomeSocket.accept();
        System.out.println("Client connected: "+welcomeSocket.getInetAddress().getHostName());

        SocketHandler socketHandler = new SocketHandler(socket, connectionPool);
        new Thread(socketHandler).start();
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}