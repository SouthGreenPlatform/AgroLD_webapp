/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.config;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Jc
 */
public class passwordSHA256 {
        public static String t(String tohash)throws NoSuchAlgorithmException, UnsupportedEncodingException {
        /* Hash password SHA-256 */
        System.out.println("Valeur de tohash : " + tohash);
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(tohash.getBytes("UTF-8")); // Change this to "UTF-16" if needed
        byte[] digest = md.digest();
        
        return String.format("%064x", new java.math.BigInteger(1, digest));
    }
}
