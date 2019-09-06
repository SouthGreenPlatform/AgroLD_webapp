/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust;

/**
 *
 * @author Jc
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConfigMySQL {
    
    private String url = "jdbc:mysql://fruges.cirad.fr:3306/agrold";
    private String utilisateur = "agrold";
    private String motDePasse = "G4sVsURNh3tZcZ22";
    private Connection connexion = null;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public String getUrl() {
        return url;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public Connection getConnexion() {
        return connexion;
    }

    public PreparedStatement getStatement() {
        return statement;
    }

    public ResultSet getResult() {
        return result;
    }

    
    
}
