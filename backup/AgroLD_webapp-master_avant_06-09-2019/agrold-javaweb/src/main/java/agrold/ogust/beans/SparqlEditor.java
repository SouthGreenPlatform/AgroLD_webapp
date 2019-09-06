/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.beans;

/**
 *
 * @author Jc
 */
public class SparqlEditor {
    private String date;
    private int id;
    private String mail;
    private String nbResult;
    private String request;

    public SparqlEditor(String date, int id, String mail, String request) {
        this.date = date;
        this.id = id;
        this.mail = mail;
        this.request = request;
    }

    public SparqlEditor(int aInt, String string, String string0, String string1, String string2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getMail() {return mail;}
    public void setMail(String mail) {this.mail = mail;}
    public String getNbResult() {return nbResult;}
    public void setNbResult(String nbResult) {this.nbResult = nbResult;}
    public String getRequest() {return request;}
    public void setRequest(String request) {this.request = request;}    
}
