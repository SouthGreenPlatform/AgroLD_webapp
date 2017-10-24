/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.dao;
import agrold.ogust.beans.User;
import agrold.ogust.beans.HistorySettings;
import static agrold.ogust.dao.DAOUtil.fermeturesSilencieuses;
import static agrold.ogust.dao.DAOUtil.initialisationRequetePreparee;
import agrold.ogust.handler.AjaxShell;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;
/**
 *
 * @author Jc
 */
public class HistorySettingsDAO {
    private DAOFactory daoFactory;
    private User user;
    private static final String SQL_INSERT_VALUE = "INSERT INTO h_settings (qs, as, sp) VALUES (?,?,?)";
    private static final String SQL_UPDATE_QUICK_SEARCH_VALUE    = "UPDATE h_settings SET qs=? WHERE mail=?";
    private static final String SQL_UPDATE_ADVANCED_SEARCH_VALUE = "UPDATE h_settings SET adv=? WHERE mail=?";
    private static final String SQL_UPDATE_SPARQL_EDITOR_VALUE   = "UPDATE h_settings SET sp=? WHERE mail=?";
    
    private static final String SQL_GET_ADVANCED_SEARCHE  = "SELECT adv FROM h_settings WHERE mail=?";
    private static final String SQL_GET_QUICK_SEARCHE  = "SELECT qs FROM h_settings WHERE mail=?";
    private static final String SQL_GET_SPARQL_EDITOR  = "SELECT sp FROM h_settings WHERE mail=?";
    
    private static final String SQL_GET_ALL_SETTINGS = "SELECT * FROM h_settings WHERE mail=?";
    private static final String SQL_GET_FIRST = "";
    private static final String URL_HTML_RESULT ="/WEB-INF/Account/General/AJAX/History/settings.jsp";
    private static final String URL_VOID = "/WEB-INF/Account/General/AJAX/Void.jsp";
    
    
    HistorySettingsDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    void setUser(User user){
        this.user = user;
    }
    public AjaxShell getHistorySettings(Object o){
        /* On cast le paramètre de type object en JSONObject */
        JSONObject json = (JSONObject)o;
        /* On déclare une ArrayList de HistorySettings pour stocker les résultats du ResultSet */
        ArrayList<HistorySettings> rep = new ArrayList<HistorySettings>();
        /* Gestion de la connexion */
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        /* La collection qui encapsule les retour de MySQL */
        ResultSet data = null;
        
        try {
            /* Accès au thread qui gère la connexion avec le SGBD */
            System.out.println("AVANT ++ ");
            connexion = daoFactory.getConnection();
            /* Mise en place d'une requête préparée avec les arguments du json ou/et l'email utilisateur */
            preparedStatement = initialisationRequetePreparee( connexion, SQL_GET_ALL_SETTINGS, false, this.user.getEmail());
            System.out.println("APRES++ ");
            /* Exécution de la requête préparée */
            data = preparedStatement.executeQuery();
            /* On parcourt les lignes du résultat SQL */
            while(data.next()){
                /* Un bean HistorySettings est instancié avec les attributs d'une ligne SQL */
                HistorySettings qs = new HistorySettings(
                    data.getInt("qs"),
                    data.getInt("adv"),
                    data.getInt("sp"),
                    data.getInt("discover"));
                /* Puis on l'ajoute dans une ArrayList de même type */
                rep.add(qs);
            }
            
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            /* On ferme la connexion */
            fermeturesSilencieuses( data, preparedStatement, connexion );
            /* On retourne au AjaxHandler une AjaxShell contenant l'url, une collection de bean et la clé d'accès dans la jsp */
            HashMap<String,Object> O = new HashMap<String,Object>();
            O.put( "inner_" + json.getString("m"),rep);
            AjaxShell shell = new AjaxShell(URL_HTML_RESULT, O);
            return shell;
        }
    }
    
    public boolean getQuickSearch(){
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet data = null;
        int result = 1;
        
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_GET_QUICK_SEARCHE, false, this.user.getEmail());
            data = preparedStatement.executeQuery();
            while(data.next()){
                result = data.getInt("qs");
                break;
            }
            
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( data, preparedStatement, connexion );
        }
        return result == 1;
    }
    public boolean getAdvancedSearch(){
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet data = null;
        int result = 1;
        
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_GET_ADVANCED_SEARCHE, false, this.user.getEmail());
            data = preparedStatement.executeQuery();
            while(data.next()){
                result = data.getInt("adv");
                System.out.println("adv : " + data.getInt("adv"));
                System.out.println("result : " + result);
                break;
            }
            
        } catch ( SQLException e ) {
            e.printStackTrace();
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( data, preparedStatement, connexion );
        }
        return result == 1;
    }
    public boolean getSparqlEditor(){
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet data = null;
        int result = 1;
        
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_GET_SPARQL_EDITOR, false, this.user.getEmail());
            data = preparedStatement.executeQuery();
            while(data.next()){
                result = data.getInt("sp");
                break;
            }
            
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( data, preparedStatement, connexion );
        }
        return result == 1;
    }
    
    public AjaxShell settingsQuickSearch(Object o) {
        JSONObject json = (JSONObject)o;
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet data = null;
        System.out.println("On est dans : settingsQuickSearch");
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_UPDATE_QUICK_SEARCH_VALUE, false, json.getInt("id"), this.user.getEmail());
            int res = preparedStatement.executeUpdate();
        } catch ( SQLException e ) {
            e.printStackTrace();
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( data, preparedStatement, connexion );
            return new AjaxShell(URL_VOID,null);
        }
    }
    public AjaxShell settingsAdvancedSearch(Object o) {
        JSONObject json = (JSONObject)o;
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet data = null;
        System.out.println("On est dans : settingsAdvancedSearch");
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_UPDATE_ADVANCED_SEARCH_VALUE, false, json.getInt("id"), this.user.getEmail());
            System.out.println("VALEUR DU JSON " + json.getInt("id"));
            int res = preparedStatement.executeUpdate();
        } catch ( SQLException e ) {
            e.printStackTrace();
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( data, preparedStatement, connexion );
            return new AjaxShell(URL_VOID,null);
        }
    }
    public AjaxShell settingsSparqlEditor(Object o) {
        JSONObject json = (JSONObject)o;
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet data = null;
        System.out.println("On est dans : settingsSparqlEditor");
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_UPDATE_SPARQL_EDITOR_VALUE, false, json.getInt("id"), this.user.getEmail());
            int res = preparedStatement.executeUpdate();
        } catch ( SQLException e ) {
            e.printStackTrace();
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( data, preparedStatement, connexion );
            return new AjaxShell(URL_VOID,null);
        }
    }
}
