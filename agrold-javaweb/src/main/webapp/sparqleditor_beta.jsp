<%-- 
        Document   : sparqleditor
        Created on : Jul 15, 2015, 3:18:01 PM
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
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>AgroLD: SPARQL Query Editor</title>
        <jsp:include page="includes.html"/>
        <!-- YasQUE pour la coloration syntaxique -->
        <link href='//cdn.jsdelivr.net/npm/yasgui-yasqe@2.11.22/dist/yasqe.min.css' rel='stylesheet' type='text/css'/>
        <script src='//cdn.jsdelivr.net/npm/yasgui-yasqe@2.11.22/dist/yasqe.bundled.min.js'></script>
        <link href='//cdn.jsdelivr.net/yasr/2.10.8/yasr.min.css' rel='stylesheet' type='text/css'/>
        <script src='//cdn.jsdelivr.net/yasr/2.10.8/yasr.bundled.min.js'></script>
        <link href="intro.js-1.0.0/introjs.css" rel="stylesheet" type="text/css"/>
        <script src="intro.js-1.0.0/intro.js" type="text/javascript"></script>
        <script type="text/javascript">

        /*<![CDATA[*/
        var lastformat = 0;
        function format_select(query_obg)
        {
            var query = query_obg.value;
            var format = query_obg.form.format;
            if ((query.match(/\bconstruct\b/i) || query.match(/\bdescribe\b/i)) && lastformat != 2) {
                for (var i = format.options.length; i > 0; i--)
                    format.options[i] = null;
                format.options[1] = new Option('Turtle', 'text/turtle');
                format.options[2] = new Option('Pretty-printed Turtle (slow!)', 'application/x-nice-turtle');
                format.options[3] = new Option('RDF/JSON', 'application/rdf+json');
                format.options[4] = new Option('RDF/XML', 'application/rdf+xml');
                format.options[5] = new Option('N-Triples', 'text/plain');
                format.options[6] = new Option('XHTML+RDFa', 'application/xhtml+xml');
                format.options[7] = new Option('ATOM+XML', 'application/atom+xml');
                format.options[8] = new Option('ODATA/JSON', 'application/odata+json');
                format.options[9] = new Option('JSON-LD', 'application/x-json+ld');
                format.options[10] = new Option('HTML (list)', 'text/x-html+ul');
                format.options[11] = new Option('HTML (table)', 'text/x-html+tr');
                format.options[12] = new Option('HTML+Microdata (inconvenient)', 'text/html');
                format.options[13] = new Option('HTML+Microdata (pretty-printed table)', 'application/x-nice-microdata');
                format.options[14] = new Option('Turtle-style HTML (for browsing, not for export)', 'text/x-html-nice-turtle');
                format.options[15] = new Option('Microdata/JSON', 'application/microdata+json');
                format.options[16] = new Option('CSV', 'text/csv');
                format.options[17] = new Option('TSV', 'text/tab-separated-values');
                format.options[18] = new Option('TriG', 'application/x-trig');
                format.selectedIndex = 1;
                lastformat = 2;
            }
            if (!(query.match(/\bconstruct\b/i) || query.match(/\bdescribe\b/i)) && lastformat != 1) {
                for (var i = format.options.length; i > 0; i--)
                    format.options[i] = null;
                format.options[1] = new Option('HTML', 'text/html');
                format.options[2] = new Option('Spreadsheet', 'application/vnd.ms-excel');
                format.options[3] = new Option('XML', 'application/sparql-results+xml');
                format.options[4] = new Option('JSON', 'application/sparql-results+json');
                format.options[5] = new Option('Javascript', 'application/javascript');
                format.options[6] = new Option('Turtle', 'text/turtle');
                format.options[7] = new Option('RDF/XML', 'application/rdf+xml');
                format.options[8] = new Option('N-Triples', 'text/plain');
                format.options[9] = new Option('CSV', 'text/csv');
                format.options[10] = new Option('TSV', 'text/tab-separated-values');
                format.selectedIndex = 1;
                lastformat = 1;
            }
        }
        function format_change(e)
        {
            var format = e.value;
            var cxml = document.getElementById("cxml");
            if (!cxml)
                return;
            if ((format.match(/\bCXML\b/i)))
            {
                cxml.style.display = "block";
            } else {
                cxml.style.display = "none";
            }
        }
        function savedav_change(e)
        {
            var savefs = document.getElementById("savefs");
            if (!savefs)
                return;
            if (e.checked)
            {
                savefs.style.display = "block";
            } else
            {
                savefs.style.display = "none";
            }
        }
        function sparql_endpoint_init()
        {
            var cxml = document.getElementById("cxml");
            if (cxml)
                cxml.style.display = "none";
            var savefs = document.getElementById("savefs");
            if (savefs)
                savefs.style.display = "none";
        }
        /*]]>*/
        </script>
        <script src="scripts/querypatterns.js"></script>        
        <style>         
            #patternslist{
                /*overflow-y: scroll;
                word-wrap: break-word;
                border-radius: 5px;
                padding: 5px;
                border: 2px solid #00B5AD!important;*/
            }
            h4{
                font-weight: bolder;
            }
            form{
                /*ackground-color: #f4f4f4;*/
                font-size: 95%;
                width: 98%;
                padding: 5px
            }
            input.aparameter{
                color: #085; 
                /*ont-family:monospace;*/
                width: 50%;
            }
            #cmd-container{
                background:#3cb0fd!important;
                color: white;
                top: 0;//right:0; 
                position: absolute;
                //min-width: 100%;
                //width: 22%;
                padding: 5px 5px 5px 5px;
                z-index: 10;
                font-size: .875rem;
                border-radius: 5px;
                border: 2px solid #3cb0fd!important;
            }  
        </style>
        <link href="sparqleditor/style.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <jsp:include page="header.jsp"></jsp:include>
            <div class="container-fluid arian-thread">
                <div class="info_title">
                    <div class="container pos-l">Search > <span class="active-p">SPARQL Query Editor</span></div>
                </div>
            </div>
            <div id="main" class="foowrap panel-container-vertical">               
                <div class="panel-top panel-container">
                    <div id="sparql" class="panel-left">                        
                        <div id="parameters">
                        </div>
                        <textarea name="query" id="query" onchange="format_select(this)" onkeyup="format_select(this)">
                        <%
                            if (request.getParameter("query") == null) {
                        %>
PREFIX agrold:<http://www.southgreen.fr/agrold/>
SELECT * 
WHERE{
    GRAPH ?graph {
        ?subject ?property ?object.
    }
    filter(REGEX(?graph, CONCAT("^", str(agrold:))))
} 
LIMIT 10
                            <%
                                } else {
                                    out.println(request.getParameter("query"));
                                }
                            %>
                </textarea>
            </div>
            <div class="splitter">
            </div>
            <div class="panel-right"  id="patternslist"  data-step="1" data-intro="Select a <b>question</b> here and then ...">
                <b style="font-size: 15px">Query Patterns</b>
            </div>
        </div>
        <div class="splitter-horizontal">
        </div>
        <div class="panel-bottom" id="yasr" data-step="4" data-intro="watch your results ... ">

            <!--add the push div here -->
            <!--div id="push"></div--> 
        </div>
    </div>
    <!--div class="jump-bot"></div-->
    <jsp:include page="footer.html"></jsp:include>
    <script>
        // label of the choose file button
        /*document.getElementById("fileToLoad").onchange = function () {
         document.getElementById("uploadFile").value = this.value;
         };*/
        // Display the list of patterns            
        var divpatt = document.getElementById("patternslist");
        var ol = document.createElement("ol");
        //divpatt.innerHTML += "<ol>";
        for (i = 0; i < patternlabels.length; i++) {
            var li = document.createElement("li");
            li.innerHTML = "<p>" + patternlabels[i] + " (<a href=\"#\" onclick=\"selectPattern(" + i + ")\">select</a>)" + "</p>";
            ol.appendChild(li);
            //divpatt.innerHTML += //qpatterns[selectedpattern][i] + ': <input class="aparameter" value="' + qpatterns[selectedpattern][i] + '" oninput="replaceParaValue(' + "/" + qpatterns[selectedpattern][i] + "/g" + ', this)"/><br><br>';
        }
        divpatt.appendChild(ol);
    </script>              
    <script type="text/javascript">
        /*$.fn.scrollView = function () { // https://web-design-weekly.com/snippets/scroll-to-position-with-jquery/
         return this.each(function () {
         $('html, body').animate({
         scrollTop: $(this).offset().top
         }, 1000);
         });
         }*/        
        //var yasqe = YASQE(document.getElementById("query"),
        var yasqe = YASQE.fromTextArea(document.getElementById('query'),
                {
                    sparql: {
                        showQueryButton: true,
                        endpoint: SPARQLENDPOINTURL,
                        collapsePrefixesOnLoad: false,
                        //persistent: true,
                        args: [{name: 'timeout', value: 20000}], //document.getElementById('timeout').value}],
                        callbacks: {
                            beforeSend: function (data) {
                                yasqe.options.sparql.args[0].value = 20000;//document.getElementById('timeout').value;
                                //console.log(yasqe.options.sparql.args[0].value);
                            },
                            success: function (data) {
                                //console.log("success", data);
                                //$('#yasr').scrollView();
                                document.getElementById("yasr").scrollIntoView();
                                //var pageElement = document.getElementById("yasr");
                                //scrollToElement(pageElement);
                            }, //, headers: {"Access-Control-Allow-Origin": "*", "Access-Control-Allow-Methods": "GET, POST, DELETE, PUT", "Access-Control-Allow-Headers": "X-Requested-With, Content-Type, X-Codingpedia"}
                            error: function (data) {
                                //$('#yasr').scrollView();
                                document.getElementById("yasr").scrollIntoView();
                            }
                        }
                    }
                });
        // add a plugin to draw response as graph with d3sparql
        //// constructeur
        /*
         * 
         * @type @call;YASR
         */
        /*function graphPlugin(){           
            this.draw = function(){return null;};
            this.canHandleResults = true;
            this.getPriority = 2;
            this.name= "Graph";
        }
        YASR.registerOutput("Graph", graphPlugin); 
        /**/
        YASR.plugins.table.defaults.datatable["lengthMenu"] = [[10, 20, 30, 50, 100, 1000, -1], [10, 20, 30, 50, 100, 1000, "All"]];
        var yasr = YASR(document.getElementById("yasr"), {
            //this way, the URLs in the results are prettified using the defined prefixes in the query
            getUsedPrefixes: yasqe.getPrefixesFromQuery,
            useGoogleCharts: false,
            //drawDownloadIcon: false,
            persistency: {
                prefix: false
            }
        });        
        //link both together (YasQUE and YASR)
        yasqe.options.sparql.callbacks.complete = yasr.setResponse;
        $(document).ready(function () {
            // Handler for .ready() called.
            // add introJs attribute
            // data-step="2" data-intro="Edit it here!"
            //$("#cmd-container").insertAfter(".yasqe");
            //$("#cmd-container").insertAfter(".yasqe_buttons");
            $(".yasqe_queryButton").attr('data-step', '3');
            $(".yasqe_queryButton").attr('data-intro', 'Run and then ...');
            // show keyboard commands
            $("#cmd-container").hover(
                    function () {
                        $("ul#cmds").css("display", "");
                        $(this).css("width", "55%");
                    }, function () {
                $("ul#cmds").css("display", "none");
                $(this).css("width", "22%");
            });
            $(".fullscreenToggleBtns").click(
                    function () {
                        /*if($(".CodeMirror").hasClass("CodeMirror-fullscreen")){
                         $("#cmd-container").
                         }*/
                        $("#cmd-container").toggle();
                        //$("#cmd-container").insertAfter(".yasqe_buttons");
                    }
            );
            yasqe.options.extraKeys.F11 = function (yasqe) {
                yasqe.setOption("fullScreen", !yasqe.getOption("fullScreen"));
                $("#cmd-container").toggle();
            };
            yasqe.options.extraKeys.Esc = function (yasqe) {
                if (yasqe.getOption("fullScreen")) {
                    yasqe.setOption("fullScreen", false);
                    $("#cmd-container").toggle();
                }
            };
        });
        //console.log(yasqe.getValue());

        function saveTextAsFile(fileNameToSaveAs)
        {
            var textToWrite = yasqe.getValue();
            var textFileAsBlob = new Blob([textToWrite], {type: 'text/plain'});

            var downloadLink = document.createElement("a");
            downloadLink.download = fileNameToSaveAs;
            downloadLink.innerHTML = "Download File";
            if (window.webkitURL != null)
            {
                // Chrome allows the link to be clicked
                // without actually adding it to the DOM.
                downloadLink.href = window.webkitURL.createObjectURL(textFileAsBlob);
            } else
            {
                // Firefox requires the link to be added to the DOM
                // before it can be clicked.
                downloadLink.href = window.URL.createObjectURL(textFileAsBlob);
                downloadLink.onclick = destroyClickedElement;
                downloadLink.style.display = "none";
                document.body.appendChild(downloadLink);
            }

            downloadLink.click();
        }

        function destroyClickedElement(event)
        {
            document.body.removeChild(event.target);
        }

        function loadFileAsText(fileToLoad)
        {
            //var fileToLoad = document.getElementById("fileToLoad").files[0];

            var fileReader = new FileReader();
            fileReader.onload = function (fileLoadedEvent)
            {
                var textFromFileLoaded = fileLoadedEvent.target.result;
                //textarea = document.getElementById("inputTextToSave");
                yasqe.setValue(textFromFileLoaded);
            };
            fileReader.readAsText(fileToLoad, "UTF-8");
        }
        $('.yasqe_queryButton.query_valid').click(function () {
            var request = yasqe.getValue();
            //if(request!==""){
            var encoded = String(encodeURIComponent(request));
            console.log('ENCODED : ' + encoded);
            var request_ = String('p={m:"setSparqlEditor",request:"' + encoded + '"}')
            console.log('REQUEST : ' + request_);
            saveRequest(encoded);
            //}
        });
        function saveRequest(r) {
            $.ajax({
                type: 'post',
                url: 'ToolHistory',
                data: {
                    p: '{m:"setSparqlEditor"}',
                    request: r
                },
                success: function (data) {
                    $('.success').html(data);
                }, error: function (data) {
                    $('.debugme').html(data);
                }
            });
        }
    </script>
    <div class="debugme"></div>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="sparqleditor/jquery-resizable.min.js"></script>
    <script>
        $(".panel-left").resizable({
            handleSelector: ".splitter",
            resizeHeight: false
        });
        $(".panel-top").resizable({
            handleSelector: ".splitter-horizontal",
            resizeWidth: false
        });
    </script>
</body>
<script>
    $(document).ready(function () {
        $.ajax({
            type: 'post',
            data: 'p={m:"setPageConsult",page:"sparqlEditor"}',
            url: 'ToolHistory',
            success: function (data) {
                $('.success').html(data);
            }
        });
    });
</script>
</html>