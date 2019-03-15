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
import agrold.ogust.beans.User;
import agrold.ogust.dao.DAOFactory;
import agrold.ogust.dao.UserDAO;
import agrold.ogust.dao.UserDAOImpl;
import agrold.ogust.form.RegisterForm;

public class Register extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String ATT_USER         = "utilisateur";
    public static final String ATT_FORM         = "form";
    public static final String VUE              = "/WEB-INF/Account/General/Register.jsp";

    private UserDAOImpl userDAO;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.userDAO = ((DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY )).getUserDAO();
        System.out.println("Register.java > init() : executed");
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Affichage de la page d'inscription */
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        System.out.println("Register.java > doGet() : executed");
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Préparation de l'objet formulaire */
        RegisterForm form = new RegisterForm(userDAO);

        /* Traitement de la requête et récupération du bean en résultant */
        User utilisateur = form.creerUser( request );
        
        /* Stockage du formulaire et du bean dans l'objet request */
        request.setAttribute( ATT_FORM, form );
        request.setAttribute( ATT_USER, utilisateur );

        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }
}
