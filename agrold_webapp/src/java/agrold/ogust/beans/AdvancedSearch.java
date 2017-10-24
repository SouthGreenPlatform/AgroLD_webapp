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
public  class AdvancedSearch{
    private int id;
    private String mail;
    private String keyword;
    private String type;
    private int nbResult;
    private String date;

    public AdvancedSearch(int id, String mail, String keyword, String type,String date) {
        this.id = id;
        this.mail = mail;
        this.keyword = keyword;
        this.type = type;
        this.nbResult = nbResult;
        this.date = date;
    }
    
    public int getId() {        return id;    }
    public void setId(int id) {     this.id = id;    }
    public String getMail() {       return mail;    }
    public void setMail(String mail) {      this.mail = mail;    }
    public String getKeyword() {        return keyword;    }
    public void setKeyword(String keyword) {        this.keyword = keyword;    }
    public String getType() {       return type;    }
    public void setType(String type) {      this.type = type;    }
    public int getNbResult() {      return nbResult;    }
    public void setNbResult(int nbResult) {     this.nbResult = nbResult;    }
    public String getDate() {       return date;    }
    public void setDate(String date) {      this.date = date;    }}
