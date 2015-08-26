/*
 * functions and constantes of the API
 */
package agrold.rest.api.sparqlaccess;

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

    //public static String sparqlEndpointURL = "http://localhost:8890/sparql";
    public static String sparqlEndpointURL = "http://volvestre.cirad.fr:8890/sparql";

    public final static String HTML = "text/html";
    public final static String JSON = "application/json";
    public final static String SPARQL_JSON = "application/sparql-results+json";
    public final static String XML = "application/sparql-results+xml";
    public final static String TSV = "text/tab-separated-values";         
    public final static String CSV = "text/csv";         
    public final static String RDF_XML = "application/rdf+xml";         
    public final static String TTL = "text/turtle";         
    
    public static String getFormatFullName(String format){
        switch(format){
            case ".json" : return JSON;
            case ".sparql-json" : return SPARQL_JSON;
            case ".html" : return HTML;
            case ".tsv" : return TSV;
            case ".csv" : return CSV;
            case ".rdf" : return RDF_XML;
            case ".xml" : return XML;
            case ".ttl" : return TTL;
            case "": return TSV;
        }
        return "";
    }

    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static String executeSparqlQuery(String sparqlQuery, String sparqlEndpoint, String resultFormat) {
        String result = "";

        String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
        String defaultGraphURI = "";
        InputStream response = null;
        resultFormat = getFormatFullName(resultFormat);
        String format = resultFormat;
        if(resultFormat.equals(APILib.JSON)){
            format = APILib.CSV;
        }
        try {
            String httpQuery = String.format("default-graph-uri=%s&query=%s&format=%s&timeout=20000",
                    URLEncoder.encode(defaultGraphURI, charset),
                    URLEncoder.encode(sparqlQuery, charset),
                    URLEncoder.encode(format, charset));
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
        if(resultFormat.equals(APILib.JSON)){
            result = csv2json(result);
        }
        return result;
    }

    public static String csv2json(String csv){
        String json = "[";
        // csv 2 line array
        String lines[] = csv.split("\\r?\\n");
        // get headers
        String headers[] = lines[0].split(",");
        for(int i=1; i<lines.length; i++){
            //System.out.println(i + ":" + headers[i]);
            String values[] = lines[i].split(",");
            json += "{";
              for(int j=0; j<headers.length;j++){
                  json+=headers[j]+":"+values[j];
                  if(j < headers.length-1)
                    json += ",";
              }
            json += "}";        
            if(i < lines.length-1)
                json += ",";
        }
        // first line == attribute of each object (each other line) in the json array        
        json +="]";
        return json;
    }
    
    public static String addLimitAndOffset(String sparqlQuery, int limit, int offset){
        if (limit > 0) {
            sparqlQuery += "\nLIMIT " + limit ;
            if(offset > 0){
                sparqlQuery += "\nOFFSET " + offset;
            }                                        
        }    
        return sparqlQuery;
    }

    public static void main(String[] args) {
        String csv="gene,desc\nAAAA,geneAAA\nBBB,geneBBB";
        System.out.println(csv +"\n"+csv2json(csv));
        //System.out.println(getAllProteinsURI(APILib.TSV));
    }
}
