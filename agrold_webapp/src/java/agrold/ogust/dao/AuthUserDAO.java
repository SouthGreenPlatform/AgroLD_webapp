/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.dao;

import agrold.ogust.config.passwordSHA256;
import static agrold.ogust.dao.DAOUtil.fermeturesSilencieuses;
import static agrold.ogust.dao.DAOUtil.initialisationRequetePreparee;
import static agrold.ogust.servlet.Register.CONF_DAO_FACTORY;
import agrold.ogust.dao.AuthUserDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import agrold.ogust.dao.DAOFactory;

/**
 *
 * @author Jc
 */
public class AuthUserDAO {
    private DAOFactory daoFactory;
    private static final String SQL_USER_AUTH_MATCHING = "SELECT COUNT(*) FROM user WHERE mail=? AND password=?";
    private String mail;
    private String pass;
    
    public AuthUserDAO(String mail,String pass){
        this.mail = mail;
        this.pass = pass;
        this.daoFactory = DAOFactory.getInstance();
    }
    public boolean authUser(){
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        boolean auth = false;
        String password = "";
        try{
            password= passwordSHA256.t(pass);
            System.out.println("password : " + password);
        }catch(Exception e){ e.printStackTrace();}
        try {
            System.out.println("------------");
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            System.out.println("------------+");
            preparedStatement = initialisationRequetePreparee( connexion, SQL_USER_AUTH_MATCHING, false, mail, password);
            System.out.println("------------++");
            ResultSet statut = preparedStatement.executeQuery();
            /* Analyse du statut retourné par la requête d'insertion */
            if(statut.next()){
                System.out.println("valeur de statut.getInt(1) == 1 : " + (statut.getInt(1) == 1));
                auth = statut.getInt(1) == 1;
            }else {
                System.out.println("Mot de passe incorrect");
                new AuthUserDAOException("Incorrect password");
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
            return auth;
        }
    }
}
