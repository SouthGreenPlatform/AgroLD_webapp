/**
 * 
 */
package ibc.agrold.agrold.indexation;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.elasticsearch.client.Client;

/**
 * @author Stella Zevio
 *
 */
public interface Indexation {
	public void Index(Client client, Data data, String index, String type) throws IOException;
	public Map<String,Object> getDocument(String name, String type, String id);
	public void updateDocument(Client client, String index, String type, String id, String field, String newValue) throws InterruptedException, ExecutionException;
	public void deleteDocument(Client client, String index, String type, String id);
}
