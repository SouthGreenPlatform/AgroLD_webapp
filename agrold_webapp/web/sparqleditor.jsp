<%-- 
    Document   : sparqleditor
    Created on : Jul 15, 2015, 3:18:01 PM
    Author     : tagny
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>AgroLD: SPARQL Query Editor</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="styles/style1.css" rel="stylesheet" type="text/css"/>
        <link href="styles/menu1.css" rel="stylesheet" type="text/css"/>
        <!-- YasQUE pour la coloration syntaxique -->
        <link href="yasqe/dist/yasqe.min.css" rel="stylesheet" type="text/css"/>
        <link href='http://cdn.jsdelivr.net/g/yasqe@2.2(yasqe.min.css),yasr@2.4(yasr.min.css)' rel='stylesheet' type='text/css'/>
        <script src="yasqe/doc/doc.min.js" type="text/javascript"></script>
        <script src="scripts/jquery-1.11.2.min.js" type="text/javascript"></script>
        <script type="text/javascript">
            /*<![CDATA[*/
            var last_format = 0;
            function format_select(query_obg)
            {
                var query = query_obg.value;
                var format = query_obg.form.format;

                if ((query.match(/\bconstruct\b/i) || query.match(/\bdescribe\b/i)) && last_format != 2) {
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
                    last_format = 2;
                }

                if (!(query.match(/\bconstruct\b/i) || query.match(/\bdescribe\b/i)) && last_format != 1) {
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
                    last_format = 1;
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
                }
                else
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
            #sparql{
                width: 56%; float:left;
                border-radius: 5px;
                background: #DDD;
                padding: 5px;
            }
            #patternslist{
                overflow-y: auto;
                width: 40%; float:right;
                height: 455px;
                border-radius: 5px;
                background: #ECDCCE;
                padding: 5px;
            }
            h4{
                font-weight: bolder;
            }
            form{
                background-color: #f4f4f4;
                width: 98%;
                padding: 5px
            }
            input.aparameter{
                color: #085; 
                font-family:monospace;
                width: 60%;
            }
        </style>
    </head>
    <body onload="sparql_endpoint_init();
            //selectPattern(0);
                ">
        <div id="wrapper">
        <jsp:include page="header.html"></jsp:include>
            <section>
                <div id="header">
                    <h3>Search > SPARQL Query Editor</h3> 
                    <p>
                        Select a sample query and run it. The sample query could be used to modify the parameters accordingly. Alternatively, enter SPARQL code in the query box below.
                    </p>
                </div>
                <div id="main" style="overflow:auto;">
                    <div id="sparql">
                        <div id="parameters">
                        </div><br/>
                        <form action="http://volvestre.cirad.fr:8890/sparql" method="get">                                            
                            <fieldset>                                
                                <label for="query">Query Text</label><br />
                                <textarea rows="15" cols="76" name="query" id="query" onchange="format_select(this)" onkeyup="format_select(this)">
<%
    if (request.getParameter("query") == null) {
%>SELECT * 
WHERE {
   ?sub ?pred ?obj .
} LIMIT 10
<%
    } else {
        out.println(request.getParameter("query"));
    }
%>
                                </textarea>
                                <div id="showcase"></div><br />
                                <label for="timeout" class="n">Execution timeout</label>
                                <input name="timeout" id="timeout" type="text" value="20000" onchange="//setTimeout(this)"/> milliseconds
                                <span class="info"><i>(values less than 1000 are ignored)</i></span>		<br />
                                <!--label class="n" for="options">Options</label-->                            
                                <br />
                                <label for="format" class="n">Results Format</label>
                                <select name="format" id="format" onchange="format_change(this)">
                                    <option value="auto" >Auto</option>
                                    <option value="text/html">HTML</option>
                                    <option value="application/vnd.ms-excel" >Spreadsheet</option>
                                    <option value="application/sparql-results+xml" >XML</option>
                                    <option value="application/sparql-results+json" >JSON</option>
                                    <option value="application/javascript" >Javascript</option>
                                    <option value="text/turtle" >Turtle</option>
                                    <option value="application/rdf+xml" selected="selected">RDF/XML</option>
                                    <option value="text/plain" >N-Triples</option>
                                    <option value="text/csv" >CSV</option>
                                    <option value="text/tab-separated-values" >TSV</option>
                                </select>                                    
                                <input type="submit" value="Download Results"/>
                                <input type="button" value="Reset" onclick="resetForm()"  style="float: right;margin-right: 10px; "/> &nbsp;&nbsp;
                            </fieldset>
                        </form> 
                    </div>
                    <div id="patternslist">
                        <h4>Query Patterns</h4>
                    </div>                 
                </div>               
                <div id="yasr">
                    <h4>Results</h4>
                </div>            
        </section><br>
    <jsp:include page="footer.html"></jsp:include>
    </div>
    <script>
            var divpatt = document.getElementById("patternslist");
            var ol = document.createElement("ol");
            divpatt.innerHTML += "<ol>";
            for (i = 0; i < patternlabels.length; i++) {
                var li = document.createElement("li");
                li.innerHTML = patternlabels[i] + " (<a href=\"#\" onclick=\"selectPattern(" + i + ")\"> select </a>)";
                ol.appendChild(li);
                //divpatt.innerHTML += //qpatterns[selectedpattern][i] + ': <input class="aparameter" value="' + qpatterns[selectedpattern][i] + '" oninput="replaceParaValue(' + "/" + qpatterns[selectedpattern][i] + "/g" + ', this)"/><br>';
            }
            divpatt.appendChild(ol);
        </script>      
        <script src='yasr/yasr.bundled.min.js'></script>
        <script src="yasqe/dist/yasqe.bundled.min.js"></script>
        <script src="yasqe/doc/doc.min.js"></script>
        <script type="text/javascript">
            //var yasqe = YASQE(document.getElementById("showcase"), 
            $.fn.scrollView = function () {
                return this.each(function () {
                    $('html, body').animate({
                        scrollTop: $(this).offset().top
                    }, 1000);
                });
            };
            var yasqe = YASQE.fromTextArea(document.getElementById('query'),
                    {
                        sparql: {
                            showQueryButton: true,
                            endpoint: "http://volvestre.cirad.fr:8890/sparql",
                            //endpoint: "http://localhost:8890/sparql",
                            collapsePrefixesOnLoad: false,
                            //persistent: true,
                            args: [{name: 'timeout', value: document.getElementById('timeout').value}],
                            callbacks: {
                                beforeSend: function (data) {
                                    yasqe.options.sparql.args[0].value = document.getElementById('timeout').value;
                                    console.log(yasqe.options.sparql.args[0].value);
                                },
                                success: function (data) {
                                    //console.log("success", data);
                                    $('#yasr').scrollView();
                                }, //, headers: {"Access-Control-Allow-Origin": "*", "Access-Control-Allow-Methods": "GET, POST, DELETE, PUT", "Access-Control-Allow-Headers": "X-Requested-With, Content-Type, X-Codingpedia"}
                                error: function (data) {
                                    $('#yasr').scrollView();
                                }
                            }
                        }
                    });

            var yasr = YASR(document.getElementById("yasr"), {
                //this way, the URLs in the results are prettified using the defined prefixes in the query
                getUsedPrefixes: yasqe.getPrefixesFromQuery,
                useGoogleCharts: false,
                persistency: {
                    prefix: false
                }
            });

            //link both together (YasQUE and YASR)
            yasqe.options.sparql.callbacks.complete = yasr.setResponse;
        </script>
</body>
</html>
