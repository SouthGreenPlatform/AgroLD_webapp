/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.membre;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Jc
 */
public class testCo {

    String url = "jdbc:mysql://127.0.0.1/agrold";
    String utilisateur = "root";
    String motDePasse = "";
    Connection connexion = null;
    PreparedStatement statement = null;
    ResultSet result = null;
    /**
     * *************************
     */
    private List<String> messages = new ArrayList<String>();

    public static final String ATT_MESSAGES = "messages";
    public static final String VUE = "/WEB-INF/test_jdbc.jsp";

    public String getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/agrold", "root", "");
            /* Ici, nous placerons nos requêtes vers la BDD */
            String resultat = "";
            /* Init Prepared Request */
            statement = connexion.prepareStatement("Select * from user where mail = ?");
            /* Bind parameters */
            statement.setString(1, "js8@live.fr");
            /* Execute Query */
            
            result = statement.executeQuery();
            
            while(result.next())
                resultat += result.getString(1) + " " + result.getString(2);
            
            
            
                /* ... */
            return resultat;
        } catch (SQLException e) {
            /* Gérer les éventuelles erreurs ici */
            e.printStackTrace();
            return "PB ! !";
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return "PB ! !";
        } finally {
            if (result != null) 
                try {
                    result.close();/* On commence par fermer le ResultSet */
                } catch (SQLException ignore) {}
            
            if (statement != null) 
                try {
                    statement.close();/* Puis on ferme le Statement */
                } catch (SQLException ignore) {}
            if (connexion != null) {
                try {
                    /* Fermeture de la connexion */
                    connexion.close();
                } catch (SQLException ignore) {
                    /* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
                }
            }
        }
    }
}
