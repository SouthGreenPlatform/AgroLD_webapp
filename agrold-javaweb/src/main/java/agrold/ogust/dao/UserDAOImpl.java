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
import agrold.ogust.beans.HistorySettings;
import static agrold.ogust.dao.DAOUtil.*;
import agrold.ogust.beans.User;
import agrold.ogust.beans.UserInfo;
import agrold.ogust.dao.*;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import agrold.ogust.dao.DAOFactory;
import agrold.ogust.config.passwordSHA256;
import java.util.Date;
import agrold.ogust.dao.HistoryDAOImpl;
import agrold.ogust.handler.AjaxShell;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.json.JSONObject;

public class UserDAOImpl implements UserDAO {
    private DAOFactory daoFactory;
    private static final String SQL_SELECT_USER_ATTR_BY_MAIL = "SELECT * FROM user WHERE mail=?";
    private static final String SQL_INSERT_NEW_USER = "INSERT INTO user (mail,password, since, last) VALUES (?,?,?,?)";
    private static final String SQL_USER_AUTH_MATCHING = "SELECT COUNT(*) FROM user WHERE mail=? AND password=?";    
    private static final String SQL_INSERT_NEW_H_SETTINGS = "INSERT INTO h_settings (mail) VALUES (?)";
    private static final String SQL_INSERT_NEW_USER_INFO = "INSERT INTO u_info (mail) VALUES (?)";
    
    private static final String URL_USER_GET_HOME = "/WEB-INF/Account/General/AJAX/Home.jsp";
    private static final String URL_USER_GET_CONTACT = "/WEB-INF/Account/General/AJAX/Contact.jsp";
    
    private HistoryDAOImpl history;
    private HistorySettingsDAO historySettings;
    private UserInfoDAO userInfoDAO;
    private User user;

    public DAOFactory getDaoFactory() {
        return daoFactory;
    }
    
    UserDAOImpl(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
        this.history = new HistoryDAOImpl(daoFactory);
    }
    /* Ajout de du bean utilisateur pour faciliter l'accès aux attributs */
    public void setUser(User user){
        this.user = user;
        this.history.setUser(this.user);
    }
    public void setDaoFactory(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    public void setHistory(HistoryDAOImpl history) {
        this.history = history;
    }
    public void setHistorySettings(HistorySettingsDAO historySettings) {
        this.historySettings = historySettings;
    }
    public void setUserInfoDAO(UserInfoDAO userInfoDAO) {
        this.userInfoDAO = userInfoDAO;
    }
    public HistoryDAOImpl getHistory() {
        return history;
    }
    public HistorySettingsDAO getHistorySettings() {
        return historySettings;
    }
    public UserInfoDAO getUserInfoDAO() {
        return userInfoDAO;
    }
    public User getUser() {
        return user;
    }
    /*
    *   Méthodes exécutées par l'utilisateur dans son espace membre
    */
    public AjaxShell onHistory(Object o) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        JSONObject json = (JSONObject)o;
        System.out.println("# Inner Call ");

        String methodName = json.getString("m");
        Method method;

        Object object = history;
        
        AjaxShell shell = null;
        try {
            /* On récupère la méthode à appeler de l'objet history */
            method = history.getClass().getMethod(methodName, Object.class);
            /* On invoke la méthode décrite par la chaine de caractère */
            shell = (AjaxShell)method.invoke(object, json);
            
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        
        return shell;
    }
    /*
    *   Méthodes exécutées par l'utilisateur dans son espace membre
    */
    public AjaxShell onPageConsult(Object o) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        JSONObject json = (JSONObject)o;
        System.out.println("# Inner Call ");

        String methodName = json.getString("m");
        Method method;

        Object object = history;
        
        AjaxShell shell = null;
        try {
            /* On récupère la méthode à appeler de l'objet history */
            method = history.getClass().getMethod(methodName, Object.class);
            /* On invoke la méthode décrite par la chaine de caractère */
            shell = (AjaxShell)method.invoke(object, json);
            
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        
        return shell;
    }
    public AjaxShell onUserInfo(Object o) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        JSONObject json = (JSONObject)o;
        System.out.println("# Inner Call ");
        this.userInfoDAO = new UserInfoDAO(this.daoFactory);
        this.userInfoDAO.setUser(this.user);
        String methodName = json.getString("m");
        Method method;

        Object object = userInfoDAO;
        
        AjaxShell shell = null;
        try {
            /* On récupère la méthode à appeler de l'objet userInfoDAO */
            method = userInfoDAO.getClass().getMethod(methodName, Object.class);
            /* On invoke la méthode décrite par la chaine de caractère */
            shell = (AjaxShell)method.invoke(object, json);
            
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        
        return shell;
    }
    public AjaxShell onHistorySettings(Object o) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        JSONObject json = (JSONObject)o;
        System.out.println("# Inner Call ");
        this.historySettings = new HistorySettingsDAO(this.daoFactory);
        this.historySettings.setUser(this.user);
        String methodName = json.getString("m");
        Method method;

        Object object = historySettings;
        
        AjaxShell shell = null;
        try {
            /* On récupère la méthode à appeler de l'hbjet HistorySettings */
            method = historySettings.getClass().getMethod(methodName, Object.class);
            /* On invoke la méthode décrite par la chaine de caractère */
            shell = (AjaxShell)method.invoke(object, json);
            
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        
        return shell;
    }
    /*
    *   Mise à jour d'un attribut en BDD
    */
    public void update(User user, String attr, String value){
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        /* traitement de la date ou du mot de passe */
        String shell = "";
        if(attr.equals("since") || attr.equals("last")){
            Date utilDate = new Date();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            shell = sqlDate.toString();
        }
        if(attr.equals("password")){
            try{
                shell = passwordSHA256.t(user.getMotDePasse());
            }catch(Exception e){ e.printStackTrace();}
        }
        String REQUEST = "UPDATE user SET " + attr + "=?;" ;
        
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, REQUEST , false, (shell.equals("") ? value:shell));
            int statut = preparedStatement.executeUpdate();
            /* Analyse du statut retourné par la requête d'insertion */
            System.out.println("Update success to " + shell + value);
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la création de l'utilisateur, aucune ligne ajoutée dans la table." );
            }
        } catch ( SQLException e ) {
            System.out.println("Erreur de la requete update");
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        } 
    }
    @Override
    public void create(User user) throws IllegalArgumentException, DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        /* Récupération de la date 'today' */
        Date utilDate = new Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        String password = "";
        try{
            password= passwordSHA256.t(user.getMotDePasse());
        }catch(Exception e){ e.printStackTrace();}
        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT_NEW_USER, true, user.getEmail(), password, sqlDate, sqlDate);
            int statut = preparedStatement.executeUpdate();
            /* Analyse du statut retourné par la requête d'insertion */
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la création de l'utilisateur, aucune ligne ajoutée dans la table." );
            }
            /* Récupération de l'id auto-généré par la requête d'insertion */
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
//            creerUserInfo(user);
//            creerSettingsHistorique(user);
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }
    public void creerUserInfo(User user) throws IllegalArgumentException, DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        /* Récupération de la date 'today' */
        //String mail = this.user.getEmail();
        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT_NEW_USER_INFO, true, user.getEmail());
            int statut = preparedStatement.executeUpdate();
            /* Analyse du statut retourné par la requête d'insertion */
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la création de l'utilisateur, aucune ligne ajoutée dans la table." );
            }
            /* Récupération de l'id auto-généré par la requête d'insertion */
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();

        } catch ( SQLException e ) {
            e.printStackTrace();
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }
    public void creerSettingsHistorique(User user) throws IllegalArgumentException, DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        /* Récupération de la date 'today' */
        //String mail = this.user.getEmail();
        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT_NEW_H_SETTINGS, true, user.getEmail());
            int statut = preparedStatement.executeUpdate();
            /* Analyse du statut retourné par la requête d'insertion */
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la création de l'utilisateur, aucune ligne ajoutée dans la table." );
            }
            /* Récupération de l'id auto-généré par la requête d'insertion */
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();

        } catch ( SQLException e ) {
            e.printStackTrace();
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }

    
    public AjaxShell getHome(){
        System.out.println("## Home LUNCHED");
            
        /* Coquille vide */
        AjaxShell shell = new AjaxShell(URL_USER_GET_HOME); 
        
        /* récupération des dates (dernière cnnexion + inscription)  */
        this.user = find(user.getEmail());
        this.history.setUser(user);
        Object stat_count_request = this.history.getTotalCount(user);

        /* récupération des settings de l'historique */
        this.historySettings = new HistorySettingsDAO(daoFactory);
        this.historySettings.setUser(user);
        boolean settingsQuickSearch = this.historySettings.getQuickSearch();
        boolean settingsAdvancedSearch = this.historySettings.getAdvancedSearch();
        boolean settingsSparqlEditor = this.historySettings.getSparqlEditor();
        
        /* Récupération des informations utilisateurs */
        this.userInfoDAO = new UserInfoDAO(daoFactory);
        UserInfo userInfo = this.userInfoDAO.getInfo(this.user);
                
        shell.add("user",user);
        shell.add("userInfo",userInfo);
        shell.add("settingsSparqlEditor",settingsSparqlEditor);
        shell.add("stat_count_request",stat_count_request);
        shell.add("settingsAdvancedSearch",settingsAdvancedSearch);
        shell.add("settingsQuickSearch",settingsQuickSearch);
        
        return shell;
    }
    
    public AjaxShell getContact(){
        return new AjaxShell(URL_USER_GET_CONTACT,null);
    }
    
    public boolean authUser(String mail, String pass){
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
    /* Implémentation de la méthode définie dans l'interface UtilisateurDao */
    public User find( String email ) throws DAOException {
    Connection connexion = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    User utilisateur = null;
    System.out.println("Avant le try");
    try {
        /* Récupération d'une connexion depuis la Factory */
        connexion = daoFactory.getConnection(); // 
        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_USER_ATTR_BY_MAIL, false, email ); //
        resultSet = preparedStatement.executeQuery(); //
        /* Parcours de la ligne de données de l'éventuel ResulSet retourné */
        System.out.println("Dans le try");
        if ( resultSet.next() ) { //
            utilisateur = map( resultSet ); //
        }
    } catch ( SQLException e ) {
            System.out.println("Catch sql exception");
        throw new DAOException( e );
    } finally {
        System.out.println("Finally");
        fermeturesSilencieuses( resultSet, preparedStatement, connexion );
    }
    System.out.println("utilisateur : " + utilisateur == null ? "null" : "pas null");
    return utilisateur;
}
    
    /*
    * Simple méthode utilitaire permettant de faire la correspondance (le
    * mapping) entre une ligne issue de la table des utilisateurs (un
    * ResultSet) et un bean Utilisateur.
    */
    private static User map( ResultSet resultSet ) throws SQLException {
        User User = new User();
        User.setEmail( resultSet.getString("mail"));
        User.setMotDePasse( resultSet.getString("password"));
        User.setDateInscription( resultSet.getDate("since") );
        User.setLast( resultSet.getDate("last") );
        User.setRight( resultSet.getInt("right") );
        /*
        User.setAdresse(resultSet.getString("adresse"));
        User.setNom( resultSet.getString("nom") );
        User.setNom( resultSet.getString("Prenom") );
        User.setJob( resultSet.getString("job") );
        User.setService( resultSet.getString("service") );
        User.setVille( resultSet.getString("ville") );
        */
        return User;
    }
        /**
     * TEST
     * @param o
     * @return 
     */
    /*
    
    */
}
