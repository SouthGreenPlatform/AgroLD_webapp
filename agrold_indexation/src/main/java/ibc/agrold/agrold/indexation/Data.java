/**
 * 
 */
package ibc.agrold.agrold.indexation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;

/**
 * Data
 * @author Stella Zevio
 * Representation of a JSON file.
 *
 */
public class Data {
	private Map<String,Object> content; // content of the JSON file
	
	/**
	 * Constructor
	 * @param filename - name of the JSON file
	 * @throws IOException
	 */
	public Data(String filename) throws IOException{
		this.content = readDocumentJSON(filename); // get content of the JSON file which name is filename
	}
	
	/**
	 * Read a JSON file and return its content
	 * @param filename - name of the JSON file
	 * @return document - the content of the JSON file
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked") // unchecked cast warning
	public Map<String,Object> readDocumentJSON(String filename) throws IOException {
		Map<String, Object> content = new HashMap<String,Object>();
		String file = new String(Files.readAllBytes(Paths.get(filename))); // get content of the file which name is the parameter
		Gson gson = new Gson(); // a Java serialization/deserialization library that can convert Java Objects into JSON and back
		content = (Map<String,Object>) gson.fromJson(file, content.getClass()); // conversion (here is the cast warning because of the extension of Map)
		return content;
	}
	
	/**
	 * Getter (content)
	 * @return content
	 */
	public Map<String, Object> getContent(){
		return content;
	}
}
