<%-- 
    Document   : formsearch
    Created on : Jul 15, 2015, 3:40:50 PM
    Author     : tagny
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>AgroLD: Form-based search</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="styles/style1.css" rel="stylesheet" type="text/css"/>
        <link href="styles/menu1.css" rel="stylesheet" type="text/css"/> 
        <script src="swagger/lib/swagger-client.js" type="text/javascript"></script>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <link href="styles/messages.css" rel="stylesheet" type="text/css"/>
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>         
        <style>
            #container .advanced-form{
                width: 50%;
            }
            .advanced-form #sform{
                position:absolute;
                top:10%;
                left:15px;
                width:70%;                  
            }
            #sform input, #sform select{
                font-size: 120%;
                margin-bottom: 5pt;
            }      
            #sform select{
                padding: 5pt;                
                width: 15%;
            }
            #sform input[type="text"]{
                width: 60%;
                padding: 5pt;
                margin-left: 5pt;
            }
            table, td, th {
                border: 1px solid green;
            }

            th {
                background-color: green;//#10a54a;
                color: white;
            }
            table {
                width: 100%;
                padding: 4px;
            }
        </style>       
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="header.html"></jsp:include>
                <section>
                    <h3>Search > Advanced form-based search</h3>
                    <div id="advanced-form" class="border-right">
                        <center>
                            <form id="sform">
                                <select id="elements">
                                    <!--option value="--">--- Select an element ---</option-->
                                    <option value="gene" selected>Gene</option>
                                    <option value="protein">Protein</option>
                                    <option value="qtl">QTL</option>
                                    <option value="pathway">Pathway</option>
                                    <option value="ontology">Ontology</option>
                                </select>
                                <input id="input" type="text" autofocus style="display: inline"/>
                                <input id="submit" class="btn" type="submit" value="Search" style="display: inline"/>
                            </form>
                        </center>
                    </div>
                    <div id="result">

                    </div>                     
                </section><br>
            <jsp:include page="footer.html"></jsp:include>
        </div>
        <script src="scripts/formsearch.js" type="text/javascript"></script>
    </body>
</html>
