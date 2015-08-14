<%-- 
    Document   : quicksearch
    Created on : Jul 15, 2015, 2:47:08 PM
    Author     : tagny
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>AgroLD:Faceted quick search - by keywords</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="styles/style1.css" rel="stylesheet" type="text/css"/>
        <link href="styles/menu1.css" rel="stylesheet" type="text/css"/>
        <style>
            #sform{
                position:relative;
                top:30%;
                left:25%;
                width:75%;                
            }
            #sform input[type="text"]{
                width: 60%;                
            }
            #sform input{
                font-size: 110%;                
            }
        </style>
    </head>
    <body>
        <jsp:include page="header.html"></jsp:include>
            <section>
                <h3>Search > Quick Search</h3>
                <div style="text-align: center">
                    <h4><b>Search and browse AgroLD</b></h4>
                    <p>Search examples: ontological concepts - ‘plant height’ or ‘regulation of gene expression’; gene names –
                        ‘GRP2’ or ‘TCP12’.</p><br/>
                </div>
                <div>
                    <form id="sform" action="http://volvestre.cirad.fr:8890/fct/facet.vsp?cmd=text&sid=86" method="post">                   
                        Enter the entire/part of the name/code of a gene, QTL, protein, ... <br/><br/>
                        Search Text <input name="q" type="text" placeholder="Type here..."/> <input type="submit" value="Search!"/>
                    </form>
                </div>
            </section><br>
        <jsp:include page="footer.html"></jsp:include>
    </body>
</html>
