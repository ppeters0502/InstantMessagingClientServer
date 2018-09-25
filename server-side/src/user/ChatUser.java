/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import client.ClientFactory;
import client.SocketClient;
import helpers.AuthenticationHelper;
import helpers.SQLHelper;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    
    
    
}
