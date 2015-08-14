var selectedNodesArray = [];
var selectedLinksArray = [];
var mappedNodesArray = [];
var mappedLinksArray = [];
var federatedNodesArray = [];
var federatedLinksArray = [];
var newNo = 0;

function getSuperNodesArray(clickedNode, nodes) {
	var superNodesArray = [];
	superNodesArray.push(clickedNode.mainNumber);
	var superNodeUri = clickedNode.superclass;
    var superNodeNo = clickedNode.superclassnumber;
    
	while(superNodeUri != "null"){
  	  superNodesArray.push(superNodeNo);
  	  var superNode = nodes[superNodeNo];
  	  superNodeNo = superNode.superclassnumber;
  	  superNodeUri = superNode.superclass;
    }
	return superNodesArray;
}

function redrawGraphs(){
	sparqlQueryNodes = [], sparqlQueryLinks = [], queryLinks = [], queryNodes = [], queryFilters = [];
	selectedLinksArray = [], selectedNodesArray = [];
	nodes = [];
	links = [];
	vis.selectAll("g").remove();
	vis.selectAll("line").remove();
	vis.empty();	
	buildVisualization();
}

function buildVisualization(){
	queryParameters = parseURI(window.location.search);
	var useCase = 0;
	var file;
	if (queryParameters.flexible == 0) {
		flexible = 0;
		file = "data/restoutputall1.json";
		useCase = 1;
	} else {
		if(storage.getItem('dslModel') != null && storage.getItem('dslModel') != '') {
			file = storage.getItem('dslModel');
			useCase = 0;
		} else {
			file = "data/miserables.json"; 
			useCase = 1;
		}
	}
		
	
	switch (useCase) {
	case 0 :
		var json = JSON.parse(storage.getItem('dslModel'));
		if (storage) {
			storage.setItem('jsonOutput', JSON.stringify(json.nodes));
		} else {
			writeCookie('jsonOutput', JSON.stringify(json.nodes), 1);
		}
		
		var allClasses = $.map(json.nodes, function(n) {
			if(n.type == "uri")
				return n;
		});
		
		filteredJSON = restart(json, queryParameters.nodes, queryParameters.links, queryParameters.filters);
		buildUIElements(filteredJSON.nodes);
		d3.select("#waiting").style("display", "none");
		init(filteredJSON);		
		break;
	case 1:
		d3.json(file, function(json){
			if (storage) {
				storage.setItem('jsonOutput', JSON.stringify(json.nodes));
			} else {
				writeCookie('jsonOutput', JSON.stringify(json.nodes), 1);
			}
			
			var allClasses = $.map(json.nodes, function(n) {
				if(n.type == "uri")
					return n;
			});
			
			filteredJSON = restart(json, queryParameters.nodes, queryParameters.links, queryParameters.filters);
			buildUIElements(filteredJSON.nodes);
			d3.select("#waiting").style("display", "none");
			init(filteredJSON);		
		});
		break;
	case 2 : 
		d3.json("/api/roadmap/topics?format=json", function(json){
			filteredJSON = restartUsingREST(json, queryParameters.nodes, queryParameters.links, queryParameters.filters);
			buildUIElements(filteredJSON.nodes);
			d3.select("#waiting").style("display", "none");
			init(filteredJSON);		
		}); 
		break;
	}
}

function redrawGraphsByParameter(url){
	alert("here");
	sparqlQueryNodes = [], sparqlQueryLinks = [], queryLinks = [], queryNodes = [];
	nodes = [];
	links = [];
	vis.selectAll("g").remove();
	vis.selectAll("line").remove();
	vis.empty();
	queryParameters = parseURI(url);
	d3.json("res/miserables.json", function(json){
		filteredJSON = restart(json, queryParameters.nodes, queryParameters.links, queryParameters.filters);
		d3.select("#waiting").style("display", "none");
		init(filteredJSON);		
	});
}

function createMappedNode(number, sourceNode, group){
	nameFrags = sourceNode.name.split(/[:#\/]/);
	var nodeName = nameFrags[nameFrags.length-1].replace(/ /gi,'_');
	mappedNode = {number: number, sourceNumber: sourceNode.mainNumber, name: "?x"+number+'_'+nodeName, instanceOf: sourceNode.name, uri: sourceNode.uri, type: "b" + sourceNode.type, group: group, radius: 5, superclass: "null"};
	return mappedNode;
}

function createNode(number, type, sourceNumber, sourceNode){
	node = {number: number, mainNumber: sourceNode.mainNumber, sourceNumber: sourceNumber, name: sourceNode.name, group: sourceNode.group, uri: sourceNode.uri, description: sourceNode.description, type: type, radius: sourceNode.radius, superclass: sourceNode.superclass, superclassnumber: sourceNode.superclassnumber};
	return node;
}

function createLink(name,source,target,value,originalSource,originalTarget){
	mappedLink = {name : name, source: source, target: target, value: value, originalSource: originalSource, originalTarget: originalTarget};
	selectedLinksArray.push(mappedLink);
	return mappedLink;
}

function restart(json, queryNodeParameter, queryLinkParameter, queryFilterParameter){	
	if(storage.getItem('extendedModel') != null || readCookie('extendedModel') != null){
		var extendedModelJSON = (storage.getItem('extendedModel') != null) ? storage.getItem('extendedModel') : readCookie('extendedModel');
		if(extendedModelJSON != ''){
			var extendedModelJSONobj = JSON.parse(extendedModelJSON);
			json.nodes = $.merge(json.nodes,extendedModelJSONobj.nodes);
			json.links = $.merge(json.links,extendedModelJSONobj.links);
		}
	}
	if(queryNodeParameter == null)
		return json;
	else {
		var queryNodes = queryNodeParameter.split("-");
		if(queryLinkParameter != null)
			queryLinks = queryLinkParameter.split("-");
		if(queryFilterParameter != null)
			queryFilters = queryFilterParameter.split("|");
		
		mappedNodesArray = $.map(queryNodes, function(n){
			if(json.nodes[n].type == "uri")
				return createMappedNode(newNo++, json.nodes[n], 100);
			else 
				return null;
		});
		
		mappedLiteralsArray = $.map(queryNodes, function(n){
			if(json.nodes[n].type == "literal")
				return createMappedNode(newNo++, json.nodes[n], 100);
			else 
				return null;
		});
		
		mappedNodesArray = $.merge(mappedNodesArray, mappedLiteralsArray);
		
		sparqlQueryNodes = $.map(queryNodes, function(n){
			return n;
		});
		
		sparqlQueryLinks = $.map(queryLinks, function(n){
			return n;
		});
		
		selectedNodesArray = $.map(queryNodes, function(n){
			if(json.nodes[n].type == "uri")
				return createNode(newNo++, "buri", json.nodes[n].mainNumber, json.nodes[n]);
			else
				return null;
		});
		
		var debugNodesArray = [];
		for(i in selectedNodesArray){
			var mappedLink = createLink("rdf:type", mappedNodesArray[i].number, selectedNodesArray[i].number, 10, mappedNodesArray[i].number, selectedNodesArray[i].mainNumber);
			
			var superNodesArray = getSuperNodesArray(selectedNodesArray[i],json.nodes);
			var debugLinks = $.grep(json.links, function(n){
				return (($.inArray(n.source, superNodesArray) > -1 && n.name != "rdfs:subClassOf") || 
						(n.target == selectedNodesArray[i].mainNumber && n.name == "rdfs:subClassOf") || 
						($.inArray(n.target,superNodesArray) > 0 && n.name != "rdfs:subClassOf"));
			});
			
			var debugNodes = $.map(debugLinks, function(n){
				if($.inArray(n.source, superNodesArray) > -1)
					return createNode(newNo++, json.nodes[n.target].type, selectedNodesArray[i].mainNumber, json.nodes[n.target]);
				else if (n.name == "rdfs:subClassOf")
					return createNode(newNo++, json.nodes[n.source].type, selectedNodesArray[i].mainNumber, json.nodes[n.source]);
				else 
					return {number: newNo++};
			});
			
			for(j in debugLinks){
				if($.inArray(debugLinks[j].source, superNodesArray) > -1){					
					var linkIdentifier = selectedNodesArray[i].mainNumber + "."+ debugNodes[j].mainNumber;
					if($.inArray(linkIdentifier,queryLinks) > -1){
						var targetMappedNode = $.grep(mappedNodesArray, function(n, int){
							return (n.sourceNumber == debugLinks[j].target && int!=i);
						});
						if(targetMappedNode.length > 0){
							for (iter in targetMappedNode){
								var match = false;
								for(k in selectedLinksArray){
									if(selectedLinksArray[k].value == 10 && selectedLinksArray[k].target == targetMappedNode[iter].number) {
										match = true;
										break;
									}
								}
								if(!match){
									var mappedLink = createLink(debugLinks[j].name, mappedNodesArray[i].number, targetMappedNode[iter].number, 10, selectedNodesArray[i].mainNumber, debugLinks[j].target);
									break;
								}
							}	
						} 
						for(f in queryFilters){
						//	alert(queryFilters[f]);
							var queryFilterParts = queryFilters[f].split(".");
							if(linkIdentifier == queryFilterParts[0] + "." + queryFilterParts[1]){
								targetMappedNode[iter].filter = queryFilterParts[3];
								targetMappedNode[iter].filterType = queryFilterParts[2];
								targetMappedNode[iter].group = 1;
							} 
						} 
					}
					var link = createLink(debugLinks[j].name, selectedNodesArray[i].number, debugNodes[j].number, 1, selectedNodesArray[i].mainNumber, debugLinks[j].target);		
				} else if (debugLinks[j].name == "rdfs:subClassOf"){
					var linkIdentifier = debugLinks[j].target + "."+ debugLinks[j].source;
					var link = createLink(debugLinks[j].name, debugNodes[j].number, selectedNodesArray[i].number, 1, debugLinks[j].source, debugLinks[j].target);
					
					if($.inArray(linkIdentifier, queryLinks) > -1){
						queryNodeParameter = queryNodeParameter.replace("-" + debugLinks[j].source, "");
						queryNodeParameter = queryNodeParameter.replace(debugLinks[j].target,debugLinks[j].source);
						queryLinkParameter = queryLinkParameter.replace("-"+linkIdentifier,"");
						var re = new RegExp(debugLinks[j].target, "g");
						queryLinkParameter = queryLinkParameter.replace(re , debugLinks[j].source);
						reloadQueryParameters = {nodes: queryNodeParameter, links:queryLinkParameter, filters: queryFilterParameter, flexible: allQueryParams.flexible};
						if(allQueryParams.task != null)
				    		reloadQueryParameters.task = allQueryParams.task;
						window.location = createURI(reloadQueryParameters);
					}					
				} else {
					var linkIdentifier = debugLinks[j].source + "." + selectedNodesArray[i].mainNumber;
					if($.inArray(linkIdentifier, queryLinks) > -1){
						var sourceMappedNode = $.grep(mappedNodesArray, function(n, int){
							return (n.sourceNumber == debugLinks[j].source && int!=i);
						});
						var mappedLink = createLink(debugLinks[j].name, sourceMappedNode[0].number, mappedNodesArray[i].number, 10, debugLinks[j].source, debugLinks[j].target);
						
					}
				}
			}
			debugNodesArray = $.merge(debugNodesArray,debugNodes);
		}
		
		
		selectedNodesArray = $.merge(mappedNodesArray, selectedNodesArray);
		selectedNodesArray = $.merge(selectedNodesArray, debugNodesArray);
		selectedNodesArray = $.merge(selectedNodesArray, federatedNodesArray);
		selectedLinksArray = $.merge(selectedLinksArray, federatedLinksArray);
		
		selectedJS = {nodes: selectedNodesArray, links: selectedLinksArray};
	
	    return selectedJS;
	}
}
