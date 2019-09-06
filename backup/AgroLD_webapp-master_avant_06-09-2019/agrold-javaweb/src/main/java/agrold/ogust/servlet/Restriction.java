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
import agrold.ogust.beans.User;
import agrold.ogust.dao.AdminDAOImpl;
import agrold.ogust.dao.DAOFactory;
import agrold.ogust.dao.UserDAOImpl;
import agrold.ogust.handler.AjaxHandler;
import static agrold.ogust.servlet.Connection.CONF_DAO_FACTORY;
import static agrold.ogust.servlet.HomeMember.HOME_ADMIN_REDIRECT;
import static agrold.ogust.servlet.HomeMember.HOME_USER_AJAX_REQUEST;
import static agrold.ogust.servlet.HomeMember.USER_SESSION;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Restriction extends HttpServlet {

    public static final String ACCES_PUBLIC = "/WEB-INF/Account/General/HomeNotAuthenticated.jsp";
    public static final String ACCES_RESTREINT = "/WEB-INF/Account/General/HomeAuthenticated.jsp";
    public static final String POST_TEST = "/WEB-INF/Account/General/AJAX/HelloWorld.jsp";
    public static final String URL_VOID = "/WEB-INF/Account/General/AJAX/Void.jsp";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    private UserDAOImpl userDAO;
    private AdminDAOImpl adminDAO;

    public void init() throws ServletException {
        UserDAOImpl userDAO_ = ((DAOFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getUserDAO();
        AdminDAOImpl adminDAO_ = ((DAOFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getAdminDAO();
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.userDAO = (userDAO_==null?null:userDAO_);
        this.adminDAO = (adminDAO_==null?null:adminDAO_);
        System.out.println("Register.java > init() : executed---");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /* Récupération de la session depuis la requête */
        HttpSession session = request.getSession();

        /*
         * Si l'objet utilisateur n'existe pas dans la session en cours, alors
         * l'utilisateur n'est pas connecté.
         */
        if (session.getAttribute(ATT_SESSION_USER) == null) {
            /* Redirection vers la page publique */
            //response.sendRedirect( request.getContextPath()+ ACCES_PUBLIC );
            response.sendRedirect("Login");
            //this.getServletContext().getRequestDispatcher(ACCES_PUBLIC).forward(request, response);
        } else {
            /* Affichage de la page restreinte */
            this.getServletContext().getRequestDispatcher(ACCES_RESTREINT).forward(request, response);
        }
    }

    /*  
    *   Ici l'utilisateur navigue sur son espace membre
    *   On traite les requêtes asynchrones du client par la méthode 'POST'
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /* On regarde quel est son niveau de privilège {1:normal, 0:admin} */
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER_SESSION);
        AjaxHandler ajax = null;
        
        if (user.getRight() == 1) {
            /* appel du handler Client */
            System.out.println("[+] USER normal");
            ajax = new AjaxHandler(request, userDAO);

        } else if(user.getRight() == 0){
            
            System.out.println("[+] Admin");
            ajax = new AjaxHandler(request, adminDAO);
        }
        /* Return nothing to the client */
        if(ajax==null)
            this.getServletContext().getRequestDispatcher(URL_VOID).forward(request, response);
        else
        /* L'url interne qui contient le .jsp est récupéré via une instance d'AjaxHandler */
            this.getServletContext().getRequestDispatcher(ajax.getURL_CALLBACK_SERVER()).forward(request, response);

    }
}
