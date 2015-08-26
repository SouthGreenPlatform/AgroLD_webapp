<%-- 
    Document   : index
    Created on : Jul 15, 2015, 1:40:56 PM
    Author     : tagny
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>AgroLD:home</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="styles/style1.css" rel="stylesheet" type="text/css"/>
        <link href="styles/menu1.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="header.html"></jsp:include>
                <section>            
                    <h2><b>The Agronomic Linked Data (AgroLD) Project</b></h2>                   
                    <p>
                        At the Institute of Computational Biology (IBC), we are involved in developing methods to aid data integration
                        and knowledge management within the plant biology domain to improve information  accessibility of 
                        heterogeneous data. Among others, a solution for the data integration challenges is
                        offered by the Semantic Web technologies. The semantic web has emerged as one of the most 
                        promising solutions for high scale integration of distributed resources. This is made possible by a 
                        stack of technologies such as the Resource Description Framework (RDF), RDF Schema (RDFS), 
                        Web Ontology Language (OWL) and the SPARQL Query Language (SPARQL) proposed by the 
                        World Wide Web Consortium (W3C). RDF forms the basis of the stack allows modeling information 
                        as a directed graph composed of triples that can be queried using SPARQL. 
                    </p>
                    <p>
                        The aim of the Agronomic Linked Data (AgroLD) project is to provide a portal for bioinformatics and 
                        domain experts to exploit the homogenized data models towards efficiently generating research 
                        hypotheses.
                    </p>
                    <p>
                        AgroLD is a RDF knowledge base that is designed to integrate data from various publically available 
                        plant centric data sources and ontologies namely:
                    </p>
                    <ul>
                        <li>Gramene (genes, biological pathways, QTL and ontology annotations)</li>
                        <li>UniPortKB (Swiss-Prot and TrEMBL)</li>
                        <li>Gene Ontology Annotations (UniProtKB-GOA)</li>
                        <li>Selected resources from the South Green platform (OryGenesDB, TropGeneDB, GreenPhylDB and Oryza Tag Line: <a href="http://www.southgreen.fr/databases" target="_blank">http://www.southgreen.fr/databases</a>)</li>
                        <li>Domain specific ontologies:</li>
                        <ul>
                            <li>Gene Ontology</li>
                            <li>Plant Ontology</li>
                            <li>Plant Trait Ontology</li>
                            <li>Plant Environment Ontology</li>
                            <li>Phenotype and Attribute Ontology</li>
                            <li>NCBI Taxonomy.</li>
                        </ul>
                    </ul>
                    <p>Currently, the knowledge base includes data on:</p>
                    <ul>
                        <li>Arabidopsis thaliana</li>
                        <li>Sorghum bicolor</li>
                        <li>Zea mays</li>
                        <li>Triticum species:</li>
                        <ul>
                            <li>Triticum aestivum</li>
                            <li>Triticum uraruta</li>
                        </ul>
                        <li>Oryza species:</li>
                        <ul>
                            <li>Oryza barthii</li>
                            <li>Oryza glaberimma</li>
                            <li>Oryza brachyantha</li>
                            <li>Oryza meridionalis</li>
                            <li>Oryza sativa indica</li>
                            <li>Oryza sativa japonica</li>
                        </ul>
                    </ul>            
                </section>
            <jsp:include page="footer.html"></jsp:include>
        </div>
    </body>
</html>
