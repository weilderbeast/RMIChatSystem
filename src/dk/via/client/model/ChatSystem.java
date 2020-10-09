package dk.via.client.model;

import dk.via.shared.transfer.Request;
import dk.via.shared.utils.Subject;

public interface ChatSystem extends Subject
{
  void sendGroupMessage(String text);
  void sendPrivateMessage();
  void setNickname(String nickname);
}
