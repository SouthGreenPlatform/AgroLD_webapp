package ibc.agrold.agrold.annotation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Annotator
 * @author Stella Zevio
 * Used to interrogate annotator from AgroPortal and get annotations for your Data
 */
public class Annotator {

    static final ObjectMapper mapper = new ObjectMapper();
    
    /**
     * Get annotation's
     * @param classDetails - annotation's details
     * @return id
     */
    private String getId(JsonNode classDetails){
    	String id = classDetails.get("@id").asText(); // get id
    	return id;
    }
    
    /**
     * Get annotation's pref label
     * @param classDetails - annotation's details
     * @return pref_label - pref label
     */
    private String getPrefLabel(JsonNode classDetails){
    	String pref_label = classDetails.get("prefLabel").asText(); // get pref label
    	return pref_label;
    }
    
    /**
     * Get annotation's ontology
     * @param classDetails - annotation's details
     * @return ontology
     */
    private String getOntology(JsonNode classDetails){
    	String ontology = classDetails.get("links").get("ontology").asText(); // get ontology
    	return ontology;
    }
    
    
    /**
     * Get annotations in a JSON (key-value) format
     * @param api_key - api key to contact the portal used to annotate
     * @param annotations - annotations
     * @return result - annotations in a JSON (key-value) format
     */
    public Map<String,Object> getAnnotations(String api_key, JsonNode annotations){
    	Map<String,Object> result = new HashMap<String,Object>(); // result (all annotations)
    	int cpt_annotations = 1; // number of annotations
    	for (JsonNode annotation : annotations) {
        	try { 
        		// Get the details for the class that was found in the annotation and print
                JsonNode classDetails = jsonToNode(get(api_key, annotation.get("annotatedClass").get("links").get("self").asText())); // get details of the current annotation
                Map<String,Object> annot = new HashMap<String,Object>(); // will contain details of the current annotation
        		String number = String.valueOf(cpt_annotations); // number of annotations (String)
                String id = getId(classDetails); // get current annotation's id
                String pref_label = getPrefLabel(classDetails); // get current annotation's pref label
                String ontology = getOntology(classDetails); // get current annotation's ontology
                // save details of the current annotation
                annot.put("id", id); 
                annot.put("prefLabel",pref_label);
                annot.put("ontology",ontology);
                JsonNode hierarchy = annotation.get("hierarchy"); // information on the hierarchy of the annotation
                // If we have hierarchy annotations, print the related class information as well
                if (hierarchy.isArray() && hierarchy.elements().hasNext()) {
                	Map<String,Object> hierarchy_annotations = new HashMap<String,Object>(); // will contain details on the hierarchy of the current annotation
                	int cpt_hier_annotations = 1; // number of classes from hierarchy
                    for (JsonNode hierarchyAnnotation : hierarchy) {
                        classDetails = jsonToNode(get(api_key, hierarchyAnnotation.get("annotatedClass").get("links").get("self").asText())); // get details on the hierarchy of the current annotation
                        Map<String,Object> hierarchy_annot = new HashMap<String,Object>(); // will contain details on the current class of the hierarchy of the current annotation
                        id = getId(classDetails); // get current class' id
                        pref_label = getPrefLabel(classDetails); // get current class' pref label
                        ontology = getOntology(classDetails); // get current class' ontology
                        // save details of the current class from hierarchy
                        hierarchy_annot.put("id", id); 
                        hierarchy_annot.put("prefLabel",pref_label);
                        hierarchy_annot.put("ontology",ontology);
                        // save all informations about the current class of the hierarchy
                        hierarchy_annotations.put("Class details "+cpt_hier_annotations, hierarchy_annot);
                        cpt_hier_annotations=cpt_hier_annotations+1;
                    }
                    // save all informations about the current annotation's hierarchy
                    annot.put("hierarchy annotations", hierarchy_annotations);
                }
                result.put("Class details "+number, annot);
        	} catch (Exception e) {
        		System.err.println("Error retrieving class: " + e.toString());
        	}
        	cpt_annotations=cpt_annotations+1;
        }
    	return result;
    }
    
    /**
     * Print annotations
     * @param api_key -  api key to contact the portal used to annotate
     * @param annotations - annotations
     */
    private static void printAnnotations(String api_key, JsonNode annotations) {
        for (JsonNode annotation : annotations) {
        	try {
        		// Get the details for the class that was found in the annotation and print
                JsonNode classDetails = jsonToNode(get(api_key, annotation.get("annotatedClass").get("links").get("self").asText()));
                System.out.println("Class details");
                System.out.println("\tid: " + classDetails.get("@id").asText());
                System.out.println("\tprefLabel: " + classDetails.get("prefLabel").asText());
                System.out.println("\tontology: " + classDetails.get("links").get("ontology").asText());
                System.out.println("\n");
                JsonNode hierarchy = annotation.get("hierarchy");
                // If we have hierarchy annotations, print the related class information as well
                if (hierarchy.isArray() && hierarchy.elements().hasNext()) {
                    System.out.println("\tHierarchy annotations");
                    for (JsonNode hierarchyAnnotation : hierarchy) {
                        classDetails = jsonToNode(get(api_key, hierarchyAnnotation.get("annotatedClass").get("links").get("self").asText()));
                        System.out.println("\t\tClass details");
                        System.out.println("\t\t\tid: " + classDetails.get("@id").asText());
                        System.out.println("\t\t\tprefLabel: " + classDetails.get("prefLabel").asText());
                        System.out.println("\t\t\tontology: " + classDetails.get("links").get("ontology").asText());
                    }
                }
        	} catch (Exception e) {
        		System.err.println("Error retrieving class: " + e.toString());
        	}
        }
    }

  
    /**
     * Convert JSON into JsonNode
     * @param json - JSON
     * @return root - JsonNode
     */
    private static JsonNode jsonToNode(String json){
        JsonNode root = null;
        try {
            root = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
        return root;
    }

  
    /**
     * GET method
     * @param api_key - api key to contact the portal used to annotate
     * @param urlToGet - url to contact
     * @return result - result of the GET method
     */
    private static String get(String api_key, String urlToGet) {
        URL url;
        HttpURLConnection conn;
        String line;
        String result = "";
        try {
            url = new URL(urlToGet);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "apikey token=" + api_key);
            conn.setRequestProperty("Accept", "application/json");
            BufferedReader rd = null;
            try {
            rd = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            } catch(FileNotFoundException e) {
            	System.err.println("File not found");
            } catch(IOException e){
            	System.err.println("IOException");
            }
        	
            if(rd != null) {
            	while ((line = rd.readLine()) != null) {
                    result += line;
                }
                rd.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

  
    /**
     * POST method
     * @param api_key - api key to contact the portal used to annotate
     * @param urlToGet - url to contact
     * @param urlParameters - parameters
     * @return result - result of the POST method
     */
    private static String post(String api_key, String urlToGet, String urlParameters) {
        URL url;
        HttpURLConnection conn;
        String line;
        String result = "";
        try {
            url = new URL(urlToGet);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "apikey token=" + api_key);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            conn.disconnect();
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * Creation of the annotated file
     * @param filename - name of the file
     * @param annotated_text - annotated file's content
     * @throws IOException 
     */
    public void createAnnotatedFile(String filename, Map<String,Object> annotated_text) throws IOException{
    	int beginning = filename.lastIndexOf("/"); // position of the beginning of the name of the file (without the absolute path). Example : for /path/to/data.json, beginning will be the position of "data.json".)
    	String annotated_filename = "annotated_"+filename.substring(beginning+1); // file name of the annotated file
    	BufferedWriter output = null; 
        try {
        	File annotated_file = new File("annotated_data/"+annotated_filename); // creation of the annotated file
            output = new BufferedWriter(new FileWriter(annotated_file));
            output.write(annotated_text.toString()); // write the content of the annotated file
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
          if ( output != null ) {
            output.close();
          }
        }
    }
    
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
    	if(args.length == 0){
			System.err.println("Please, enter a correct number of arguments. See documentation.");
		}
		else {	
			Configuration config = new Configuration(); // configuration file
			Annotator annotator = new Annotator();
			String rest_url = config.getProperty("rest.url"); // read rest url from configuration file
			String api_key = config.getProperty("api.key"); // read api key from configuration file
			String urlParameters; // parameters
	        JsonNode annotations;
	        Data d = new Data(args[1]); // file to annotate
	        @SuppressWarnings("deprecation")
			String textToAnnotate = URLEncoder.encode(d.getContent().toString()); // text to annotate (content of the file to annotate)
	        Map<String, Object> annotations_for_text = new HashMap<String,Object>(); // annotations
	        Map<String,Object> annotated_text = new HashMap<String,Object>(); // original file + annotations
	        
			switch(args[0]){
			case "get": // GET method
				System.out.println("\n------------------------------ Annotations (get) ------------------------------");
				if(args.length == 2){
					urlParameters = "display=prefLabel&text=" + textToAnnotate;
			        annotations = jsonToNode(get(api_key, rest_url + "/annotator?" + urlParameters));
			        System.out.println(annotations);
			        printAnnotations(api_key, annotations); // prints annotations on the console
			        annotations_for_text = annotator.getAnnotations(api_key, annotations); // get annotations in JSON (key-value) format
				}
				else {
					System.err.println("Please, enter a correct number of arguments for annotation. See documentation.");
				}
				break;
			case "post": // POST method
				System.out.println("\n------------------------------ Annotations (post) ------------------------------");
				if(args.length == 2){
					urlParameters = "text=" + textToAnnotate;
			        annotations = jsonToNode(post(api_key, rest_url + "/annotator", urlParameters));
			        printAnnotations(api_key, annotations); // prints annotations on the console
			        annotations_for_text = annotator.getAnnotations(api_key, annotations); // get annotations in JSON (key-value) format
				}
				else {
					System.err.println("Please, enter a correct number of arguments for annotation. See documentation.");
				}
				break;
			case "hierarchy": // Annotations + hierarchy
				System.out.println("\n------------------------------ Annotations (with hierarchy) ------------------------------");
				if(args.length == 2){
					urlParameters = "max_level=3&text=" + textToAnnotate;
			        annotations = jsonToNode(get(api_key, rest_url + "/annotator?" + urlParameters));
			        printAnnotations(api_key, annotations); // prints annotations on the console
			        annotations_for_text = annotator.getAnnotations(api_key, annotations); // get annotations in JSON (key-value) format        
				}
				else {
					System.err.println("Please, enter a correct number of arguments for annotation. See documentation.");
				}
				break;
			case "labels": // Only pref labels
				System.out.println("\n------------------------------ Annotations (labels) ------------------------------");
				if(args.length == 2){
					urlParameters = "include=prefLabel,synonym,definition&text=" + textToAnnotate;
			        annotations = jsonToNode(get(api_key, rest_url + "/annotator?" + urlParameters));
			        ArrayList<String> labels = new ArrayList<String>();
			        for (JsonNode annotation : annotations) { // for all annotations
			            if(!labels.contains(annotation.get("annotatedClass").get("prefLabel").asText().toLowerCase())){ // if pref label does not exists
			            	labels.add(annotation.get("annotatedClass").get("prefLabel").asText().toLowerCase()); // add it
			            }
			        }
			        for(String label : labels){
			        	System.out.println(label+"\n"); // prints labels on the console
			        }
			        annotations_for_text.put("labels", labels); // get pref labels in JSON (key-value) format
				}
				else {
					System.err.println("Please, enter a correct number of arguments for annotation. See documentation.");
				}
				break;	
			default:
				System.out.println("Please enter a correct input for annotation. See documentation.");
				break;
			}
			annotated_text.putAll(d.getContent()); // annotated text contains content of the data...
			annotated_text.put("annotation", annotations_for_text); // ...+ annotations !
			annotator.createAnnotatedFile(args[1], annotated_text); // creation of the annotated file
			System.out.println("------------------------------ Annotation is done ! ------------------------------");	
		}
    }
}
