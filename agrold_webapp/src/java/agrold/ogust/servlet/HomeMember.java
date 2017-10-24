/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import agrold.ogust.beans.User;
import agrold.ogust.handler.AjaxHandler;

/**
 *
 * @author Jc
 */
public class HomeMember extends HttpServlet {
    public static final String HOME_USER_REDIRECT       = "WEB-INF/Account/General/UserHomeAuthenticated.jsp";
    public static final String HOME_ADMIN_REDIRECT      = "WEB-INF/Account/General/AdminHomeAuthenticated.jsp";
    public static final String HOME_USER_AJAX_REQUEST   = "WEB-INF/Account/General/AJAX/HelloWorld.jsp";
    public static final String USER_SESSION             = "sessionUtilisateur";
    /*
    *   Ici l'utilisateur arrive sur son espace membre
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute(USER_SESSION);
        /* On regarde quel est son niveau de privilège {1:normal, 0:admin} */
        if(user.getRight() == 1){
            System.out.println("USER normal");
            this.getServletContext().getRequestDispatcher(HOME_USER_REDIRECT).forward(request, response);
        }else{
            this.getServletContext().getRequestDispatcher(HOME_ADMIN_REDIRECT).forward(request, response);
            System.out.println("USER admin");
        }
        
    }
    /*  
    *   Ici l'utilisateur navigue sur son espace membre
    *   On traite les requêtes asynchrone du client par la méthode 'POST'
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute(USER_SESSION);
        System.out.println("On passe le DOPOSST HOMEMEMBER");
        /* On regarde quel est son niveau de privilège {1:normal, 0:admin} */
        if(user.getRight() == 1){
            
            /* appel du handler Client */
            // AjaxHandler ajax = new AjaxHandler();
            // request.setAttribute("test",ajax.test());
            System.out.println("USER normal");
            this.getServletContext().getRequestDispatcher(HOME_USER_AJAX_REQUEST).forward(request, response);
        }else{
            this.getServletContext().getRequestDispatcher(HOME_ADMIN_REDIRECT).forward(request, response);
            System.out.println("USER admin");
        }
    }
}
