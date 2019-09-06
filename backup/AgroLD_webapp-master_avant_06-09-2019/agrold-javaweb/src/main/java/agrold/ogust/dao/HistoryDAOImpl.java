/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Classe de gestion de l'historique, récupération, ajout, suppression.
 * Cette classe est étendue au comportement USER, voir l'héritage à la classe
 * UserDAOImpl.java
 */
package agrold.ogust.dao;

import agrold.ogust.beans.AdvancedSearch;
import agrold.ogust.beans.HistorySettings;
import agrold.ogust.beans.QuickSearch;
import agrold.ogust.beans.SparqlEditor;
import static agrold.ogust.dao.DAOUtil.*;
import agrold.ogust.handler.AjaxShell;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import agrold.ogust.beans.User;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Jc
 */
public class HistoryDAOImpl implements HistoryDAO  {
    private boolean isAdmin;
    private DAOFactory daoFactory;
    private User user;
    //
    private static final String SQL_FIND_QUICK_SEARCH = "SELECT * FROM h_quick_search WHERE id = ? ORDER BY date DESC";
    //
    /* Get List of log by user mail */
    private static final String SQL_GET_QUICK_SEARCH = "SELECT * FROM h_quick_search WHERE mail = ? ORDER BY date DESC";
    private static final String SQL_GET_ADVANCED_SEARCH = "SELECT * FROM h_advanced_search WHERE mail = ? ORDER BY date DESC";
    private static final String SQL_GET_SPARQL_EDITOR = "SELECT * FROM h_sparql_editor WHERE mail = ? ORDER BY date DESC";
    /* Save the request used inside each tool */
    private static final String SQL_SET_QUICK_SEARCH = "INSERT INTO h_quick_search (date,keyword,mail) VALUES (?,?,?)";
    private static final String SQL_SET_ADVANCED_SEARCH = "INSERT INTO h_advanced_search (date,keyword,mail,type) VALUES (?,?,?,?)";
    private static final String SQL_SET_SPARQL_EDITOR = "INSERT INTO h_sparql_editor (date,mail,request) VALUES (?,?,?)";
    /* Delete entry by user  and id's entry */
    private static final String SQL_DELETE_QUICK_SEARCH = "UPDATE h_quick_search SET mail=? WHERE mail=? AND id=?";
    private static final String SQL_DELETE_ADVANCED_SEARCH = "UPDATE h_advanced_search SET mail=? WHERE mail=? AND id=?";
    private static final String SQL_DELETE_SPARQL_EDITOR = "UPDATE h_sparql_editor SET mail=? WHERE mail=? AND id=?";
    /* Get total of advanced_searc, quickk_search, sparql_editor request and the summ */
    private static final String SQL_GET_TOTAL_COUNT = ""+
        "select * , a.quick_search+b.advanced_search+c.sparql_editor as total " +
        "from (select count(*) as quick_search from h_quick_search where mail=?) a," +
        "(select count(*) as advanced_search from h_advanced_search where mail=?) b," +
        "(select count(*) as sparql_editor from h_sparql_editor where mail=?) c";
    
    private static final String SQL_GET_TOTAL_COUNT_QUICK_SEARCH = "SELECT COUNT(*) FROM h_quick_search WHERE mail=?";
    private static final String SQL_GET_TOTAL_COUNT_ADVANCED_SEARCH = "SELECT COUNT(*) FROM h_quick_search WHERE mail=?";
    private static final String SQL_GET_TOTAL_COUNT_SPARQL_EDITOR = "SELECT COUNT(*) FROM h_sparql_editor WHERE mail=?";
    
    private static final String URL_VOID = "/WEB-INF/Account/General/AJAX/Void.jsp";
    private static final String URL_GET_QUICK_SEARCH = "/WEB-INF/Account/General/AJAX/History/QuickSearchList.jsp";
    private static final String URL_GET_ADVANCED_SEARCH = "/WEB-INF/Account/General/AJAX/History/AdvancedSearchList.jsp";
    private static final String URL_GET_SPARQL_EDITOR = "/WEB-INF/Account/General/AJAX/History/SparqlEditorList.jsp";
    /* Insert the consulted page by anonyme or logged in user */
    private static final String SQL_SET_PAGE_CONSULT = "INSERT INTO h_visited_page (page, visitor, date) VALUES (?,?,?)";
    /* Admin or simple user, {0: admin, normal:1} */
    public HistoryDAOImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    public void setUser(User user){
        this.user = user;
    }
    /**
     * Les trois méthodes qui suivent : 
     * Méthodes qui économisent le code, initialise une AjaxShell
     * @param rep La réponse en ArrayList de QuickSearch / AdvancedSearch / SparqlEditor
     * @param URL La page jsp de retour au client
     * @param KEY le nom de la collection > sert à l'accès dans la jsp, c'est un index KEY > value
     *              avec pour value une ArrayList du bean concerné ( le paramètre rep )
     *              Voir un exemple dans /WEB-INF/General/AJAX/QuickSearchList.jsp
     * @return AjaxShell
     */
    public AjaxShell buildShellQuickSearch(ArrayList<QuickSearch> rep, String URL, String KEY){
        HashMap<String,Object> O = new HashMap<String,Object>();
        O.put(KEY,rep);
        AjaxShell shell = new AjaxShell(URL,O);
        return shell;
    }
    public AjaxShell buildShellAdvancedSearch(ArrayList<AdvancedSearch> rep, String URL, String KEY){
        HashMap<String,Object> O = new HashMap<String,Object>();
        O.put(KEY,rep);
        AjaxShell shell = new AjaxShell(URL,O);
        return shell;
    }
    public AjaxShell buildShellSparqlEditor(ArrayList<SparqlEditor> rep, String URL, String KEY){
        HashMap<String,Object> O = new HashMap<String,Object>();
        O.put(KEY,rep);
        AjaxShell shell = new AjaxShell(URL,O);
        return shell;
    }

    /*
    * Les trois méthodes qui suivent : 
    * Accès à l'historique : un 'SELECT *' sur les historiques de l'utilisateur
    * return : un AjaxShell : Objet contenant une liste d'objet*, l'url du JSP, la clé d'accès pour la jsp
    *   Les AjaxShell sont retournées au AjaxHandler, c'est l'appelant de cette class
    *   -> Stockés en REQUEST > accessibles en jsp et itérable avec les jstl tag, java, EL syntax
    */
    public AjaxShell getQuickSearch(Object o) {
        /* On cast le paramètre de type object en JSONObject */
        JSONObject json = (JSONObject)o;
        /* On déclare une ArrayList de QuickSearch pour stocker les résultats du ResultSet */
        ArrayList<QuickSearch> rep = new ArrayList<QuickSearch>();
        /* Gestion de la connexion */
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        /* La collection qui encapsule les retour de MySQL */
        ResultSet data = null;
        
        try {
            /* Accès au thread qui gère la connexion avec le SGBD */
            connexion = daoFactory.getConnection();
            /* Mise en place d'une requête préparée avec les arguments du json ou/et l'email utilisateur */
            preparedStatement = initialisationRequetePreparee( connexion, SQL_GET_QUICK_SEARCH, false, this.user.getEmail());
            /* Exécution de la requête préparée */
            data = preparedStatement.executeQuery();
            /* On parcourt les lignes du résultat SQL */
            while(data.next()){
                /* Un bean QuickSearch est instancié avec les attributs d'une ligne SQL */
                QuickSearch qs = new QuickSearch(
                    data.getString("keyword"),
                    data.getString("date"),
                    data.getInt("id"),
                    data.getString("mail"));
                /* Puis on l'ajoute dans une ArrayList de même type */
                rep.add(qs);
            }
            
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            /* On ferme la connexion */
            fermeturesSilencieuses( data, preparedStatement, connexion );
            /* On retourne au AjaxHandler une AjaxShell contenant l'url, une collection de bean et la clé d'accès dans la jsp */
            return buildShellQuickSearch(rep, URL_GET_QUICK_SEARCH, "inner_" + json.getString("m"));
        }
    }

    public AjaxShell getAdvancedSearch(Object o) {
        JSONObject json = (JSONObject)o;
        ArrayList<AdvancedSearch> rep = new ArrayList<AdvancedSearch>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet data = null;
        
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_GET_ADVANCED_SEARCH, false, this.user.getEmail());
            data = preparedStatement.executeQuery();
            while(data.next()){
                AdvancedSearch qs = new AdvancedSearch(
                    data.getInt("id"),
                    data.getString("mail"),
                    data.getString("keyword"),
                    data.getString("type"),
                    data.getString("date"));
                rep.add(qs);
            }
            
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( data, preparedStatement, connexion );
            //System.out.println(">> inner_" + json.getString("object")+"_"+ json.getString("m"));
            return buildShellAdvancedSearch(rep, URL_GET_ADVANCED_SEARCH, "inner_" + json.getString("m"));
        }
    }
                     
    public AjaxShell getSparqlEditor(Object o) {
        JSONObject json = (JSONObject)o;
        ArrayList<SparqlEditor> rep = new ArrayList<SparqlEditor>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet data = null;
        
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_GET_SPARQL_EDITOR, false, this.user.getEmail());
            data = preparedStatement.executeQuery();
            while(data.next()){
                SparqlEditor qs = new SparqlEditor(
                    data.getString("date"),
                    data.getInt("id"),
                    data.getString("mail"),
                    data.getString("request"));
                rep.add(qs);
            }
            
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( data, preparedStatement, connexion );
            return buildShellSparqlEditor(rep, URL_GET_SPARQL_EDITOR, "inner_" + json.getString("m"));
        }
    }
    
    /* Suppression d'une entrée <=> passage en anonymous */
    public AjaxShell deleteQuickSearch(Object o) {
        JSONObject json = (JSONObject)o;
        ArrayList<QuickSearch> rep = new ArrayList<QuickSearch>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet data = null;
        
        try {
            connexion = daoFactory.getConnection();
            System.out.println("VALUE BEFOR REQUEST: " + this.user.getEmail() + " " + json.getInt("id"));
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_QUICK_SEARCH, false, "ANONYMOUS_USER", this.user.getEmail(),json.getInt("id"));
            System.out.println("AFTER REQUEST: ");
            int res = preparedStatement.executeUpdate();
            System.out.println("AFTER EXEC: " + res);
        } catch ( SQLException e ) {
            System.out.println("AFTER EXEC: ");
            e.printStackTrace();
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( data, preparedStatement, connexion );
            return new AjaxShell(URL_VOID,null);
        }
    }

    public AjaxShell deleteAdvancedSearch(Object o) {
        JSONObject json = (JSONObject)o;
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet data = null;
        
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_ADVANCED_SEARCH, false,"ANONYMOUS_USER", this.user.getEmail(),json.getInt("id"));
            int res = preparedStatement.executeUpdate();
            while(data.next()){
                
            }
            
        } catch ( SQLException e ) {
            e.printStackTrace();
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( data, preparedStatement, connexion );
            return new AjaxShell(URL_VOID,null);
        }
    }

    public AjaxShell deleteSparqlEditor(Object o) {
        JSONObject json = (JSONObject)o;
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet data = null;
        
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_SPARQL_EDITOR, false,"ANONYMOUS_USER", this.user.getEmail(),json.getInt("id"));
            int res = preparedStatement.executeUpdate();
            while(data.next()){
            }
            
        } catch ( SQLException e ) {
            e.printStackTrace();
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( data, preparedStatement, connexion );
            return new AjaxShell(URL_VOID,null);
        }
    }
    /* Enregistrement d'une entrée en base de donnée */
    public AjaxShell setQuickSearch(Object o) {
        JSONObject json = (JSONObject)o;
        ArrayList<QuickSearch> rep = new ArrayList<QuickSearch>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet data = null;
        Date utilDate = new Date();java.sql.Date date = new java.sql.Date(utilDate.getTime());
        
        HistorySettingsDAO settings = new HistorySettingsDAO(daoFactory);
        settings.setUser(user);
        
        try {
            /* Est ce que l'utilisateur est connecté (=pas anonyme) et a activé le quick search ? */
            if(!json.getString("user").equals("ANONYMOUS_USER") && !settings.getQuickSearch())
                json.put("user","ANONYMOUS_USER");
            
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_SET_QUICK_SEARCH, false, date.toString(), json.getString("keyword"), json.getString("user"));
            int res = preparedStatement.executeUpdate();
            while(data.next()){
                QuickSearch qs = new QuickSearch(
                    data.getString("keyword"),
                    data.getString("date"),
                    data.getInt("id"),
                    data.getString("mail"));
                rep.add(qs);
            }
            
        } catch ( SQLException e ) {
            e.printStackTrace();
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( data, preparedStatement, connexion );
            return new AjaxShell(URL_VOID,null);
        }
    }

    public AjaxShell setAdvancedSearch(Object o) {
        JSONObject json = (JSONObject)o;
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet data = null;
        Date utilDate = new Date();java.sql.Date date = new java.sql.Date(utilDate.getTime());
        System.out.println("------------------------------------------------------");
        HistorySettingsDAO settings = new HistorySettingsDAO(daoFactory);
        settings.setUser(user);
            
        
        System.out.println("------------------------------------------------------");
        try {
            if(!json.getString("user").equals("ANONYMOUS_USER") && !settings.getAdvancedSearch())
                json.put("user","ANONYMOUS_USER");

            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_SET_ADVANCED_SEARCH, false,
                                                                                                        date,
                                                                                                        json.getString("keyword"),
                                                                                                        json.getString("user"),
                                                                                                        json.getString("type"));
            int res = preparedStatement.executeUpdate();
        } catch ( SQLException e ) {
            e.printStackTrace();
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( data, preparedStatement, connexion );
            return new AjaxShell(URL_VOID,null);
        }
    }

    public AjaxShell setSparqlEditor(Object o) {
        JSONObject json = (JSONObject)o;
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet data = null;
        String req =null;
        
        HistorySettingsDAO settings = new HistorySettingsDAO(daoFactory);
        settings.setUser(user);
        
        try {
            req = URLDecoder.decode(json.getString("request"), StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(HistoryDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Date utilDate = new Date();java.sql.Date date = new java.sql.Date(utilDate.getTime());
        try {
            if(!json.getString("user").equals("ANONYMOUS_USER") && !settings.getSparqlEditor())
                json.put("user","ANONYMOUS_USER");
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion,SQL_SET_SPARQL_EDITOR,false,
                                                                                                    date,
                                                                                                    json.getString("user"),
                                                                                                    req);
            int res = preparedStatement.executeUpdate();

            
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( data, preparedStatement, connexion );
            return new AjaxShell(URL_VOID,null);
        }
    }
    
    /* Retourne Void au client - On enregistre la page qu'il consulte */
    public AjaxShell setPageConsult(Object o){
        JSONObject json = (JSONObject)o;
        ArrayList<QuickSearch> rep = new ArrayList<QuickSearch>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet data = null;
        Date utilDate = new Date();java.sql.Date date = new java.sql.Date(utilDate.getTime());
        
        HistorySettingsDAO settings = new HistorySettingsDAO(daoFactory);
        settings.setUser(user);
        
        try {
            /* Est ce que l'utilisateur est connecté (=pas anonyme) et a activé le quick search ? */
            if(!json.getString("user").equals("ANONYMOUS_USER") && !settings.getQuickSearch())
                json.put("user","ANONYMOUS_USER");
            
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_SET_PAGE_CONSULT, false,json.getString("page"), json.getString("user"), date.toString());
            int res = preparedStatement.executeUpdate();
            
        } catch ( SQLException e ) {
            e.printStackTrace();
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( data, preparedStatement, connexion );
            return new AjaxShell(URL_VOID,null);
        }
    }
    
    /* Récupération d'un résumé sur l'utilisation que l'utilisateur fait des outils AgroLD */
    public HashMap<String,Integer> getTotalCount(User u){
        Connection co = null;
        PreparedStatement stmt = null;
        HashMap<String,Integer> map = null;
        ResultSet data = null;
        try{
            String mail = u.getEmail();
            co = daoFactory.getConnection();
            stmt = initialisationRequetePreparee(co, SQL_GET_TOTAL_COUNT, false, mail, mail, mail);
            data = stmt.executeQuery();
            map = new HashMap<String,Integer>();
            while(data.next()){
                map.put("quick_search", data.getInt("quick_search"));
                map.put("advanced_search", data.getInt("advanced_search"));
                map.put("sparql_editor", data.getInt("sparql_editor"));
                map.put("total", data.getInt("total"));
                break;
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new DAOException(e);
        }finally{
            fermeturesSilencieuses(data, stmt, co);
        }
        
        return map;
    }
}
