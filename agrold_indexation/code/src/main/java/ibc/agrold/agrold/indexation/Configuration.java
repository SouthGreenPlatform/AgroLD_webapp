package ibc.agrold.agrold.indexation;
/**
 * 
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Configuration
 * @author Stella Zevio
 * Configuration of the ElasticSearch cluster's contact information.
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
	
	/**
	 * Cast the port number from configuration file (config.properties) from String to int
	 * @param port - port number of the ElasticSearch server (String)
	 * @return nport - port number of the ElasticSearch server (int)
	 * @throws IOException 
	 */
	public int readPort() throws IOException{
		String port = getProperty("server.port"); // get server port from configuration file
		int nport = Integer.parseInt(port); // parsing
		return nport;
	}
	
	/**
	 * Set property of the configuration file (config.property)
	 * @param property - a property from configuration file (cluster.name, server.host, etc...)
	 * @param value - a value for the property
	 * @throws IOException
	 */
	public void setProperty(String property, String value) throws IOException{
		FileInputStream in = new FileInputStream("config.properties"); // get configuration file
		Properties props = new Properties(); // properties
		props.load(in); // load properties from configuration file
		in.close(); // close configuration file
		FileOutputStream out = new FileOutputStream("config.properties"); // get configuration file to edit it
		props.setProperty(property, value); // set new value for a property (or create property with value)
		props.store(out, null); // save changes in configuration file
		out.close(); // close configuration file
	}
	
	/**
	 * Edit configuration file (config.properties)
	 * @throws IOException 
	 */
	public void configure() throws IOException{
		String input;
		System.out.println("\n------------------------------ Configuration ------------------------------\n"
				+ "Please, check and edit if needed ElasticSearch cluster's contact information.\n"
				+ "-------------------------------------------------------------\n"
				+ "Cluster name is actually \n"+getProperty("cluster.name")+"\n"
				+ "If you want to edit cluster name, simply write the new cluster name. Else, write 'no'.");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		input = br.readLine();
		if(!input.equals("no")){
			setProperty("cluster.name", input); // edit cluster name in configuration file
			System.out.println("Cluster name is now "+getProperty("cluster.name"));
		}
		System.out.println("\n-------------------------------------------------------------\n"
				+ "Server host is actually \n"+getProperty("server.host")+"\n"
				+ "If you want to edit server host, simply write the new server host. Else, write 'no'.");
		br = new BufferedReader(new InputStreamReader(System.in));
		input = br.readLine();
		if(!input.equals("no")){
			setProperty("server.host", input); // edit server host in configuration file
			System.out.println("Server host is now "+getProperty("server.host"));
		}
		System.out.println("\n-------------------------------------------------------------\n"
				+ "Server port is actually \n"+getProperty("server.port")+"\n"
				+ "If you want to edit server port, simply write the new server port. Else, write 'no'.");
		br = new BufferedReader(new InputStreamReader(System.in));
		input = br.readLine();
		if(!input.equals("no")){
			setProperty("server.port", input); // edit server port in configuration file
			System.out.println("Server port is now "+getProperty("server.port"));
		}
		System.out.println("\n-------------------------------------------------------------\n"
				+ "Do you use Shield (security plugin for ElasticSearch ? yes/no\n");
		br = new BufferedReader(new InputStreamReader(System.in));
		input = br.readLine();
		if(input.equals("yes")){ // Shield
			System.out.println("\n-------------------------------------------------------------\n"
					+ "Shield user is actually \n"+getProperty("shield.user")+"\n"
					+ "If you want to edit shield user, simply write the new shield user. Else, write 'no'.\n");
			br = new BufferedReader(new InputStreamReader(System.in));
			input = br.readLine();
			if(!input.equals("no")){
				setProperty("shield.user", input); // edit shield user in configuration file (Shield)
				System.out.println("Shield user is now "+getProperty("shield.user"));
			}
			System.out.println("\n-------------------------------------------------------------\n"
					+ "Do you use SSL for Shield (security plugin for ElasticSearch ? yes/no\n");
			br = new BufferedReader(new InputStreamReader(System.in));
			input = br.readLine();
			if(input.equals("no")){ // No SSL for Shield
				System.out.println("Configuration is done !");
			}
			else if(input.equals("yes")){  // SSL (Shield)
				System.out.println("\n-------------------------------------------------------------\n"
						+ "Do you use SSL with client authentification for Shield (security plugin for ElasticSearch ?) yes/no\n");
				br = new BufferedReader(new InputStreamReader(System.in));
				input = br.readLine();
				if(input.equals("no")){ // SSL without client authentification (Shield)
					System.out.println("\n-------------------------------------------------------------\n"
							+ "Truststore path is actually \n"+getProperty("shield.ssl.truststore.path")+"\n"
							+ "If you want to edit truststore path, simply write the new truststore path. Else, write 'no'.\n");
					br = new BufferedReader(new InputStreamReader(System.in));
					input = br.readLine();
					if(!input.equals("no")){
						setProperty("shield.ssl.truststore.path", input); // edit truststore path in configuration file
					}
					System.out.println("\n-------------------------------------------------------------\n"
							+ "Truststore password is actually \n"+getProperty("shield.ssl.truststore.password")+"\n"
							+ "If you want to edit truststore password, simply write the new truststore password. Else, write 'no'.\n");
					br = new BufferedReader(new InputStreamReader(System.in));
					input = br.readLine();
					if(!input.equals("no")){
						setProperty("shield.ssl.truststore.password", input); // edit truststore password in configuration file
					}
					setProperty("shield.ssl.keystore.path", ""); // no keystore path in configuration file
					setProperty("shield.ssl.keystore.password", ""); // no keystore password in configuration file
					System.out.println("Configuration is done !");
				}
				else if(input.equals("yes")){ // SSL with client authentification (Shield)
					System.out.println("\n-------------------------------------------------------------\n"
							+ "Keystore path is actually \n"+getProperty("shield.ssl.keystore.path")+"\n"
							+ "If you want to edit keystore path, simply write the new keystore path. Else, write 'no'.\n");
					br = new BufferedReader(new InputStreamReader(System.in));
					input = br.readLine();
					if(!input.equals("no")){
						setProperty("shield.ssl.keystore.path", input); // edit keystore path in configuration file
					}
					System.out.println("\n-------------------------------------------------------------\n"
							+ "Keystore password is actually \n"+getProperty("shield.ssl.keystore.password")+"\n"
							+ "If you want to edit keystore password, simply write the new keystore password. Else, write 'no'.\n");
					br = new BufferedReader(new InputStreamReader(System.in));
					input = br.readLine();
					if(!input.equals("no")){
						setProperty("shield.ssl.keystore.password", input); // edit keystore password in configuration file
					}
					setProperty("shield.ssl.truststore.path", ""); // no truststore path in configuration file
					setProperty("shield.ssl.truststore.password", ""); // no truststore path in configuration file
					System.out.println("Configuration is done !");
				}
			}
			else {
				System.err.println("Incorrect argument. Configuration may be incomplete if the ElasticSearch cluster you try to contact is configured with SSL for Shield.");
			}
		}
		else if(input.equals("no")){ // No Shield
			System.out.println("Configuration is done !");
		}
		else {
			System.err.println("Incorrect argument. Configuration may be incomplete if the ElasticSearch cluster you try to contact is configured with Shield.");
		}
	}
}
