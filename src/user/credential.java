/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

/**
 *
 * @author ppeters
 */
public class credential {
    private static byte[] salt;
    private static String hash;

    
    public credential(String H, byte[] s )
    {
        this.hash = H;
        this.salt = s;

    }
    
    public byte[] getSalt(){
        return salt;
    }
    public static String getHash(){
        return hash;
    }
}
