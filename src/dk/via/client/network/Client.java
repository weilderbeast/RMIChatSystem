package dk.via.client.network;

import dk.via.shared.transfer.Request;
import dk.via.shared.utils.Subject;

public interface Client extends Subject
{
  void startClient();
  void sendToServer(Request request);
}
