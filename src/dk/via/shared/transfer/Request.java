package dk.via.shared.transfer;

import dk.via.shared.utils.UserAction;

import java.io.Serializable;

public class Request implements Serializable
{
  private UserAction type;
  private Message message;

  public Request(UserAction type, Message message) {
    this.type = type;
    this.message = message;
  }

  public UserAction getType() {
    return type;
  }

  public Message getMessage() {
    return message;
  }
}
