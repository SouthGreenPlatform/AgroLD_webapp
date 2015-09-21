/* 
 * SPARQL QUERY PATTERNS
 */
var qpatterns = new Array();
var selectedPatternIdx = 0;
var patternlabels = ['Retrieve list of graphs', 'Search terms by label', 'List relation types in a given graph',
    'Retrieve the local neighbourhood of Oryza sativa japonica protein: <b>IAA16</b> - Auxin-responsive protein (UniProt accession:P0C127)',
    'Identify Wheat proteins that are involved in root development.',
    'Retrieve genes that participate in a given pathway: <b>Calvin cycle</b>', 'Retrieve Proteins associated with a given QTL: <b>DTHD</b> (days to heading)',
    'Get the ID corresponding to the ontology term "<b>homoaconitate hydratase activity</b>"', 'Get the name of the ontological element that has the ID "<b>GO:0003824</b>"',
    'Get the level <b>4</b> ancestor of <b>GO:0004409</b>', 'Get the level <b>2</b> descendence of <b>GO:0003824</b>',
    'Get protein ids associated with the ontological id <b>GO:0003824</b>', 'Get QTL ids associated with the ontological id <b>EO:0007403</b>',
    'Describe <b>uniprot:P0C127</b>'];
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
qpatterns['PREFIX obo:<http://purl.obolibrary.org/obo/>\n\
PREFIX taxon:<http://purl.obolibrary.org/obo/NCBITaxon_>\n\
PREFIX uniprot:<http://purl.uniprot.org/uniprot/>\n\
PREFIX vocab:<vocabulary/>\n\
PREFIX graph:<protein.annotations>\n\
\n\
SELECT distinct ?protein ?name ?evidence ?evidence_label ?evidence_code\n\
WHERE {\n\
 GRAPH graph: {\n\
    {\n\
     ?protein vocab:taxon taxon:4565.\n\
     ?protein rdfs:label ?name.\n\
      {     \n\
       ?protein ?p obo:GO_0048364.\n\
       ?protein vocab:has_annotation ?bp.\n\
       ?bp rdf:subject ?protein.\n\
       ?bp rdf:object obo:GO_0048364.\n\
       ?bp vocab:evidence_code ?evidence_code.\n\
       ?bp vocab:evidence ?evidence.       \n\
      } UNION {\n\
       ?protein ?p obo:GO_2000280.\n\
       ?bp rdf:subject ?protein.\n\
       ?protein vocab:has_annotation ?bp. \n\
       ?bp rdf:object obo:GO_2000280.\n\
       ?bp vocab:evidence_code ?evidence_code.\n\
       ?bp vocab:evidence ?evidence.\n\
      }\n\
    } UNION {\n\
     ?protein vocab:taxon taxon:4572.\n\
     ?protein rdfs:label ?name.\n\
      {     \n\
       ?protein ?p obo:GO_0048364.\n\
       ?protein vocab:has_annotation ?bp.\n\
       ?bp rdf:subject ?protein.\n\
       ?bp rdf:object obo:GO_0048364.\n\
       ?bp vocab:evidence_code ?evidence_code.\n\
       ?bp vocab:evidence ?evidence.       \n\
      } UNION {\n\
       ?protein ?p obo:GO_2000280.\n\
       ?protein vocab:has_annotation ?bp. \n\
       ?bp rdf:subject ?protein.\n\
       ?bp rdf:object obo:GO_2000280.\n\
       ?bp vocab:evidence_code ?evidence_code.\n\
       ?bp vocab:evidence ?evidence.               \n\
      }    \n\
    }\n\
 }\n\
    GRAPH ?g {\n\
    ?evidence rdfs:label ?evidence_label.\n\
\n\
    }\n\
}'] = [];
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
qpatterns['PREFIX owl: <http://www.w3.org/2002/07/owl#>\n\n\
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
qpatterns['SELECT ?OntoTerm\n\
WHERE { \n\
 {\n\
  SELECT ?subject\n\
  WHERE\n\
  {\n\
   ?subject rdfs:subClassOf ?o.\n\
   FILTER REGEX(STR(?subject), CONCAT(REPLACE("GO:0003824", ":", "_"), "$"))\n\
  }limit 1\n\
 }\n\
 ?subject rdfs:label ?OntoTerm .\n\
 ?subject a owl:Class .\n\
}  '] = ["GO:0003824"];
qpatterns['PREFIX owl: <http://www.w3.org/2002/07/owl#>\nSELECT ?ancestorId\n\
 WHERE \n\ {     \n\
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

qpatterns['PREFIX owl: <http://www.w3.org/2002/07/owl#>\nSELECT ?descendentId\n\
 WHERE \n\
 {     \n\
  { \n\
    SELECT ?descendent1 ?subject\n\
    WHERE\n\
    {\n\
    	?descendent1 rdfs:subClassOf ?subject. \n\
  		FILTER REGEX(STR(?subject), CONCAT(REPLACE("GO:0003824", ":", "_"), "$"))\n\
    }   \n\
  }\n\
  ?descendent2 rdfs:subClassOf ?descendent1.\n\
  ?descendent2 a owl:Class .\n\
   BIND(REPLACE(str(?descendent2), \'^.*(#|/)\', "") AS ?descendentLocalname)\n\
   BIND(REPLACE(?descendentLocalname, "_", ":") as ?descendentId)\n\
}'] = ["GO:0003824"];

qpatterns['SELECT DISTINCT ?proteinId\n\
WHERE\n\
{\n\
  { \n\
    SELECT ?ontoElt\n\
    WHERE\n\
    {\n\
    	?ontoElt rdfs:subClassOf ?ontoEltClass.\n\
  		FILTER REGEX(STR(?ontoElt), CONCAT(REPLACE("GO:0003824", ":", "_"), "$"))\n\
    } limit 1\n\
  }\n\
  ?protein ?predicate ?ontoElt .\n\
  ?protein rdfs:subClassOf <http://purl.obolibrary.org/obo/SO_0000104> .\n\
  BIND(REPLACE(str(?protein), \'^.*(#|/)\', "") AS ?proteinId) .\n\
}\n\
ORDER BY ?protein\n\
LIMIT 5 # page size > 0\n\
OFFSET 0 # page number >= 0'] = ["GO:0003824"];
qpatterns['SELECT DISTINCT ?qtlId\n\
WHERE\n\
{\n\
  { \n\
    SELECT ?ontoElt\n\
    WHERE\n\
    {\n\
    	?ontoElt rdfs:subClassOf ?ontoEltClass.\n\
  		FILTER REGEX(STR(?ontoElt), CONCAT(REPLACE("EO:0007403", ":", "_"), "$"))\n\
    } limit 1\n\
  }\n\
  GRAPH <qtl.annotations>{\n\
    ?qtl ?predicate ?ontoElt .\n\
    ?qtl rdfs:subClassOf <http://purl.obolibrary.org/obo/SO_0000771> .\n\
    BIND(REPLACE(str(?qtl), \'^.*(#|/)\', "") AS ?qtlId) .\n\
  }\n\
}\n\
ORDER BY ?qtl\n\
LIMIT 5 # page size > 0\n\
OFFSET 0 # page number >= 0'] = ["EO:0007403"];

qpatterns['PREFIX uniprot:<http://purl.uniprot.org/uniprot/>\n\
SELECT *\n\
WHERE{\n\
 GRAPH ?graph{\n\
 uniprot:P0C127 ?property ?object.\n\
 }\n\
}'] = ["uniprot:P0C127"];
prefixes = "BASE <http://www.southgreen.fr/agrold/>\n" +
        'PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n' +
        'PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n';


function patternChange() {
    selectPattern(selectedPatternIdx);
}

function selectPattern(patternIdx) {
    selectedPatternIdx = patternIdx;
    console.log("icizzz");
    var selectedpattern = Object.keys(qpatterns)[patternIdx];
    document.getElementById("query").value = prefixes + selectedpattern;
    yasqe.setValue(document.getElementById("query").value);
    //console.log(document.getElementById("query").value);

    document.getElementById("parameters").innerHTML = "";
    document.getElementById("parameters").innerHTML = "";
    if (qpatterns[selectedpattern].length > 0) {
        document.getElementById("parameters").innerHTML += "<b style=\"font-size: 15px\">Set values of parameters:</b><br>"
        for (i = 0; i < qpatterns[selectedpattern].length; i++) {
            document.getElementById("parameters").innerHTML += "Replace \"" + qpatterns[selectedpattern][i] + '" by : <input class="aparameter" value="' + qpatterns[selectedpattern][i] + '" oninput="replaceParaValue(' + "/" + qpatterns[selectedpattern][i] + "/g" + ', this)" /><br>';
        }
    } else {
        document.getElementById("parameters").innerHTML = "";
    }
}

function replaceParaValue(apara, anInput) {
    var pattern = Object.keys(qpatterns)[selectedPatternIdx];
    var qtext = pattern.replace(apara, anInput.value);
    document.getElementById("query").value = prefixes + qtext;
    yasqe.setValue(document.getElementById("query").value);
}
function resetForm() {
    //var x = document.getElementById("patterns").children[0];
    //x.setAttribute("selected", "selected");
    patternChange();
}