/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.NullPointerException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @role Allow Developer to edit this class like a Config File for DB connections
 * @author Jc
 */
public class MySQLProperties {

    private static final List<String> conf = readLoginConfigurations();
    private static final String url = "jdbc:mysql://" + conf.get(0);
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String utilisateur = conf.get(1);
    private static final String motDePasse = conf.get(2);

    
    private MySQLProperties(){
        /* prevent usage of constructor bcause it's a static class */
    }
    
    
    /**
     *
     * @return an ArrayList of String with the lines of the text in the same
     * order as in the text
     */
    public static List<String> readLoginConfigurations() {
        List<String> lines = new ArrayList<>();
        try {
            String URL = System.getProperty("agrold.db_connection_url");
            String usr = System.getProperty("agrold.db_username");
            String pwd = System.getProperty("agrold.db_password");

            if (URL == null) throw new NullPointerException("Missing system property: agrold.db_connection_url");
            if (usr == null) throw new NullPointerException("Missing system property: agrold.db_username");
            if (pwd == null) throw new NullPointerException("Missing system property: agrold.db_password");

            lines.add(URL);
            lines.add(usr);
            lines.add(pwd);
        } catch (NullPointerException ex) {
            Logger.getLogger(MySQLProperties.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
    
    public static String getUrl() {
        return url;
    }

    public static String getDriver() {
        return driver;
    }

    public static String getUtilisateur() {
        return utilisateur;
    }

    public static String getMotDePasse() {
        return motDePasse;
    }
    
    public static void main(String[] args) {
        System.out.println(readLoginConfigurations());
    }
    
}
