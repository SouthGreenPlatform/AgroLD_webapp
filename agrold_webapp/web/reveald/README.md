ReVeaLD - Real-time Visual Explorer and Aggregator of Linked Data
=======

ReVeaLD (Real-time Visual Explorer and Aggregator of Linked Data), a Visual Query System (VQS), was developed to facilitate the biomedical user towards intuitive formulation of advanced SPARQL SELECT queries using visual cues. ReVeaLD employs a click-input-select mechanism, where domain users interact with a concept map representation of the domain-specific language (in our prototype, the CanCO model available at http://bioportal.bioontology.org/ontologies/CANCO, and stored in the 'data' folder), visualized in a force-directed layout. This approach allows the user to easily translate the knowledge graph for solving any domain-specific problem (examples provided), into a SPARQL query, without having any technical knowledge related to RDF, SPARQL and ontologies. 

The formulated visual query model could be executed against any SPARQL endpoint (in our example, http://srvgal78.deri.ie:8080/sparql, but could be changed in the 'config.js'), and results are aggregated and presented in a tabular data-browser, with 'smart' icons for the retrieved resources. A set of concept-based graphic rules mediate the conversion of the query results to domain-specific visualizations (3D Molecular Structure for molecule, Bar chart for assay results, Pathway Diagram for biological pathways, etc.). The rules also govern the assembly of these visualizations in Lens-based dialogs, as well as link out to provide additional information on data portals like ChemSpider and PubChem. To further increase the usability of the ReVeaLD platform, a dictionary of biological titles was extracted and was used for allowing Single-entity search, as well as providing a more human-readable representation of the RDF URIs.

**Live Demo :** http://srvgal78.deri.ie:8080/explorer

**Journal Paper :** ReVeaLD: A user-driven domain-specific interactive search platform for biomedical research, Journal of Biomedical Informatic (February 2014), Volume 47(0):112-130, available at http://www.sciencedirect.com/science/article/pii/S1532046413001536

**Presentation :** http://maulik-kamdar.com/2013/06/deri-friday-seminar-presentation/

**Poster :** http://maulik-kamdar.com/wp-content/uploads/2013/07/reveald_poster.pdf

**System Requirements :**
* Apache 2 Server
* PHP5
* PHP-CURL (Check support by pointing the browser to /info.php)

**Dependencies :**
* jQuery 1.7
* D3 JS (http://d3js.org/)
* Twitter Bootstrap
* Backbone JS
* Recline JS

**TODO Technicalities:**
* The Links to ChemSpider and PubChem work in the live demo, but do not function when deployed as a standalone component on a APACHE Server, due to the Cross-domain AJAX limitations. This would be modified by provision of a server side proxy (Similar to how current SPARQL queries are executed)
* The above also presents a problem for domain-specific visualizations in the standalone component
* The code is very messy !!



