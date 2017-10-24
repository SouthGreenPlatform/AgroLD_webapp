/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.dao;

/**
 *
 * @author Jc
 *   DAO For User and Database Interactions
 */

import agrold.ogust.dao.*;
import agrold.ogust.beans.User;

    public interface UserDAO {
    
    void create(User user) throws DAOException;
    User find(String mail) throws DAOException;
    void creerUserInfo(User user)throws DAOException;
    void creerSettingsHistorique(User user)throws DAOException;
}
