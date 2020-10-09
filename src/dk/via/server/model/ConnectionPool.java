package dk.via.server.model;

import dk.via.shared.transfer.Request;
import dk.via.shared.utils.Subject;
import dk.via.shared.utils.UserAction;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

public class ConnectionPool implements Subject
{
  private PropertyChangeSupport support;

  public ConnectionPool()
  {
    support = new PropertyChangeSupport(this);
  }

  public void broadcast(Request request) throws IOException
  {
    support.firePropertyChange(UserAction.SEND_ALL.toString(),null,request);
  }

  public void sendPrivate(Request request) throws IOException
  {
    support.firePropertyChange(UserAction.SEND.toString(),null,request);
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
}
