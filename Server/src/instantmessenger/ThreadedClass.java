/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instantmessenger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author unouser
 */
public class ThreadedClass implements Runnable {
    
    private final Socket client;
    public final String clientName;
    final DataInputStream inputStream;
    final DataOutputStream outputStream;
    Scanner sc = new Scanner(System.in);
    boolean isLoggedIn = false;
    
    
    //Constructor
    public ThreadedClass(Socket client, String cn, DataInputStream i, DataOutputStream o){
        this.client = client;
        this.clientName = cn;
        this.inputStream = i;
        this.outputStream = o;
        this.isLoggedIn = true;
    }
    

    @Override
    public void run() {
        String receivedMessage;
	while(true)
	{
		try
		{
			//read the string
			receivedMessage = inputStream.readUTF();
			System.out.println(receivedMessage);

			//if received message is logout, we want to close the socket
			if(receivedMessage.equals("logout"))
			{
				this.isLoggedIn=false;
				this.client.close();
				break;
			}
            
            //Take the clientName passed in and check it against the vector
            //of clients
            for(ThreadedClass mc : InstantMessenger.clientList)
            {
                //If receipient is found, write oun its output stream
                if(mc.clientName.equals(clientName) && mc.isLoggedIn==true)
                {
                    mc.outputStream.writeUTF(this.clientName+" : "+receivedMessage);
                    break;
                }
            }

		}catch(IOException e)
                {System.out.println("blah");}
	}
    try{
        //closing resources
        this.inputStream.close();
        this.outputStream.close();
    }catch(IOException e){
        e.printStackTrace();
    }
    }
    
    public String getClientName(){
        return clientName;
    }
}
