/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import helpers.AuthenticationHelper;
import helpers.SQLHelper;
import helpers.SQLResult;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author ppeters
 */
public class UserFactory {
    public static User getUser(String username, String password, String usertype){
        
        //First need to authenticate
        boolean auth = Authenticate(username, password);
        if(auth == true)
        {
            try{
                if(usertype == null)
                    return null;
                if(usertype.equalsIgnoreCase("CHATUSER"))
                    return new ChatUser(username, password);
            } catch(UnknownHostException e){
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }
            //If we've gotten this far, we're in the dark forest
            return null;
        }
        else
            return null;
    }
    
    private static boolean Authenticate(String name, String pass){
         
        //First check SQLite db for existing username. If it exists, grab hash and salt as well
        SQLHelper helperInstance = new SQLHelper();
        String username = "";
        String Hash = "";
        byte[] Salt = null;
        try{
            //String nameDB, String nameTable, String selection, String values, String where
            ArrayList<SQLResult> queryResults = helperInstance.select("instantmessenger.db", "Users", "*", "UserName like \'"+name+"\'");
            //Loop through ArrayList to find UserName
            for(int i=0; i<queryResults.size(); i++)
            {
                switch(queryResults.get(i).getColumnName()){
                    case "UserName":
                        username = queryResults.get(i).getStringResult();
                        break;
                    case "Hash":
                        Hash = queryResults.get(i).getStringResult();
                        break;
                    case "Salt":
                        Salt = queryResults.get(i).getByteResult();
                        //Salt = blob.getBytes(1l, (int)blob.length());
                        break;
                        
                
                }

            }
            

            if(username.toLowerCase().equals(name.toLowerCase()))
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
                    return true;
                else if(isVerified==false)
                    return false; 
            }
            else
            {
                System.out.println("This Username Does Not Exist.");
                return false;
            }
        } 
//        catch(SQLException e)
//        {
//            e.printStackTrace();
//        }
         catch(ClassNotFoundException e)
         {
             e.printStackTrace();
         }
        return false;
    }
}
