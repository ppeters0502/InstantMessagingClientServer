/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;
import client.SocketClient;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
/**
 *
 * @author ppeters
 */
public interface User {
    SocketClient startChatClient(String user) throws UnknownHostException, IOException;
}
