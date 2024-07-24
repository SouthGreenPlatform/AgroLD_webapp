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
import agrold.ogust.config.MySQLProperties;
import agrold.ogust.tracking.Tracking;

public class DAOFactory {
    private static final String PROPERTY_URL             = MySQLProperties.getUrl();
    private static final String PROPERTY_DRIVER          = MySQLProperties.getDriver(); // "driver" ;// 
    private static final String PROPERTY_NOM_UTILISATEUR = MySQLProperties.getUtilisateur(); // "nomutilisateur" ;//  
    private static final String PROPERTY_MOT_DE_PASSE    = MySQLProperties.getMotDePasse(); // "motdepasse" ;//  
    private static final boolean ENABLED                 = PROPERTY_DRIVER != null && 
                                                           PROPERTY_URL != null && 
                                                           PROPERTY_NOM_UTILISATEUR != null && 
                                                           PROPERTY_MOT_DE_PASSE != null;
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    /*
     * Méthode chargée de récupérer les informations de connexion à la base de
     * données, charger le driver JDBC et retourner une instance de la Factory
     */
    public static DAOFactory getInstance() throws DAOConfigurationException {
       
         try {
            Class.forName( PROPERTY_DRIVER );
        } catch ( ClassNotFoundException e ) {
            throw new DAOConfigurationException( "Le driver est introuvable dans le classpath.", e );
        }

        return new DAOFactory( /*ROPERTY_URL, PROPERTY_NOM_UTILISATEUR, PROPERTY_MOT_DE_PASSE */);
    }

    /* Méthode chargée de fournir une connexion à la base de données */
    /* package */
    Connection getConnection() throws SQLException {
        return DriverManager.getConnection( PROPERTY_URL, PROPERTY_NOM_UTILISATEUR, PROPERTY_MOT_DE_PASSE );
    }
     
    /*
     * Méthodes de récupération de l'implémentation des différents DAO
     */
    public Tracking getTracking() {
        return new Tracking( this );
    }
}