/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.tracking;

import agrold.ogust.beans.User;
import agrold.ogust.dao.DAOFactory;
import agrold.ogust.dao.UserDAOImpl;
import agrold.ogust.handler.AjaxShell;
import static agrold.ogust.servlet.HomeMember.USER_SESSION;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;
import agrold.ogust.dao.HistoryDAOImpl;

/**
 *
 * @author Jc
 * 
 * @role : This class is a view of Ajax tracking event.
 *          We send the asyncs data from the client to MySQL database
 *          in order to historyze his behavior toward the AgroLD webApp
 */
public class Tracking {
    private User user;
    private UserDAOImpl userDAO; 
    private DAOFactory daoFactory;
    private HistoryDAOImpl history;
    private HttpServletRequest request;
    private String URL_CALLBACK_SERVER = "";
    private Map<String, Object> data = new HashMap<String, Object>();
    private static final String URL_VOID = "/WEB-INF/Account/General/AJAX/Void.jsp";
    private static final String ANONYMOUS = "ANONYMOUS_USER";


    /**
     * Instatiate the URL attribute in constructor
     */
    public void setTracking(HttpServletRequest request, UserDAOImpl userDAO) {
        this.request = request;
        HttpSession session = request.getSession();
        this.user = (User) session.getAttribute(USER_SESSION);
        this.userDAO = userDAO;
        this.userDAO.setUser(user);
    }

    public Tracking(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Cette fonction consulte la requete et invoke la méthode nécessaire
     */
    public AjaxShell execute() throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        AjaxShell shell = null;
        System.out.println("LES DONNEES AVANT PARSAGE JSON : " + request.getParameter("p"));
        JSONObject params = new JSONObject(request.getParameter("p"));
        String method =  params.getString("m");
        String SpaRQLRequest =  request.getParameter("request");
        
        
        /* Méthode et paramètres sont existants */
        if((method !=null) && (params != null || SpaRQLRequest!= null )){
            /* On indique ANONYMOUS si l'utilisateur n'est pas connecté */
            if(this.user == null){
                params.put("user", ANONYMOUS);
                System.out.println("User anonyme");
            }else{
                /* Sinon on récupère l'adresse email de l'utilisateur   */
                /* On vérifiera lors de l'exécution de la reqête SQL    */
                /* s'il veut sauvegarder l'historique de l'outil appelé */
                params.put("user", this.user.getEmail());
            }
            
            switch (method) {
                case "setPageConsult":
                    System.out.println("On va exécuter la méthode setPageConsult (SWITCH) yeah");
                    shell = this.userDAO.onHistory(params);
                    break;
                case "setQuickSearch":
                    System.out.println("On va exécuter la méthode setQuickSearch (SWITCH)");
                    shell = this.userDAO.onHistory(params);
                    break;
                case "setAdvancedSearch":
                    System.out.println("On va exécuter la méthode setAdvancedSearch (SWITCH)");
                    shell = this.userDAO.onHistory(params);
                    break;
                case "setSparqlEditor":
                    System.out.println("On va exécuter la méthode setSparqlEditor (SWITCH)");
                    params.put("request",SpaRQLRequest);
                    shell = this.userDAO.onHistory(params);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid day of the week: ");
                }
            if(shell==null)
                return new AjaxShell(URL_VOID, null);
            return shell;
        }
        return new AjaxShell(URL_VOID, null);
    }

    /**
     * Call Java Object Method By String name
     * With aeguments
     */


    public String getURL_CALLBACK_SERVER() {
        return URL_CALLBACK_SERVER;
    }

    public void setURL_CALLBACK_SERVER(String URL_CALLBACK_SERVER) {
        this.URL_CALLBACK_SERVER = URL_CALLBACK_SERVER;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
