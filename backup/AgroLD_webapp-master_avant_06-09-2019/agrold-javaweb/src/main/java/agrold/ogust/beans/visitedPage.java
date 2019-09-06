/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.beans;
import java.sql.Date;
/**
 *
 * @author Jc
 */
public class visitedPage {

    private String page;
    private String visitor;
    private Date date;

    public visitedPage(String page, String visitor) {
        this.page = page;
        this.visitor = visitor;
    }

    public visitedPage(String page, String visitor, Date date) {
        this.page = page;
        this.visitor = visitor;
        this.date = date;
    }

    public visitedPage(String page) {
        this.page = page;
    }
    
    
}
