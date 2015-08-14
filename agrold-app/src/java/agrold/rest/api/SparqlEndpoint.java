/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.rest.api;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

/**
 *
 * @author tagny
 */
public class SparqlEndpoint {
    private String url;

    public SparqlEndpoint() {
    }    
    
    public SparqlEndpoint(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }     
    
    public String convertitNoeudEnString(RDFNode n) {
        String str = null;
        // Tester les choses retournées
        if (n.isLiteral()) {
            str = ((Literal) n).getLexicalForm();
        }
        if (n.isResource()) {
            Resource r = (Resource) n;
            if (!r.isAnon()) {
                str = r.getURI();
            }
        }
        return str;
    }
    
    public ResultSet select(String requete) {
        System.out.println("Select to "+this.getUrl());
        ResultSet rs = null;
        try {
            Query query = QueryFactory.create(requete);
            // Remote execution.
            QueryExecution qexec = QueryExecutionFactory.sparqlService(this.getUrl(), query);
            // Set the specific timeout.
            ((QueryEngineHTTP) qexec).addParam("timeout", "10000");

            // Execute.            
            rs = qexec.execSelect();
            //qexec.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }
}
