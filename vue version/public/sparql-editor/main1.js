/*<![CDATA[*/
var lastformat = 0;
function format_select(query_obg) {
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
function format_change(e) {
    var format = e.value;
    var cxml = document.getElementById("cxml");
    if (!cxml)
        return;
    if ((format.match(/\bCXML\b/i))) {
        cxml.style.display = "block";
    } else {
        cxml.style.display = "none";
    }
}
function savedav_change(e) {
    var savefs = document.getElementById("savefs");
    if (!savefs)
        return;
    if (e.checked) {
        savefs.style.display = "block";
    } else {
        savefs.style.display = "none";
    }
}
function sparql_endpoint_init() {
    var cxml = document.getElementById("cxml");
    if (cxml)
        cxml.style.display = "none";
    var savefs = document.getElementById("savefs");
    if (savefs)
        savefs.style.display = "none";
}
/*]]>*/