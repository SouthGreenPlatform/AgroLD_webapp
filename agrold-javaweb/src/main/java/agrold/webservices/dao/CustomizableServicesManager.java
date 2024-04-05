/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.webservices.dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Gildas
 */
public class CustomizableServicesManager {

    public static final String PATH_FIXED_PART = "/customizable/";
    public static final String CUSTOMIZABLE_SERVICES_TAG = "customizable";

    /*public static String validateName(String name) {
        
    }*/
    public static String readAPISpecification(String apiSpecificationPath, String host) {
        //JSON parser object to parse read file        
        JSONObject jsonObj = null;
        try (FileReader reader = new FileReader(apiSpecificationPath)) {
            //Read JSON file
            JSONTokener tokener = new JSONTokener(reader);
            jsonObj = new JSONObject(tokener);

            // add a new tag
            JSONObject tagObj = new JSONObject();

            // Host only has hostname and port, meaning no protocol
            String strippedUrl = host.replaceAll("http://", "").replaceAll("https://", "");

            // here we will fill the host property defined by the hosts from incoming requests & the system property agrold.name
            jsonObj.put("host", strippedUrl);

            jsonObj.put("basePath", "/" + System.getProperty("agrold.name", "aldp") + "/api");
            
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    public static void writeAPISpecification(JSONObject apiSpecification, String apiSpecificationPath) {
        // write
        try (FileWriter writer = new FileWriter(apiSpecificationPath)) {
            if (apiSpecification != null) {
                writer.write(apiSpecification.toString());
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param name The name of the service
     * @param httpMethod The the HTTP protocol method
     * @param webServiceSpecification : a JSON object with the method as key
     * @return the confirmation message
     */
    public static String addService(String name, String httpMethod, String webServiceSpecification, String host) {
        JSONObject apiSpecification = new JSONObject(readAPISpecification(Utils.AGROLDAPIJSONURL, host));
        JSONObject newServiceSpec = new JSONObject();
        String sparqlPattern = "get";
        newServiceSpec.put(httpMethod, new JSONObject(webServiceSpecification));
        //System.out.println(newServiceSpec.toString());
        String servicePath = PATH_FIXED_PART + name.replaceAll("\\s+", "");
        apiSpecification.getJSONObject("paths").put(servicePath, newServiceSpec);
        //System.out.println(apiSpecification.toString());
        writeAPISpecification(apiSpecification, Utils.AGROLDAPIJSONURL);
        return "The service /api/customizable/" + name + " has been created!";
    }

    public static String deleteService(String name, String httpMethod, String host) {
        JSONObject apiSpecification = new JSONObject(readAPISpecification(Utils.AGROLDAPIJSONURL, host));
        String servicePath = PATH_FIXED_PART + name.replaceAll("\\s+", "");
        if (apiSpecification.getJSONObject("paths").has(servicePath)) {
            apiSpecification.getJSONObject("paths").remove(servicePath);
            writeAPISpecification(apiSpecification, Utils.AGROLDAPIJSONURL);
        }
        //System.out.println(apiSpecification.toString());
        return "The service /api/customizable/" + name + " has been deleted!";
    }

    public static String updateService(String name, String httpMethod, String webServiceSpecification, String host) {
        JSONObject apiSpecification = new JSONObject(readAPISpecification(Utils.AGROLDAPIJSONURL, host));
        JSONObject serviceNewSpec = new JSONObject(webServiceSpecification);
        //System.out.println(newServiceSpec.toString());
        String servicePath = PATH_FIXED_PART + name.replaceAll("\\s+", "");
        JSONObject serviceCurrentSpec = apiSpecification.getJSONObject("paths").getJSONObject(servicePath);
        if (serviceCurrentSpec != null) {
            serviceNewSpec.keySet().forEach((key) -> {
                (serviceCurrentSpec.getJSONObject(httpMethod)).put(key, serviceNewSpec.get(key));
                apiSpecification.getJSONObject("paths").put(servicePath, serviceCurrentSpec);
            });
            writeAPISpecification(apiSpecification, Utils.AGROLDAPIJSONURL);
            return "The service /api/customizable/" + name + " has been updated!";
        } else {
            return "The service  /api/customizable/" + name + " doesn't exist yet!";
        }
        //System.out.println(apiSpecification.toString());
    }

    public static String queryCustomizableService(String serviceLocalName, MultivaluedMap<String, String> queryParams, String httpMethod, MediaType reponseMediaType, String host) throws IOException {
        JSONObject apiSpecification = new JSONObject(readAPISpecification(Utils.AGROLDAPIJSONURL, host));
        String servicePath = PATH_FIXED_PART + serviceLocalName.replaceAll("\\s+", "");
        JSONObject serviceCurrentSpec = apiSpecification.getJSONObject("paths").getJSONObject(servicePath);
        if (serviceCurrentSpec != null) {            
            String sparqlQuery = serviceCurrentSpec.getJSONObject(httpMethod).getString("sparql");
            for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
                String paramName = entry.getKey();
                String paramValue = entry.getValue().get(0);                
                sparqlQuery = sparqlQuery.replaceAll("[?]"+paramName+"(\\s+|[.]|$)", " "+paramValue+" ");
            }
            System.out.println("Sparql: " + sparqlQuery);
            System.out.println("queryParams: " +  queryParams);
            return Utils.executeSparqlQuery(sparqlQuery, Utils.sparqlEndpointURL, reponseMediaType.toString());
            //return "macho";
        } else {
            return "The service /api/customizable/" + serviceLocalName + " doesn't exist yet!";
        }
    }

    public static void main(String[] args) {
        String name = "graph_relations";
        String sparql = "BASE <http://www.southgreen.fr/agrold/>\n"
                + "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
                + "\n"
                + "SELECT distinct ?relation\n"
                + "WHERE { \n"
                + "#  values (?graph){(<gramene.genes>)}\n"
                + " GRAPH ?graph { \n"
                + "  ?subject ?relation ?object . \n"
                + " } \n"
                + "} \n"
                + "ORDER BY ?relation \n \nOFFSET ?page \nLIMIT ?pageSize";
        String httpMethod = "get";
    }
}
