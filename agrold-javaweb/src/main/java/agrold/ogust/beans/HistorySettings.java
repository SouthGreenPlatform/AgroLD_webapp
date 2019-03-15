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

public class HistorySettings {
    private String mail;
    private int qs;
    private int as;
    private int sp;
    private String theme;
    private int discover;

    public HistorySettings(int qs, int as, int sp, int discover) {
        this.qs = qs;
        this.as = as;
        this.sp = sp;
        this.discover = discover;
    }

    public String getMail() {               return mail;    }
    public void setMail(String mail) {      this.mail = mail;    }
    public int getQs() {                    return qs;    }
    public void setQs(int qs) {             this.qs = qs;    }
    public int getAs() {                    return as;    }
    public void setAs(int as) {             this.as = as;    }
    public int getSp() {                    return sp;    }
    public void setSp(int sp) {             this.sp = sp;    }
    public String getTheme() {              return theme;    }
    public void setTheme(String theme) {    this.theme = theme;    }
    public int getDiscover() {              return discover;    }
    public void setDiscover(int discover) { this.discover = discover;    }    
    
}
