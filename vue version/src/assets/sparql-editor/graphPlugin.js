/* 
 * graphPlugin.js
 * Specifies the YASR plugin to display SPARQL result as a visual graph
 * 
 * @author: tagny
 * 
 */

var GraphInYASR = function (yasr) {
    /*
     * d3sparql drawing
     * @returns {undefined}
     */
    // ############## beginning d3sparql functions ################
    function exec() {
        var endpoint = SPARQLENDPOINTURL;
        var sparql = yasqe.getValue();
        d3sparql.query(endpoint, sparql, render);
    }
    function render(json) {
        var config = {
            "radius": 10,
            "charge": -500,
            "distance": 100,
            "width": 1000,
            "height": 750,
            "selector": ".yasr_results"
        };
        try {
            d3sparql.forcegraph(json, config);
        } catch (e) {
            $(config["selector"]).html('<p style="color:red">Error: sorry, D3Sparql is unable to build the graph!</p><br/>');
            console.error("D3SPARQL Error: " + e);
        }
    }
// ############## end d3sparql functions ################

    /*
     * Draw the results. Use the yasr object to retrieve the SPARQL response
     * @returns {undefined}
     */
    var draw = function () {
        //exec();
        render(JSON.parse(yasr.results.getOriginalResponseAsString()));
    };
    /*
     * Return whether this plugin is able to draw the current results
     */
    var canHandleResults = function () { // as defined for the plugin "Table"
        return yasr.results &&
                yasr.results.getVariables &&
                yasr.results.getVariables() &&
                yasr.results.getVariables().length > 0;
    };
    var getDownloadInfo = function () {
        if (!yasr.results)
            return null;
        var contentType = "image/png";
        var type = "png";
        return {
            getContent: function () {
                var node = document.getElementsByClassName('d3sparql')[0];
                return domtoimage.toPng(node).then(function (dataUrl) {
                    var img = new Image();
                    img.src = dataUrl;
                    img.id = "gaphinpng";
                    //document.body.appendChild(img);
                    return dataUrl.replace("image/png", "image/stream");
                });
            },
            filename: "queryResults" + (type ? "." + type : ""),
            contentType: contentType ? contentType : "text/plain",
            buttonTitle: "Download response"
        };
    };
    /*
     * Return the configurationObject
     */
    return {
        draw: draw,
        name: "Graph",
        canHandleResults: canHandleResults,
        //getDownloadInfo: getDownloadInfo,
        getPriority: 7 // The priority of a plugin is used to select the output plugin when the currently selected plugin is not able to draw the results.
    };
};