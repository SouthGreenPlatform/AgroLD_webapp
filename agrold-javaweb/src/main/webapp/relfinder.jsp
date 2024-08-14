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
        <title>AgroLD: Find Relationships</title>
        <!-- Les includes -->
        <jsp:include page="includes.jsp"></jsp:include>
        <style>
            iframe {
                height: 100%;
                width: 100%;
                position: absolute;
                left: 0;
                top: 0;
                border-style: none;
            }
        </style>
    </head>
    <body>        
        <jsp:include page="header.jsp"></jsp:include>
            <%! String rfLink = System.getProperty("agrold.rf_link", "http://rf.southgreen.fr/"); %>
            <div class="container-fluid arian-thread">
                <div class="info_title">
                    <div class="container pos-l">Search > <span class="active-p">Explore relationship</span></div>
                </div>
            </div>
            
            <div class="foowrap m-4 d-flex flex-column">
                <div class="d-flex flex-row justify-content-start" >
                    <h3>Quick guide to RelFinder Reformed</h3>
                    <h3 class="mr-3 ml-3">|</h3>
                    <button class="btn btn-primary" onclick="window.open('<%= rfLink %>','_blank')">
                        Open RelFinder Reformed
                    </button>
                </div>

                <hr/>

                <div class="d-flex align-items-center flex-xl-row flex-column">
                    <div>
                        <h4>What is it?</h4>
                        <p>
                            Relfinder reformed is a reimplementation of the now deprecated <a href="https://github.com/VisualDataWeb/RelFinder">RelFinder</a>.
                            It is a tool for the interactive exploration and visualization of relationships between entities in RDF data. This tool will help 
                            you visualize the relationships between entities in the AgroLD knowledge base.
                        </p>

                        <h4>Interface</h4>
                        <ol>
                            <li>Text inputs where you type in targetted entities, you can either type their URI or their label</li>
                            <li>Buttons to respectively add a target entity and refresh text inputs</li>
                            <li>Switch button that hides Litterals, you can see attributes of an entity by clicking on it</li>
                            <li>Slider to set how long can the paths be</li>
                            <li>An entity, you can hover on it to see its neighbors and click on it to see details</li>
                            <li>Entities that are triangle-shaped are targeted entities, the one you typed in</li>
                            <li>
                                Control buttons that allows you to (from left to right):
                                <ul>
                                    <li>Search for an entity in the graph</li>
                                    <li>Take a screenshot</li>
                                    <li>Reset the graph</li>
                                    <li>Zoom (can also be done with your scrollwheel)</li>
                                    <li>Dezoom</li>
                                </ul>

                            </li>
                        </ol>

                        <h4>Links</h4>
                        <ul>
                            <li><a href="https://github.com/WoodenMaiden/RelfinderReformedFront">Frontend Code</a></li>
                            <li><a href="https://github.com/WoodenMaiden/RelfinderReformedAPI"  >Backend Code</a></li>
                        </ul>
                    </div>

                    <img class="m-2" width="100%" src="images/rfr_annotated.png" alt="Annotated screenshot of RelFinderReformed"/>
                </div>
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
