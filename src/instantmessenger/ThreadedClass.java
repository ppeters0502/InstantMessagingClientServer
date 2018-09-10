/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instantmessenger;

import java.net.Socket;

/**
 *
 * @author unouser
 */
public class ThreadedClass implements Runnable {
    
    private final Socket client;
    private final String clientName;
    public ThreadedClass(Socket client, String cn){
        this.client = client;
        this.clientName = cn;
    }
    

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
