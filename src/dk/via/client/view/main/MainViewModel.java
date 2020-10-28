package dk.via.client.view.main;

import dk.via.client.model.ChatSystem;
import dk.via.shared.transfer.Message;
import dk.via.shared.utils.Conversation;
import dk.via.shared.utils.Subject;
import dk.via.shared.utils.UserAction;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class MainViewModel implements Subject {
    private final ChatSystem chatSystem;
    private final StringProperty sentText;
    private final PropertyChangeSupport support;
    private final StringProperty SOURCE;
    private final StringProperty nickname;
    private ArrayList<Conversation> logs;

    public MainViewModel(ChatSystem chatSystem) {
        this.chatSystem = chatSystem;

        sentText = new SimpleStringProperty();
        SOURCE = new SimpleStringProperty();
        SOURCE.setValue("General");
        logs = new ArrayList<>();
        logs.add(new Conversation());
        nickname = new SimpleStringProperty();
        nickname.bind(chatSystem.getNickname());

        this.chatSystem.addListener(UserAction.RECEIVE_ALL.toString(), this::onReceiveMessage);
        this.chatSystem.addListener(UserAction.RECEIVE.toString(), this::onReceiveMessage);
        this.chatSystem.addListener(UserAction.USER_LIST.toString(), this::createUserList);

        support = new PropertyChangeSupport(this);
        System.out.println("Created view model");
    }

    private void createUserList(PropertyChangeEvent propertyChangeEvent) {
        ArrayList<String> users = (ArrayList<String>) propertyChangeEvent.getNewValue();
        users.remove(nickname.getValue());
        support.firePropertyChange(UserAction.USER_LIST.toString(), null, users);
    }

    public StringProperty getSentText() {
        return sentText;
    }

    public StringProperty getSOURCE() {
        return SOURCE;
    }

    public void loadUsers() {
        chatSystem.getUserList();
    }

    private void onReceiveMessage(PropertyChangeEvent propertyChangeEvent) {
        //add checks here whether to display the message, or to store it somewhere and ping a notification,
        //preferably put a dot or something next to the user that sent this
        Message message = (Message) propertyChangeEvent.getNewValue();

        System.out.println("Received message \"" + message.getMessageBody() + "\" from " + message.getMessageSender());

        //checking to see if the current chat should receive the message
        //this will only be true for private messages
        if (message.getMessageSender().equals(SOURCE.get())) {
            support.firePropertyChange(UserAction.TEXT.toString(), null, message);
            addToLogs(message);
        } else
            //checking to see if the message is for the general chat
            //this will only happen when the general chat is in view
            if (message.getMessageReceiver().equals(SOURCE.getValue())) {
                support.firePropertyChange(UserAction.TEXT.toString(), null, message);
                addToLogs(message);
            } else {
                //if neither is true, a notification will be sent, along with the message
                //the message will be logged to it's corresponding conversation
                support.firePropertyChange(UserAction.NOTIFICATION.toString(), null, message);
                addToLogs(message);
            }
    }

    public void loadLogs() {
        boolean logsExist = false;

        //checking to see if we want the general chat log
        //this is always the first one in the array, a.k.a. logs.get(0)
        if (SOURCE.getValue().equals("General")) {
            support.firePropertyChange(UserAction.LOAD_LOGS.toString(), null, logs.get(0));
        } else {
            //check to see if there is a conversation to be loaded
            for (Conversation log : logs) {
                if ((SOURCE.getValue().equals(log.getUser1()) || SOURCE.getValue().equals(log.getUser2()))
                        && (nickname.getValue().equals(log.getUser1()) || nickname.getValue().equals(log.getUser2()))) {
                    support.firePropertyChange(UserAction.LOAD_LOGS.toString(), null, log);
                    logsExist = true;
                }
            }
            //if there is no conversation to load, fire a null
            if (!logsExist) {
                support.firePropertyChange(UserAction.LOAD_LOGS.toString(), null, null);
            }
        }
    }

    private void addToLogs(Message message) {
        System.out.println("-- logging a message for chat between " + message.getMessageSender() + " and " + message.getMessageReceiver());
        boolean conversationExists = false;

        //check if the message is for the general chat
        //the logs variable will start with the general chat log in it, at position 0
        if (message.getMessageReceiver().equals("General")) {
            logs.get(0).addMessage(message);
        } else {
            //check to see if there is a conversation between the 2 users
            //using the sender and the receiver, compare against all conversations in the list
            //the if is a bit long, but i didn't know how to add a better check
            //a conversation has user1 and user2, so the order in which they are created does not matter, as we check for both cases
            for (Conversation log : logs) {
                if ((message.getMessageSender().equals(log.getUser1()) || message.getMessageReceiver().equals(log.getUser1()))
                        && (message.getMessageSender().equals(log.getUser2()) || message.getMessageReceiver().equals(log.getUser2()))) {
                    //make this flag true so it skips the next if
                    conversationExists = true;
                    //add the message to the existing conversation
                    log.addMessage(message);
                }
            }
            //if a conversation between these users does not exist already, we create it, and add it to the list
            if (!conversationExists) {
                Conversation conversation = new Conversation(message.getMessageSender(), message.getMessageReceiver());
                conversation.addMessage(message);
                logs.add(conversation);
            }
        }
    }

    public void sendMessage() {
        System.out.println("---- sending to " + SOURCE.getValue() + " the message \" " + sentText.getValue() + " \" ");
        //create the message
        Message message = new Message(nickname.getValue(), sentText.getValue(), SOURCE.getValue());
        //send the message
        chatSystem.sendMessage(message);
        //add the message to the logs
        addToLogs(message);
    }

    public void disconnect() {
        chatSystem.disconnect();
    }

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    public StringProperty getNickname() {
        return nickname;
    }

}
