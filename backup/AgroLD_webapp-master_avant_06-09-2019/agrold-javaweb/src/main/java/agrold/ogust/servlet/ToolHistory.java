/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.servlet;

import agrold.ogust.beans.User;
import agrold.ogust.dao.DAOFactory;
import agrold.ogust.dao.UserDAOImpl;
import agrold.ogust.handler.AjaxHandler;
import agrold.ogust.handler.AjaxShell;
import static agrold.ogust.servlet.Connection.CONF_DAO_FACTORY;
import static agrold.ogust.servlet.HomeMember.HOME_ADMIN_REDIRECT;
import static agrold.ogust.servlet.HomeMember.USER_SESSION;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import agrold.ogust.tracking.Tracking;

/**
 * Ce servlet sert à rediriger les requêtes que fait l'utilisateur sur le site,
 * notemment dans les outils tels que l'advancedSearch, le quickSearch et le Spa
 * rqlEditor.
 * # On interdit l'accès à cette page dans le navigateur, d'où le fait que le 
 *      DOGET redirige sur la page d'accueil
 * # On est perméable aux reqêtes de type POST, en déléguant les request à
 *      l'objet métier chargé de faire les inscriptions en base de donnée.
 * /!\ On l'utilise pour l'historique mais on peut aussi l'utiliser pour tracer
 * les pages visitées.
 * V
 * @author Jc
 */
public class ToolHistory extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String ACCES_PUBLIC  = "/Login";
    public static final String ACCES_RESTREINT  = "/WEB-INF/Account/General/HomeAuthenticated.jsp";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    private UserDAOImpl userDAO;
    private Tracking tracking;
    
      public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
            System.out.println("/* Récupération d'une instance de notre DAO Utilisateur */");
        
       this.tracking = ((DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY )).getTracking();
        this.userDAO = ((DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY )).getUserDAO();
    }
    /* Interdiction d'accéder à l'URL : www.site.org/agrold/ToolHistory dans le navigateur */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Récupération de la session depuis la requête */
        HttpSession session = request.getSession();
        /* Redirection vers la page d'acceuil */
        response.sendRedirect(ACCES_PUBLIC);
    }
    /*  
    *   Ici l'utilisateur envoie une requête via xHttpRequest (Javascript)
    *   On traite les requêtes asynchrones du client par la méthode 'POST'
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /**************************/
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute(USER_SESSION);
        userDAO.setUser(user);
        
        //tracking = new Tracking(new DAOFactory());
        tracking.setTracking(request, userDAO);
        
        AjaxShell data = null;
        try {
            /* appel Tracking */
             data = tracking.execute();
        } catch (Exception e) {
            System.out.println("Exception déclarée : doPst, data=ajax.execute()");
            e.printStackTrace();
        }
        System.out.println("---------------------");
        System.out.println("ajax.getU " + data.getURL());
        System.out.println("---------------------");
        /* L'url interne qui contient le .jsp est récupéré via une instance d'AjaxHandler */
        this.getServletContext().getRequestDispatcher(data.getURL()).forward(request, response);

    }

    public void doPPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute(USER_SESSION);
        /* On regarde quel est son niveau de privilège {1:normal, 0:admin} */
        if(user != null){
            
            AjaxHandler ajax = new AjaxHandler(request, userDAO);
            /* Insertion des valeurs de la réponse dans la requête */
            if(ajax.getData()!=null)
                for(Map.Entry<String, Object> entry: ajax.getData().entrySet()){
                    request.setAttribute(entry.getKey(),entry.getValue());
                }
            /* L'url interne qui contient le .jsp est récupéré via une instance d'AjaxHandler */
            this.getServletContext().getRequestDispatcher(ajax.getURL_CALLBACK_SERVER()).forward(request, response);
        }else{
            this.getServletContext().getRequestDispatcher(HOME_ADMIN_REDIRECT).forward(request, response);
            System.out.println("USER admin");
        }
        System.out.println("La requête post asynchrone est contrôlée");
        this.getServletContext().getRequestDispatcher("").forward(request, response);
    }
}
