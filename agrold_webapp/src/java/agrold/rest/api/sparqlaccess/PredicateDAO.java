/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.rest.api.sparqlaccess;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tagny
 */
public class PredicateDAO {

    /**
     *  What predicates are in AgroLD?
     * @return the list of URIs of predicates
     */
    public List<Predicate> getAllPredicates() {
        List<Predicate> predicates;
        predicates = new ArrayList<>();
        Predicate predicate;

        SparqlEndpoint sepAgrold = new SparqlEndpoint("http://volvestre.cirad.fr:8890/sparql");
        ResultSet rs;
        String sparqlq;
        sparqlq = "select distinct ?p where { GRAPH ?g {\n"
                + "    ?s ?p ?o\n"
                + "  }\n"
                + "FILTER (REGEX(?g, \"http://www.southgreen.fr/agrold/*\"))}";
        rs = sepAgrold.select(sparqlq);
        System.out.println(sparqlq);
        List<String> varnames = rs.getResultVars();
        for (; rs.hasNext();) {
            QuerySolution soln = rs.nextSolution();
            predicate = new Predicate(sepAgrold.convertitNoeudEnString(soln.get(varnames.get(0))));
            predicates.add(predicate);
        }
        return predicates;
    }
}
