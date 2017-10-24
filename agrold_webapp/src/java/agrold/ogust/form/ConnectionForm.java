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
import javax.servlet.http.HttpServletRequest;
import agrold.ogust.beans.User;
import agrold.ogust.dao.UserDAO;
import agrold.ogust.dao.UserDAOImpl;
import agrold.ogust.dao.AuthUserDAO;


public final class ConnectionForm {
    private static final String CHAMP_EMAIL  = "email";
    private static final String CHAMP_PASS   = "motdepasse";
    private UserDAOImpl userDAO;
    private String              resultat;
    private Map<String, String> erreurs      = new HashMap<String, String>();
    
    public ConnectionForm(UserDAOImpl userDAO){
        this.userDAO = userDAO;
    }
    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public User connecterUtilisateur( HttpServletRequest request ) {
        /* Récupération des champs du formulaire */
        String email = getValeurChamp( request, CHAMP_EMAIL );
        String motDePasse = getValeurChamp( request, CHAMP_PASS );
        
        User user = null;
        //userDAO.create(user);
        /* Validation du champ email. */
        try {
            validationEmail( email );
        } catch ( Exception e ) {
            setErreur( CHAMP_EMAIL, e.getMessage() );
        }

        /* Validation du champ mot de passe. */
        AuthUserDAO auth = new AuthUserDAO(email,motDePasse);
        System.out.println("Avant le test de match DB account");
        if(!userDAO.authUser(email,motDePasse)){
            System.out.println("Erreur de connexion");
            resultat = "Le mot de passe est incorrect";
            setErreur("auth", "User or password incorrect");
        }else{
            user = userDAO.find(email);
            userDAO.update(user, "last", "");
        }

        /* Initialisation du résultat global de la validation. */
        if ( erreurs.isEmpty() ) {
            System.out.println("Succes de la connexion");
            resultat = "Succès de la connexion.";
        } else {
            resultat = "Email or password invalid";
        }

        return user;
    }

    /**
     * Valide l'adresse email saisie.
     */
    private void validationEmail( String email ) throws Exception {
        if ( email != null && !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
            throw new Exception( "Merci de saisir une adresse mail valide." );
        }
    }

    /*  
    *   Valide l'authentification
    */
    private boolean auth(User user){
        
        
        return false;
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