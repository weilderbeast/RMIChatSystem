package dk.via.client.core;

import dk.via.client.network.Client;
import dk.via.client.network.SocketClient;

public class ClientFactory {
    private Client client;

    public Client getClient() {
        if (client == null) {
            client = new SocketClient();
        }
        return client;
    }
}
