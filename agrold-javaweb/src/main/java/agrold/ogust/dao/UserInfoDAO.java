/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.dao;
import agrold.ogust.beans.QuickSearch;
import agrold.ogust.beans.User;
import agrold.ogust.beans.UserInfo;
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
public class UserInfoDAO {
    private DAOFactory daoFactory;
    private UserInfo userInfo;
    private User user;
    private static final String SQL_SELECT_USER_INFO = "SELECT * FROM u_info WHERE mail=?";
    private static final String SQL_UPDATE_USER_INFO = "UPDATE u_info SET nom=?, prenom=?, service=?, ville=?, job=? WHERE mail=?";
    private static final String URL_SHOW_LIST_USER_INFO = "/WEB-INF/Account/General/AJAX/User/settings.jsp";
    private static final String URL_VOID = "/WEB-INF/Account/General/AJAX/Void.jsp";

    public UserInfoDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    public void setUser(User user){
        this.user = user;
    }
    /* Check if the user_information row exist in database */
    public boolean exist(){
        return true;
    }
    /* create the row about user_information in database */
    public AjaxShell getUserInfo(Object o){
        JSONObject json = (JSONObject)o;
        ArrayList<UserInfo> rep = new ArrayList<UserInfo>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet data = null;
        System.out.println("++GETUSERINFO+++");
        
        try {
            connexion = daoFactory.getConnection();
            System.out.println("Avant La requête p^réparée");
            preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_USER_INFO, false, this.user.getEmail());
            System.out.println("Avant l'exécutuib du Query");
            data = preparedStatement.executeQuery();
            System.out.println("Après l'exécutuib du Query");
            while(data.next()){
                System.out.println("Dans la boucle userInfo instance adding to list");
                UserInfo iu = new UserInfo(
                    data.getString("nom"),
                    data.getString("prenom"),
                    data.getString("service"),
                    data.getString("ville"),
                    data.getString("job"),
                    data.getString("mail"));
                rep.add(iu);
            }
            
        } catch ( SQLException e ) {
            System.out.println("ERREUR ++++ ");
            e.printStackTrace();
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( data, preparedStatement, connexion );
            HashMap<String,Object> O = new HashMap<String,Object>();
            O.put("inner_getUserInfo",rep);
        return new AjaxShell(URL_SHOW_LIST_USER_INFO,O);
        }
    }
    public UserInfo getInfo(User user){
        UserInfo userInfo = null;
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet data = null;
        
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_USER_INFO, false, user.getEmail());
            data = preparedStatement.executeQuery();
            while(data.next()){
                userInfo = new UserInfo(
                    data.getString("nom"),
                    data.getString("prenom"),
                    data.getString("service"),
                    data.getString("ville"),
                    data.getString("job"),
                    data.getString("mail"));
                break;
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( data, preparedStatement, connexion );
        return userInfo;
        }
    }
        /* Update the row about user_information in database */
    public AjaxShell updateUserInfo(Object o){
        JSONObject json = (JSONObject)o;
        ArrayList<UserInfo> rep = new ArrayList<UserInfo>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet data = null;
        
        
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_UPDATE_USER_INFO, false,
                                                                                                        json.getString("name"),
                                                                                                        json.getString("fname"),
                                                                                                        json.getString("service"),
                                                                                                        json.getString("town"),
                                                                                                        json.getString("job"),
                                                                                                        this.user.getEmail());
            int res = preparedStatement.executeUpdate();
            
        } catch ( SQLException e ) {
            e.printStackTrace();
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( data, preparedStatement, connexion );
            return new AjaxShell(URL_VOID,null);
        }
    }
    public void getOrCreate(){
        
    }
    /* Update the row about user_informations in database */
}
