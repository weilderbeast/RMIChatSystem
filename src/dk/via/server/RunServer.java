package dk.via.server;

import dk.via.server.model.ConnectionPool;
import dk.via.server.networking.SocketServer;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RunServer {
    public static void main(String[] args) throws UnknownHostException {
        SocketServer socketServer = new SocketServer(new ConnectionPool());
        System.out.println("Started server on " + InetAddress.getLocalHost().getHostAddress());
        socketServer.startServer();
    }
}
