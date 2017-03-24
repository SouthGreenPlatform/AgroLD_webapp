<%-- 
    Document   : relfinder
    Created on : 22 avr. 2015, 21:27:06
    Author     : tagny
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <!--[if lt IE 9]>
          <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
        <!-- 3rd party CSS libraries -->
        <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css" />
        <link rel="stylesheet" href="reveald/libraries/bootstrap/2.0.2/css/bootstrap-responsive.css">
        <link rel="stylesheet" href="reveald/libraries/slickgrid/2.0.1/slick.grid.css">
        <link rel="stylesheet" href="reveald/libraries/bootstrap/2.0.2/css/bootstrap.css" />  
        <link rel="stylesheet" href="reveald/libraries/bootstrap/2.0.2/css/font-awesome.min.css" />  
        <!-- Recline CSS components -->
        <link rel="stylesheet" href="reveald/css/recline.css">
        <!-- /Recline CSS components -->
        <!-- Custom CSS for the Data Explorer Online App -->
        <link rel="stylesheet" href="reveald/css/style.css">
        <link rel="stylesheet" href="reveald/css/search.css">

        <!-- 3rd party JS libraries -->
        <script type="text/javascript" src="reveald/libraries/jquery/1.7.1/jquery.js"></script>
        <script type="text/javascript" src="reveald/libraries/underscore/1.1.6/underscore.js"></script>
        <script type="text/javascript" src="reveald/libraries/backbone/0.5.1/backbone.js"></script>
        <script type="text/javascript" src="reveald/libraries/moment/1.6.2/moment.js"></script>
        <script type="text/javascript" src="reveald/libraries/jquery.flot/0.7/jquery.flot.js"></script>
        <script type="text/javascript" src="reveald/libraries/mustache/0.5.0-dev/mustache.js"></script>
        <script type="text/javascript" src="reveald/libraries/bootstrap/2.0.2/bootstrap.js"></script>
        <!--<script type="text/javascript" src="libraries/slickgrid/2.0.1/jquery-ui-1.8.16.custom.min.js"></script>-->
        <script type="text/javascript" src="reveald/libraries/slickgrid/2.0.1/jquery.event.drag-2.0.min.js"></script>
        <script type="text/javascript" src="reveald/libraries/slickgrid/2.0.1/slick.grid.min.js"></script>
        <script type="text/javascript" src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
        <script type="text/javascript" src="reveald/libraries/jOWL.js"></script>
        <script type="text/javascript" src="reveald/libraries/json2.js"></script>
        <!-- D3 javascript Library for Visual Query Builder -->
        <script type="text/javascript" src="reveald/libraries/d3/d3.js"></script>
        <script type="text/javascript" src="reveald/libraries/d3/d3.layout.js"></script>
        <script type="text/javascript" src="reveald/libraries/d3/d3.geom.js"></script>
        <!-- recline library -->
        <script type="text/javascript" src="reveald/libraries/recline.js"></script>  
        <!--script type="text/javascript" src="http://maulikkamdar.cloudant.com/biologicaltitlecorpus/_design/couchdb-xd/couchdb.js"></script-->


        <!-- non-library javascript containing utility functions -->
        <script type="text/javascript" src="reveald/js/revealdUtils.js"></script>
        <script type="text/javascript" src="reveald/js/config.js"></script>
        <!-- -->
        <!-- non-library javascript specific to faceted-browser -->
        <script type="text/javascript" src="reveald/js/app.js"></script>
        <!-- non-library CSS files -->
        <link rel="stylesheet" href="reveald/css/granatum.css" />
        <link rel="stylesheet" href="reveald/css/grid.css" />
        
        <title>AgroLD:Visual Query</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="styles/style1.css" rel="stylesheet" type="text/css"/>
        <link href="styles/menu1.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>        
        <div id="wrapper">
            <jsp:include page="header.html"></jsp:include>
                <section>
                    <div class="info_title">Search > Visual Query<div>
                    <div class="recline-app">
                        <div class="navbar">
                            <div class="navbar-inner">
                                <div class="container-fluid">
                                    <!--a class="brand" href="#">ReVeaLD</a-->
                                    <ul class="nav pull-right">
                                        <li>
                                            <a href="#" class="js-reveald-demo-link">
                                                About
                                                <i class="icon-cog icon-white"></i>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="#" class="js-sample-queries-dialog">
                                                Examples
                                                <i class="icon-sitemap icon-white"></i>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>


                        <div class="container-fluid">
                            <div class="content">
                                <div style="" class="page-home backbone-page">
                                    <div class="row-fluid">
                                        <div class="span12" style="text-align:center">
                                            <input id="autoComSearchBox" type="text" placeholder="Search"  dir="ltr" style="direction: ltr; text-align: left;">
                                        </div>
                                    </div>
                                    <div class='searchContainer'> 
                                        <div id="chart" class="infovis"> </div>   
                                        <div id="searchTool">
                                            <div class="logo"><a href="?reset=true"><img src="reveald/img/Insight-JPG.jpg"></a></div>
                                            <div>
                                                <fieldset>
                                                    <legend class="usage">Usage</legend>
                                                    <strong>Scroll, Double-Click</strong> &#8594; Zoom. <br>
                                                    <strong>Drag node</strong> &#8594; Move node. <br>
                                                    <strong>Drag background</strong> &#8594; Move graph. <br>		
                                                    <img id="waiting" alt="waiting icon" src="reveald/img/waiting.gif"/>
                                                </fieldset>
                                            </div>
                                            <div id="uiTabHolder">
                                                <ul id="uiTitleHolder">
                                                    <li><a href="#query">Query</a></li>
                                                </ul>
                                                <div id="query" align='center'>
                                                    <div id="inner-details">
                                                        <!--	<div id="queryQuestion"></div> -->
                                                        <div class="ui-widget">
                                                            <!--<input type="checkbox" checked id="debug"/>Debug Mode &nbsp;
                                                    <input type="checkbox" checked id="properties"/>Hide properties <br>
                                                            <div style="display:none"><input type="checkbox" id="hidePredicates"/>Hide predicates <br></div>
                                                            <input id="autocomplete"><br>-->
                                                            <input type="checkbox" id="allSelect"/>Select All &nbsp;
                                                            <input type="checkbox" checked id="properties"/>Hide Links &nbsp;
                                                            <input type="checkbox" id="flexible"/>Flexible 
                                                            <br>
                                                        </div>

                                                        <div id="selectConcept"></div>
                                                        <!--<input type='text' id='url' name='concept' value='' size='100'/> --><br>
                                                        <input class="btn btn-primary" type="submit" id="render" value="Select"/>

                                                    </div>	
                                                    <div class="queryBuilder" id='sparqlQuery'>
                                                        <textarea id='queryDisplay' class='box' name="query" cols="30" rows="1" style=''></textarea>
                                                        <select name="limit" id="limitResults">
                                                            <option value="100">100</option>
                                                            <option value="500">500</option>
                                                            <option value="1000">1000</option>
                                                            <option value="2000">2000</option>
                                                            <option value="5000">5000</option>
                                                            <option value="10000">10000</option>
                                                        </select><br>
                                                        <select name="output" id='QueryResultsOutputOpt'>
                                                            <option value="display">Display results</option>
                                                            <option value="download">Download results</option>
                                                        </select> <br>
                                                        <input type="submit" id="results" class="resultsBtn btn btn-primary" value = "Retrieve">
                                                    </div>
                                                    <div class='taskEvalPanel' id='taskEvalPanel' style='display:none;'>
                                                        <br><button onclick="validateTask()" id="taskEvalBtn" class="taskEvalBtn btn btn-primary">Validate</button>
                                                        <button onclick="revealTask()" id="taskHintBtn" class="taskHintBtn btn btn-primary">Reveal</button>
                                                        <br><br><div align='center'><button onclick='skipEval()' class="skipEvalBtn btn btn-primary">Skip Evaluation</button> </div>
                                                    </div>
                                                    <div id="log"></div>	
                                                </div>
                                            </div>		 	
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div style="display: block;" class="page-explorer backbone-page">
                                <div class="data-explorer-here"></div>   
                            </div>

                        </div>

                        <div class="scoreVisualizer" align='center' style='display:none;' >
                            <br>
                            <h1 class='evaluateQueryHeader' align='center'>Incrementation Help</h1>
                            <p class='evaluateQuery' id='showError'></p>
                            <div align='center'>
                                <button onclick="dismiss()" id="dismissBtn" class="dismissBtn btn btn-primary">Dismiss</button></div>
                        </div>
                        <div class="modal fade in js-login-dialog" style="display: none;">
                            <div class="modal-header">
                                <a class="close" data-dismiss="modal">×</a>
                                <h3 class="sidebar">Login</h3>
                            </div>
                            <div class="modal-body">
                                <form class="js-login">
                                    <div class="control-group">
                                        <div class="controls">
                                            <input type="text" name="source" class="span5" placeholder="BSCW ID" />
                                            <input type="text" name="source" class="span5" placeholder="Password" />
                                            <p class="help-block">Login using your BSCW Credentials for ontology incrementation and query logging.</p>
                                            <input name="backend_type" style="display: none;" />
                                        </div>
                                    </div>
                                    <button type="submit" class="btn btn-primary">Login &raquo;</button>
                                </form>
                            </div>
                        </div>

                        <div class="modal fade in js-popup-info" style="display: none;">
                            <div class="modal-header">
                                <a class="close" data-dismiss="modal">×</a>
                            </div>
                            <div class="modal-body">
                                <div id="additionalTabs">
                                    <ul id="additionalTitleHolder">
                                    </ul>
                                </div>
                            </div>
                        </div>

                        <div class="modal fade in js-saved-queries" style="display: none;">
                            <div class="modal-header">
                                <a class="close" data-dismiss="modal">×</a>
                                <h3 class="sidebar">Saved Queries</h3>
                            </div>
                            <div class="modal-body">
                                <div id='savedQueryList'>
                                </div>
                            </div>
                        </div>

                        <div class="modal fade in js-extended-ontologies" style="display: none;">
                            <div class="modal-header">
                                <a class="close" data-dismiss="modal">×</a>
                                <h3 class="sidebar">Extended Ontologies</h3>
                            </div>
                            <div class="modal-body">
                                <div id="extendedOntoResult"></div>
                                <div id="extendedOntoList">

                                </div>
                            </div>
                        </div>

                        <div class="incrementMetaData" align='center' style='display:none;' >
                            <br><h1 class='increMetaHeader' align='center'>Incrementation Details</h1>
                            <br>
                            <div id='fileNameMetaField' align='center'>
                                <p class='increText'>Please fill in additional details</p>
                                <input type='text' id='fileNameMeta' value='Model Name:' onblur="if (value == '')
                                            value = 'Model Name:'" onfocus="if (value == 'Model Name:')
                                                        value = ''"><br>
                                <!--<p>Select Folder: <span id='folderNameMeta'></span><button id='folderSelBtn' class="folderSelBtn btn">Browse</button></p>-->
                                <select name="folderSelMeta" id="folderSelMeta"></select>
                                <textarea id='fileDescMeta' value='Model Description:' onblur="if (value == '')
                                            value = 'Model Description:'" onfocus="if (value == 'Model Description:')
                                                        value = ''">Model Description:</textarea><br>
                                <button id="metaSubmitBtn" class="metaSubmitBtn btn btn-primary">Submit</button>
                                <button id="metaSkipBtn" class="metaSkipBtn btn btn-primary">Skip</button>
                                <br>
                            </div>
                        </div>

                        <div class="queryResultsMetaData" align='center' style='display:none;' >
                            <br><h1 class='queryResMetaHeader' align='center'>Query Results Details</h1>
                            <br>
                            <div id='resNameMetaField' align='center'>
                                <p class='increText'>Please fill in additional details</p>
                                <input type='text' id='resNameMeta' value='Query Name:' onblur="if (value == '')
                                            value = 'Query Name:'" onfocus="if (value == 'Query Name:')
                                                        value = ''"><br>
                                <select id='resFormatMeta'>
                                    <option value="TSV">Tab-Separated Value (TSV)</option>
                                    <option value="SMI">SMILES Tab Separated Value (SMI)</option>
                                    <option value="Text">Plain Text (TXT)</option>
                                    <option value="CSV">Comma-Separated Value (CSV)</option>
                                    <option value="XML">Extensible Markup Language (XML)</option>
                                    <option value="Turtle">Turtle (TTL)</option>
                                    <option value="JSON">JavaScript Object Notation (JSON)</option>
                                </select><br>
                                <select name="resfolderSelMeta" id="resfolderSelMeta" style="display:none;"></select><br>
                                <button id="resMetaSubmitBtn" class="resMetaSubmitBtn btn btn-primary">Submit</button>
                                <button id="resMetaSkipBtn" class="resMetaSkipBtn btn btn-primary">Default</button>
                                <br>
                            </div>
                        </div>

                        <div class="selectQueryElems" align='center' style='display:none;' >
                            <br><h1 class='queryElemsMetaHeader' align='center'>Select Query Elements</h1>
                            <br>
                            <div id='selField' align='center'>
                                <p class='increText'>Visualize and Formulate Query Models with the Selected Elements.</p>
                                <button id="selQESubmitBtn" class="resMetaSubmitBtn btn btn-primary">Submit</button>
                                <button id="selQESkipBtn" class="resMetaSkipBtn btn btn-primary">Default</button><br><br>
                                <div id="topQueryElems" align='left'>

                                </div>
                            </div>
                        </div>

                        <div class="modal fade in js-sample-queries" style="display: none;">
                            <div class="modal-header">
                                <a class="close" data-dismiss="modal">×</a>
                                <h2 class="sidebar">Sample Queries</h2>
                            </div>
                            <div class="modal-body">
                                <table border='0px' width='100%' class='sampleQueryTable'>
                                    <tr><td colspan="3" align="center"><p class='increText'>These queries use a lighter version of the Granatum Model. <a href='/explorer?type=sampleQuery'>Switch here</a></p></tr> 
                                    <tr>
                                        <!--	<td align='center' width='33%'>
                                                <a href='/explorer?type=sampleQuery&nodes=31-26-38-42-22-105-68-77-103-113-105&links=31.26-31.38-31.42-31.22-26.105-38.68-42.77-42.103-42.113-22.105&filters=26.105.c.estrogen|38.68.c.pomegranate&flexible=1'><img src='res/query2.jpg' width='200px' height='200px'></a></td>-->
                                        <td align='center' width='33%'>
                                            <a href='?type=sampleQuery&nodes=45-17-1-25-83-89-69-84-101-65-76&links=45.17-45.1-1.25-45.83-45.89-17.69-17.84-17.101-1.65-25.76&filters=17.69.lt.300|1.65.c.estrogen%20receptor|25.76.c.human&flexible=1'><img src='reveald/img/query6.jpg' width='200px' height='200px'></a></td>
                                        <td align='center' width='33%'></td>
                                        <td align='center' width='33%'>
                                            <a href='?type=sampleQuery&nodes=507-880-891-698-691-1198-892-881&links=undefined-507.880-507.891-507.698-507.691-507.1198-507.892-507.881&filters=507.881.c.mouse&flexible=0'><img src='reveald/img/query3.jpg' width='200px' height='200px'></a></td>
                                    </tr>
                                    <tr>
                                        <!--<td align='center' width='33%'><b>Toxicity</b> and <b>Published Work</b> information of <b>Chemopreventive Agents</b> from <i>'Pomegranate'</i> which affect <i>'Estrogen'</i>-related <b>Pathways</b></td>-->
                                        <td align='center' width='33%'><b>Assays</b>, which input <i>Estrogen Receptor</i> <b>Targets</b> of <b>Organisms</b> common named <i>Human</i>, and identify potential <b>Chemopreventive Agents</b> with a <i>Molecular Weight</i> less than 300 </td>
                                        <td align='center' width='33%'></td>
                                        <td align='center' width='33%'><b>Uniprot Journal Citations</b> detailing experiments on <i>'Mouse'</i></td>
                                    </tr>
                                    <tr>
                                        <td align='center' width='33%'>
                                            <a href='?type=sampleQuery&nodes=503-668-870-770-882-939&links=undefined-503.668-503.870-503.770-503.882-503.939&filters=503.882.c.cyclo|503.939.lt.200&flexible=0'><img src='reveald/img/query4.jpg' width='200px' height='200px'></a></td>
                                        <td align='center' width='33%'></td>
                                        <td align='center' width='33%'>
                                            <a href='?type=sampleQuery&nodes=108-591-999-1011&links=108.591-108.999-591.1011&filters=108.999.c.colon%20cancer|591.1011.lt.400&flexible=0'><img src='reveald/img/query5.jpg' width='200px' height='200px'></a></td>
                                    </tr>
                                    <tr>
                                        <td align='center' width='33%'><i>IUPAC Names, Inchi Keys & SMILES notations</i> of <b><u>Chebi Compounds</u></b> with <i>Molecular Mass</i> less than 200 and have a ring-like structure (Have the word <i>'cyclo'</i> in the <i>title</i>)</td>
                                        <td align='center' width='33%'></td>
                                        <td align='center' width='33%'><b><u>DrugBank Drugs</u></b> which have <i>Molecular weight</i> less than 400 and are <i>possible Drugs</i> for <b><u>Diseasome Diseases</u></b> labelled <i>Colon Cancer</i></td>
                                    </tr>
                                </table>
                            </div>
                        </div>

                        <div class="modal fade in js-increment-error" style="display: none;">
                            <div class="modal-header">
                                <a class="close" data-dismiss="modal">×</a>
                                <h2 class="sidebar"></h2>
                            </div>
                            <div class="modal-body">
                                <h2 class='sidebar' align='center'>Granatum ReVeaLD Error</h2>
                                <p align='justify'> An error seems to have occured. Some possible reasons can be :-
                                <ul><li> Your browser does not support Javascript. We recommend usage of our system on Firefox 4.0+, Chrome 11.0+, IE 9.0+, Safari 5.0+ versions for best performance.</li>
                                    <li>Your browser has disabled 'Cookies'. Please ensure that Cookies are enabled for customized access</li>
                                    <li>Your session has expired. Please log-in back to BSCW to use features like model extension and query logging.</li>
                                </ul>
                            </div>
                        </div>

                        <div class="modal fade in js-about-reveald" style="display: none;">
                            <div class="modal-header">
                                <a class="close" data-dismiss="modal">×</a>
                                <h2 class="sidebar">About ReVeaLD</h2>
                            </div>
                            <div class="modal-body">
                            </div>
                        </div>

                        <div class="modal fade in js-reveald-demo" style="display: none;">
                            <div class="modal-header">
                                <a class="close" data-dismiss="modal">×</a>
                                <h2 class="sidebar">Granatum ReVeaLD</h2>
                            </div>
                            <div class="modal-body">
                                <p align='center' class='evaluateQuery'>To know about our Data Sources and to evaluate the links connecting these sources, please visit <a href='http://srvgal78.deri.ie/RoadMapEvaluation/' target='_blank'>http://srvgal78.deri.ie/RoadMapEvaluation/</a>. </p>
                                <hr>
                                <iframe width="750" height="422" src="http://www.youtube.com/embed/6HHK4ASIkJM?vq=hd720" frameborder="0" allowfullscreen></iframe>
                                <hr>
                                <p align='center' class='evaluateQuery'>Please change the video format quality settings to 720p HD for better clarity.</p>
                                <p align='center'>In case of video-loading problems, you can access the video at <a href='http://www.youtube.com/watch?v=nZqjQekKGGY&hd=1' target='_blank'>http://www.youtube.com/watch?v=nZqjQekKGGY&hd=1</a>.</p>
                            </div>
                        </div>



                        <div class="splashScreenExplorer">
                            <img src="reveald/img/loading-animation.gif"><img src="reveald/img/Insight-JPG.jpg">
                        </div>


                    </div>

                    <script type="text/javascript" src='reveald/js/visualSparql.js'></script>
                    <script type="text/javascript" src='reveald/js/main.js'></script>
                    <script type="text/javascript" src="reveald/js/dslExtractor.js"></script>
                    <script type="text/javascript" src="reveald/js/barChart.js"></script>
                    <script type="text/javascript" src='reveald/js/search.js'></script>

                    <!-- GLMol Library - needs to be included after DOM is loaded-->
                    <script type="text/javascript" src="reveald/libraries/glmol/Three49custom.js"></script>
                    <script type="text/javascript" src="reveald/libraries/glmol/GLmol.js"></script>
                    <!--/div-->

                </section>
            <jsp:include page="footer.html"></jsp:include>
        </div>
        <script src="yasqe/dist/yasqe.bundled.min.js"></script>
        <script src="yasqe/doc/doc.min.js"></script>
        <!--script type="text/javascript">
            var yasqe = YASQE.fromTextArea(document.getElementById('queryDisplay'),
                    {
                        sparql: {
                            showQueryButton: false,
                            endpoint: "http://volvestre.cirad.fr/sparql/",
                            //endpoint: "http://localhost:8890/sparql",
                            //persistent: true,
                            args: [],
                            callbacks: {
                                beforeSend: function (data) {                                    
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
        </script-->
    </body>
</html>
