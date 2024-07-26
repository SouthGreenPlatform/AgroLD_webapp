package agrold.config;

import lombok.Getter;

/**
 * This Static class is used to store and get the properties used across all of the application.
 */
public class PropertiesBean {
    @Getter private static final String sparqlEndpoint = System.getProperty("agrold.sparql_endpoint", "http://sparql.southgreen.fr");
    @Getter private static final String DBConnectionUrl = System.getProperty("agrold.db_connection_url");
    @Getter private static final String DBUsername = System.getProperty("agrold.db_username");
    @Getter private static final String DBPassword = System.getProperty("agrold.db_password");
    @Getter private static final String RFLink = System.getProperty("agrold.rf_link", "http://rf.southgreen.fr/");
    @Getter private static final String instance = System.getProperty("agrold.instance");

    @Getter private static final String APIVersion = System.getProperty("APIVersion", "undefined");
    
    public static boolean hasDB() {
        return DBConnectionUrl != null && DBUsername != null && DBPassword != null;
    }
}
