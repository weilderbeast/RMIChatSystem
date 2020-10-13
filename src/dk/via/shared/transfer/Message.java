package dk.via.shared.transfer;

import java.io.Serializable;

public class Message implements Serializable
{
  private final String messageBody;
  private final String messageSender;
  private final String messageReceiver;

  public Message(String messageSender,String messageBody,String messageReceiver)
  {
    this.messageBody = messageBody;
    this.messageSender = messageSender;
    this.messageReceiver = messageReceiver;
  }
  public Message(String messageSender,String messageBody)
  {
    this.messageBody = messageBody;
    this.messageSender = messageSender;
    this.messageReceiver = "default";
  }


  public String getMessageBody(){ return messageBody; }
  public String getMessageSender(){ return messageSender; }
  public String getMessageReceiver(){ return messageReceiver; }

}
