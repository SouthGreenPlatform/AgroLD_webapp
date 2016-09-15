/**
 * 
 */
package ibc.agrold.agrold.indexation;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.apache.log4j.BasicConfigurator;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.shield.ShieldPlugin;


/**
 * Server
 * @author Stella Zevio
 * Communication with an ElasticSearch cluster. Used to index data and update/delete an indexed document on an ElasticSearch cluster.
 *
 */
public class Server implements Indexation{
	
	private Client client; // used to communicate with the ElasticSearch cluster
	
	/**
	 * Constructor
	 * @param config - configuration parameters (given in config.properties file)
	 * @throws IOException
	 */
	public Server(Configuration config) throws IOException{		
		// Settings for the client
		Settings settings = setSettings(config);
		// Get some properties from configuration file
		String host = config.getProperty("server.host"); // get server host
		int nport = config.readPort(); // get server port and cast (string -> int) 		
		// Contact the ElasticSearch server
		this.client = initializeClient(settings, host, nport);
	}
	
	/**
	 * Settings for the ElasticSearch client when using Shield plugin
	 * @param config - configuration parameters (given in config.properties file)
	 * @return settings - settings for the client (used to contact the ElasticSearch server)
	 * @throws IOException 
	 */
	public Settings setSettingsForShield(Configuration config) throws IOException{
		Settings settings;
		// Get some properties from configuration file
		String cluster = config.getProperty("cluster.name");
		String user = config.getProperty("shield.user");
		String keystore_path = config.getProperty("shield.ssl.keystore.path"); // SSL informations for TransportClient : with client authentification (Shield)
		String truststore_path = config.getProperty("shield.ssl.truststore.path"); // SSL informations for TransportClient : without client authentification (Shield)
		/* SSL with client authentification */
		if(!keystore_path.isEmpty() && truststore_path.isEmpty()){
			String keystore_password = config.getProperty("shield.ssl.keystore.password");
			settings = Settings.settingsBuilder()
					.put("client.transport.sniff", true) // sniffing feature (allows to dynamically add new hosts and remove old ones)
					.put("cluster.name", cluster) // cluster name
					.put("shield.user", user) // user for a secured connexion (Shield plugin)
					// client authentication for secured transport communication (Shield)
					.put("shield.ssl.keystore.path", keystore_path) // path to client (jks) : the client.jks keystore needs to contain the client’s signed CA certificate and the CA certificate
				    .put("shield.ssl.keystore.password", keystore_password) // password 
				    .put("shield.transport.ssl", "true")
					.build();
		}
		/* SSL without client authentification */
		else if(keystore_path.isEmpty() && !truststore_path.isEmpty()){
			String truststore_password = config.getProperty("shield.ssl.truststore.password");
			settings = Settings.settingsBuilder()
					.put("client.transport.sniff", true) // sniffing feature (allows to dynamically add new hosts and remove old ones)
					.put("cluster.name", cluster) // cluster name
					.put("shield.user", user) // user for a secured connexion (Shield plugin)
					// without client authentication (Shield)
					.put("shield.ssl.truststore.path", truststore_path) // path
				    .put("shield.ssl.truststore.password", truststore_password) // password 
				    .put("shield.transport.ssl", "true")
					.build();
		}
		/* Shield without SSL */
		else {
			/* If information for SSL with and without client authentification are both given in config.properties, we choose to ignore both and we try to contact the cluster with Shield settings (without SSL) */
			if(keystore_path.isEmpty() && truststore_path.isEmpty()){
				System.err.println("Please, set config.properties properly. If your ElasticSearch cluster uses Shield plugin, please enter a Shield user.\n"
						+ "If Shield is configured with SSL, please give also information about keystore (for SSL with client authentification) or about truststore (for SSL without client authentification).\n"
						+ "You should not enter information for both.\n"
						+ "The application is actually trying to contact the ElasticSearch cluster without SSL configuration.");
			}
			settings = Settings.settingsBuilder()
					.put("client.transport.sniff", true) // sniffing feature (allows to dynamically add new hosts and remove old ones)
					.put("cluster.name", cluster) // cluster name
					.put("shield.user", user) // user for a secured connexion (Shield plugin)
					.build();
		}
		return settings;
	}

	/**
	 * Settings for the ElasticSearch client
	 * @param cluster - name of the cluster to contact
	 * @param user - user (Shield plugin) 
	 * @return settings - settings for the client (used to contact the ElasticSearch server)
	 * @throws IOException 
	 */
	public Settings setSettings(Configuration config) throws IOException{
		Settings settings;
		String cluster = config.getProperty("cluster.name");
		String user = config.getProperty("shield.user");
		
		// if we don't use the Shield plugin
		if(user.isEmpty()){ 
			settings = Settings.settingsBuilder()
					.put("client.transport.sniff", true) // sniffing feature (allows to dynamically add new hosts and remove old ones)
					.put("cluster.name", cluster) // cluster name
					.build();
		}
		
		// if we use the Shield plugin
		else { 
			settings = setSettingsForShield(config); // settings when using Shield plugin
		} 
		return settings;
	}
	
	
	/**
	 * Initialize the ElasticSearch client (used to contact the ElasticSearch cluster)
	 * Transport client connection (connects remotely to an ElasticSearch cluster using the transport module)
	 * @param settings - client settings (sniffing feature and cluster name)
	 * @param name - server name
	 * @param port - port number
	 * @return client - client initialized
	 * @throws UnknownHostException 
	 */
	public Client initializeClient(Settings settings, String host, int nport) throws UnknownHostException{
		Client client = TransportClient.builder()
				.addPlugin(ShieldPlugin.class) // necessary if the ElasticSearch cluster uses Shield plugin
				.settings(settings).build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), nport)); // transport client
		return client;	
	}

	
	/**
	 * Getter (client)
	 * @return client
	 */
	public Client getClient() {
		return client;
	}
	
	
	/* (non-Javadoc)
	 * @see ibc.agrold.agrold.indexation.Indexation#Index(org.elasticsearch.client.Client, ibc.agrold.agrold.indexation.Data, java.lang.String, java.lang.String)
	 */
	public void Index(Client client, Data data, String index, String type) throws IOException {
		Map<String,Object> source = new HashMap<String,Object>(); // source of indexation
		source.putAll(data.getContent()); // source is content of data
		IndexResponse response = client.prepareIndex(index, type).setSource(source).get(); // indexation
		System.out.println("\nIndex: ");
    	System.out.println("------------------------------");
		System.out.println("Id: "+response.getId());
		System.out.println("Version: "+response.getVersion());
		System.out.println("Index: "+response.getIndex());
		System.out.println("Type: "+response.getType());
    	System.out.println("------------------------------\n");
		
	}

	/* (non-Javadoc)
	 * @see ibc.agrold.agrold.indexation.Indexation#getDocument(java.lang.String, java.lang.String, java.lang.String)
	 */
	public Map<String, Object> getDocument(String index, String type, String id) {
		GetResponse response = client.prepareGet(index, type, id).get(); // get document
    	Map<String, Object> document = response.getSource(); 
    	System.out.println("\n------------------------------");
    	System.out.println("Index: " + response.getIndex());
    	System.out.println("Type: " + response.getType());
    	System.out.println("Id: " + response.getId());
    	System.out.println("Version: " + response.getVersion());
    	System.out.println(document);
    	System.out.println("------------------------------\n");
    	return document;
	}

	/* (non-Javadoc)
	 * @see ibc.agrold.agrold.indexation.Indexation#updateDocument(org.elasticsearch.client.Client, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void updateDocument(Client client, String index, String type, String id, String field, String newValue) throws InterruptedException, ExecutionException {
		Map<String,Object> update = new HashMap<String,Object>(); 
    	update.put(field, newValue); // new version of the source
    	IndexRequest indexRequest = new IndexRequest(index, type, id).source(update); // index request
    	UpdateRequest updateRequest = new UpdateRequest(index, type, id).doc(update).upsert(indexRequest); // update request       
    	client.update(updateRequest).get(); // update
		
	}

	/* (non-Javadoc)
	 * @see ibc.agrold.agrold.indexation.Indexation#deleteDocument(org.elasticsearch.client.Client, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void deleteDocument(Client client, String index, String type, String id) {
		DeleteResponse response = client.prepareDelete(index, type, id).get(); // deletion
        System.out.println("\nDeleted: ");
        System.out.println("Index: " + response.getIndex());
        System.out.println("Type: " + response.getType());
        System.out.println("Id: " + response.getId());
        System.out.println("Version: " + response.getVersion()+"\n");
	}
	
	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		if(args.length == 0){
			System.err.println("Please, enter a correct number of arguments. See documentation.");
		}
		else {	
			Configuration config = new Configuration();
			switch(args[0]) {
			case "configuration": // edit config properties (ElasticSearch cluster's contact information)
				config.configure();
				break;
				
			case "indexation": // indexation of a file	
				BasicConfigurator.configure(); // configuration for ElasticSearch (log4j)
				Server server_indexation = new Server(config); // server
				System.out.println("\n------------------------------ Indexation ------------------------------");
				if(args.length == 4){
					Data d = new Data(args[1]);
					server_indexation.Index(server_indexation.client, d, args[2], args[3]);
				}
				else {
					System.err.println("Please, enter a correct number of arguments for indexation. See documentation.");
				}
				server_indexation.client.close(); // close connection between cluster and application
				break;
			case "update": // update of an index
				BasicConfigurator.configure(); // configuration for ElasticSearch (log4j)
				Server server_update = new Server(config); // server
				System.out.println("\n------------------------------ Update of an index ------------------------------");
				if(args.length == 6){
					server_update.updateDocument(server_update.client, args[1], args[2], args[3], args[4], args[5]);
				}
				else {
					System.err.println("Please, enter a correct number of arguments for updating an index. See documentation.");	
				}
				server_update.client.close(); // close connection between cluster and application
				break;
			case "deletion": // deletion of an index
				BasicConfigurator.configure(); // configuration for ElasticSearch (log4j)
				Server server_deletion = new Server(config); // server
				System.out.println("\n------------------------------ Deletion of an index ------------------------------");
				if(args.length == 4) {
					server_deletion.deleteDocument(server_deletion.client, args[1], args[2], args[3]);
				}
				else {
					System.err.println("Please, enter a correct number of arguments for deleting an index. See documentation.");
				}
				server_deletion.client.close(); // close connection between cluster and application
				break;
			default:
				System.err.println("Please, enter correct arguments. See documentation.");
				break;
			}
		}
	}
}
