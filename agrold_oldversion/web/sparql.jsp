<%-- 
    Document   : sparql
    Created on : Jun 25, 2015, 12:52:42 PM
    Author     : tagny
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html version="-//W3C//DTD XHTML 1.1//EN"
      xmlns="http://www.w3.org/1999/xhtml"
      xml:lang="en"
      >
    <head>
        <title>AgroLD SPARQL Query Editor</title>
        <meta name="Copyright" content="Copyright &copy; 2015 AgroLD" />
        <meta name="Keywords" content="AgroLD Sparql" />
        <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
        <link href="styles/style1.css" rel="stylesheet" type="text/css"/>
        <link href="styles/style3.css" rel="stylesheet" type="text/css"/>  
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
        <script>
            /* 
             * SPARQL QUERY PATTERNS
             */
            var qpatterns = new Array();
            var selectedPatternIdx = 0;
            var patternlabels = ['Retrieve list of graphs', 'Search terms by label', 'List relation types in a given graph',
                'Retrieve the local neighbourhood of Oriza sativa japonica protein: IAA16 - Auxin-responsive protein (UniProt accession:P0C127)',
                'Retrieve genes that participate in a pathway: Calvin cycle',
                'Retrieve Proteins associated with a QTL: DTHD (days to heading)',
                'Get the ID corresponding to the ontology term "<b>homoaconitate hydratase activity</b>"', 'Get the level <b>4</b> ancestor of <b>GO:0004409</b>', 'Describe uniprot:P0C127'];
            qpatterns['PREFIX obo:<http://purl.obolibrary.org/obo/>\n' +
                    'PREFIX vocab:<vocabulary/>\n\n' +
                    'SELECT distinct ?graph\n' +
                    'WHERE {\n' +
                    ' GRAPH ?graph {\n' +
                    '   ?subject ?predicate ?object.\n' +
                    ' }\n filter(REGEX(?graph, "^http://www.southgreen.fr/agrold/"))\n' +
                    '}'] = [];
            qpatterns['PREFIX vocab:<vocabulary/>\n\n' +
                    'SELECT DISTINCT ?term_id ?term_name ?graph\n' +
                    'WHERE { \n' +
                    ' GRAPH ?graph { \n' +
                    '  ?term_id rdfs:label ?term_name . \n' +
                    " FILTER regex(str(?term_name), 'plant height') \n" +
                    ' } \n' +
                    '}'] = ["plant height"];
            qpatterns['PREFIX graph:<gramene.genes>\n\n' +
                    'SELECT distinct ?relation\n' +
                    'WHERE { \n' +
                    ' GRAPH graph: { \n' +
                    '  ?subject ?relation ?object . \n' +
                    ' } \n' +
                    '} \nORDER BY ?relation'] = [];
            qpatterns['PREFIX obo:<http://purl.obolibrary.org/obo/>\n\
PREFIX uniprot:<http://purl.uniprot.org/uniprot/>\n\
PREFIX vocab:<vocabulary/>\n\
PREFIX graph:<protein.annotations>\n\n\
SELECT distinct ?predicate ?object ?object_label\n\
WHERE {\n\
 GRAPH graph: {\n\
  uniprot:P0C127 ?predicate ?object.\n\
  OPTIONAL {\n\
   GRAPH ?g {\n\
    ?object rdfs:label ?object_label.\n\
   }\n\
  }\n\
 }\n\
}'] = ["uniprot:P0C127"];
            qpatterns["PREFIX obo:<http://purl.obolibrary.org/obo/>\n\
PREFIX uniprot:<http://purl.uniprot.org/uniprot/>\n\
PREFIX vocab:<vocabulary/>\n\
PREFIX graph:<gramene.cyc>\n\
PREFIX pathway:<biocyc.pathway/CALVIN-PWY>\n\n\
SELECT DISTINCT ?gene ?name ?taxon_name\n\
WHERE {\n\
 GRAPH graph: {\n\
  ?gene vocab:is_agent_in pathway:. \n\
  ?gene rdfs:label ?name.\n\
  ?gene vocab:taxon ?taxon_name.\n\
 }\n\
}\n\
LIMIT 100"] = [];
            qpatterns['PREFIX obo:<http://purl.obolibrary.org/obo/>\n\
PREFIX uniprot:<http://purl.uniprot.org/uniprot/>\n\
PREFIX vocab:<vocabulary/>\n\
PREFIX graph1:<protein.annotations>\n\
PREFIX graph2:<qtl.annotations>\n\
PREFIX qtl: <http://www.identifiers.org/gramene.qtl/AQAT004> # DTHD \n\n\
SELECT distinct ?id ?name \n\
WHERE {\n\
 GRAPH graph1: {\n\
  ?id vocab:has_trait ?to.\n\
  ?id rdfs:label ?name.\n\
 }\n\
 GRAPH graph2: {\n\
  qtl: vocab:has_trait ?to.\n\
 }\n\
}\nORDER BY ?name'] = [];
            qpatterns['\n\
SELECT ?id\n\
WHERE { \n\
 {\n\
  SELECT ?subject\n\
  WHERE\n\
  {\n\
   ?subject rdfs:label "homoaconitate hydratase activity"^^xsd:string .\n\
   ?subject a owl:Class .\n\
  }limit 1\n\
 }\n\
 BIND(REPLACE(str(?subject), \'^.*(#|/)\', "") AS ?localname)\n\
 BIND(REPLACE(?localname, "_", ":") as ?id).\n\
} '] = ["homoaconitate hydratase activity"];
            qpatterns['\nSELECT ?ancestorId\n\
 WHERE \n\
 {     \n\
  { \n\
    SELECT ?ancestor1\n\
    WHERE\n\
    {\n\
        ?subject rdfs:subClassOf ?ancestor1.\n\
                FILTER REGEX(STR(?subject), CONCAT(REPLACE("GO:0004409", ":", "_"), "$"))\n\
    } limit 1 \n\
  }\n\
    ?ancestor1 rdfs:subClassOf ?ancestor2.\n\
    ?ancestor2 rdfs:subClassOf ?ancestor3.\n\
    ?ancestor3 rdfs:subClassOf ?ancestor4.\n\
    ?ancestor4 a owl:Class .\n\
   BIND(REPLACE(str(?ancestor4), \'^.*(#|/)\', "") AS ?ancestorLocalname)\n\
   BIND(REPLACE(?ancestorLocalname, "_", ":") as ?ancestorId)\n\
}'] = ["GO:0004409"];

            qpatterns['PREFIX uniprot:<http://purl.uniprot.org/uniprot/>\n\nDESCRIBE uniprot:P0C127'] = ["uniprot:P0C127"];
            prefixes = "BASE <http://www.southgreen.fr/agrold/>\n" +
                    'PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n' +
                    'PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n';
            // init Patterns DIV: add patterns            

            function patternChange() {
                selectPattern(selectedPatternIdx);
            }

            function selectPattern(patternIdx) {
                selectedPatternIdx = patternIdx;
                var selectedpattern = Object.keys(qpatterns)[patternIdx];
                document.getElementById("query").value = prefixes + selectedpattern;
                yasqe.setValue(document.getElementById("query").value);
                //console.log(document.getElementById("query").value);

                document.getElementById("parameters").innerHTML = "";
                document.getElementById("parameters").innerHTML = "";
                if (qpatterns[selectedpattern].length > 0) {
                    document.getElementById("parameters").innerHTML += "<b>Set values of parameters:</b><br>"
                    for (i = 0; i < qpatterns[selectedpattern].length; i++) {
                        document.getElementById("parameters").innerHTML += "Replace \"" + qpatterns[selectedpattern][i] + '" by : <input class="aparameter" value="' + qpatterns[selectedpattern][i] + '" oninput="replaceParaValue(' + "/" + qpatterns[selectedpattern][i] + "/g" + ', this)"/><br>';
                    }
                } else {
                    document.getElementById("parameters").innerHTML = "";
                }
            }

            function replaceParaValue(apara, avalue) {
                var pattern = Object.keys(qpatterns)[selectedPatternIdx];
                var qtext = pattern.replace(apara, avalue.value);
                document.getElementById("query").value = prefixes + qtext;
                yasqe.setValue(document.getElementById("query").value);
            }
            function resetForm() {
                //var x = document.getElementById("patterns").children[0];
                //x.setAttribute("selected", "selected");
                patternChange();
            }
        </script>   
        <style>
            #sparql{
                width: 59%; float:left;
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
                width: 100%;
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
            selectPattern(0);">
        <div class="container">                                   
            <div id="header">
                <h3>SPARQL Query Editor</h3> 
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
                            </textarea>
                            <div id="showcase"></div>
                            <label for="timeout" class="n">Execution timeout</label>
                            <input name="timeout" id="timeout" type="text" value="20000" onchange="//setTimeout(this)"/> milliseconds
                            <span class="info"><i>(values less than 1000 are ignored)</i></span>		<br />
                            <label class="n" for="options">Options</label>                            
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
                useGoogleCharts: false
            });

            //link both together (YasQUE and YASR)
            yasqe.options.sparql.callbacks.complete = yasr.setResponse;
        </script>
    </body>
</html>
