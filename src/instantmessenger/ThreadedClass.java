/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instantmessenger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author unouser
 */
public class ThreadedClass implements Runnable {
    
    private final Socket client;
    private final String clientName;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
