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
        <link href="intro.js-1.0.0/introjs.css" rel="stylesheet" type="text/css"/>
        <script src="intro.js-1.0.0/intro.js" type="text/javascript"></script>
        <style>
            #sform{
                position:relative;
                top:30%;
                left:20%;
                width:75%;                
            }
            #sform input[type="submit"]{
                       
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
                <div id="sform">
                    <form  action="http://volvestre.cirad.fr:8890/fct/facet.vsp?cmd=text&sid=86" method="post">                   
                        Enter the entire/part of the name/code of a gene, QTL, protein, ... <br/><br/>
                        Search Text <input class="keyword" name="q" type="text" placeholder="Type here..." data-step="1" data-intro="Type your expression and then ..."/> <input class="btn" type="submit" value="Search!" data-step="2" data-intro="launch the search engine!"/>
                    </form>
                </div>
            </section><br>
        <jsp:include page="footer.html"></jsp:include>
    </body>
</html>
