/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.dao;

import agrold.ogust.handler.AjaxShell;

/**
 *
 * @author Jc
 */
public interface AdminDAO extends UserDAO {
    
     
    AjaxShell getUserList();
    AjaxShell getInfoUser();
    AjaxShell Home();
    
}
