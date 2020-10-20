package dk.via.shared.utils;

import dk.via.shared.transfer.Message;

import java.util.ArrayList;

public class Conversation {
    private ArrayList<Message> messages;
    private String user1;
    private String user2;

    public Conversation(String user1,String user2){
        this.user1 = user1;
        this.user2 = user2;
        messages = new ArrayList<>();
    }
    public Conversation(){
        user1 = user2 = "General";
        messages = new ArrayList<>();
    }

    public void addMessage(Message message){
        messages.add(message);
    }
    public void removeMessage(Message message){
        messages.remove(message);
    }
    public String getUser1(){
        return user1;
    }
    public String getUser2(){
        return user2;
    }
    public ArrayList<Message> getMessages(){
        return messages;
    }
}
