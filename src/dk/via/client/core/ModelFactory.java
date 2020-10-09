package dk.via.client.core;

import dk.via.client.model.ChatSystem;
import dk.via.client.model.ChatSystemManager;

public class ModelFactory
{
  private final ClientFactory clientFactory;
  private ChatSystem chatSystem;

  public ModelFactory(ClientFactory clientFactory)
  {
    this.clientFactory = clientFactory;
  }

  public ChatSystem getChatSystem(){
    if(chatSystem == null)
      chatSystem = new ChatSystemManager(clientFactory.getClient());
    return chatSystem;
  }
}
