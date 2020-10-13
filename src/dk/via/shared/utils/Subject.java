package dk.via.shared.utils;

import java.beans.PropertyChangeListener;

public interface Subject {

    //TODO create my own propchangelistener with a for loop to check for who should receive the message
    void addListener(String eventName, PropertyChangeListener listener);

    void removeListener(String eventName, PropertyChangeListener listener);
}
