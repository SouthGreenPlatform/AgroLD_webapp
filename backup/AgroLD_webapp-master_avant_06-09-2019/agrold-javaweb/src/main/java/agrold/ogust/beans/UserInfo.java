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
public class UserInfo {
    private String nom;
    private String prenom;
    private String service;
    private String ville;
    private String job;
    private String mail;   

    public UserInfo(String nom, String prenom, String service, String ville, String job, String mail) {
        this.nom = nom;
        this.prenom = prenom;
        this.service = service;
        this.ville = ville;
        this.job = job;
        this.mail = mail;
    }

    public UserInfo(String nom, String prenom, String service, String ville, String job) {
        this.nom = nom;
        this.prenom = prenom;
        this.service = service;
        this.ville = ville;
        this.job = job;
    }    
    public String getNom() {        return nom;     }
    public void setNom(String nom) {        this.nom = nom;     }
    public String getPrenom() {     return prenom;      }
    public void setPrenom(String prenom) {      this.prenom = prenom;       }
    public String getService() {        return service;     }
    public void setService(String service) {        this.service = service;     }
    public String getVille() {      return ville;       }
    public void setVille(String ville) {        this.ville = ville;     }
    public String getJob() {        return job;     }
    public void setJob(String job) {        this.job = job;     }
    public String getMail() {       return mail;        }
    public void setMail(String mail) {      this.mail = mail;       }    
    
}
