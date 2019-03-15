/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.dao;

import agrold.ogust.beans.User;
import agrold.ogust.beans.UserInfo;
import static agrold.ogust.dao.DAOUtil.fermeturesSilencieuses;
import static agrold.ogust.dao.DAOUtil.initialisationRequetePreparee;
import agrold.ogust.handler.AjaxShell;
import static agrold.ogust.servlet.Restriction.URL_VOID;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONObject;
import java.sql.Date;
import org.jvnet.hk2.component.MultiMap;

/**
 *
 * @author Jc
 */
public class AdminDAOImpl extends UserDAOImpl {

//    private static final String;
    private static final String SQL_GET_ALL_USER_EXCEPT_ADMIN = "SELECT * FROM user WHERE user.right=? ORDER BY user.mail LIMIT ? OFFSET ?";
    private static final String SQL_COUNT_NUMBER_OF_USER = "SELECT COUNT(*) as count FROM user WHERE user.right=?";
    private static final String SQL_COUNT_TOTAL_PAGE_VISITED = "SELECT COUNT(*) as total FROM h_visited_page ";
    private static final String SQL_COUNT_TOTAL_ACCOUNT = "SELECT COUNT(*) as total FROM user";
    private static final String SQL_GET_TOOL_TOTAL_REQUESTS = ""
            + "select c.sp + a.qs + b.ad as total "
            + "from (select count(*) as qs from h_quick_search) as a,"
            + " (select count(*) as ad from h_advanced_search) as b,"
            + " (select count(*) as sp from h_sparql_editor) as c ";
    private static final String SQL_GET_STAT_TOOL_TOTAL_REQUESTS = ""
            + "select * , a.quick_search+b.advanced_search+c.sparql_editor as total "
            + "from (select count(*) as quick_search from h_quick_search) a,"
            + "(select count(*) as advanced_search from h_advanced_search) b,"
            + "(select count(*) as sparql_editor from h_sparql_editor) c";

    private static final String SQL_GET_USER_GLOBAL_STAT = ""
            + "SELECT user.mail, user.since as since, user.last as last, coalesce(advanced_search,0) as advanced_search,coalesce(quick_search,0) as quick_search,coalesce(sparql_editor,0) as sparql_editor,  coalesce(advanced_search,0) + coalesce(quick_search,0) + coalesce(sparql_editor,0) as total\n"
            + "FROM user\n"
            + "LEFT JOIN (SELECT mail, coalesce(count(id), 0) as advanced_search\n"
            + "           FROM h_advanced_search\n"
            + "           GROUP BY mail) advanced_search\n"
            + "ON advanced_search.mail = user.mail\n"
            + "LEFT JOIN (SELECT mail, coalesce(count(id), 0) as quick_search\n"
            + "           FROM h_quick_search\n"
            + "           GROUP BY mail) quick_search\n"
            + "ON quick_search.mail = user.mail\n"
            + "LEFT JOIN (SELECT mail, coalesce(count(id), 0) as sparql_editor\n"
            + "           FROM h_sparql_editor\n"
            + "           GROUP BY mail) sparql_editor\n"
            + "ON sparql_editor.mail = user.mail"
            + " limit ? offset ?";

    private static final String SQL_DELETE_QUICK_SEARCH_ANONYMOUS = "Delete from h_quick_search where h_quick_search.mail='ANONYMOUS_USER' \n"
                                                                    + "and DATE(h_quick_search.date) BETWEEN DATE(?) AND DATE(?)";
    private static final String SQL_DELETE_ADVANCED_SEARCH_ANONYMOUS = "Delete from h_advanced_search where h_advanced_search.mail='ANONYMOUS_USER' \n"
                                                                    + "and DATE(h_advanced_search.date) BETWEEN DATE(?) AND DATE(?)";
    private static final String SQL_DELETE_SPARQL_EDITOR_ANONYMOUS = "Delete from h_sparql_editor where h_sparql_editor.mail='ANONYMOUS_USER' \n"
                                                                    + "and DATE(h_sparql_editor.date) BETWEEN DATE(?) AND DATE(?)";
    private static final String SQL_DELETE_QUICK_SEARCH_USER = "Delete from h_quick_search where DATE(h_quick_search.date) BETWEEN DATE(?) AND DATE(?)";
    private static final String SQL_DELETE_ADVANCED_SEARCH_USER = "Delete from h_advanced_search where DATE(h_advanced_search.date) BETWEEN DATE(?) AND DATE(?)";
    private static final String SQL_DELETE_SPARQL_EDITOR_USER = "Delete from h_sparql_editor where DATE(h_sparql_editor.date) BETWEEN DATE(?) AND DATE(?)";
    
    
    private static final String SQL_DELETE_INTERVAL_REQUESTED_TOOL_HISTORY_USER = ""
            + "Delete from h_quick_search, h_advanced_search, h_sparql_editor \n"
            + "where emp_id='$id' \n"
            + "and DATE(?) BETWEEN DATE(?) AND DATE(?)";
    private static final String SQL_DELETE_INTERVAL_REQUESTED_TOOL_HISTORY_BOTH = ""
            + "Delete from h_quick_search, h_advanced_search, h_sparql_editor \n"
            + "where emp_id='$id' \n"
            + "and DATE(?) BETWEEN DATE(?) AND DATE(?)";
    /* Pass user to super user (admin) */
    private static final String SQL_TOGGLE_SUPER_USER = "UPDATE user SET user.right=? WHERE user.mail=?";
    /* get stats of visitor page */
    private static final String SQL_GET_VISITED_PAGE = "SELECT h.page\n"
            + "      ,SUM(CASE WHEN h.visitor = 'ANONYMOUS_USER' THEN 1 ELSE 0 END) AS anonymous\n"
            + "      ,SUM(CASE WHEN h.visitor != 'ANONYMOUS_USER' THEN 1 ELSE 0 END) AS user\n"
            + "      ,COUNT(*) as total\n"
            + "  FROM h_visited_page h\n"
            + " GROUP BY h.page;";

    public AdminDAOImpl(DAOFactory daoFactory) {
        super(daoFactory);
    }
    private static final String URL_GET_FUTURE_ADMIN = "/WEB-INF/Account/General/AJAX/Admin/FutureUser.jsp";
    private static final String URL_GET_ADMIN_PANEL = "/WEB-INF/Account/General/AJAX/Admin/AdminPanel.jsp";
    private static final String URL_GET_USER_MANAGER = "/WEB-INF/Account/General/AJAX/Admin/UserManager.jsp";
    private static final String URL_GET_COMPLETE_USER_DATA = "/WEB-INF/Account/General/AJAX/Admin/_USER_FULL_DATA_LOADER.jsp";
    private static final String URL_GET_AGROLD_STAT = "/WEB-INF/Account/General/AJAX/Admin/AgroldStat.jsp";

    /**
     * get List of user only, called by client
     *
     * @param o Object : JSONObject
     * @return AjaxShell
     */
    public AjaxShell getFutureAdmin(Object o) {
        JSONObject json = (JSONObject) o;
        AjaxShell shell = new AjaxShell(URL_GET_FUTURE_ADMIN);
        /* Récupération des paramètres de pagination */
        int limit = json.getInt("limit");
        int offset = json.getInt("offset");

        /* Requête de récupération des users */
        ArrayList<User> users = getAllUsers(limit, offset);

        int count = countAllUsers(1);
        int nb_page = count;

        while (nb_page % 10 != 0) {
            nb_page++;
        }

        shell.add("nb_page", nb_page);
        shell.add("user_list", users);
        shell.add("total_users", count);

        return shell;
    }

    /**
     * Pass an user to super user
     *
     * @param o
     * @return VOID JSP
     */
    public AjaxShell toggleAdmin(Object o) {
        AjaxShell shell = new AjaxShell(URL_VOID, null);
        JSONObject json = (JSONObject) o;
        Iterator<?> keys = json.keys();

        if (!json.getString("0").equals("null")) {
            for (int i = 0; i < json.length(); i++) {
                String x = String.valueOf(i);
                toggleAdminById(json.getString(x));
            }
        }
        return shell;
    }

    public int countAllUsers(int right) {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        int res = 0;
        try {
            connexion = super.getDaoFactory().getConnection();
            preparedStatement = initialisationRequetePreparee(connexion, SQL_COUNT_NUMBER_OF_USER, false, right);
            ResultSet data = preparedStatement.executeQuery();
            /* Analyse du data retourné par la requête de consultation */
            if (data.next()) {
                res = data.getInt("count");
                System.out.println("[/] " + res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException(e);
        } finally {
            fermeturesSilencieuses(valeursAutoGenerees, preparedStatement, connexion);
        }
        return res;
    }

    public int countAllPageVisited() {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        int res = 0;
        try {
            connexion = super.getDaoFactory().getConnection();
            preparedStatement = initialisationRequetePreparee(connexion, SQL_COUNT_TOTAL_PAGE_VISITED, false);
            ResultSet data = preparedStatement.executeQuery();
            /* Analyse du data retourné par la requête de consultation */
            if (data.next()) {
                res = data.getInt("total");
                System.out.println("[/] " + res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException(e);
        } finally {
            fermeturesSilencieuses(valeursAutoGenerees, preparedStatement, connexion);
        }
        return res;
    }

    public int countAllAccount() {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        int res = 0;
        try {
            connexion = super.getDaoFactory().getConnection();
            preparedStatement = initialisationRequetePreparee(connexion, SQL_COUNT_TOTAL_ACCOUNT, false);
            ResultSet data = preparedStatement.executeQuery();
            /* Analyse du data retourné par la requête de consultation */
            if (data.next()) {
                res = data.getInt("total");
                System.out.println("[/] " + res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException(e);
        } finally {
            fermeturesSilencieuses(valeursAutoGenerees, preparedStatement, connexion);
        }
        return res;
    }

    public ArrayList<User> getAllUsers(int limit, int offset) {
        ArrayList<User> users = new ArrayList<User>();

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        boolean auth = false;
        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = super.getDaoFactory().getConnection();
            preparedStatement = initialisationRequetePreparee(connexion, SQL_GET_ALL_USER_EXCEPT_ADMIN, false, 1, limit, offset);
            ResultSet data = preparedStatement.executeQuery();
            /* Analyse du data retourné par la requête de consultation */
            while (data.next()) {
                User u = new User(
                        data.getString("mail"),
                        data.getDate("since"),
                        data.getDate("last"),
                        data.getInt("right"));
                users.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException(e);
        } finally {
            fermeturesSilencieuses(valeursAutoGenerees, preparedStatement, connexion);
        }
        return users;
    }

    public int getTotalRequestedTool() {
        Connection co = null;
        Statement stmt = null;
        int res = 0;
        ResultSet data = null;
        try {
            co = super.getDaoFactory().getConnection();
            stmt = co.createStatement();
            data = stmt.executeQuery(SQL_GET_TOOL_TOTAL_REQUESTS);
            if (data.next()) {
                res = data.getInt("total");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            fermeturesSilencieuses(data, stmt, co);
        }

        return res;
    }

    public void toggleAdminById(String Id) {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet data = null;

        try {
            connexion = super.getDaoFactory().getConnection();
            preparedStatement = initialisationRequetePreparee(connexion, SQL_TOGGLE_SUPER_USER, false, 0, Id);
            int res = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException(e);
        } finally {
            fermeturesSilencieuses(data, preparedStatement, connexion);
        }
    }

    public HashMap<String, Object> getTotalCount() {

        Connection co = null;
        Statement stmt = null;
        HashMap<String, Integer> map = null;
        HashMap<String, Object> mMap = null;
        ResultSet data = null;
        try {
            co = super.getDaoFactory().getConnection();
            stmt = co.createStatement();
            data = stmt.executeQuery(SQL_GET_VISITED_PAGE);

            mMap = new HashMap<String, Object>();
            while (data.next()) {
                ArrayList<Integer> values = new ArrayList<Integer>();
                values.add(data.getInt("anonymous"));
                values.add(data.getInt("user"));
                values.add(data.getInt("total"));
                mMap.put(data.getString("page"), values);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException(e);
        } finally {
            fermeturesSilencieuses(data, stmt, co);
        }

        return mMap;
    }

    /* Terminée */
    public AjaxShell getAdminPannel() {
        AjaxShell shell = new AjaxShell(URL_GET_ADMIN_PANEL);

        shell.add("resume_info", getTotalCount());
        shell.add("nb_pages_visited", countAllPageVisited());
        shell.add("nb_account", countAllAccount());
        shell.add("nb_request_tool", getTotalRequestedTool());

        return shell;
    }

    public ArrayList<Object> getUserListStat(int limit, int offset) {
        Connection co = null;
        PreparedStatement stmt = null;
        HashMap<String, Integer> map = null;
        ArrayList<Object> result = new ArrayList<Object>();
        ResultSet data = null;
        try {
            co = super.getDaoFactory().getConnection();
            stmt = initialisationRequetePreparee(co, SQL_GET_USER_GLOBAL_STAT, false, limit, offset);
            data = stmt.executeQuery();

            while (data.next()) {
                HashMap<String, Object> mMap = new HashMap<String, Object>();
                ArrayList<Object> values = new ArrayList<Object>();
                values.add(data.getDate("since"));
                values.add(data.getDate("last"));
                values.add(data.getInt("advanced_search"));
                values.add(data.getInt("quick_search"));
                values.add(data.getInt("sparql_editor"));
                values.add(data.getInt("total"));
                mMap.put("mail", data.getString("mail"));
                mMap.put("user", values);
                result.add(mMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException(e);
        } finally {
            fermeturesSilencieuses(data, stmt, co);
        }

        return result;
    }

    public AjaxShell getUserManager(Object o) {
        AjaxShell shell = new AjaxShell(URL_GET_USER_MANAGER);
        JSONObject json = (JSONObject) o;

        int count = countAllUsers(1);
        int nb_page = count;

        while (nb_page % 10 != 0) {
            nb_page++;
        }

        shell.add("nb_page", nb_page); // Pour la pagination        
        shell.add("table_user_list", getUserListStat(json.getInt("limit"), json.getInt("offset")));

        return shell;
    }

    public AjaxShell getCompleteUserData(Object o) {
        JSONObject json = (JSONObject) o;
        String userMail = json.getString("user");
        JSONObject catch_all = new JSONObject();
        /* Coquille vide */
        AjaxShell shell = new AjaxShell(URL_GET_COMPLETE_USER_DATA);

        /* On récupère l'utilisateur passé en paramètre (id:email) */
        User USER = super.find(userMail);
        HistoryDAOImpl HISTORY = super.getHistory();
        super.setUser(USER);
        HISTORY.setUser(USER);
        Object stat_count_request = HISTORY.getTotalCount(USER);

        /* récupération des settings de l'historique */
        super.setHistorySettings(new HistorySettingsDAO(super.getDaoFactory()));
        super.getHistorySettings().setUser(USER);
        catch_all.put("m", "getQuickSearch");
        shell.bindAjaxShell(HISTORY.getQuickSearch(catch_all));
        catch_all.put("m", "getSparqlEditor");
        shell.bindAjaxShell(HISTORY.getSparqlEditor(catch_all));
        catch_all.put("m", "getAdvancedSearch");
        shell.bindAjaxShell(HISTORY.getAdvancedSearch(catch_all));

        boolean settingsQuickSearch = super.getHistorySettings().getQuickSearch();
        boolean settingsAdvancedSearch = super.getHistorySettings().getAdvancedSearch();
        boolean settingsSparqlEditor = super.getHistorySettings().getSparqlEditor();

        /* Récupération des informations utilisateurs */
        super.setUserInfoDAO(new UserInfoDAO(super.getDaoFactory()));
        UserInfo userInfo = super.getUserInfoDAO().getInfo(USER);

        shell.add("user", USER);
        shell.add("userInfo", userInfo);
        shell.add("settingsSparqlEditor", settingsSparqlEditor);
        shell.add("stat_count_request", stat_count_request);
        shell.add("settingsAdvancedSearch", settingsAdvancedSearch);
        shell.add("settingsQuickSearch", settingsQuickSearch);

        return shell;
    }

    public AjaxShell deleteUserHistory(Object o) {
        JSONObject json = (JSONObject)o;
        Connection co = null;
        PreparedStatement stmt = null;
        HashMap<String, Integer> map = null;
        ResultSet data = null;
        System.out.println("[i] Delete interval");
        String START_DATE = json.getString("start");
        String END_DATE = json.getString("end");
        int CHOICE = json.getInt("choice");
        try {
            co = super.getDaoFactory().getConnection();
            switch (CHOICE) {
                case (0): // Only ANONYMOUS_USER
                    stmt = initialisationRequetePreparee(co, SQL_DELETE_QUICK_SEARCH_ANONYMOUS, false, START_DATE, END_DATE);
                    stmt.executeUpdate();
                    stmt = initialisationRequetePreparee(co, SQL_DELETE_ADVANCED_SEARCH_ANONYMOUS, false, START_DATE, END_DATE);
                    stmt.executeUpdate();
                    stmt = initialisationRequetePreparee(co, SQL_DELETE_SPARQL_EDITOR_ANONYMOUS, false, START_DATE, END_DATE);
                    stmt.executeUpdate();
                    break;
                case (1): // Only USERS
                    stmt = initialisationRequetePreparee(co, SQL_DELETE_QUICK_SEARCH_USER, false, START_DATE, END_DATE);
                    stmt.executeUpdate();
                    stmt = initialisationRequetePreparee(co, SQL_DELETE_ADVANCED_SEARCH_USER, false, START_DATE, END_DATE);
                    stmt.executeUpdate();
                    stmt = initialisationRequetePreparee(co, SQL_DELETE_SPARQL_EDITOR_USER, false, START_DATE, END_DATE);
                    stmt.executeUpdate();
                    break;
                case (2): // BOTH kind
                    stmt = initialisationRequetePreparee(co, SQL_DELETE_QUICK_SEARCH_USER, false, START_DATE, END_DATE);
                    stmt.executeUpdate();
                    stmt = initialisationRequetePreparee(co, SQL_DELETE_ADVANCED_SEARCH_USER, false, START_DATE, END_DATE);
                    stmt.executeUpdate();
                    stmt = initialisationRequetePreparee(co, SQL_DELETE_SPARQL_EDITOR_USER, false, START_DATE, END_DATE);
                    stmt.executeUpdate();
                    stmt = initialisationRequetePreparee(co, SQL_DELETE_QUICK_SEARCH_ANONYMOUS, false, START_DATE, END_DATE);
                    stmt.executeUpdate();
                    stmt = initialisationRequetePreparee(co, SQL_DELETE_ADVANCED_SEARCH_ANONYMOUS, false, START_DATE, END_DATE);
                    stmt.executeUpdate();
                    stmt = initialisationRequetePreparee(co, SQL_DELETE_SPARQL_EDITOR_ANONYMOUS, false, START_DATE, END_DATE);
                    stmt.executeUpdate();
                    break;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException(e);
        } finally {
            fermeturesSilencieuses(data, stmt, co);
        }
        return new AjaxShell(URL_VOID);
    }

    public AjaxShell getAgroldStat() {
        AjaxShell shell = new AjaxShell(URL_GET_AGROLD_STAT);
        
        
        
        return shell;
    }
}
