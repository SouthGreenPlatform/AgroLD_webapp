var w = 850,
    h = 800

var vis = d3.select("#chart").append("svg:svg")
    .attr("width", w)
    .attr("height", h)
    .attr("pointer-events", "all")
    .append('svg:g')
    .call(d3.behavior.zoom().on("zoom", redraw))
    .append('svg:g');

vis.append('svg:rect')
    .attr('width', w)
    .attr('height', h)
    .attr('fill', 'white');

var files = d3.select("body").append("div").style("width", 100).style("height", 100).style("border-style", "solid").style("border-width", "1px").style("float", "left");
var color = d3.scale.category20();
var preds=false;
var types=true;
var debug=true;
var nodes = [];
var links = [];
var linkedArrowhead = [];
var force;
var plotJSON;
var flexible = 1;

if(allQueryParams.flexible == null || allQueryParams.flexible == 1)
	$('#flexible').prop('checked', true);
else 
	$('#flexible').prop('checked', false);

if(allQueryParams.links != null) {
	var checkedLinks = allQueryParams.links.split('-');
	for (i in checkedLinks) {
	//	var selector = 'input:checkbox[value='+checkedLinks[i]+']';
	//	$(selector).prop("checked", true);
	}
}

function redraw() {
  vis.attr("transform",
      "translate(" + d3.event.translate + ")"
      + " scale(" + d3.event.scale + ")");
}

function mergeGraphs(newNodes, newLinks){
  for(i in newLinks){
   sIdx = newLinks[i].source;
   tIdx = newLinks[i].target;
  
   if(nodes.indexOf(newNodes[sIdx]) == -1){
	   nodes.push(newNodes[sIdx]);
   }
   newLinks[i].source = nodes.indexOf(newNodes[sIdx]);
  
   if(nodes.indexOf(newNodes[tIdx]) == -1){
	   nodes.push(newNodes[tIdx]);
   }
   newLinks[i].target = nodes.indexOf(newNodes[tIdx]);
   links.push(newLinks[i]);
  }

}

function renderPopUp(json,label,image,resource){
	if(label.length > 10)
		var truncatedLabel = label.substring(0,8) + "...";
	else 
		var truncatedLabel = label;
	
	var resourceTerms = resource.split(/[:#\/]/);
	var resourceTerm = makeid() + resourceTerms[resourceTerms.length-1] ;
	
	var selector = "#" + resourceTerm;
	$infoTabs.tabs('add',selector, truncatedLabel);  
	$('.splashScreenExplorer').hide();
	var imageTag = "<img src="+image+">";
	
	var structure = false;
	var structureFile = '';
	d3.select(selector).html("" +
			"<h4 class='sidebar'>" + label + "</h4><br>" + imageTag);
	d3.select(selector).append("table").attr("border","0px").selectAll("tr").data(json).enter().append("tr").html(function(d){
		var cellValue;
		if(d.object.substring(0,4) == 'http'){
			var uriTerms = d.object.split(".");
			var ext = uriTerms[uriTerms.length-1];
			switch(ext) {
				case "png" : cellValue = "<img src='" + d.object + "'>";
					break;
				case "jpg" : cellValue = "<img src='" + d.object + "'>";
					break;
				case "gif" : cellValue = "<img src='" + d.object + "'>";
					break;
				case "jpeg" : cellValue = "<img src='" + d.object + "'>";
					break;
				case "html" : cellValue = "<iframe src='" + d.object + "' width='100%'></iframe><br>" +
						"<a href='"+ d.object +"' target='_blank'>Open link in a new window</a>";
					break;
				case "sdf" : cellValue = "<div id='"+resourceTerm+"sdf' style='width: 300px; height: 200px; background-color: black;'></div> " +
							"<textarea wrap='off' id='"+resourceTerm+"sdf_src' style='display: none;'>" +
							"</textarea>" +
							"<a href='"+d.object+"' target='_blank'>Download Structure File</a>";
					structure = true;
					structureFile = d.object;
					structureRepo = '';
					break;
				default : cellValue = "<a href=javascript:popUpInfo(\'"+d.object+"\')>"+d.object+"</a>";
			}
		}
		switch(d.predicate){
			case "pdbIdPage" : cellValue = "<div id='"+resourceTerm+"sdf' style='width: 500px; height: 350px; background-color: black;'></div> " +
								"<textarea wrap='off' id='"+resourceTerm+"sdf_src' style='display: none;'>" +
								"</textarea>" +
								"<a href='"+d.object+"' target='_blank'>Visit PDB Structure Page</a>";
				structure = true;
				var structureFrags = d.object.split('=');
				structureFile = structureFrags[structureFrags.length-1];
				structureRepo = 'pdb';
				break;
			case "hgncIdPage" : cellValue = "<iframe src='" + d.object + "' width='100%'></iframe><br>" +
							"<a href='"+ d.object +"' target='_blank'>Open link in a new window</a>";
				break;
			case "pubchemPage" : cellValue = "<iframe src='" + d.object + "' width='100%'></iframe><br>" +
							"<a href='"+ d.object +"' target='_blank'>Open link in a new window</a>";
				break;
			case "image" : cellValue = "<img src='" + d.object + "'>";
				break;
			case "drugReference" : return null;
				break;
			case "generalReference" : return null;
				break;
			case "sdf_file" : cellValue = "<div id='"+resourceTerm+"sdf' style='width: 300px; height: 200px; background-color: black;'></div> " +
								"<textarea wrap='off' id='"+resourceTerm+"sdf_src' style='display: none;'>" +
								"</textarea>" +
								"<a href='"+d.object+"' target='_blank'>Download Structure File</a>";
				structure = true;
				structureFile = d.object;
				break;
			default : break;
		}
		return "<th>"+d.predicate+"</th><td class='infoTableCell'>"+
			((d.object.substring(0,4) == 'http') ? cellValue : d.object)+
			"</td>";
	});
	
	if(structure){
		loadStructure(resourceTerm, structureFile);
	}
	
}

// Create the XHR object.
function createCORSRequest(method, url) {
  var xhr = new XMLHttpRequest();
  if ("withCredentials" in xhr) {
    // XHR for Chrome/Firefox/Opera/Safari.
    xhr.open(method, url, true);
  } else if (typeof XDomainRequest != "undefined") {
    // XDomainRequest for IE.
    xhr = new XDomainRequest();
    xhr.open(method, url);
  } else {
    // CORS not supported.
    xhr = null;
  }
  return xhr;
}

function makeCorsRequest(url, resourceTerm) {
  var xhr = createCORSRequest('GET', url);
  if (!xhr) {
    console.log('CORS not supported');
    return;
  }

  // Response handlers.
  xhr.onload = function() {
    var data = xhr.responseText;
    $('#' + resourceTerm + 'sdf_src').val(data);
    new GLmol(resourceTerm+'sdf');
  };

  xhr.onerror = function() {
    console.log('Woops, there was an error making the request.');
  };

  xhr.send();
}

function loadStructure(resourceTerm, structureFile){
	var sdf_source;
        makeCorsRequest(structureFile, resourceTerm);
        /*var structureURL = window.location.protocol + "//" + window.location.host + window.location.pathname + "crossDomFileRet.php?id=" + structureFile + "&location=" + structureRepo ;
	$.ajax({
		url: structureURL,
		type: 'GET',
		async: false
	}).done(function ( data ) {
		console.log(data);
		$('#' + resourceTerm + 'sdf_src').val(data);
		new GLmol(resourceTerm+'sdf');
	});*/
}

function renderLiterature(data, resource, label) {
	if(label.length > 10)
		var truncatedLabel = label.substring(0,8) + "...";
	else 
		var truncatedLabel = label;
	
	var resourceTerms = resource.split(/[:#\/]/);
	var resourceTerm = makeid() + resourceTerms[resourceTerms.length-1] ;
	var selector = "#" + resourceTerm;
	
	console.log(selector);
	$infoTabs.tabs('add',selector, truncatedLabel);  
	$('.splashScreenExplorer').hide();
	d3.select(selector).html("<h4 class='sidebar'>" + label + "</h4><br>");
	d3.select(selector).append("table").attr("border","0px").selectAll("tr").data(data).enter().append("tr").html(function(d){
		return "<th>"+d.predicate+"</th><td class='infoTableCell'>"+d.object+"</td>";
	});
	
}


function buildUIElements(nodes){
	var optionsMenu = [];
    var inputMenu = [];
    var autoCompleteMenu = [];
  //  console.log("hello Joker");
    for(i in selectedLinksArray){
    	if(selectedLinksArray[i].name == "rdf:type" || selectedLinksArray[i].name ==  "rdfs:subClassOf" || selectedLinksArray[i].name ==  "voidext:subClassOf")
    		continue;
  		else {
			if(selectedNodesArray[selectedLinksArray[i].target].type != "literal"){
				if(selectedNodesArray[selectedLinksArray[i].source].name.substring(0,2) == "?x")
					continue;
				optionsItem = {name : selectedNodesArray[selectedLinksArray[i].source].name + "-->" + selectedLinksArray[i].name + "-->" + selectedNodesArray[selectedLinksArray[i].target].name, uri : selectedNodesArray[selectedLinksArray[i].target].uri};
				optionsMenu.push(optionsItem);
				var autoCompleteItem = selectedNodesArray[selectedLinksArray[i].source].name + "-->" + selectedLinksArray[i].name + "-->" + selectedNodesArray[selectedLinksArray[i].target].name;
				autoCompleteMenu.push(autoCompleteItem);
			} else {
				var labelTerms = selectedLinksArray[i].name.split(/[:#\/]/);
 	    		var label = labelTerms[labelTerms.length-1];
				inputItem = {name : selectedNodesArray[selectedLinksArray[i].source].name + ":" + label, value : selectedLinksArray[i].originalSource + "." + selectedLinksArray[i].originalTarget};
				inputMenu.push(inputItem);
			}
		}
   }
   
   if(selectedLinksArray.length == 0){
	   autoCompleteMenu = $.map(nodes, function(n){
		  if(n.type == "uri")
			  return n.name;
		  else 
			  return null;
	   });
   }
   
 // var obj = actb(document.getElementById('autocomplete'),autoCompleteMenu);
//   console.log(autoCompleteMenu);
   var filterOptions = "id='' class='filterOptions'>" +
   		"<option value='c'>Contains String</option>" +
   		"<option value='lt'>Less Than</option>" +
		"<option value='lte'>Less Than or Equals</option>" +
		"<option value='e'>Equals</option>" +
		"<option value='gte'>Greater Than or Equals</option>" +
		"<option value='gt'>Greater Than</option>" +
		"<option value='ne'>Not Equals</option>" +
		"</select>";
   var tr = d3.select("#selectConcept").append("table").attr("border","0px").selectAll("tr").data(inputMenu).enter().append("tr").html(function(d){ 
	   return "<td class='checkbox'><input type='checkbox' name='properties[]' value='"+d.value+"'>" +
	   		"<td>"+d.name+
	   		"<td><select name='filter-"+d.value+"' "+filterOptions+
	   		"<td><input type='text' name='"+d.value+"'>";
	   });
   
   if(selectedNodesArray.length > 0){
	   var queryQuestion = "List all " + selectedNodesArray[0].instanceOf;
	   var queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
		"prefix xsd: <http://www.w3.org/2001/XMLSchema#> \n" +
		//"prefix granatumx: <http://chem.deri.ie/granatum/> \n" +
		"SELECT DISTINCT * WHERE { \n" ;
	   
	   queryText += selectedNodesArray[selectedLinksArray[0].source].name + " a <" + selectedNodesArray[selectedLinksArray[0].target].uri + "> . \n";   
		  
	/*   for(i in selectedLinksArray){
		   if(selectedLinksArray[i].value != 10 || selectedLinksArray[i].name != "rdf:type")
			   continue;
		   queryText += selectedNodesArray[selectedLinksArray[i].source].name + " a <" + selectedNodesArray[selectedLinksArray[i].target].uri + "> . \n";   
	   }
	  */ //---- Limits the results severely, so am commenting this clause
	   
	   for(i in selectedLinksArray){
		   if(selectedLinksArray[i].value != 10 || selectedLinksArray[i].name == "rdf:type")
			   continue;
		   queryText += selectedNodesArray[selectedLinksArray[i].source].name + " <" +selectedLinksArray[i].name + "> " + selectedNodesArray[selectedLinksArray[i].target].name + " . \n";
	//	   queryText += selectedNodesArray[selectedLinksArray[i].source].name + " granatum:" +selectedLinksArray[i].name + " " + selectedNodesArray[selectedLinksArray[i].target].name + " . \n";
	   }
	   
	   /*
	   for(i in selectedLinksArray){
		   if(selectedLinksArray[i].name == "rdf:type")
			   continue;
		   queryText += "OPTIONAL {" +selectedNodesArray[selectedLinksArray[i].source].name + " <" +selectedLinksArray[i].name + "> ?" + selectedNodesArray[selectedLinksArray[i].target].name + "} . \n";
	   }
	   */ //----- Idea was to retrieve all the literal properties of any concept, but this increases the execution time.
	   
	   for(i in selectedNodesArray){
		   if(selectedNodesArray[i].type == "bliteral" && selectedNodesArray[i].group == 1 && selectedNodesArray[i].filter != null){
			   if(selectedNodesArray[i].filterType == 'c') {
				   queryText += "FILTER( regex( <http://www.w3.org/2001/XMLSchema#string>( "+selectedNodesArray[i].name+" ), \""+selectedNodesArray[i].filter+"\", \"is\" ) ) . \n";
			   } else {
				   var filterChar = '<'
				   switch(selectedNodesArray[i].filterType) {
				   		case 'lt' : filterChar = '<'; break;
				   		case 'lte' : filterChar = '<='; break;
				   		case 'gte' : filterChar = '>=' ; break;
				   		case 'gt' : filterChar = '>' ; break;
				   		case 'e' : filterChar = '=' ; break;
				   		case 'ne' : filterChar = '!=' ; break;
				   		default: break;
				   }
				   queryText += "FILTER( <http://www.w3.org/2001/XMLSchema#double>( "+selectedNodesArray[i].name+" ) "+filterChar+ " " + selectedNodesArray[i].filter+" ) . \n";
			   }
		   } else {
			   continue;
		   }
	   }
	   queryText += "}";
	   
	   d3.select("#queryDisplay").html(queryText); 
   }
   initSearchMenu(autoCompleteMenu);
}

function init(json){
	plotJSON = json;
    force = self.force = d3.layout.force();
    mergeGraphs(json.nodes, json.links);
      force.nodes(nodes)
      .links(links)
      .gravity(0.2)
      .distance(200)
      .charge(-1000)
      .linkDistance(50)
      .size([w, h])
      .start();
      
      var link = vis.selectAll("g.link")
      .data(links)
      .enter()
      .append("svg:g").attr("class", "link").attr("class", function(d){return d.name})
      .call(force.drag);
      link.append("svg:line")
      .attr("class", "link")
      .attr("x1", function(d){return d.x1})
      .attr("y1", function(d){return d.y1})
      .attr("x2", function(d){return d.x1})
      .attr("y2", function(d){return d.y2})
      .style("stroke-width", function(d){return d.value});
      link.append("svg:text")
      .attr("class", "link")
      .attr("x", function(d) { return d.source.x; })
      .attr("y", function(d) { return d.source.y; })
      .text(function(d){return d.name;}).style("display", "none");
      

	  linkArrowhead = link.append("svg:polygon")
	  .attr("class", "arrowhead")
	  .attr("transform",function(d) {
	   angle = Math.atan2(d.target.y-d.source.y, d.target.x-d.source.x);
	   return "rotate("+angle+", "+d.target.x+", "+d.target.y+")";
	  })
	  .attr("points", function(d) {
	   return [[d.target.x,d.target.y].join(","),
	   [d.target.x-3,d.target.y+26].join(","),
	   [d.target.x+3,d.target.y+26].join(",")].join(" ");
	  });
      
      var node = vis.selectAll("g.node")
      .data(nodes)
      .enter().append("svg:g")
      .attr("class", "node")
      .attr("dx", "80px")
      .attr("dy", "80px")
      .call(force.drag);
      
    /*  if(allQueryParams.task != null)
    	  var queryNode = "?task="+allQueryParams.task+"&flexible="+flexible+"&nodes=";
      else
    	  var queryNode = "?flexible="+flexible+"&nodes="; */
      
      if(allQueryParams.folderId != null)
    	  var queryNode = "?folderId="+allQueryParams.folderId+"&flexible="+flexible+"&nodes=";
      else
    	  var queryNode = "?flexible="+flexible+"&nodes=";
      
      for(i in sparqlQueryNodes){
    	  queryNode += sparqlQueryNodes[i]+"-";
      }
      
      var queryLink = "";
      if(sparqlQueryNodes.length > 0){
	      queryLink += "links=";
	      for(i in sparqlQueryLinks){
	    	  queryLink += sparqlQueryLinks[i]+"-";
	      }
	      if(allQueryParams.filters != null)
	    	  queryFilters = '&filters='+allQueryParams.filters;
	      else 
	    	  queryFilters = '';
      }
      
      node.filter(function(d){return d.type == "uri"})
      .append("svg:a").attr("xlink:href", function(d){return (queryLink.length > 0) ? (queryNode+d.mainNumber+"&"+queryLink+d.sourceNumber+"."+d.mainNumber+queryFilters) : (queryNode+d.mainNumber+queryFilters);} )
      .append("svg:circle")
      .attr("class", "node")
      .attr("r", function(d){return 5*Math.sqrt((d.radius)); })
      .attr("x", "-8px")
      .attr("y", "-8px")
      .attr("width", "16px")
      .attr("height", "16px")
      .style("fill",  function(d) { return color(d.group); })
      .style("stroke", "#000").append("title")
      .text(function(d) { return d.description; });
      
      node.filter(function(d){return d.type == "buri"})
      .append("svg:circle")
      .attr("class", "node")
      .attr("r", function(d){return 5*Math.sqrt(d.radius); })
      .attr("x", "-8px")
      .attr("y", "-8px")
      .attr("width", "16px")
      .attr("height", "16px")
      .style("fill",  function(d) { return color(d.group); })
      .style("stroke", "#000").append("title")
      .text(function(d) { return d.description; });

      node.filter(function(d){return d.type == "literal" || (d.type == "bliteral" && d.group == 100)}).append("svg:rect")
      .attr("class", "node")
      .attr("x", "-4px")
      .attr("y", "-8px")
      .attr("width", function(d){ return d.name.width() + 12;})
      .attr("height", "16px")
      .style("fill", function(d) { return (d.group == 0 || d.group == 100) ? "#CFEFCF" : "#FF0000" ;})
      .style("stroke", "#000").append("title")
      .text(function(d) { return d.description; });
      
      // ----------- separated filtered literals from the generic literals to show the filter text
      var filteredLiterals = node.filter(function(d){return (d.type == "bliteral" && d.group == 1)}).append("svg:g")
      .attr("class", "node")
      
      filteredLiterals.append("svg:rect")
      .attr("x", "-4px")
      .attr("y", "-8px")
      .attr("width", function(d){ return d.name.width() + 20;})
      .attr("height", "16px")
      .style("fill", "#FF0000")
      .style("stroke", "#000");
      
      filteredLiterals.append("svg:rect")
      .attr("x", "-4px")
      .attr("y", "8px")
      .attr("width", function(d){ return d.filter.width() + 20;})
      .attr("height", "16px")
      .style("fill", "#FFFFFF")
      .style("stroke", "#000");
      
      filteredLiterals.append("svg:text")
      .attr("class", "filter")
      .attr("dx", 0)
      .attr("dy", "4px")
      .text(function(d) { return d.name });
      
      filteredLiterals.append("svg:text")
      .attr("class", "filter")
      .attr("dx", 0)
      .attr("dy", "20px")
      .text(function(d) { return d.filter });
      
   /*   arr1 = d3.selectAll("text.filter");
      arr = arr1[0];
      for(var i=0; i<arr.length; i++){
       filterTitle = arr[i].previousSibling;
       filterNode = filterTitle.previousSibling
    //   d3.select(filterTitle).attr("width", arr[i].clientWidth+8);
    //   d3.select(filterNode).attr("width", arr[i].clientWidth+8);
      }*/
      // -------------------------------------------------------------
      
      node.filter(function(d){return d.type == "buri" || d.type == "uri"}).append("svg:text")
      .attr("class", "nodetext")
      .attr("dx", 12)
      .attr("dy", ".35em")
      .text(function(d) { return d.name });
      
      node.filter(function(d){return d.type == "literal" || (d.type == "bliteral" && d.group == 100)}).append("svg:text")
      .attr("class", "literal")
      .attr("dx", 0)
      .attr("dy", ".35em")
      .text(function(d) { return d.name });
      
  /*    arr1 = d3.selectAll("text.literal");
      arr = arr1[0];
      for(var i=0; i<arr.length; i++){
       x = arr[i].previousSibling;
  //     console.log(arr[i].getComputedTextLength())
  //     d3.select(x).attr("width", arr[i].textLength+8);
      }*/
      
      var ticks = 0;
      force.on("tick", function() {
       ticks++;
       if (ticks > 300) {
                force.stop();
                force.charge(0)
                    .linkStrength(0)
                    .linkDistance(0)
                    .gravity(0);
                force.start();
            }
       link.selectAll("line.link").attr("x1", function(d) { return d.source.x; })
          .attr("y1", function(d) { return d.source.y; })
          .attr("x2", function(d) { return d.target.x; })
          .attr("y2", function(d) { return d.target.y; });
          link.selectAll("text.link").attr("x", function(d) { return (d.source.x+d.target.x)/2; })
          .attr("y", function(d) { return (d.source.y+d.target.y)/2; });
          
          node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")";
          });
          
        linkArrowhead.attr("points", function(d) {
		   return [[d.target.x,d.target.y+10].join(","),
		   [d.target.x-3,d.target.y+16].join(","),
		   [d.target.x+3,d.target.y+16].join(",")].join(" ");
		   })
		   .attr("transform",function(d) {
		   angle = Math.atan2(d.target.y-d.source.y, d.target.x-d.source.x)*180/Math.PI + 90;
		   return "rotate("+angle+", "+d.target.x+", "+d.target.y+")";
		   });
      	});      
      /*   
      d3.selectAll("circle.node").on("click", function(d){
    	 redrawGraphsByParameter("\"?nodes=" + d.number + "\""); 
      });  */ //--- To not reload the page everytime a node was selected. This messed up the visualization state, so needs to be worked out.
}

d3.select('#debug').on('click', function(){
	if(debug){
		d3.selectAll("line.link").filter(function (d) { return d.value == 1}).style("display", "none");
		d3.selectAll("polygon.arrowhead").filter(function (d) { return d.value == 1}).style("display", "none");
		d3.selectAll("circle.node").filter(function (d) { return d.type == "uri"}).style("display", "none");
		d3.selectAll("rect.node").filter(function (d) { return d.type == "literal"}).style("display", "none");
		d3.selectAll("text.nodetext").filter(function (d) { return d.type == "uri"}).style("display", "none");
		d3.selectAll("text.literal").filter(function (d) { return d.type == "literal"}).style("display", "none");
		debug = false;
	} else {
		d3.selectAll("line.link").filter(function (d) { return d.value == 1}).style("display", "inline");
		d3.selectAll("polygon.arrowhead").filter(function (d) { return d.value == 1}).style("display", "inline");
		d3.selectAll("circle.node").filter(function (d) { return d.type == "uri"}).style("display", "inline");
		d3.selectAll("rect.node").filter(function (d) { return d.type == "literal"}).style("display", "inline");
		d3.selectAll("text.nodetext").filter(function (d) { return d.type == "uri"}).style("display", "inline");
		d3.selectAll("text.literal").filter(function (d) { return d.type == "literal"}).style("display", "inline");
		debug = true;
	}	
});

d3.select('#render').on('click', function(){
	var queryParameters = parseURI(window.location.search);
	var filters = queryParameters.filters;
	if(filters == null)
		filters = "";
	else 
		filters += "|";
	$('input:checkbox[name="properties[]"]:checked').each(function(index) { 
		var value = $(this).val();
		queryParameters.nodes += "-" + value.split(".")[1];
		queryParameters.links += "-" + value;
		if($('input:text[name="'+value+'"]').val() != "") {
		//	filters += value+"."+$('input:text[name="'+value+'"]').val()+"-";
			filters += value+"."+$('select[name="filter-'+value+'"]').val()+"."+$('input:text[name="'+value+'"]').val()+"|";
		}		
	});
	filters = filters.substring(0, filters.length -1);
	reloadQueryParameters = {nodes: queryParameters.nodes, links:queryParameters.links, filters: filters, flexible: queryParameters.flexible};
	if(allQueryParams.task != null)
		reloadQueryParameters.task = allQueryParams.task;
	window.location = createURI(reloadQueryParameters);
});

d3.select('#flexible').on('click', function(){
	var flexible = 1;
	$('#flexible').is(':checked') ? flexible = 1 : flexible = 0;
	reloadQueryParameters = {nodes: null, flexible : flexible};
	if(allQueryParams.task != null)
		reloadQueryParameters.task = allQueryParams.task;
	window.location = createURI(reloadQueryParameters);
});

d3.select('#allSelect').on('click', function(){
	var allSelect = 1;
	$('#allSelect').is(':checked') ? allSelect = 1 : allSelect = 0;
	if(allSelect)
		$('input:checkbox[name="properties[]"]').prop('checked', true);
	else
		$('input:checkbox[name="properties[]"]').prop('checked', false);
});

d3.select("#properties").on('click', function(){
	if(preds){
		d3.selectAll("text.link").style("display", "none")	;
		preds = false;
	}else{
		if(debug)
			d3.selectAll("text.link").style("display", "inline")	;
		else 
			d3.selectAll("text.link").filter(function(d) { return d.value == 10}).style("display", "inline")	;
		preds = true;
	}	
});

$('#autocomplete').bind('keydown', function(e) {
    if(e.keyCode == 9 || e.keyCode == 13){
    	var queryParameters = parseURI(window.location.search);
    	if(queryParameters.nodes != null){
    		var input = $(this).val().split("-->");
    		var targetLink = $.grep(plotJSON.links, function(n){
    			return (n.name == input[1]);
    		});
    		queryParameters.nodes += "-" + targetLink[0].originalTarget;
    		if(queryParameters.links != null)
    			queryParameters.links += "-" + targetLink[0].originalSource + "." + targetLink[0].originalTarget;
    		else 
    			queryParameters.links = targetLink[0].originalSource + "." + targetLink[0].originalTarget;
    	} else{
    		var input = $(this).val();
    		var targetNode = $.grep(plotJSON.nodes, function(n){
    			return (n.name == input);
    		});
    		queryParameters.nodes = targetNode[0].mainNumber;
    	}
    	
    	var flexibleParam = (queryParameters.flexible != null) ? queryParameters.flexible : flexible;
    	reloadQueryParameters = {nodes: queryParameters.nodes, links: queryParameters.links, filters: queryParameters.filters, flexible: flexibleParam};
    	if(allQueryParams.task != null)
    		reloadQueryParameters.task = allQueryParams.task;
    	window.location = createURI(reloadQueryParameters);
    }
});

var $infoTabs = $( "#additionalTabs" ).tabs({
	tabTemplate: "<li><a href='#{href}'>#{label}</a> <span class='ui-icon ui-icon-close'>Remove Tab</span></li>",
    add: function(event, ui) {
    //	console.log(ui);
        $infoTabs.tabs('select', '#' + ui.panel.id);
    }
});

$( "#additionalTabs span.ui-icon-close" ).live( "click", function() {
	var index = $( "li", $infoTabs ).index( $( this ).parent() );
	$infoTabs.tabs( "remove", index );
});


d3.select('#resMetaSubmitBtn').on('click',  function(){
	console.log($("#resFormatMeta").val())
	if(readCookie('disposition') != null){
		var desiredName = '';
		if($("#resNameMeta").val() != 'Query Name:'){
			desiredName = $("#resNameMeta").val().replace(/[^\w\s]/gi, '');
		}
		var folderId = ($("#resfolderSelMeta").val() != null && $("#resfolderSelMeta").val() != '') ? $("#resfolderSelMeta").val() : allQueryParams.folderId;
		console.log(folderId);
		$('.queryResultsMetaData').hide();
		makeSparqlRequest(desiredName, $("#resFormatMeta").val(), folderId);
	}
	else
		alert("No Query Found");
});

d3.select('#resMetaSkipBtn').on('click',  function(){
	if(readCookie('disposition') != null){
		$('.queryResultsMetaData').hide();
		makeSparqlRequest('', 'TSV', allQueryParams.folderId);
	}
	else
		alert("No Query Found");
});

$( "#uiTabHolder" ).tabs();
$( "#uiTabHolder" ).resizable();
$('.splashScreenExplorer').hide();
var currentModelNoText = ((readCookie('useModel') == '') ? defaultID : readCookie('useModel'));
$('#currentModelNo').append(currentModelNoText);
//redrawGraphs();
