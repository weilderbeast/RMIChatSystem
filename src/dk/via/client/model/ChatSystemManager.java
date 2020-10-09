package dk.via.client.model;

import dk.via.client.network.Client;
import dk.via.shared.transfer.Message;
import dk.via.shared.transfer.Request;
import dk.via.shared.utils.UserAction;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ChatSystemManager implements ChatSystem
{
  private PropertyChangeSupport support;
  private Client client;
  private String nickname;

  public ChatSystemManager(Client client)
  {
    this.client = client;
    client.startClient();
    client.addListener(UserAction.RECEIVE_ALL.toString(), this::onReceiveMessage);
    client.addListener(UserAction.RECEIVE.toString(), this::onReceiveDirectMessage);

    support = new PropertyChangeSupport(this);
  }

  private void onReceiveDirectMessage(PropertyChangeEvent propertyChangeEvent)
  {
    Message message = (Message) propertyChangeEvent.getNewValue();
    if(message.getMessageReceiver().equals(nickname))
    support.firePropertyChange(propertyChangeEvent);
  }

  private void onReceiveMessage(PropertyChangeEvent propertyChangeEvent)
  {
    Message message = (Message) propertyChangeEvent.getNewValue();
    if(!message.getMessageSender().equals(nickname))
    support.firePropertyChange(propertyChangeEvent);
  }

  @Override public void addListener(String eventName,
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(eventName, listener);
  }

  @Override public void removeListener(String eventName,
      PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(eventName, listener);
  }

  @Override public void sendGroupMessage(String text)
  {
    Message message = new Message(nickname,text);
    System.out.println("sending to server from: "+nickname+" the text: "+text);
    client.sendToServer(new Request(UserAction.SEND_ALL,message));
  }

  @Override public void sendPrivateMessage()
  {

  }

  @Override public void setNickname(String nickname)
  {
    this.nickname = nickname;
    System.out.println("nickname in chatsysmanager: "+nickname);
  }
}
