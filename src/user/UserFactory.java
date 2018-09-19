/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 *
 * @author ppeters
 */
public class UserFactory {
    public static User getUser(String username, String password, String usertype){
        try{
        if(usertype == null)
            return null;
        if(usertype.equalsIgnoreCase("CHATUSER"))
            return new ChatUser(username, password);
        } catch(UnknownHostException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
        //If we've gotten this far, we're in the dark forest
        return null;
    }
}
