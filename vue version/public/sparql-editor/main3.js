$(document).ready(function () {

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
                endpoint: SPAR_QL_ENDPOINT_URL,
                collapsePrefixesOnLoad: false,
                //persistent: true,
                args: [{ name: 'timeout', value: document.getElementById('timeout').value }],
                callbacks: {
                    beforeSend: function (data) {
                        yasqe.options.sparql.args[0].value = document.getElementById('timeout').value;
                        //console.log(yasqe.options.sparql.args[0].value);
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
    // add a plugin to draw response as graph with d3sparql
    YASR.registerOutput("graph", GraphInYASR);

    var yasr = YASR(document.getElementById("yasr"), {
        // List of enabled output plugins. The order of these plugins specifies the order of the output buttons as well
        outputPlugins: ["error", "boolean", "table", "rawResponse", "pivot", "graph"],
        //this way, the URLs in the results are prettified using the defined prefixes in the query
        getUsedPrefixes: yasqe.getPrefixesFromQuery,
        useGoogleCharts: false,
        //output: "Graph",
        //drawDownloadIcon: false,
        persistency: {
            prefix: false
        }
    });
    //link both together (YasQUE and YASR)
    yasqe.options.sparql.callbacks.complete = yasr.setResponse;
    $(document).ready(function () {
        // Handler for .ready() called.
        // add introJs attribute
        // data-step="2" data-intro="Edit it here!"
        //$("#cmd-container").insertAfter(".yasqe");
        //$("#cmd-container").insertAfter(".yasqe_buttons");
        $(".yasqe_queryButton").attr('data-step', '3');
        $(".yasqe_queryButton").attr('data-intro', 'Run and then ...');
        // show keyboard commands
        $("#cmd-container").hover(
            function () {
                $("ul#cmds").css("display", "");
                $(this).css("width", "55%");
            }, function () {
                $("ul#cmds").css("display", "none");
                $(this).css("width", "22%");
            });
        $(".fullscreenToggleBtns").click(
            function () {
                /*if($(".CodeMirror").hasClass("CodeMirror-fullscreen")){
                 $("#cmd-container").
                 }*/
                $("#cmd-container").toggle();
                //$("#cmd-container").insertAfter(".yasqe_buttons");
            }
        );
        yasqe.options.extraKeys.F11 = function (yasqe) {
            yasqe.setOption("fullScreen", !yasqe.getOption("fullScreen"));
            $("#cmd-container").toggle();
        };
        yasqe.options.extraKeys.Esc = function (yasqe) {
            if (yasqe.getOption("fullScreen")) {
                yasqe.setOption("fullScreen", false);
                $("#cmd-container").toggle();
            }
        };
    });
    //console.log(yasqe.getValue());

    function saveTextAsFile(fileNameToSaveAs) {
        var textToWrite = yasqe.getValue();
        var textFileAsBlob = new Blob([textToWrite], { type: 'text/plain' });

        var downloadLink = document.createElement("a");
        downloadLink.download = fileNameToSaveAs;
        downloadLink.innerHTML = "Download File";
        if (window.webkitURL != null) {
            // Chrome allows the link to be clicked
            // without actually adding it to the DOM.
            downloadLink.href = window.webkitURL.createObjectURL(textFileAsBlob);
        } else {
            // Firefox requires the link to be added to the DOM
            // before it can be clicked.
            downloadLink.href = window.URL.createObjectURL(textFileAsBlob);
            downloadLink.onclick = destroyClickedElement;
            downloadLink.style.display = "none";
            document.body.appendChild(downloadLink);
        }

        downloadLink.click();
    }

    function destroyClickedElement(event) {
        document.body.removeChild(event.target);
    }

    function loadFileAsText(fileToLoad) {
        //var fileToLoad = document.getElementById("fileToLoad").files[0];

        var fileReader = new FileReader();
        fileReader.onload = function (fileLoadedEvent) {
            var textFromFileLoaded = fileLoadedEvent.target.result;
            //textarea = document.getElementById("inputTextToSave");
            yasqe.setValue(textFromFileLoaded);
        };
        fileReader.readAsText(fileToLoad, "UTF-8");
    }
    $('.yasqe_queryButton.query_valid').click(function () {
        const request = yasqe.getValue().trim();
        console.log(request);
        const history = JSON.parse(
            localStorageGet("sparqlEditor.history") ?? "[]"
        );

        history.unshift(request);

        localStorageSet(
            "sparqlEditor.history",
            JSON.stringify(history)
        );
        saveRequest(request);

    });
    function saveRequest(r) {
        $.ajax({
            type: 'post',
            url: 'ToolHistory',
            data: {
                p: '{m:"setSparqlEditor"}',
                request: encodeURIComponent(r)
            },
            success: function (data) {
                $('.success').html(data);
            }, error: function (data) {
                $('.debugme').html(data);
            }
        });
    }

});