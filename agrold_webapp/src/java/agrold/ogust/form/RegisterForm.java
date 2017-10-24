/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.form;

/**
 *
 * @author Jc
 */

import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;
import javax.servlet.http.HttpServletRequest;
import agrold.ogust.beans.User;
import agrold.ogust.dao.UserDAO;
import agrold.ogust.dao.UserDAOImpl;
import java.lang.String;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class RegisterForm  {
    private static final String CHAMP_NOM       = "nomUser";
    private static final String CHAMP_PRENOM    = "prenomUser";
    private static final String CHAMP_ADRESSE   = "adresseUser";
    private static final String CHAMP_TELEPHONE = "telephoneUser";
    private static final String CHAMP_EMAIL     = "email";
    private static final String CHAMP_PASS_1    = "password1";
    private static final String CHAMP_PASS_2    = "password2";
    private UserDAOImpl userDAO;
    private String resultat;
    private boolean errors = false;
    private Map<String, String> erreurs         = new HashMap<String, String>();
    
    public RegisterForm() {}

    public RegisterForm(UserDAOImpl userDAO){
        this.userDAO = userDAO;
    }
    
    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public String getResultat() {
        return resultat;
    }

    public User creerUser( HttpServletRequest request ) {
        String email = getValeurChamp( request, CHAMP_EMAIL );
        String password1 = getValeurChamp( request, CHAMP_PASS_1 );
        String password2 = getValeurChamp( request, CHAMP_PASS_2 );

        User user = new User();
        resultat = "";
        try {
            validationEmail( email );
            user.setEmail(email);
        } catch ( Exception e ) {
            setErreur( CHAMP_EMAIL, e.getMessage() );
            errors = true;
        }
        user.setEmail( email );
        try {
            validationPassword(password1,password2);
            user.setMotDePasse(password1);
        }catch(Exception e){
            setErreur(CHAMP_PASS_1 + "-" + CHAMP_PASS_2, e.getMessage());
            resultat += "<li>Password error (Minimum 8 char)</li>";
            errors = true;
        }
        if ( resultat.equals("") ) {
            /* Création de l'utilisateur en base de donnée */
            userDAO.create(user);
            /* Création d'une entrée pour les données de l'utilisateur, nom prenom etc. */
            userDAO.creerUserInfo(user);
            /* Création d'une entrée pour les réglages utilisateurs */
            userDAO.creerSettingsHistorique(user);
            
            resultat = "The account was created with success, you will be redirected in 3 secondes ...<i id=\"res\" class=\"ok\" style=\"display:none;color:none\"></i>";
        } else {
            resultat = "<ul>" + resultat + "</ul>";
        }
        return user;
    }

    private void validationEmail( String email ) throws Exception {
        if ( email != null ) {
           int dot = email.indexOf("@");
            String[] mailed = email.split("@");
            
            /*if(email.length()<8 || mailed.length != 2 || mailed[1].indexOf(".")==-1 || mailed[1].indexOf(".") != mailed[1].length()+1 ){
                throw new FormValidationException( "Merci de saisir une adresse mail valide." );
            } else */if ( userDAO.find( email ) != null) {
                resultat +="<li>This user ID or Mail already exist</li>";
                throw new FormValidationException( "This user ID or Mail already exist" );
            }
        } else {
            resultat +="<li>The email field is empty</li>";
            throw new FormValidationException( "Merci de saisir une adresse mail." );
        }
    }
    
    private void validationPassword(String password1, String password2) throws Exception {
        if( password1 == null || password2 == null){
            resultat +="<li>Password must be length of 8 character minimum</li>";
            throw new Exception("Password must be length of 8 character minimum");
        }else{
            if(password1.length()<8 || password2.length()<8)
                resultat +="<li>Password must be length of 8 character minimum</li>";
            if(!(password2.equals(password1)))
                resultat +="<li>Both passwords are not equal</li>";
        }
    }
    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    private void setErreur( String champ, String message ) {
        erreurs.put( champ, message );
    }

    /*
     * Méthode utilitaire qui retourne null si un champ est vide, et son contenu
     * sinon.
     */
    private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur;
        }
    }
}