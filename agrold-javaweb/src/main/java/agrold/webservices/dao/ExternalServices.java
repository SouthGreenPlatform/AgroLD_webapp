package agrold.webservices.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author tagny
 */
public class ExternalServices {
    //public static String EUROPEPMC_SERVICE = "http://www.ebi.ac.uk/europepmc/webservices/rest/search?";
    public static String EUROPEPMC_SERVICE = "https://www.ebi.ac.uk/europepmc/webservices/rest/search?";

    public static String getOntoTermsAssociatedWithGene(String geneId) {
        String url = "https://data.gramene.org/v67/genes?q=" + geneId + "&bedFeature=gene&bedCombiner=canonical";
        String grameneJsonStr = Utils.executeHttpQuery(url);
        System.out.println("grameneJsonStr: " + grameneJsonStr);
        if (grameneJsonStr != null) {
            JSONArray resultsArray = new JSONArray(grameneJsonStr);
            int count = resultsArray.length(); // get totalCount of all jsonObjects
            for (int i = 0; i < count; i++) {   // iterate through jsonArray 
                JSONObject jsonObject = resultsArray.getJSONObject(i);  // get jsonObject @ i position 
                String objId = jsonObject.getString("_id");
                if (objId.toLowerCase().equals(geneId.toLowerCase())) {
                    JSONArray termsArray = new JSONArray();
                    if (jsonObject.has("annotations")) {
                        JSONObject annotations = jsonObject.getJSONObject("annotations");
                        for (String onto : new String[]{"GO", "PO", "TO"}) {
                            if (annotations.has(onto)) {
                                termsArray = annotations.getJSONObject(onto).getJSONArray("entries");
                            }
                        }
                    }
                    int entriesNb = termsArray.length();
                    System.err.println("entriesNb: " + entriesNb);
                    for (int j = 0; j < entriesNb; j++) {
                        JSONObject termObject = termsArray.getJSONObject(j);
                        System.out.println(termObject.getString("name"));
                    }
                    return termsArray.toString();
                }
            }
        }
        return (new JSONArray()).toString();
    }

    // URL on PubMED: https://www.ncbi.nlm.nih.gov/pmc/articles/<pmcid>/ (e.g. PMC2189073)
    public static String getValueFromXMLDocTagName(String xmlDoc, String tagName) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlDoc));

            Document doc = db.parse(is);
            NodeList name = doc.getElementsByTagName(tagName);
            Element line = (Element) name.item(0);
            return getCharacterDataFromElement(line);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    // https://www.ebi.ac.uk/europepmc/webservices/rest/search?query=PEX11-5%20sort_cited:y&pageSize=1000
    // %20: blank space

    public static JSONArray getPublicationsFromEBIEuropePMCByLabel(String label) {
        JSONArray resultJsonArray = new JSONArray();
        //String pubXMLDetails = Utils.executeHttpQuery(EUROPEPMC_SERVICE + "query=" + label);// + " sort_cited:y");        
        String pubXMLDetails = Utils.executeHttpQuery(EUROPEPMC_SERVICE + "query=" + label + "%20sort_date:y&pageSize=100");
        System.out.println("pubXMLDetails: " + pubXMLDetails);
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            /*InputSource is = new InputSource();
             is.setCharacterStream(new StringReader(pubXMLDetails));*/

            InputStream is = new ByteArrayInputStream(pubXMLDetails.getBytes("UTF-8"));

            Document doc = db.parse(is);
            Node resultListNode = doc.getElementsByTagName("resultList").item(0);
            NodeList resultNodes = resultListNode.getChildNodes();
            for (int i = 0; i < resultNodes.getLength(); i++) {
                Node resultNode = resultNodes.item(i);
                JSONObject paperObj = new JSONObject();
                NodeList fieldList = resultNode.getChildNodes();
                for (int j = 0; j < fieldList.getLength(); j++) {
                    Node fieldNode = fieldList.item(j);
                    paperObj.put(fieldNode.getNodeName(), fieldNode.getTextContent());
                }
                System.out.println(paperObj.toString());
                resultJsonArray.put(paperObj);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultJsonArray;
    }

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }

    public static void main(String[] args) {
        //System.out.println(getPublicationsOfGeneOrProteinById("P0C127"));
        System.out.println(getPublicationsFromEBIEuropePMCByLabel("TBP1"));
        //getOntoTermsAssociatedWithGene("Os02g0803700");
    }
}

/*
 G-Links & EuropePMC version
 public static String getPublicationsOfGeneOrProteinById(String entityId) {
 String source = "PubMed";
 String format = "json";
 String glinkJsonStr = Utils.executeHttpQuery(G_LINKS_SERVICE + entityId + "/extract=" + source + "/format=" + format);
 JSONArray resultJsonArray = new JSONArray();
 // extract pubmed row from the json result
 try {
 JSONArray jsonArray = new JSONArray(glinkJsonStr);
 int count = jsonArray.length(); // get totalCount of all jsonObjects
 for (int i = 0; i < count; i++) {   // iterate through jsonArray 
 JSONObject jsonObject = jsonArray.getJSONObject(i);  // get jsonObject @ i position 
 JSONObject tempObj = jsonObject.getJSONObject(entityId);
 Iterator<String> keys = tempObj.keys();
 ArrayList<String> ids = new ArrayList<>();
 while (keys.hasNext()) {
 String key = keys.next();
 JSONArray tempArray = tempObj.getJSONArray(key);
 int nb = tempArray.length();                    
 for (int j = 0; j < nb; j++) {
 try {
 JSONObject paperObj = tempArray.getJSONObject(j);
 String database = paperObj.getString("Database");
 if (database.equals(source)) {
 String id = paperObj.getString("ID");
 if (!ids.contains(id)) { // avoid duplications e.g. gene GRMZM2G022192
 ids.add(id);
 //System.out.println(id);
 String pubXMLDetails = Utils.executeHttpQuery(EUROPEPMC_SERVICE + "query=" + id);
 paperObj.put("Authors", Utils.getValueFromXMLDocTagName(pubXMLDetails, "authorString"));
 paperObj.put("Title", Utils.getValueFromXMLDocTagName(pubXMLDetails, "title"));
 paperObj.put("Year", Utils.getValueFromXMLDocTagName(pubXMLDetails, "pubYear"));
 paperObj.put("Journal", Utils.getValueFromXMLDocTagName(pubXMLDetails, "journalTitle"));
 resultJsonArray.put(paperObj);
 } else {
 //System.out.println(id);
 }                                
 }
 } catch (JSONException e) {

 }
 //System.out.println(ids);
 }
 }
 }
 } catch (JSONException e) {
 }
 return resultJsonArray.toString();
 }
 */
