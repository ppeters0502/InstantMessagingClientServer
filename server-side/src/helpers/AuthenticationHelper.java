/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Arrays;
import user.credential;

/**
 *
 * @author ppeters
 */
public class AuthenticationHelper {
    private static byte[] Salt;
    
    public AuthenticationHelper(){
    }
    
    public boolean verifySecurePassword(String password, byte[] storedSalt, String storedHash) throws NoSuchAlgorithmException, NoSuchProviderException {
        Salt = null;
        Salt = storedSalt;
        String tempSalt = new String(Salt, StandardCharsets.UTF_8);
        System.out.println("VerificationPassword: Salt in bytes: "+Arrays.toString(Salt));
        credential credInstance = getSecurePassword(password, Salt);
        System.out.println("StoredHash is "+storedHash+"\nsecuredPassword is "+credInstance.getHash());
        String securedPassword = credInstance.getHash();
        if(storedHash.equals(securedPassword))
            return true;
        else
            return false;
    }
    
    public credential createSecurePassword(String newPassword) throws NoSuchAlgorithmException, NoSuchProviderException {
        String[] credentials = null;
        Salt = getSalt();
        credential credInstance = getSecurePassword(newPassword, Salt);        
        return credInstance;
    }
    
    

    private static credential getSecurePassword(String passwordToHash, byte[] salt) throws NoSuchAlgorithmException, NoSuchProviderException
    {
        String generatedPassword = null;
        if (salt.length == 0)
        {
            salt = getSalt();
        }
        MessageDigest md = MessageDigest.getInstance("MD5");
        //Now add salt
        md.update(salt);
        //Get the hash's bytes
        byte[] bytes = md.digest(passwordToHash.getBytes());
        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        //Get complete hashed password in hex format
        generatedPassword = sb.toString();
        credential credInstance = new credential(generatedPassword, salt);
        
        return credInstance;
    }
    
    //Add salt
    private static byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException
    {
        //Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        //Create array for salt
        byte[] salt = new byte[16];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        return salt;
    }
    
}
