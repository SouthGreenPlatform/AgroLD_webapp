
# AgroLD

AgroLD is a RDF knowledge base that consists of data integrated from a variety of plant resources and ontologies. The aim of the Agronomic Linked Data (AgroLD) project is to provide a portal for bioinformatics and domain experts to exploit the homogenized data models towards efficiently generating research hypotheses.

![graphique](img/Graphique1v2.png "Agrold_UI")


# The Architecture of project

- AgroLD_ETL
	- model
	- rdf_ttl
	- riceKB
	- test_files
	- riceKBpipeline.py
- agrold_webapp

### AgroLD_ETL

Contains parsers and model used to convert data considered for AgroLD to RDF.

* [model](/AgroLD_ETL/model): All documents who describe how data are transformed
* [rdf_ttl](/AgroLD_ETL/rdf_ttl): All output of transformation sort by dataset
* [riceKB](/AgroLD_ETL/riceKB): Contains scripts used for each data set
* [test_files](/AgroLD_ETL/test_files): All test files in input ( heterogeneous format: csv, tabbed files, gff3 )
* [riceKBpipeline.py](/AgroLD_ETL/riceKBpipeline.py): Script file where we have centralised all execution

###agrold_webapp

Scripts used for website UI, contains scripts for AgroLD API feature and all SPARQL query.

![graphique](img/Screenshot_webApp.png "Agrold_application")

