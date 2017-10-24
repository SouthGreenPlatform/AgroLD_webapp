/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.config;

/**
 * @role Allow Developer to edit this class like a Config File for DB connections
 * @author Jc
 */
public class MySQLProperties {

    private static final String url = "jdbc:mysql://localhost/agrold";
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String utilisateur = "root";
    private static final String motDePasse = "";

    
    private MySQLProperties(){
        /* prevent usage of constructor bcause it's a static class */
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
    
    
}
