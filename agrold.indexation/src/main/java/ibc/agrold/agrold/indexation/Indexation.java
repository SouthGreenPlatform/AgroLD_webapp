/**
 * 
 */
package ibc.agrold.agrold.indexation;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.elasticsearch.client.Client;

/**
 * Indexation
 * @author Stella Zevio
 *
 */
public interface Indexation {
	
	/**
	 * @param client - client for an ElasticSearch cluster 
	 * @param data - the data to index
	 * @param index - future index in the ElasticSearch cluster
	 * @param type - future type in the ElasticSearch cluster
	 * @throws IOException
	 */
	public void Index(Client client, Data data, String index, String type) throws IOException;
	
	/**
	 * @param index - index of the document to retrieve
	 * @param type - type of the document to retrieve in the ElasticSearch cluster
	 * @param id - id of the document to retrieve in the ElasticSearch cluster
	 * @return document - an indexed document 
	 */
	public Map<String,Object> getDocument(String index, String type, String id);
	
	/**
	 * @param client - client for an ElasticSearch cluster
	 * @param index - index of the document to update in the ElasticSearch cluster
	 * @param type - type of the document to update in the ElasticSearch cluster
	 * @param id - id of the document to update in the ElasticSearch cluster
	 * @param field - field to update
	 * @param newValue - new value for the field
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void updateDocument(Client client, String index, String type, String id, String field, String newValue) throws InterruptedException, ExecutionException;
	
	/**
	 * @param client - client for an ElasticSearch cluster
	 * @param index - index of the document to delete in the ElasticSearch cluster
	 * @param type - type of the document to delete in the ElasticSearch cluster
	 * @param id - id of the document to delete in the ElasticSearch cluster
	 */
	public void deleteDocument(Client client, String index, String type, String id);
}
