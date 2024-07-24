/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.tracking;

import agrold.ogust.dao.DAOFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 *
 * @author Jc
 * 
 * @role : This class is a view of Ajax tracking event.
 *          We send the asyncs data from the client to MySQL database
 *          in order to historyze his behavior toward the AgroLD webApp
 */
public class Tracking {
    private DAOFactory daoFactory;
    private HttpServletRequest request;
    private String URL_CALLBACK_SERVER = "";
    private Map<String, Object> data = new HashMap<String, Object>();
    private static final String URL_VOID = "/WEB-INF/Account/General/AJAX/Void.jsp";
    private static final String ANONYMOUS = "ANONYMOUS_USER";


    /**
     * Instatiate the URL attribute in constructor
     */
    public void setTracking(HttpServletRequest request) {
        this.request = request;
        HttpSession session = request.getSession();
    }

    public Tracking(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
