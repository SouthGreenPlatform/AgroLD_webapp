/*
 * functions and constantes of the API
 */
package agrold.rest.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tagny
 */
public class APILib {

    //public static String speURL = "http://localhost:8890/sparql";
    public static String speURL = "http://volvestre.cirad.fr:8890/sparql";

    public static String HTML = "text/html";
    public static String JSON = "application/sparql-results+json";
    public static String XML = "application/sparql-results+xml";

    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static String executeSparqlQuery(String sparqlQuery, String sparqlEndpoint, String resultFormat) {
        String result = "";

        String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
        String defaultGraphURI = "";
        InputStream response = null;
        try {
            String httpQuery = String.format("default-graph-uri=%s&query=%s&format=%s&timeout=20000",
                    URLEncoder.encode(defaultGraphURI, charset),
                    URLEncoder.encode(sparqlQuery, charset),
                    URLEncoder.encode(resultFormat, charset));
            // Firing a HTTP GET request with (optionally) query parameters
            URLConnection connection = new URL(sparqlEndpoint + "?" + httpQuery).openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            response = connection.getInputStream();
            result = convertStreamToString(response);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GeneDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GeneDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ex) {
                    Logger.getLogger(GeneDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return result;
    }

    // describe a resource
    public static String getResourceDescription(String resourceURI, String resultFormat) {
        String resource;

        String sparqlQuery = "prefix	agrold_vocabulary:<http://www.southgreen.fr/agrold/vocabulary/> \n"
                + "SELECT distinct ?property ?object\n"
                + "WHERE {\n"
                + "  GRAPH ?g {\n"
                + "    <" + resourceURI + "> ?property ?object \n"
                + "  }\n"
                + "FILTER(REGEX(?g, \"http://www.southgreen.fr/agrold/*\"))\n"
                + "}";
        //+ "GROUP BY ?property";
        System.out.println(sparqlQuery);

        resource = APILib.executeSparqlQuery(sparqlQuery, APILib.speURL, resultFormat);
        return resource;

    }

    public static String listGraph(String resultFormat) {
        String graphs;

        String sparqlQuery = "SELECT distinct ?graph\n"
                + "WHERE {\n"
                + " GRAPH ?graph {\n"
                + "   ?subject ?predicate ?object.\n"
                + " }\n"
                + " filter(REGEX(?graph, \"http://www.southgreen.fr/agrold/*\"))\n"
                + "}";
        //+ "GROUP BY ?property";
        System.out.println(sparqlQuery);

        graphs = APILib.executeSparqlQuery(sparqlQuery, APILib.speURL, resultFormat);
        return graphs;
    }

}
