var extractedNodesArray = [];
var extractedNodesLocator = [];
var extractedLinksArray = [];
var extractedTopClasses = [];
var initClassesCounter = 0, initOPCounter = 0, initDPCounter = 0;
var namespaceSeparator = ""; // Due to the misleading namespaces


if (allQueryParams.type != null && readCookie('useModel') != '00000') {
    writeCookie('useModel', "00000", 1);
    storage.removeItem('dslModel');
    window.location = window.location.protocol + "//" + window.location.host + '/explorer' + window.location.search;
}

if (storage.getItem('dslModel') == null || storage.getItem('dslModel') == '') {
    if (storage.getItem('newHTMLModel') == null || storage.getItem('newHTMLModel') == '') {
        //jOWL.load("data/CancerChemopreventionOntology_light_hypo.owl", initjOWLNodes, {reason : true, locale : 'en' });
        //var defaultLoc = 'data/geography.owl';
        console.log(defaultLoc);
        jOWL.load(defaultLoc, initjOWLNodes, {reason: true, locale: 'en'});
    }
    //var x_v = "";
    else {
        console.log("here?");
        jOWL.parse(storage.getItem('newHTMLModel'), {reason: true, locale: 'en'});
        //	console.log(jOWL);
        initjOWLNodes();
    }

    $('.splashScreenExplorer').show();
    var initInterval = setInterval(function () {
        if (initClassesCounter && initOPCounter && initDPCounter) {
            $('.splashScreenExplorer').hide();
            clearInterval(initInterval);
            storage.removeItem('newHTMLModel');
            $('#topQueryElems').empty();
            for (i in extractedTopClasses) {
                var topClass = extractedTopClasses[i];
                $('#topQueryElems').append('<a href=javascript:loadTopClass(\"' + topClass + '\")>' +
                        '<h2 class="accordion">Concept - ' + topClass + '</h2></a>' +
                        '<div class="collapsible" id="' + topClass + '_accordion" style="display:none;"><h2><b>Main Concept</b> - ' + topClass + '</h2></div>');
            }
            var jsonOutput = {"nodes": extractedNodesArray, "links": extractedLinksArray};
            initSettings(jsonOutput);
            //	$('.selectQueryElems').show();
        }
    }, 3000);
} else {
    var initInterval = setInterval(function () {
        //	$('.splashScreenExplorer').hide();
        clearInterval(initInterval);
        redrawGraphs();
    }, 100)
}

///// -------------------------- Parsing OWL

function initjOWLNodes() {
    jOWL.SPARQL_DL("Class(?x)").execute({onComplete: function (results) {
            classArray = results.jOWLArray("?x");
            console.log(classArray);
            var count = 0;
            for (i in classArray.items) {
                //	if(i == 83)
                //		console.log(classArray.items[i]);
                if (classArray.items[i].isExternal)
                    continue;
                node = {"number": parseInt(count),
                    "mainNumber": parseInt(count),
                    "sourceNumber": parseInt(count),
                    "name": jOWL(classArray.items[i].URI).label(),
                    "uri": (classArray.items[i].URI.substring(0, 7) == "http://") ? classArray.items[i].URI : classArray.items[i].baseURI + namespaceSeparator + classArray.items[i].URI,
                    "description": (jOWL(classArray.items[i].URI).description().length > 0) ? jOWL(classArray.items[i].URI).description()[0] : "",
                    "type": "uri",
                    "radius": 1,
                    "group": 1
                };
                count++;
                extractedNodesArray.push(node);
                extractedNodesLocator[classArray.items[i].URI] = node.number;
                incrementRadius(classArray.items[i].URI);
            }
            //	console.log(extractedNodesArray);
            //	console.log(extractedNodesLocator);
            initjOWLSuperClasses(classArray);
            initjOWLObjectProperties();
            initjOWLDataTypeProperties();
        }});
}

function initjOWLSuperClasses(classArray) {
    for (i in classArray.items) {
        if (classArray.items[i].isExternal)
            continue;
        var superClass = jOWL(classArray.items[i].URI).parents().items;
        var childNumber = extractedNodesLocator[classArray.items[i].URI];
        /// ------ Change this to account for more than one superClasses (for j in superClass)
        if (superClass.length > 0) {
            if (superClass[0].isExternal) {
                extractedNodesArray[childNumber].superclass = "null";
                extractedTopClasses.push(classArray.items[i].URI); // Blunt Assumption to Effectively Group Classes
            } else {
                var number = extractedNodesLocator[superClass[0].URI];
                if (!number) {
                    console.log(superClass[0].URI);  ///---- Push Node to Array in the bottom
                }
                extractedNodesArray[childNumber].superclass = superClass[0].baseURI + namespaceSeparator + superClass[0].URI;
                extractedNodesArray[childNumber].superclassnumber = parseInt(number);
                link = {"number": extractedLinksArray.length, "name": "rdfs:subClassOf", source: childNumber, target: number, value: 1};
                extractedLinksArray.push(link);
            }
        }
    }
    initializeGroups();
}

function incrementRadius(classURI) {
    //console.log(classURI);
    jOWL.SPARQL_DL("SubClassOf(?sc, " + classURI + ")").execute({onComplete: function (results) {
            arr = results.jOWLArray("?sc");
            var number = extractedNodesLocator[classURI];
            extractedNodesArray[number].radius += arr.length;
        }});
}

function initializeGroups() {
    for (i in extractedTopClasses) {
        extractedNodesArray[extractedNodesLocator[extractedTopClasses[i]]].group = parseInt(i) + 1;
        penetrateGroups(extractedTopClasses[i], parseInt(i) + 1);
    }
    initClassesCounter = 1;
}

function penetrateGroups(classURI, groupID) {
    jOWL.SPARQL_DL("SubClassOf(?sc, " + classURI + ")").execute({onComplete: function (results) {
            subClassArr = results.jOWLArray("?sc");
            //////// tagny 
            //console.log(subClassArr);
            /////// tagny end
            for (i in subClassArr.items) {
                if (subClassArr.items[i].isExternal)
                    continue;
                var number = extractedNodesLocator[subClassArr.items[i].URI];                
                extractedNodesArray[number].group = parseInt(groupID);
            }
        }});
}

function initjOWLObjectProperties() {
    jOWL.SPARQL_DL("ObjectProperty(?x)").execute({onComplete: function (results) {
            objectextractedLinksArray = results.jOWLArray("?x");
            //	console.log(objectextractedLinksArray);
            for (i in objectextractedLinksArray.items) {
                if (objectextractedLinksArray.items[i].isExternal || !objectextractedLinksArray.items[i].domain || !objectextractedLinksArray.items[i].range)
                    continue;
                var allNodes = objectextractedLinksArray.items[i].jnode[0].childNodes;
                var domainNodes = [], rangeNodes = [];
                for (j in allNodes) {
                    if (allNodes[j].nodeName == "rdfs:domain") {
                        if (allNodes[j].attributes.length > 0) {
                            var domainValue = allNodes[j].attributes[0].nodeValue;
                            var domain = (domainValue.substring(0, 1) == "#") ? domainValue.substring(1, domainValue.length) : domainValue;
                            if (domain.substring(0, 7) == "http://") {
                                var domainFrags = domain.split(/[:#\/]/);
                                domain = domainFrags[domainFrags.length - 1];
                            }
                            domainNodes.push(domain);
                        }
                    }
                    if (allNodes[j].nodeName == "rdfs:range") {
                        if (allNodes[j].attributes.length > 0) {
                            var rangeValue = allNodes[j].attributes[0].nodeValue;
                            var range = (rangeValue.substring(0, 1) == "#") ? rangeValue.substring(1, rangeValue.length) : rangeValue;
                            if (range.substring(0, 7) == "http://") {
                                var rangeFrags = range.split(/[:#\/]/);
                                range = rangeFrags[rangeFrags.length - 1];
                            }
                            rangeNodes.push(range);
                        }
                    }
                }

                for (dn in domainNodes) {
                    for (rn in rangeNodes) {
                        var sourceNumber = extractedNodesLocator[domainNodes[dn]];
                        var targetNumber = extractedNodesLocator[rangeNodes[rn]];
                        if ((sourceNumber && targetNumber)) {
                            link = {"number": extractedLinksArray.length, "name": objectextractedLinksArray.items[i].baseURI + namespaceSeparator + objectextractedLinksArray.items[i].URI, "source": parseInt(sourceNumber),
                                "target": parseInt(targetNumber), "value": 1};
                            //		console.log(link.name + ":" + link.source + ":" + link.target);
                            extractedLinksArray.push(link);
                        }
                    }
                }
            }
            initOPCounter = 1;
        }});
}

function initjOWLDataTypeProperties() {
    jOWL.SPARQL_DL("DatatypeProperty(?x)").execute({onComplete: function (results) {
            dataextractedLinksArray = results.jOWLArray("?x");
            for (i in dataextractedLinksArray.items) {
                if (dataextractedLinksArray.items[i].isExternal || !dataextractedLinksArray.items[i].domain || !dataextractedLinksArray.items[i].range)
                    continue;
                var domainNodes = dataextractedLinksArray.items[i].jnode[0].childNodes;
                for (j in domainNodes) {
                    if (domainNodes[j].nodeName == "rdfs:domain") {
                        var domainValue = domainNodes[j].attributes[0].nodeValue;
                        var domain = (domainValue.substring(0, 1) == "#") ? domainValue.substring(1, domainValue.length) : domainValue;
                        if (domain.substring(0, 7) == "http://") {
                            var domainFrags = domain.split(/[:#\/]/);
                            domain = domainFrags[domainFrags.length - 1];
                        }
                        var sourceNumber = extractedNodesLocator[domain];
                        var targetNumber = extractedNodesLocator[dataextractedLinksArray.items[i].URI];
                        if (!targetNumber) {
                            var nodeLabel = jOWL(dataextractedLinksArray.items[i].URI).label();
                            var nodeComment = (jOWL(dataextractedLinksArray.items[i].URI).description().length > 0) ? jOWL(dataextractedLinksArray.items[i].URI).description() : "No Description Available";
                            node = {"number": extractedNodesArray.length, "mainNumber": extractedNodesArray.length, "sourceNumber": extractedNodesArray.length,
                                "name": nodeLabel, "uri": dataextractedLinksArray.items[i].range + "#" + dataextractedLinksArray.items[i].URI,
                                "type": "literal", "radius": 1, "group": 0, "description": nodeComment,
                                "superclass": "null"};
                            extractedNodesArray.push(node);
                            extractedNodesLocator[dataextractedLinksArray.items[i].URI] = extractedNodesArray.length - 1;
                            targetNumber = extractedNodesArray.length - 1;
                        }
                        if ((sourceNumber && targetNumber)) {
                            link = {"number": extractedLinksArray.length, "name": dataextractedLinksArray.items[i].baseURI + namespaceSeparator + dataextractedLinksArray.items[i].URI, "source": parseInt(sourceNumber),
                                "target": parseInt(targetNumber), "value": 1};
                            extractedLinksArray.push(link);
                        }
                    }
                }
            }
            initDPCounter = 1;
        }});
}

//// ------------------------------ Customizing against a baseLight Version Ontology

function loadTopClass(topClass) {
    $('.collapsible').hide();
//	console.log(extractedNodesLocator);
    var nodeLocation = extractedNodesLocator[topClass];
    $('#' + topClass + '_accordion').show();
    $('#' + topClass + '_accordion').empty();
    $('#' + topClass + '_accordion').append('<h3 class="sidebar">Child Concepts</h3>');
    $('#' + topClass + '_accordion').append('<table width="100%" class="extendedChild">');
    $('#' + topClass + '_accordion').append('<h3 class="sidebar">Relationships</h3>');
    $('#' + topClass + '_accordion').append('<table width="100%" class="extendedRelationships">');
    $('#' + topClass + '_accordion').append('<h3 class="sidebar">Literal Properties</h3>');
    $('#' + topClass + '_accordion').append('<table width="100%" class="extendedLiterals">');

    var subNodes = findSubNodes(topClass);
    var relationships = findProperties(topClass, "uri");
    var literals = findProperties(topClass, "literal");

    publishProperties('#' + topClass + '_accordion table.extendedRelationships', relationships, topClass, topClass);
    publishProperties('#' + topClass + '_accordion table.extendedLiterals', literals, topClass, topClass);

    for (i in subNodes) {
        var target = subNodes[i];
        $('#' + topClass + '_accordion table.extendedChild').append('<tr><td>' + target + '<td align="center"><input type="checkbox" checked name="extendedChild_' + target + '">');
        var subRelationships = findProperties(target, "uri");
        var subLiterals = findProperties(target, "literal");
        publishProperties('#' + topClass + '_accordion table.extendedRelationships', subRelationships, target, topClass);
        publishProperties('#' + topClass + '_accordion table.extendedLiterals', subLiterals, target, topClass);
    }
}

function publishProperties(div, properties, callingClass, topClass) {
    for (i in properties) {
        var keyParts = properties[i].split("-->");
        console.log(callingClass);
        if (keyParts.length > 1) {
            var linkParts = keyParts[0].split(/[:#\/]/);
            var key = callingClass + " --> " + linkParts[linkParts.length - 1] + " --> " + keyParts[1];
            $(div).append('<tr><td>' + key + '<td align="center"><input type="checkbox" checked name="extendedChild_' + topClass + "_" + linkParts[linkParts.length - 1] + '">')
        } else {
            var linkParts = keyParts[0].split(/[:#\/]/);
            $(div).append('<tr><td>' + callingClass + " --> " + linkParts[linkParts.length - 1] +
                    '<td align="center"><input type="checkbox" checked name="extendedChild_' + topClass + "_" + linkParts[linkParts.length - 1] + '">')
        }

    }
}

function findProperties(topClass, type) {
    var nodeLocation = extractedNodesLocator[topClass];
    var properties = [];
    for (i in extractedLinksArray) {
        if (extractedLinksArray[i].source == nodeLocation && extractedLinksArray[i].name != "rdfs:subClassOf") {
            var targetType = extractedNodesArray[extractedLinksArray[i].target].type;
            var target = extractedNodesArray[extractedLinksArray[i].target].name;
            if (targetType == type) {
                if (type == "uri")
                    properties.push(extractedLinksArray[i].name + " --> " + target);
                else
                    properties.push(extractedLinksArray[i].name);
            }
        }
    }
    return properties;
}

function findSubNodes(topClass) {
    var nodeLocation = extractedNodesLocator[topClass];
    var subNodesArray = [];
    for (i in extractedLinksArray) {
        if (extractedLinksArray[i].target == nodeLocation && extractedLinksArray[i].name == "rdfs:subClassOf") {
            var target = extractedNodesArray[extractedLinksArray[i].source].name;
            var subChildNodes = findSubNodes(target);
            subNodesArray.push(target);
            for (j in subChildNodes) {
                subNodesArray.push(subChildNodes[j]);
            }
        }
    }
    return subNodesArray;
}

function initSettings(jsonOutput) {
    storage.setItem('dslModel', JSON.stringify(jsonOutput));
    redrawGraphs();
}


////---------------- Invoke Setting Controls 

d3.select('#selQESubmitBtn').on('click', function () {
});

d3.select('#selQESkipBtn').on('click', function () {
    $('.selectQueryElems').hide();
    var jsonOutput = {"nodes": extractedNodesArray, "links": extractedLinksArray};
    initSettings(jsonOutput);
});
