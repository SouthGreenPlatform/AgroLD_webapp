/**
 * First, make sure we do not draw the ...Label variables as column
 */
YASR.plugins.table.defaults.getColumns = function (yasr) {

    var vars = yasr.results.getVariables();
    yasr.results.getVariables().forEach(function (variable) {
        var labelIndex = vars.indexOf(variable + "Label");
        if (labelIndex >= 0) {
            //ah, a label equivalent for this variable exists. remove it
            vars.splice(labelIndex, 1);
        }
    });

    //we've filtered the list. Now convert array to one that is used as input for the datatables plugin 
    var cols = [];
    cols.push({"title": ""});//row numbers column
    vars.forEach(function (variable) {
        cols.push({"title": variable});
    });
    return cols;
};

/**
 * Now, extend the original 'drawCellContent' function to use the label instead of the URI as text
 */
YASR.plugins.table.defaults.drawCellContent = (function (originalDrawContentFunction) {
    return function extendDrawFunction(yasr, rowId, colId, bindings, sparqlVar, usedPrefixes) {
        var cellContent = originalDrawContentFunction(yasr, rowId, colId, bindings, sparqlVar, usedPrefixes);
        if (bindings[sparqlVar].type == "uri" && bindings[sparqlVar + "Label"]) {
            //ok, this is a URI, and we have a label equivalent. do the magic

            //create the new label (and append lang tag if there is one
            var newLabel = bindings[sparqlVar + "Label"].value;
            if (bindings[sparqlVar + "Label"]["xml:lang"])
                newLabel += "@" + bindings[sparqlVar + "Label"]["xml:lang"];

            //replace the cell content text with the new label (we keep the href link to the original uri)
            //also store the label as a data attribute (used for showing/hiding the label)
            cellContent = $(cellContent).attr('toggleLabel', newLabel).text(newLabel).prop('outerHTML');
        }
        return cellContent;
    }
})(YASR.plugins.table.defaults.drawCellContent);

/**
 * Use data attribute assigned in the previous step ('toggleLabel'), to make sure the original URI shows on hover
 */
YASR.plugins.table.defaults.callbacks.onCellMouseEnter = function (tdEl) {
    var toggleLabel = $(tdEl).find("[toggleLabel]");
    toggleLabel.text(toggleLabel.attr("href"));
};
YASR.plugins.table.defaults.callbacks.onCellMouseLeave = function (tdEl) {
    var toggleLabel = $(tdEl).find("[toggleLabel]");
    toggleLabel.text(toggleLabel.attr("toggleLabel"));
};

/**
 * Now, finally, initialize YASR, and fetch some example SPARQL results
 */
var yasr = YASR(document.getElementById("yasr"));
//$.get('results.json', function (sparqlJson) {
//while(sparqljson === ""){} // keep waiting  
//yasr.setResponse(sparqlJson);
//});