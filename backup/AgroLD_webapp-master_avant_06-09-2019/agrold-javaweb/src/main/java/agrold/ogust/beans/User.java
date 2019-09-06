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

import java.sql.Date;
import agrold.ogust.beans.HistorySettings;

public class User {
    /* Propriétés du bean */
    private String JSESSION_ID;
    
    private String nom;
    private String motDePasse;
    private String job;
    private String ville;
    private String service;
    private String prenom;
    private String adresse;
    private String telephone;
    private String email;
    private java.sql.Date dateInscription;
    private java.sql.Date last;
    private int right;
    private HistorySettings history;

    public User(String email, Date dateInscription, Date last, int right) {
        this.email = email;
        this.dateInscription = dateInscription;
        this.last = last;
        this.right = right;
    }

    public User() {
    }
    
    
    
    public int getRight() {                                     return right;                           }
    public void setRight(int right) {                           this.right = right;                     }
    public String getJSESSION_ID() {                            return JSESSION_ID;                     }
    public void setJSESSION_ID(String JSESSION_ID) {            this.JSESSION_ID = JSESSION_ID;         }
    public String getJob() {                                    return job;                             }
    public void setJob(String job) {                            this.job = job;                         }
    public String getVille() {                                  return ville;                           }
    public void setVille(String ville) {                        this.ville = ville;                     }
    public String getService() {                                return service;                         }
    public void setService(String service) {                    this.service = service;                 }
    public java.sql.Date getDateInscription() {                     return dateInscription;                 }
    public void setDateInscription(java.sql.Date dateInscription) { this.dateInscription = dateInscription; }
    public java.sql.Date getLast() {                                return last;                            }
    public void setLast(java.sql.Date last) {                       this.last = last;                       }
    public String getMotDePasse() {                             return motDePasse;                      }
    public void setMotDePasse(String motDePasse) {              this.motDePasse = motDePasse;           }
    public void setNom( String nom ) {                          this.nom = nom;                         }
    public String getNom() {                                    return nom;                             }
    public void setPrenom( String prenom ) {                    this.prenom = prenom;                   }
    public String getPrenom() {                                 return prenom;                          }
    public void setAdresse( String adresse ) {                  this.adresse = adresse;                 }
    public String getAdresse() {                                return adresse;                         }
    public String getTelephone() {                              return telephone;                       }
    public void setTelephone( String telephone ) {              this.telephone = telephone;             }
    public void setEmail( String email ) {                      this.email = email;                     }
    public String getEmail() {                                  return email;                           }
}
