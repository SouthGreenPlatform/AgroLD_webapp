/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.dao;

/**
 *
 * @author Jc
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import agrold.ogust.config.PropertiesBean;
import agrold.ogust.tracking.Tracking;

public class DAOFactory {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL_PREFIX = "jdbc:postgresql://";

    private static final String URL = PropertiesBean.hasDB()?(
        PropertiesBean.getDBConnectionUrl().startsWith(URL_PREFIX)? 
                PropertiesBean.getDBConnectionUrl():
                URL_PREFIX + PropertiesBean.getDBConnectionUrl()
    ) : null; // to avoid concatenation of null

    /*
     * Méthode chargée de récupérer les informations de connexion à la base de
     * données, charger le driver JDBC et retourner une instance de la Factory
     */
    public static DAOFactory getInstance() throws DAOConfigurationException {
        try {
            Class.forName( DRIVER );
        } catch ( ClassNotFoundException e ) {
            throw new DAOConfigurationException( "Le driver " + DRIVER +" est introuvable dans le classpath.", e );
        }

        return new DAOFactory( /*ROPERTY_URL, PROPERTY_NOM_UTILISATEUR, PROPERTY_MOT_DE_PASSE */);
    }

    /* Méthode chargée de fournir une connexion à la base de données, retourne null si il n'y en a pas */
    /* package */
    Connection getConnection() throws SQLException {
        return PropertiesBean.hasDB()? DriverManager.getConnection( 
            URL, 
            PropertiesBean.getDBUsername(), 
            PropertiesBean.getDBPassword() 
        ) : null;
    }
     
    /*
     * Méthodes de récupération de l'implémentation des différents DAO
     */
    public Tracking getTracking() {
        return new Tracking( this );
    }
}