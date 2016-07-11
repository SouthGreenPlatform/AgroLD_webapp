/**
 * 
 */
package ibc.agrold.agrold.indexation;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;


/**
 * Server
 * @author Stella Zevio
 * Communication with an ElasticSearch server. Used to automatically index data.
 *
 */
public class Server implements Indexation{
	
	private Client client; // used to communicate with the ElasticSearch server
	
	/**
	 * Constructor
	 * @throws IOException
	 */
	public Server() throws IOException{
		// Get configuration properties
		String cluster = getClusterName(); // get cluster name
		String host = getHost(); // get server host
		String port = getPort(); // get server port
		int nport = readPort(port); // cast server port (string -> int) 
			
		// Settings for the client
		Settings settings = setSettings(cluster); 
			
		// Contact the ElasticSearch server
		this.client = initializeClient(settings, host, nport);
	}
	
	/**
	 * Get the cluster name from configuration file (config.properties)
	 * @return cluster - name of the ElasticSearch cluster
	 */
	public String getClusterName(){
		ResourceBundle bundle = ResourceBundle.getBundle("indexation.properties.config");
		String cluster = bundle.getString("cluster.name");
		return cluster;
	}
	
	/**
	 * Get the server host from configuration file (config.properties)
	 * @return host - host of the ElasticSearch server
	 */
	public String getHost(){
		ResourceBundle bundle = ResourceBundle.getBundle("indexation.properties.config");
		String host = bundle.getString("server.host");
		return host;
	}
	
	/**
	 * Get the port number from configuration file (config.properties)
	 * @return port - port number of the ElasticSearch server (String)
	 */
	public String getPort(){
		ResourceBundle bundle = ResourceBundle.getBundle("indexation.properties.config");
		String port = bundle.getString("server.port");
		return port;
	}
	
	/**
	 * Cast the port number from configuration file (config.properties) from String to int
	 * @param port - port number of the ElasticSearch server (String)
	 * @return nport - port number of the ElasticSearch server (int)
	 */
	public int readPort(String port){
		int nport = Integer.parseInt(port);
		return nport;
	}
	
	/**
	 * Settings for the ElasticSearch client
	 * @param cluster - name of the cluster to contact
	 * @return settings - settings for the client (used to contact the ElasticSearch server)
	 */
	public Settings setSettings(String cluster){
		Settings settings = Settings.settingsBuilder()
				.put("client.transport.sniff", true) // sniffing feature (allows to dynamically add new hosts and remove old ones)
				.put("cluster.name", cluster) // cluster name
				.build(); 
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
				.settings(settings).build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), nport)); // Transport client
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
	 * @see ibc.agrold.agrold.indexation.Indexation#Index(org.elasticsearch.client.Client, ibc.agrold.agrold.indexation.Data)
	 */
	public void Index(Client client, Data data, String index, String type) throws IOException {
		// TODO Auto-generated method stub
		Map<String,Object> source = new HashMap<String,Object>(); // source of indexation
		source.putAll(data.getContent());
		System.out.println("Source: "+source);
		IndexResponse response = client.prepareIndex(index, type).setSource(source).get();
    	System.out.println("------------------------------");
		System.out.println("Id: "+response.getId());
		System.out.println("Version: "+response.getVersion());
		System.out.println("Index: "+response.getIndex());
		System.out.println("Type: "+response.getType());
    	System.out.println("------------------------------");
		
	}

	public Map<String, Object> getDocument(String name, String type, String id) {
		GetResponse response = client.prepareGet(name, type, id).get();
    	Map<String, Object> document = response.getSource();
    	System.out.println("------------------------------");
    	System.out.println("Index: " + response.getIndex());
    	System.out.println("Type: " + response.getType());
    	System.out.println("Id: " + response.getId());
    	System.out.println("Version: " + response.getVersion());
    	System.out.println(document);
    	System.out.println("------------------------------");
    	return document;
	}

	public void updateDocument(Client client, String index, String type, String id, String field, String newValue) throws InterruptedException, ExecutionException {
		Map<String,Object> update = new HashMap<String,Object>();
    	update.put(field, newValue);
    	IndexRequest indexRequest = new IndexRequest(index, type, id).source(update);
    	UpdateRequest updateRequest = new UpdateRequest(index, type, id).doc(update).upsert(indexRequest);              
    	client.update(updateRequest).get();
		
	}

	public void deleteDocument(Client client, String index, String type, String id) {
		DeleteResponse response = client.prepareDelete(index, type, id).get();
        System.out.println("Information on the deleted document: ");
        System.out.println("Index: " + response.getIndex());
        System.out.println("Type: " + response.getType());
        System.out.println("Id: " + response.getId());
        System.out.println("Version: " + response.getVersion());
	}

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		
		Server server = new Server();
		
		switch(args[0]) {
			case "indexation":
				Data d = new Data(args[1]);
				server.Index(server.client, d, args[2], args[3]);
				break;
			case "update":
				server.updateDocument(server.client, args[1], args[2], args[3], args[4], args[5]);
				break;
			case "deletion":
				server.deleteDocument(server.client, args[1], args[2], args[3]);
				break;
		}
		server.client.close();
	}
}
