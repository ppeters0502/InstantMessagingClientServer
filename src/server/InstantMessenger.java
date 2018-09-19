/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author unouser
 */
public class InstantMessenger {

    //Client Counter and Client list
    public static ArrayList<ThreadedClass> clientList = new ArrayList<ThreadedClass>();
    public static int numberOfClients;
    public static Scanner sc = new Scanner(System.in);
    
    public static void main(String[] args) {
             //Server listening on port 5000
             try{
                Server serverInstance = Server.getServer();
                Socket client;
                while(true){
                    client = serverInstance.start();
                    System.out.println("New Client has connected: "+client);

                    //Grab the input and output streams
                    DataInputStream inputStream = new DataInputStream(client.getInputStream());
                    //Check InputStream for a username
                    String input = inputStream.readUTF();
                    String clientName = "";
                    StringTokenizer st = new StringTokenizer(input, "###$$$###");
                    if(st.countTokens()>0)
                    {
                        clientName = st.nextToken();
                        System.out.println("Client "+clientName+" has connected.");
                    }
                  
                    DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
                    
                    
                    //We now need a thread handler for this new client. Start one up!
                    ThreadedClass handler = new ThreadedClass(client, clientName, inputStream, outputStream);
                    Thread thread = new Thread(handler);
                    
                    //Add client to list of Clients and start up the thread
                    clientList.add(handler);
                    
                    thread.start();
                    
                    numberOfClients++;
                    
                }
             }
             catch(IOException e)
             {
                 Logger.getLogger(InstantMessenger.class.getName()).log(Level.SEVERE, null, e);
             }
         
    }
    

    
}


