<%-- 
    Document   : about
    Created on : Jun 29, 2015, 11:55:56 AM
    Author     : tagny
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title> About AgroLD </title> 
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="styles/style1.css" rel="stylesheet" type="text/css"/>
        <link href="styles/menu1.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <div id="wrapper"> 
            <jsp:include page="header.html"></jsp:include>
                <section>
                    <h3>About:</h3>
                    <p>
                        The Agronomic Linked Data project has been developed by Institut de Biologie Computationnelle (IBC, 

                        <a href="http://www.ibc-montpellier.fr/wp/wp5" target="_blank">http://www.ibc-montpellier.fr/wp/wp5</a>) in collaboration with the Centre de coopération International en 

                        Recherche Agronomique pour le Développement (CIRAD) and Institut de recherche pour le 

                        développement (IRD).
                    </p>
                    <h3>Team:</h3>
                    <div>               
                        <p><b>Aravind Venkatesan,</b><br>
                            <i>Post-doctoral researcher, IBC</i><br>
                            <em>aravind[dot]venkatesan[at]lirmm[dot]fr</em></p>
                    </div>
                    <div>               
                        <p><b>Pierre Larmande,</b><br>
                            <i>Researcher, IBC and IRD</i><br>
                            <em>Pierre[dot]larmande[at]ird[dot]fr</em></p>
                    </div>           
                    <div>               
                        <p><b>Gildas Tagny,</b><br>
                            <i>Project intern, IBC</i><br></p>
                    </div>
                    <div>               
                        <p><b>Imene chentli,</b><br>
                            <i>Project intern, IBC</i><br>                
                    </div>
                    <div>               
                        <p><b>Nordine El Hassouni,</b><br>
                            <i>Engineer, CIRAD</i><br>
                            <em>nordine[dot]el_hassouni[at]cirad[dot]fr</em></p>
                    </div>
                    <div>               
                        <p><b>Manuel Ruiz</b><br>
                            <i>Researcher, CIRAD</i><br>
                            <em>manuel[dot]ruiz[at]cirad[dot]fr</em><br></p>
                    </div>
                    <div>               
                        <p><b>Patrick Valduriez</b><br>
                            <i>Senior Researcher, INRIA</i><br>
                            <i>Head, Data and knowledge management workpackage, IBC</i><br>
                            <em>Patrick[dot]Valduriez[at]inria[dot]fr</em><br></p>
                    </div>
                    <div style="text-align: center">                
                        <img src="images/ibc.png" width="130px" height="100px"><img src="images/cirad.jpg" width="130px" height="100px"><img src="images/ird.jpg" width="130px" height="100px"><img src="images/inria.png" width="190px" height="100px">
                    </div>
                </section><br>
            <jsp:include page="footer.html"></jsp:include>
        </div>
    </body>
</html>
