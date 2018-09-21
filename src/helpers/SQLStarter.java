/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import user.credential;

/**
 *
 * @author ppeters
 */
public class SQLStarter {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, ClassNotFoundException {
        SQLHelper helper = new SQLHelper();
        AuthenticationHelper authInstance = new AuthenticationHelper();
        helper.connectDB("instantmessenger.db");
        helper.createUserTable();
        String username = "Pat";
        boolean isAdmin = true;
        credential credentials = null;
        try
        {
            credentials = authInstance.createSecurePassword("Password123");
            System.out.println("Credentials are created. Salt is:"+Arrays.toString(credentials.getSalt()));
            System.out.println("Credendtials are created. Hash is: "+credentials.getHash());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(NoSuchProviderException e)
        {
            e.printStackTrace();
        }
 
        
        
        
        System.out.println("Time to verify the Hash!");
        boolean isValid = authInstance.verifySecurePassword("Password123", credentials.getSalt(), credentials.getHash());
        if(isValid == true)
            System.out.println("Hash is Valid!!!");
        else if(isValid == false)
            System.out.println("You fucked up somewhere");
        
        String[] users = new String[]{username, "true"};
        //(String nameDB, String nameTable, String[] user, credential values)   
        helper.insertUsers("instantmessenger.db", "Users", users, credentials);
    }
}
