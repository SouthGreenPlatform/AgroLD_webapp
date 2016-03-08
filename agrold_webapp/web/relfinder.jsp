<%-- 
    Document   : relfinder
    Created on : 22 avr. 2015, 21:27:06
    Author     : tagny
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>AgroLD:Find Relationships</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="styles/style1.css" rel="stylesheet" type="text/css"/>
        <link href="styles/menu1.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>        
        <div id="wrapper">
            <jsp:include page="header.html"></jsp:include>
                <section>
                    <div class="info_title">Search > Explore</div>
                    <OBJECT CLASSID="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="100%" 
                            CODEBASE="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,040,0" >
                        <PARAM name="movie" VALUE="RelFinder.swf">
                        <PARAM name="quality" VALUE="high">
                        <PARAM name="bgcolor" VALUE="#FFFFFF">            
                        <EMBED SRC="relfinder/RelFinder.swf" quality="high" bgcolor="#FFFFFF" type="application/x-shockwave-flash" 
                               PLUGINSPAGE="http://www.macromedia.com/go/getflashplayer" width="100%" height="700px" >
                        </EMBED>            
                    </OBJECT>    
                </section>
            <jsp:include page="footer.html"></jsp:include>
        </div>
    </body>
</html>
