/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import client.ChatClient;
import client.ClientFactory;
import client.SocketClient;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
/**
 *
 * @author ppeters
 */
public class ChatUser implements User {
    
    private String _username;
    private String _password;
    private boolean isAdmin;
    private boolean isLoggedIn;
    
    public ChatUser(String name, String pass) throws UnknownHostException, IOException
    {
        Authenticate(name, pass);
        _username = name;
        _password = pass;
        
    }
    @Override
    public SocketClient startChatClient(String user)throws UnknownHostException, IOException{
        int ServerPort = 5000;
        InetAddress ip = InetAddress.getByName("localhost");
        Scanner sc = new Scanner(System.in);
        SocketClient chatClientInstance =  ClientFactory.getClient("CLIENT", _username, ip, ServerPort, sc);
        chatClientInstance.startReceive();
        chatClientInstance.startSend();
        return chatClientInstance; 
    }
    
    @Override
    public void Authenticate(String name, String pass){
        System.out.println("User is now authenticated");
    }
    
}
