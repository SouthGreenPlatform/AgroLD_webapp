/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.beans;

/**
 * Ordre MySQL
 * Colonne
 * date
 * id
 * keyword
 * mail
nbResult
 *
 * @author Jc
 */
public class QuickSearch {

    private String keyword;
    private String date;
    private int nbResult;
    private int id;
    private String mail;

    public QuickSearch(){}
    
    public QuickSearch(String keyword, String date, int id, String mail) {
        this.keyword = keyword;
        this.date = date;
        this.nbResult = nbResult;
        this.id = id;
        this.mail = mail;
    }

    public QuickSearch(int aInt, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
     
    
    public String getMail() {       return mail;    }
    public void setMail(String mail) {      this.mail = mail;    }
    public String getKeyword() {        return keyword;    }
    public void setKeyword(String keyword) {        this.keyword = keyword;    }
    public String getDate() {       return date;    }
    public void setDate(String date) {      this.date = date;    }
    public int getNbResult() {      return nbResult;    }
    public void setNbResult(int nbResult) {     this.nbResult = nbResult;    }
    public int getId() {        return id;    }
    public void setId(int id) {     this.id = id;    }
    }