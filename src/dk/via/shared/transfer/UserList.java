package dk.via.shared.transfer;

import java.util.ArrayList;

public class UserList {
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
}
