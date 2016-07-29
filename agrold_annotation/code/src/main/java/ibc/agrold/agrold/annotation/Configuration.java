/**
 * 
 */
package ibc.agrold.agrold.annotation;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration
 * @author Stella Zevio
 * Read config.properties
 *
 */
public class Configuration {
	/**
	 * Get property from configuration file (config.properties)
	 * @return property - name of the property to read in configuration file
	 * @throws IOException 
	 */
	public String getProperty(String property) throws IOException{
		Properties props = new Properties(); // properties
		FileInputStream in = new FileInputStream("config.properties"); // get configuration file
		props.load(in); // load properties from configuration file
		String prop = props.getProperty(property); // get property
		return prop; 
	}
}
