package dk.via.shared.utils;

import java.util.Random;

public class UserID {
    private String ID;

    public String createID(){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * chars.length());
            salt.append(chars.charAt(index));
        }
        ID = salt.toString();
        return ID;
    }
}
