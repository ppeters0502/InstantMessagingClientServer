/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;
import client.SocketClient;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;
/**
 *
 * @author ppeters
 */
public class UserMain {
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        System.out.println("Hello! Please input a username:");
        String username = sc.nextLine();
        System.out.println("Please enter your password: ");
        String password = sc.nextLine();
        try {
        User newUser = UserFactory.getUser(username, password, "CHATUSER");
        if(newUser == null)
        {
            System.out.println("Your Password attempt was invalid");
            System.exit(0);
        }
            
        SocketClient socketInstance = newUser.startChatClient(username);
        } catch(UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
