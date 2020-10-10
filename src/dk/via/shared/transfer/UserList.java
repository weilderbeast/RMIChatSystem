package dk.via.shared.transfer;

import java.io.Serializable;
import java.util.ArrayList;

public class UserList implements Serializable {
    private ArrayList<String> users;

    public UserList()
    {
        users = new ArrayList<>();
    }

    public void addUser(String nickname){
        users.add(nickname);
    }
    public void removeUser(String nickname){
        users.remove(nickname);
    }
    public ArrayList<String> getUsers(){ return users;}
}
