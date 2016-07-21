/*
 * functions and constantes of the API
 */
package agrold.rest.api.sparqlaccess;

import static agrold.rest.api.sparqlaccess.ExternalServices.getCharacterDataFromElement;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author tagny
 */
public class APILib {

    //public static String sparqlEndpointURL = "http://localhost:8890/sparql";
    public static String sparqlEndpointURL = "http://volvestre.cirad.fr:8890/sparql";
    //public static String sparqlEndpointURL = "http://volvestre.cirad.fr:3128/sparql";

    public final static String HTML = "text/html";
    public final static String JSON = "application/json";
    public final static String SPARQL_JSON = "application/sparql-results+json";
    public final static String XML = "application/sparql-results+xml";
    public final static String TSV = "text/tab-separated-values";
    public final static String CSV = "text/csv";
    public final static String RDF_XML = "application/rdf+xml";
    public final static String TTL = "text/turtle";

    public static String getFormatFullName(String format) {
        switch (format) {
            case ".json":
                return JSON;
            case ".sparql-json":
                return SPARQL_JSON;
            case ".html":
                return HTML;
            case ".tsv":
                return TSV;
            case ".csv":
                return CSV;
            case ".rdf":
                return RDF_XML;
            case ".xml":
                return XML;
            case ".ttl":
                return TTL;
            //case "": return TSV;
            default:
                return TSV;
        }
        // return "";
    }

    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static String executeSparqlQuery(String sparqlQuery, String sparqlEndpoint, String resultFormat) {
        System.out.println("BEGINNING...");
        String result = "";

        String defaultGraphURI = "";
        resultFormat = getFormatFullName(resultFormat);
        String format = resultFormat;
        if (resultFormat.equals(APILib.JSON)) {
            format = APILib.TSV;
        }
        InputStream response = null;
        String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
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
            Logger.getLogger(APILib.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(APILib.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ex) {
                    Logger.getLogger(APILib.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (resultFormat.equals(APILib.JSON)) {
            result = tsv2json(result);
        }
        System.out.println("...END");
        return result;
    }

    public static String executeHttpQuery(String url) {
        String result = "";
        InputStream response = null;
        String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
        try {
            // Firing a HTTP GET request with (optionally) query parameters
            URLConnection connection = new URL(url).openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            response = connection.getInputStream();
            result = convertStreamToString(response);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(APILib.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(APILib.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ex) {
                    Logger.getLogger(APILib.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    public static String tsv2json(String tsv) {
        String json = "[";
        // csv 2 line array
        String lines[] = tsv.split("\\r?\\n");
        // get headers
        String headers[] = lines[0].split("\t");
        for (int i = 1; i < lines.length; i++) {
            //System.out.println(i + ":" + headers[i]);
            String values[] = lines[i].split("\t");
            json += "{";
            for (int j = 0; j < headers.length; j++) {
                json += headers[j] + ":" + values[j];
                if (j < headers.length - 1) {
                    json += ",";
                }
            }
            json += "}";
            if (i < lines.length - 1) {
                json += ",";
            }
        }
        // first line == attribute of each object (each other line) in the json array        
        json += "]";
        return json;
    }

    public static String addLimitAndOffset(String sparqlQuery, int limit, int offset) {
        if (limit > 0) {
            sparqlQuery += "\nLIMIT " + limit;
            if (offset >= 0) {
                sparqlQuery += "\nOFFSET " + (limit * offset); // to go after the previous result
            }
        }
        return sparqlQuery;
    }

    public static String getEntitiesByKeyWord(String keyword, String typeUri, int page, int pageSize, String resultFormat) {
        String sparqlQuery = "prefix	agrold:<http://www.southgreen.fr/agrold/vocabulary/> \n"
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
                + "SELECT  distinct ?Id ?Name ?Description  (?entity as ?URI)\n"
                + "WHERE {\n"
                + "  VALUES ?keyword {\n"
                + "    \"" + keyword + "\" \n"
                + "  }  \n"
                + "  ?entity agrold:description ?Description .\n"
                + "  ?entity rdfs:subClassOf <" + typeUri + ">.\n"
                + "  ?entity rdfs:label ?Name .\n"
                + "  BIND(REPLACE(str(?entity), '^.*(#|/)', \"\") AS ?Id)   \n"
                + "  FILTER(REGEX(?Name, ?keyword,\"i\")  || REGEX(?Id, ?keyword,\"i\") || REGEX(?Description, ?keyword,\"i\"))\n"
                + "}";
        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);
        String result = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
        return result;
    }

    public static String getValueFromXMLDocTagName(String xmlDoc, String tagName) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlDoc));

            Document doc = db.parse(is);
            NodeList name = doc.getElementsByTagName(tagName);
            Element line = (Element) name.item(0);
            return getCharacterDataFromElement(line);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(APILib.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }

    public static boolean arrayListContains(ArrayList<String> array, String val) {
        for (String s : array) {
            if(s.equals(val))
                return true;
        }
        return false;
    }

    public static void main(String[] args) {
        //String csv = "gene\tdesc\nAAAA\tgeneAAA\nBBB\tgeneBBB";
        //System.out.println(csv + "\n" + tsv2json(csv));
        //System.out.println(getAllProteinsURI(APILib.TSV));
        //System.out.println(getEntitiesByKeyWord("A0A060D1L3", "http://purl.obolibrary.org/obo/SO_0000771", 0, 1, TSV));
        //System.out.println(executeHttpQuery("http://link.g-language.org/A6MCY9/extract=PubMed/format=json"));

    }
}
