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
        System.out.println("Hello "+username+", welcome to the chatroom!");
        try {
        User newUser = UserFactory.getUser(username, "password", "CHATUSER");
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
