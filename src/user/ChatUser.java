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
         
        //First check SQLite db for existing username. If it exists, grab hash and salt as well
        SQLHelper helperInstance = new SQLHelper();
        String username = "";
        String Hash = "";
        byte[] Salt = null;
        try{
            ResultSet queryResults = helperInstance.select("instantmessenger.db", "Users", "*", "UserName like '"+name+"'");
            while(queryResults.next())
            {
             username = queryResults.getString("UserName");
             Hash = queryResults.getString("Hash");
             Blob blo = queryResults.getBlob("Salt");
             Salt = blo.getBytes(0, (int)blo.length());
            }
            if(username.toLowerCase()==name.toLowerCase())
            {
                AuthenticationHelper authInstance = new AuthenticationHelper();
                boolean isVerified = false;
                try
                {
                    isVerified = authInstance.verifySecurePassword(pass, Salt, Hash);
                }
                catch(NoSuchAlgorithmException e)
                {
                    e.printStackTrace();
                }
                catch(NoSuchProviderException e)
                {
                    e.printStackTrace();
                }
                if(isVerified==true)
                    System.out.println("User is Authenticated. Welcome "+username+"!");
                else if(isVerified==false)
                    System.out.println("Password is Invalid. Please try again");    
            }
            else
            {
                System.out.println("This Username Does Not Exist.");
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
         catch(ClassNotFoundException e)
         {
             e.printStackTrace();
         }
        
        
        System.out.println("User is now authenticated");
    }
    
}
