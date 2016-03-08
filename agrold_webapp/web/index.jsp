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
        <style>
            .part1 {
                width:50%;
                height: 50%;
                float: left;
                background: purple;
            }
            .part2 {
                width:50%;
                height: 50%;
                float: left;
                background: red;
            }
            .part3 {
                width:50%;
                height: 50%;
                float: left;
                background: green;
            }
            .part4 {
                width:50%;
                height: 50%;
                float: left;
                background: silver;
            }

            .content{
                width: 100%;
                position: relative;
            }
            .feature{
                border: 1px solid transparent;                
                border-color: #ccc;
                border-width: 3px;                
                border-radius: 6px;
                padding: 6px 12px;
                width: 300px;
                vertical-align: top;
            }
            .feature h4{
                text-align: center;
            }
            .feature p{
                text-align: center;
            }
        </style>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="header.html"></jsp:include>
                <section>            
                    <h2><b>The Agronomic Linked Data (AgroLD) Project</b></h2>                   
                    <p>
                        At the Institute of Computational Biology (IBC), we are involved in developing methods 
                        to aid data integration and knowledge management within the plant biology domain to 
                        improve information accessibility of heterogeneous data. Among others, a solution for 
                        the data integration challenges is offered by the Semantic Web technologies. The 
                        semantic web has emerged as one of the most promising solutions for high scale 
                        integration of distributed resources. This is made possible by a stack of technologies 
                        such as the Resource Description Framework (RDF), RDF Schema (RDFS), Web Ontology 
                        Language (OWL) and the SPARQL Query Language (SPARQL) proposed by the World Wide Web 
                        Consortium (W3C). RDF forms the basis of the stack allows modeling information as a 
                        directed graph composed of triples that can be queried using SPARQL.  
                    </p>
                    <p>
                        AgroLD is a RDF knowledge base that consists of data integrated from a variety of plant 
                        resources and ontologies.  The aim of the Agronomic Linked Data (AgroLD) project is to 
                        provide a portal for bioinformatics and domain experts to exploit the homogenized data 
                        models towards efficiently generating research hypotheses. 
                    </p>
                    <center>
                        <table border="0" cellspacing="20">
                            <!--thead>
                                <tr><th>Searching features</th></tr>
                            </thead-->
                            <tbody>
                                <tr>
                                    <td class="feature">
                                        <div>
                                            <h4><a href="quicksearch.jsp">Quick Search</a></h4>
                                            <p>Search with keywords and browse AgroLD Knowledge Base</p>                                        
                                        </div>
                                    </td>                                
                                    <td class="feature">
                                        <div>
                                            <h4><a href="advancedSearch.jsp">Advanced Search</a></h4>
                                            <p>Search with keywords, browse, and get answers to some biological questions</p>                                        
                                        </div>
                                    </td>                                
                                    <td class="feature">
                                        <div>
                                            <h4><a href="relfinder.jsp">Explore Relationships</a></h4>
                                            <p>Search easily existing relationships between entities</p>                                        
                                        </div>
                                    </td>                                
                                    <td class="feature">
                                        <div>
                                            <h4><a href="sparqleditor.jsp">SPARQL Query Editor</a></h4>
                                            <p>Edit and submit your SPARQL Queries to the sparql endpoint of AgroLD located 
                                                at <a href="http://volvestre.cirad.fr:8890/sparql" target="_blank">http://volvestre.cirad.fr:8890/sparql</a></p>                                        
                                        </div>
                                    </td>                                
                                </tr>
                            </tbody>
                        </table>
                    </center>
                </section>
            <jsp:include page="footer.html"></jsp:include>
        </div>
    </body>
</html>
