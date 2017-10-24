<%-- 
        Document   : relfinder
        Created on : 22 avr. 2015, 21:27:06
        Author     : tagny
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <!-- Script for google analytic -->
    <script>
        (function (i, s, o, g, r, a, m) {
            i['GoogleAnalyticsObject'] = r;
            i[r] = i[r] || function () {
                (i[r].q = i[r].q || []).push(arguments)
            }, i[r].l = 1 * new Date();
            a = s.createElement(o),
                    m = s.getElementsByTagName(o)[0];
            a.async = 1;
            a.src = g;
            m.parentNode.insertBefore(a, m)
        })(window, document, 'script', 'https://www.google-analytics.com/analytics.js', 'ga');

        ga('create', 'UA-88660031-1', 'auto');
        ga('send', 'pageview');

    </script>
    <head>
        <title>AgroLD:Find Relationships</title>
        <!-- Les includes -->
        <jsp:include page="includes.html"></jsp:include>
        </head>
        <body>        
        <jsp:include page="header.jsp"></jsp:include>
            <div class="container-fluid arian-thread">
                <div class="info_title">
                    <div class="container pos-l">Search > <span class="active-p">Explore relationship</span></div>
                </div>
            </div>
            <div class="foowrap">
                <section>
                    <OBJECT CLASSID="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="100%" height="100%" 
                            CODEBASE="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,040,0" >
                        <PARAM name="movie" VALUE="RelFinder.swf">
                        <PARAM name="quality" VALUE="high">
                        <PARAM name="bgcolor" VALUE="#FFFFFF">            
                        <EMBED SRC="relfinder/RelFinder.swf" quality="high" bgcolor="#FFFFFF" type="application/x-shockwave-flash" 
                               PLUGINSPAGE="http://www.macromedia.com/go/getflashplayer" width="100%" height="700px" >
                        </EMBED>            
                    </OBJECT>    
                </section>
            </div>
        <jsp:include page="footer.html"></jsp:include>
    </body>
    <script>
        $(document).ready(function () {
            $.ajax({
                type: 'post',
                data: 'p={m:"setPageConsult",page:"relfinder"}',
                url: 'ToolHistory',
                success: function (data) {
                    $('.success').html(data);
                }
            });
        });
    </script>
</html>
