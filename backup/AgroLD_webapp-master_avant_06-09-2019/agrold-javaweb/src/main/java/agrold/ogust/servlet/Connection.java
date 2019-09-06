/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.servlet;

/**
 *
 * @author Jc
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import agrold.ogust.beans.User;
import agrold.ogust.dao.DAOFactory;
import agrold.ogust.dao.UserDAOImpl;
import agrold.ogust.form.ConnectionForm;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;


public class Connection extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String ATT_USER         = "utilisateur";
    public static final String ATT_FORM         = "form";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    public static final String VUE_FIRST_LOAD   = "/WEB-INF/Account/General/Login.jsp";
    //public static final String VUE              = "/WEB-INF/Account/General/HomeAuthenticated.jsp";
    
    private UserDAOImpl userDAO;
    @Override
    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.userDAO = ((DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY )).getUserDAO();
        System.out.println("Register.java > init() : executed---");
    }
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Affichage de la page de connexion */
        this.getServletContext().getRequestDispatcher( VUE_FIRST_LOAD ).forward( request, response );
    }
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Préparation de l'objet formulaire */
        ConnectionForm form = new ConnectionForm(userDAO);

        /* Traitement de la requête et récupération du bean en résultant */
        User utilisateur = form.connecterUtilisateur( request );
        
        System.out.println("[} "+ form.getResultat());

        if(utilisateur != null){
            /* Récupération de la session depuis la requête */
            HttpSession session = request.getSession();
            //request.setAttribute( ATT_FORM, form );
            request.setAttribute( ATT_USER, utilisateur );
            System.out.println("Demande d'autentification");
            /**
             * Si aucune erreur de validation n'a eu lieu, alors ajout du bean
             * Utilisateur à la session, sinon suppression du bean de la session.
             */
            if ( form.getErreurs().isEmpty() ) {
                session.setAttribute( ATT_SESSION_USER, utilisateur);
                response.sendRedirect("Member");
                //this.getServletContext().getRequestDispatcher(  ).forward( request, response );
                //this.getServletContext().getRequestDispatcher( "/Member" ).forward( request, response );
                //return ;
            } else {
                session.setAttribute( ATT_SESSION_USER, null );
                response.sendRedirect("Login?error=1");
                //this.getServletContext().getRequestDispatcher( "/Login" ).forward( request, response );
            }
        }else{
                response.sendRedirect("Login?error=1");
        }
    }
    
    /*  
    *   @role: Check if the user exist in the MySQL DB
    *   @param: mail : String
    *   @param: pass : String
    *   @return: Boolean
    */
    boolean checkDB(String mail, String pass){
        
        return true;
    }
}
