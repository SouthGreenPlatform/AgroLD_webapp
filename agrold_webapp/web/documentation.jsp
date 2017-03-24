<%-- 
    Document   : documentation
    Created on : Jul 16, 2015, 9:47:05 AM
    Author     : tagny
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
        <!-- Script for google analytic -->
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-88660031-1', 'auto');
  ga('send', 'pageview');

</script>
    <head>
        <title>AgroLD:Documentation</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="styles/style1.css" rel="stylesheet" type="text/css"/>
        <link href="styles/menu1.css" rel="stylesheet" type="text/css"/>
        <style type="text/css">
            .tg  {border-collapse:collapse;border-spacing:0; border: 3px;}
            .tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}
            .tg th{font-weight: bold; font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}
            .tg .tg-s6z2{text-align:center}           
            div.fixed {
                position: fixed;
                //bottom: inherit;
                left: inherit;
                padding: 3px;
                width: 15%;
                background-color: white;
                border-radius: 5px;
                border: 2px solid #3cb0fd!important;
            }
            div.fixed a {
                //background-color: lightblue;
                text-decoration: none;
                font-size: 16px;                
            }
            div.fixed a:hover {
                //background-color: lightblue;
                font-weight: bold;
                //text-decoration: underline;                
            }
            div.fixed li:hover {
                background-color: #DCE3DF;
            }
            div.fixed li {list-style-type: none;}
            div.fixed li:before {
                content: "\A4\ "; /* caractère UTF-8 */
            }
            div.main {
                padding-left: 18%;
            }
            div.main thead tr{
                color: #ffffff;               
                background-color: #2671A2;
            }
        </style>
    </head>
    <body>        
        <div id="wrapper">
            <jsp:include page="header.html"></jsp:include>
                <section>
                    <div class="fixed">
                        <a href="#species"><li>Species</li></a><br>
                        <a href="#ontologies"><li>Ontologies</li></a><br>
                        <a href="#sources"><li>Data sources</li></a><br>
                        <a href="#break-down"><li>Species break-down</li></a><br>
                        <a href="#graphs"><li>Graphs names</li></a><br>
                        <a href="#uri"><li>URIs</li></a>
                    </div>
                    <div class="main">
                        <h3 id="top">Documentation</h3>
                        <p>This page provides a summary on the species, data sources and URI patterns.</p>
                        <h4 id="species">AgroLD includes data on the following species on: </h4>
                        <ul>
                            <li>Arabidopsis thaliana (NCBI Taxon ID: 3702)</li>
                            <li>Oryza species:
                                <ul>
                                    <li>Oryza barthii (NCBI Taxon ID: 65489)</li>
                                    <li>Oryza brachyantha (NCBI Taxon ID: 4533)</li>
                                    <li>Oryza glaberrima (NCBI Taxon ID: 4538)</li>
                                    <li>Oryza meridionalis (NCBI Taxon ID: 40149)</li>
                                    <li>Oryza sativa (NCBI Taxon ID: 4530)
                                        <ul>
                                            <li>Oryza sativa indica (NCBI Taxon ID: 39946)</li>
                                            <li>Oryza sativa japonica (NCBI Taxon ID: 39947)</li>
                                        </ul>
                                    </li>
                                </ul>
                            </li>
                            <li>Sorghum bicolor (NCBI Taxon ID: 4558)</li>
                            <li>Triticum species:
                                <ul>
                                    <li>Triticum aestivum (NCBI Taxon ID: 4565)</li>
                                    <li>Triticum urartu (NCBI Taxon ID: 4572)</li>                                
                                </ul>
                            </li>
                            <li>Zea mays (NCBI Taxon ID: 4577)</li>
                        </ul>
                        <h4 id="ontologies">Ontologies in AgroLD:</h4>
                        <p>The OWL versions of the ontologies have been loaded to AgroLD. The original namespaces and URIs 
                            have been retained.</p>
                        <table border="1" class="tg">
                            <thead>
                                <tr>
                                    <th>Ontology</th>
                                    <th>Website</th>
                                    <th>Example(s)</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>Gene Ontology (GO)</td>
                                    <td><a href="http://geneontology.org/" target="_blank">http://geneontology.org/</a></td>
                                    <td><a href="http://purl.obolibrary.org/obo/GO_0008150" target="_blank">http://purl.obolibrary.org/obo/GO_0008150</a></td>
                                </tr>
                                <tr>
                                    <td>Plant Ontology (PO)</td>
                                    <td rowspan="3"><a href="http://planteome.org/" target="_blank">http://planteome.org/</a></td>
                                    <td><a href="http://purl.obolibrary.org/obo/PO_0025131" target="_blank">http://purl.obolibrary.org/obo/PO_0025131</a></td>
                                </tr>
                                <tr>
                                    <td>Plant Trait Ontology (TO)</td>
                                    <td><a href="http://purl.obolibrary.org/obo/TO_0000387" target="_blank">http://purl.obolibrary.org/obo/TO_0000387</a></td>
                                </tr>
                                <tr>
                                    <td>Plant Environment Ontology (EO)</td>
                                    <td><a href="http://purl.obolibrary.org/obo/EO_0007359" target="_blank">http://purl.obolibrary.org/obo/EO_0007359</a></td>
                                </tr>
                                <tr>
                                    <td>Sequence Ontology (SO)</td>
                                    <td rowspan="4"><a href="http://www.berkeleybop.org/ontologies/" target="_blank">http://www.berkeleybop.org/ontologies/</a></td>
                                    <td><a href="http://purl.obolibrary.org/obo/SO_0000104" target="_blank">http://purl.obolibrary.org/obo/SO_0000104</a></td>
                                </tr>
                                <tr>
                                    <td>Phenotype and Attribute Ontology (PATO)</td>
                                    <td><a href="http://purl.obolibrary.org/obo/PATO_0000462" target="_blank">http://purl.obolibrary.org/obo/PATO_0000462</a></td>
                                </tr>
                                <tr>
                                    <td>NCBI Taxonomy</td>
                                    <td><a href="http://purl.obolibrary.org/obo/NCBITaxon_4565" target="_blank">http://purl.obolibrary.org/obo/NCBITaxon_4565</a></td>
                                </tr>
                                <tr>
                                    <td>Evidence code Ontology</td>
                                    <td><a href="http://purl.obolibrary.org/obo/ECO_0000033" target="_blank">http://purl.obolibrary.org/obo/ECO_0000033</a></td>
                                </tr>
                            </tbody>
                        </table>
                        <h4 id="sources">Data sources in AgroLD:</h4>
                        <table border="1" class="tg">
                            <thead>
                                <tr>
                                    <th>Data source</th>
                                    <th>Information</th>
                                    <th>Website</th>
                                    <th>Note</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>Gramene</td>
                                    <td>Gene, QTL, Pathways and ontology associations (PO, TO and EO)</td>
                                    <td><a href="http://www.gramene.org"  target="_blank">www.gramene.org</a></td>
                                    <td>In the future versions, ontology associations will be taken from the Planteome project (<a href="http://planteome.org/" target="_blank">http://planteome.org/</a>)</td>
                                </tr>
                                <tr>
                                    <td>GOA</td>
                                    <td>Gene Ontology associations</td>
                                    <td><a href="http://www.ebi.ac.uk/GOA" target="_blank">www.ebi.ac.uk/GOA</a></td>
                                    <td></td>
                                </tr<>
                                <tr>
                                    <td>UniprotKB</td>
                                    <td>Protein Information</td>
                                    <td><a href="http://www.uniprot.org" target="_blank">www.uniprot.org</a></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>GreenPhylDB</td>
                                    <td>Web resource for phylogenetic-based approach to predict homologous relationships.</td>
                                    <td><a href="http://www.greenphyl.org" target="_blank">www.greenphyl.org</a></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>Oryza Tag Line</td>
                                    <td>Database consists of phenotypic data resulting from the evaluation of the Génoplante rice insertion line library.</td>
                                    <td><a href="http://oryzatagline.cirad.fr" target="_blank">oryzatagline.cirad.fr</a></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>OryGenesDB</td>
                                    <td>Database for sequence information resulting from the T-DNA and Ds flanking sequence tags FSTs. Also contains information on cDNA full length, EST and Markers</td>
                                    <td><a href="http://orygenesdb.cirad.fr" target="_blank">orygenesdb.cirad.fr</a></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>TropGeneDB</td>
                                    <td>Database for genomic, genetic and phenotypic information about tropical crops.</td>
                                    <td><a href="http://tropgenedb.cirad.fr" target="_blank">tropgenedb.cirad.fr</a></td>
                                    <td></td>
                                </tr>
                            </tbody>
                        </table>
                        <h4 id="break-down">Species specific break down of the data sources:</h4>

                        <table class="tg">
                            <thead>
                                <tr>
                                    <th colspan="2" rowspan="2">Data<br>Sources</th>
                                    <th colspan="5">Crop species</th>
                                </tr>
                                <tr>
                                    <td>Oryza species</td>
                                    <td >Triticum species</td>
                                    <td >A.thaliana</td>
                                    <td >S.bicolor</td>
                                    <td >Z.mays</td>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td rowspan="4">Ontology associations</td>
                                    <td>GOA</td>
                                    <td >&#10004;</td>
                                    <td >&#10004;</td>
                                    <td >&#10004;</td>
                                    <td >&#10004;</td>
                                    <td >&#10004;</td>
                                </tr>
                                <tr>
                                    <td>Gramene-PO</td>
                                    <td >&#10004;</td>
                                    <td >&#10004;</td>
                                    <td >&#10004;</td>
                                    <td >&#10004;</td>
                                    <td >&#10004;</td>
                                </tr>
                                <tr>
                                    <td>Gramene-TO</td>
                                    <td >&#10004;</td>
                                    <td ></td>
                                    <td >&#10004;</td>
                                    <td ></td>
                                    <td ></td>
                                </tr>
                                <tr>
                                    <td>Gramene-EO</td>
                                    <td >&#10004;</td>
                                    <td ></td>
                                    <td ></td>
                                    <td ></td>
                                    <td ></td>
                                </tr>
                                <tr>
                                    <td colspan="2">Gramene-gene</td>
                                    <td >&#10004;</td>
                                    <td >&#10004;</td>
                                    <td >&#10004;</td>
                                    <td >&#10004;</td>
                                    <td >&#10004;</td>
                                </tr>
                                <tr>
                                    <td colspan="2">Gramene-QTL</td>
                                    <td >&#10004;</td>
                                    <td ></td>
                                    <td ></td>
                                    <td ></td>
                                    <td ></td>
                                </tr>
                                <tr>
                                    <td colspan="2">Gramene-Cyc,pathways</td>
                                    <td >&#10004;</td>
                                    <td ></td>
                                    <td >&#10004;</td>
                                    <td >&#10004;</td>
                                    <td >&#10004;</td>
                                </tr>
                                </tr>
                                <tr>
                                    <td colspan="2">UniprotKB</td>
                                    <td >&#10004;</td>
                                    <td >&#10004;</td>
                                    <td >&#10004;</td>
                                    <td >&#10004;</td>
                                    <td >&#10004;</td>
                                </tr>
                                </tr>
                                <tr>
                                    <td colspan="2">OryGenesDB</td>
                                    <td >&#10004;</td>
                                    <td ></td>
                                    <td >&#10004;</td>
                                    <td ></td>
                                    <td ></td>
                                </tr>
                                <tr>
                                    <td colspan="2">Oryza Tag Line</td>
                                    <td >&#10004;</td>
                                    <td ></td>
                                    <td ></td>
                                    <td ></td>
                                    <td ></td>
                                </tr>
                                <tr>
                                    <td colspan="2">TropGeneDB</td>
                                    <td >&#10004;</td>
                                    <td ></td>
                                    <td ></td>
                                    <td ></td>
                                    <td ></td>
                                </tr>
                                <tr>
                                    <td colspan="2">GreenPhylDB</td>
                                    <td >&#10004;</td>
                                    <td ></td>
                                    <td >&#10004;</td>
                                    <td >&#10004;</td>
                                    <td >&#10004;</td>
                                </tr>
                            </tbody>
                        </table>
                        <h4 id="graphs">Graph Names:</h4>
                        <p>The RDF graphs have a common name-space: <a href="http://www.southgreen.fr/agrold/" target="_blank">http://www.southgreen.fr/agrold/</a> </p>
                        <p>The list of graphs are follows:</p>
                        <ul>
                            <li>http://www.southgreen.fr/agrold/eco</li>
                            <li>http://www.southgreen.fr/agrold/eo</li>
                            <li>http://www.southgreen.fr/agrold/go</li>
                            <li>http://www.southgreen.fr/agrold/gramene.cyc</li>
                            <li>http://www.southgreen.fr/agrold/gramene.genes</li>
                            <li>http://www.southgreen.fr/agrold/gramene.qtl</li>
                            <li>http://www.southgreen.fr/agrold/greenphyldb</li>
                            <li>http://www.southgreen.fr/agrold/ncbitaxon</li>
                            <li>http://www.southgreen.fr/agrold/orygenesdb.a.thaliana</li>
                            <li>http://www.southgreen.fr/agrold/orygenesdb.o.s.indica</li>
                            <li>http://www.southgreen.fr/agrold/orygenesdb.o.s.japonica</li>
                            <li>http://www.southgreen.fr/agrold/otl</li>
                            <li>http://www.southgreen.fr/agrold/pato</li>
                            <li>http://www.southgreen.fr/agrold/po</li>
                            <li>http://www.southgreen.fr/agrold/protein.annotations</li>
                            <li>http://www.southgreen.fr/agrold/qtl.annotations</li>
                            <li>http://www.southgreen.fr/agrold/so</li>
                            <li>http://www.southgreen.fr/agrold/to</li>
                            <li>http://www.southgreen.fr/agrold/tropgenedb</li>
                            <li>http://www.southgreen.fr/agrold/uniprot.plants</li>
                        </ul>
                        <h4 id="uri">URIs:</h4>
                        <p>To make AgroLD Linked Data compliant, de-referenceable stable URIs provided by Identifiers.org 
                            (<a href="http://identifiers.org/" target="_blank">http://identifiers.org/</a>), Ontobee (<a href="http://www.ontobee.org" target="_blank">www.ontobee.org</a>) 
                            and canonical stable URIs provided UniprotKB are used. </p>

                        <p>Datasets that are not included in these registries, new URIs were minted the identifiers for these datasets take the 
                            form:
                        <pre>http://www.southgreen.fr/agrold/<b>[resource_namespace]</b>/<b>[identifier]</b></pre>
                        <b>Example:</b><br>
                        <i>http://www.southgreen.fr/agrold/<b>biocyc.pathway</b>/<b>CALVIN-PWY</b></i>
                        </p>
                        <p>
                            Similarly, properties, would be of the form: 
                        <pre>http://www.southgreen.fr/agrold/<b>[vocabulary]</b>/<b>[property]</b></pre>
                        <b>Example:</b><br>
                        <i>http://www.southgreen.fr/agrold/<b>vocabulary</b>/<b>expressed_in</b></i>
                        </p>
                    </div>
                </section>
            <jsp:include page="footer.html"></jsp:include>
        </div>
    </body>