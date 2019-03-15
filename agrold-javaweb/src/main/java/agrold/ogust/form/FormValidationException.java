/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.form;

/**
 *
 * @author Jc
 */

public class FormValidationException extends Exception {
    /*
     * Constructeur
     */
    public FormValidationException( String message ) {
        super( message );
    }
}