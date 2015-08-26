/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.rest.api.sparqlaccess;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tagny
 */
@XmlRootElement(name = "predicate")
/**
 * Mapping Class Predicate
 */
public class Predicate implements Serializable{

    private String uri;

    public Predicate() {}

    public Predicate(String uri) {
        this.uri = uri;
    }
    
    public String getUri() {
        return uri;
    }

    @XmlElement
    public void setUri(String uri) {
        this.uri = uri;
    }
              
}
