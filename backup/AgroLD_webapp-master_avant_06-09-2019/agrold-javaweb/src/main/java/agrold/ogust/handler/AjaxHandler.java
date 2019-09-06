/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.handler;

import agrold.ogust.beans.User;
import agrold.ogust.dao.AdminDAO;
import agrold.ogust.dao.AdminDAOImpl;
import static agrold.ogust.servlet.HomeMember.USER_SESSION;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import agrold.ogust.dao.UserDAOImpl;
import agrold.ogust.handler.AjaxShell;
import agrold.ogust.dao.UserDAO;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import org.json.JSONObject;

/**
 * Grab a xHttp Request and return the jsp location and the view's returned
 * datas
 *
 * @author Jc
 */
public class AjaxHandler {

    private Map<String, Object> data = new HashMap<String, Object>();
    private String URL_CALLBACK_SERVER = "";
    private User user;
    private AdminDAOImpl adminDAO;
    private HttpServletRequest request;
    private UserDAOImpl userDAO;

    /* Utile ? */
    /**
     * Instatiate the URL attribute in constructor
     */
    public AjaxHandler(HttpServletRequest request, UserDAOImpl userDAO) {
        this.request = request;
        HttpSession session = request.getSession();
        this.user = (User) session.getAttribute(USER_SESSION);
        this.userDAO = userDAO;
        this.userDAO.setUser(user);
        try {
            delegationUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Instatiate the URL attribute in constructor
     */
    public AjaxHandler(HttpServletRequest request, AdminDAOImpl admin) {
        this.request = request;
        HttpSession session = request.getSession();
        this.user = (User) session.getAttribute(USER_SESSION);
        this.adminDAO = admin;
        this.adminDAO.setUser(user);
        try {
            delegationAdmin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cette fonction consulte la requete et invoke la méthode nécessaire
     */
    public void delegationUser() throws InvocationTargetException, IllegalAccessException {
        System.out.println("Nom de la fonction à appeler : " + request.getParameter("f"));
        JSONObject parametres = new JSONObject(request.getParameter("p"));
        if (parametres == null || parametres.length()==0) {
            System.out.println("Pas de paramètres");
            invoke(userDAO, request.getParameter("f"));
        }else{
            System.out.println("Avec Paramètres : " + request.getParameter("p"));
            invoke(userDAO, request.getParameter("f"), (String)request.getParameter("p"));
        }
    }
    public void delegationAdmin() throws InvocationTargetException, IllegalAccessException {
        System.out.println("Nom de la fonction à appeler : " + request.getParameter("f"));
        JSONObject parametres = new JSONObject(request.getParameter("p"));
        if (parametres == null || parametres.length()==0) {
            System.out.println("Pas de paramètres");
            invoke(adminDAO, request.getParameter("f"));
        }else{
            System.out.println("Avec Paramètres : " + request.getParameter("p"));
            invoke(adminDAO, request.getParameter("f"), (String)request.getParameter("p"));
        }
    }

    /**
     * Call Java Object Method By String name
     * With aeguments
     */
    public Object invoke(Object object, String methodName,String arguments) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        Method method;
        
        try {
            /* On récupère la méthode à appeler */
            method = object.getClass().getMethod(methodName, Object.class);
            /* On invoke la méthode décrite par la chaine de caractère */
            AjaxShell shell;
            shell = (AjaxShell)method.invoke(object, new JSONObject(arguments));
            /* On récupère l'url que la méthode appelée nous retourne */
            this.URL_CALLBACK_SERVER = shell.getURL();
            /* On déplace la data de retour dans la session afin que les template puisse afficher les différentes données */
            System.out.println("on passe dans la boucle de transfert HASHMAP > REQUEST");
            if(!shell.isEmpty())
                for(Map.Entry<String, Object> entry: shell.getCollection().entrySet()){
                    request.setAttribute(entry.getKey(),entry.getValue());
                    System.out.println("entry.getKey(),entry.getValue() : " + entry.getKey() + ", " + entry.getValue());
                }
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
        return null;
    }
    /**
     * Without argument
     * @param object
     * @param methodName
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException 
     */
    public Object invoke(Object object, String methodName) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        Method method;
        //method.invoke(object, arguments);

        try {
            System.out.println("OBJET ADMIN VAUT : " + object.toString());
            method = object.getClass().getMethod(methodName);
            AjaxShell shell = (AjaxShell)method.invoke(object);
            /* !!!!!!!!!!!!!!!!!    A MODIFIER     !!!!!!!!!!!!!!!!!!!*/
            /* On récupère l'url que la méthode appelée nous retourne */
            this.URL_CALLBACK_SERVER = shell.getURL();
            /* On déplace la data de retour dans la session afin que les template puisse afficher les différentes données */
            System.out.println("on passe dans la boucle de transfert HASHMAP > REQUEST");
            for(Map.Entry<String, Object> entry: shell.getCollection().entrySet()){
                request.setAttribute(entry.getKey(),entry.getValue());
                System.out.println("entry.getKey(),entry.getValue() : " + entry.getKey() + ", " + entry.getValue());
            }
            
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

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
