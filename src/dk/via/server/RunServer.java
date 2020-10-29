package dk.via.server;

import dk.via.server.model.ConnectionPool;
import dk.via.server.networking.RMIServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

public class RunServer {
    public static void main(String[] args) throws UnknownHostException, RemoteException, AlreadyBoundException {
        RMIServer server = new RMIServer(new ConnectionPool());
        server.startServer();
        System.out.println("started server");
    }
}
