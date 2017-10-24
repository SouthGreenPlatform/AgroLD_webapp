/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.dao;

/**
 *
 * @author Jc
 */
public class AuthUserDAOException extends RuntimeException{
    
    /*
    * Constructeurs
    */
    public AuthUserDAOException( String message ) {
        super( message );
    }

    public AuthUserDAOException( String message, Throwable cause ) {
        super( message, cause );
    }

    public AuthUserDAOException( Throwable cause ) {
        super( cause );
    }
    
}
