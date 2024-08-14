package agrold.config;

import lombok.Getter;

/**
 * This Static class is used to store and get the properties used across all of the application.
 */
public class PropertiesBean {
    @Getter private static final String sparqlEndpoint = System.getProperty("agrold.sparql_endpoint", "http://sparql.southgreen.fr");
    @Getter private static final String RFLink = System.getProperty("agrold.rf_link", "http://rf.southgreen.fr/");
    @Getter private static final String instance = System.getProperty("agrold.instance", "null");
    @Getter private static final String facetedSearchURL = System.getProperty("agrold.faceted_search_url", "http://sparql.southgreen.fr/faceted");

    @Getter private static final String APIVersion = System.getProperty("APIVersion", "undefined");

}
