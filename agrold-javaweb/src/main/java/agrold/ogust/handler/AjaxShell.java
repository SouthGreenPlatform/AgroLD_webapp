/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Classe servant de transport universel de donnée serializées, entre les classes,
 * la session ainsi que les JSP.
 * L'AjaxHandler Appel une méthode sur un objet, Cette dernière lui retournera un
 * AjaxShell qui contient un lien absolue du JSP à traiter et renvoyer au client,
 * c'est l'attribut URL; Les objets contenu dans l'AjaxShell vont être transférés
 * en session avant que le dispatcher ne soit appelé avec l'URL de l'AjaxShell en
 * argument.
 * Bon courage V
 */
package agrold.ogust.handler;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jc
 */
public class AjaxShell {

    // Contient l'url du fichier JSP cible
    private String URL;
    // Contient un ensemble de réponse accessible par une clé, généralement des beans ( Objet Java serializable )
    private HashMap<String, Object> collection = null;

    /* Constructors */
    public AjaxShell() {
        this.collection = new HashMap<String, Object>();
    }

    public AjaxShell(String URL) {
        this.URL = URL;
        this.collection = new HashMap<String, Object>();
    }

    public AjaxShell(String URL, HashMap<String, Object> collection) {
        this.URL = URL;
        this.collection = collection;
    }

    /* Getters Setters  */
    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public HashMap<String, Object> getCollection() {
        return collection;
    }

    public boolean isEmpty() {
        return this.collection == null;
    }

    public void setCollection(HashMap<String, Object> collection) {
        this.collection = collection;
    }

    /* MEYHODES */
    public void add(String key, Object value) {
        this.collection.put(key, value);
    }

    public void bindAjaxShell(AjaxShell shell2) {

        HashMap<String, Object> shell3 = shell2.getCollection();
        for (Map.Entry< String, Object> entry : shell3.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
            this.add(entry.getKey(), entry.getValue());
        }

    }

}
