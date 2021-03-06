/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instantmessenger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author unouser
 */
public class InstantMessenger {

    //Client Counter and Client list
    static ArrayList<ThreadedClass> clientList = new ArrayList<ThreadedClass>();
    public static int numberOfClients;
    
    
    public static void main(String[] args) {
             //Server listening on port 5000
             try{
                Server serverInstance = Server.getServer();
                Socket client;
                while(true){
                    client = serverInstance.start();
                    System.out.println("New Client has connected");

                    //Grab the input and output streams
                    DataInputStream inputStream = new DataInputStream(client.getInputStream());
                    DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
                    
                    //We now need a thread handler for this new client. Start one up!
                    ThreadedClass handler = new ThreadedClass(client, "Client number "+numberOfClients, inputStream, outputStream);
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


