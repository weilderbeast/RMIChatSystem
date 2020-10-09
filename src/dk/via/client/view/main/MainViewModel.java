package dk.via.client.view.main;

import dk.via.client.model.ChatSystem;
import dk.via.shared.transfer.Message;
import dk.via.shared.transfer.Request;
import dk.via.shared.utils.UserAction;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.beans.PropertyChangeEvent;

public class MainViewModel
{
  private ChatSystem chatSystem;
  private StringProperty sentText;

  public MainViewModel(ChatSystem chatSystem)
  {
    this.chatSystem = chatSystem;

    sentText = new SimpleStringProperty();

    this.chatSystem.addListener(UserAction.SEND_ALL.toString(),this::onGroupMessage);
    this.chatSystem.addListener(UserAction.SEND.toString(),this::onPrivateMessage);
//    this.chatSystem.addListener(UserAction.SEND.toString(),this::onSendPrivateMessage);
//    this.chatSystem.addListener(UserAction.SEND_ALL.toString(),this::onSendGroupMessage);
  }

//  private void onSendGroupMessage(PropertyChangeEvent propertyChangeEvent)
//  {
//
//  }
//
//  private void onSendPrivateMessage(PropertyChangeEvent propertyChangeEvent)
//  {
//
//  }

  private void onPrivateMessage(PropertyChangeEvent propertyChangeEvent)
  {
    Message message = (Message) propertyChangeEvent.getNewValue();
    System.out.println(message.getMessageSender()+":"+message.getMessageBody()+" to "+message.getMessageReceiver());
  }

  private void onGroupMessage(PropertyChangeEvent propertyChangeEvent)
  {
    Message message = (Message) propertyChangeEvent.getNewValue();
    System.out.println(message.getMessageSender()+":"+message.getMessageBody());
  }

  public StringProperty getSentText()
  {
    return sentText;
  }

  public void sendGroupMessage()
  {
    chatSystem.sendGroupMessage(sentText.get());
  }
}
