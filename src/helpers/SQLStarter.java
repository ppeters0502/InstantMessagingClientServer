/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import user.credential;
import java.util.Scanner;

/**
 *
 * @author ppeters
 */
public class SQLStarter {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, ClassNotFoundException, SQLException {
        Scanner sc = new Scanner(System.in);
        SQLHelper helper = new SQLHelper();        
        helper.connectDB("instantmessenger.db");  
        helper.createUserTable();
        helper.TestSQLSelect("Pat");
        AuthenticationHelper authInstance = new AuthenticationHelper();
        System.out.println("Select * from Users where UserName like 'Pat'");
//        ArrayList<SQLResult> results = new ArrayList<SQLResult>();
//        //String nameDB, String nameTable, String selection, String values
//        results = helper.select("instantmessenger.db", "Users","*", "UserName like \'Pat\'");
//        for(int i=0; i<results.size(); i++)
//        {
//            switch(results.get(i).getColumnType()){
//                case "text":
//                    
//            }
//        }
//        AuthenticationHelper authInstance = new AuthenticationHelper();
//        System.out.println("Enter a username for a new user to create!");
//        String username = sc.nextLine();
//        System.out.println("Now enter a password");
//        String password = sc.nextLine();
//        credential credentials = null;
//        try
//        {
//            credentials = authInstance.createSecurePassword(password);
//            System.out.println("Credentials are created. Salt is:"+Arrays.toString(credentials.getSalt()));
//            System.out.println("Credendtials are created. Hash is: "+credentials.getHash());
//        }
//        catch(NoSuchAlgorithmException e)
//        {
//            e.printStackTrace();
//        }
//        catch(NoSuchProviderException e)
//        {
//            e.printStackTrace();
//        }
//        
//        System.out.println("Time to verify the Hash!");
//        boolean isValid = authInstance.verifySecurePassword(password, credentials.getSalt(), credentials.getHash());
//        if(isValid == true)
//            System.out.println("Hash is Valid!!!");
//        else if(isValid == false)
//            System.out.println("You messed up somewhere");
//        
//        String[] users = new String[]{username, "false"};
//        //(String nameDB, String nameTable, String[] user, credential values)   
//        helper.insertUsers("instantmessenger.db", "Users", users, credentials);
    }
}
