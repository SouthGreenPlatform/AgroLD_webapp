/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.beans;
import java.sql.Date;

import lombok.AllArgsConstructor;
/**
 *
 * @author Jc
 */
@AllArgsConstructor
public class VisitedPage {

    private String page;
    private String visitor;
    private Date date;  

    public VisitedPage(String page, String visitor) {
        this.page = page;
        this.visitor = visitor;
    }

    public VisitedPage(String page) {
        this.page = page;
    }
    
}
