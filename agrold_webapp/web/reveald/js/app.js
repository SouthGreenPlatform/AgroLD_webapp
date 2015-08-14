jQuery(function($) {
  window.ReclineDataExplorer = new ExplorerApp({
    el: $('.recline-app')
  })
});

var retrievedURIs = [] ;

var ExplorerApp = Backbone.View.extend({
  events: {
    'click .nav .js-load-dialog-url': '_onLoadURLDialog',
    'click .nav .js-sample-queries-dialog': '_onLoadSampleQueries',
    'click .js-saved-queries-dialog': '_onLoadSavedQueries',
    'click .nav .js-tool-usage-dialog': '_onLoadToolUsage',
    'click .nav .js-tool-about-dialog': '_onLoadAboutReVeaLD',
    'click .nav .js-reveald-demo-link': '_onLoadDemoReVeaLD',
    'click .resultsBtn': '_onCreateExplorer',
    'submit form.js-load-url': '_onLoadURL',
    'submit .js-load-dialog-file form': '_onLoadFile',
    'submit .js-settings form': '_onSettingsSave'
  },

  _onCreateExplorer: function() {
	  var queryDisplayValue = $('textarea[name="query"]').val() + " LIMIT " + $('#limitResults').val();
	  /////// tagny
	  console.log(queryDisplayValue);
	  console.log(queryDisplayValue);
	  /////// tagny end
	  var outputSelection = $("#QueryResultsOutputOpt").val();
	  if (outputSelection == 'display') {
		  var queryMap = { 'query': queryDisplayValue, 'endpoint': uriStr};
	   	  var self = this;
		  $('.splashScreenExplorer').show();
		  $.post("makeRequest.php", queryMap, function(data){
			// small function force converting sparql-xml to json (should be done on the server side)
			// this is not required when the SPARQL Engine exposes ReVeaLD as a service (should use AJAX call below)
			var jsonData = convertToJson(data);
			// normal flow
			$('.page-home').hide();
	   	  //  	$('.page-visualizer').hide();
	   	    	$('.splashScreenExplorer').hide();
	   	    	$('.page-explorer').show();  	
			
	   	    	myRecords = loadDataSet(JSON.stringify(jsonData));
	   	    	var dataset = new recline.Model.Dataset({
	   	    		records: myRecords
	   	    	});
	   	    //	return dataset;
	   	    	self.createExplorer(dataset);
	   	    	replaceSemantics();			
	          }).fail(function() {
		   	$('.splashScreenExplorer').hide();
			myRecords = [];
	   	    	var dataset = new recline.Model.Dataset({
	   	    		records: myRecords
	   	    	});
	   	    //	return dataset;
	   	    	self.createExplorer(dataset);
		  });  
		// To be used if direct integration with the SPARQL Engine
	   	 /* $.ajax({
	   	    type : "POST",
	   	    url : "makeRequest.php",
	   	    data : queryMap,
	   	    dataType : "json",
	   	    beforeSend : function(data){
	   	    	$('.splashScreenExplorer').show();
	   	    },
	   	    success : function(data){
	   	    	$('.page-home').hide();
	   	  //  	$('.page-visualizer').hide();
	   	    	$('.splashScreenExplorer').hide();
	   	    	$('.page-explorer').show();  	
			alert(data);
	   	    	myRecords = loadDataSet(JSON.stringify(data));
	   	    	var dataset = new recline.Model.Dataset({
	   	    		records: myRecords
	   	    	});
	   	    //	return dataset;
	   	    	self.createExplorer(dataset);
	   	    	replaceSemantics();
	   	    },
	   	    error : function(xhr, status, error) {
				$('.splashScreenExplorer').hide();;
				myRecords = [];
	   	    	var dataset = new recline.Model.Dataset({
	   	    		records: myRecords
	   	    	});
	   	    //	return dataset;
	   	    	self.createExplorer(dataset);
			}
	   	  });*/
	  } else {
		  $('.queryResultsMetaData').show();
		  writeCookie('disposition', outputSelection, 1);
		  if(outputSelection == "download")
			  $("#resFormatMeta option[value='SMI']").remove();
		  if(outputSelection == "save") {
			  renderFolderList();
		  }
		//  writeCookie('query', queryDisplay.value, 1);
	  }
  },
  
  initialize: function() {
    this.el = $(this.el);
    this.dataExplorer = null;
    this.explorerDiv = $('.data-explorer-here');
    _.bindAll(this, 'viewExplorer', 'viewHome');

    this.router = new Backbone.Router();
    this.router.route('', 'home', this.viewHome);
    this.router.route(/explorer/, 'explorer', this.viewExplorer);
    Backbone.history.start();

    var state = recline.View.parseQueryString(decodeURIComponent(window.location.search));
    if (state) {
      _.each(state, function(value, key) {
        try {
          value = JSON.parse(value);
        } catch(e) {}
        state[key] = value;
      });
      if (state.embed) {
        $('.navbar').hide();
        $('body').attr('style', 'padding-top: 0px');
      }
    }
  
    this._initializeSettings();
  },

  viewHome: function() {
    this.switchView('home');
  },

  viewExplorer: function() {
    this.router.navigate('explorer');
    this.switchView('explorer');
  },

  switchView: function(path) {
    $('.backbone-page').hide(); 
    var cssClass = path.replace('/', '-');
    $('.page-' + cssClass).show();
  },


  // make Explorer creation / initialization in a function so we can call it
  // again and again
  createExplorer: function(dataset, state) {
    var self = this;
    // remove existing data explorer view
    var reload = false;
    if (this.dataExplorer) {
      this.dataExplorer.remove();
      reload = true;
    }
    this.dataExplorer = null;
    var $el = $('<div />');
    $el.appendTo(this.explorerDiv);
    var views = [
       {
         id: 'grid',
         label: 'Grid', 
         view: new recline.View.Grid({
           model: dataset
         })
       }
    ];

    this.dataExplorer = new recline.View.MultiView({
      model: dataset,
      el: $el,
      state: state,
      views: views
    });
    this._setupPermaLink(this.dataExplorer);
    this._setupEmbed(this.dataExplorer);

    this.viewExplorer();
  },

  _setupPermaLink: function(explorer) {
    var self = this;
    var $viewLink = this.el.find('.js-share-and-embed-dialog .view-link');
    explorer.state.bind('change', function() {
      $viewLink.val(self.makePermaLink(explorer.state));
    });
    $viewLink.val(self.makePermaLink(explorer.state));
  },

  _setupEmbed: function(explorer) {
    var self = this;
    var $embedLink = this.el.find('.js-share-and-embed-dialog .view-embed');
    function makeEmbedLink(state) {
      var link = self.makePermaLink(state);
      link = link + '&amp;embed=true';
      var out = Mustache.render('<iframe src="{{link}}" width="100%" min-height="500px;"></iframe>', {link: link});
      return out;
    }
    explorer.state.bind('change', function() {
      $embedLink.val(makeEmbedLink(explorer.state));
    });
    $embedLink.val(makeEmbedLink(explorer.state));
  },

  makePermaLink: function(state) {
    var qs = recline.View.composeQueryString(state.toJSON());
    return window.location.protocol + "//" + window.location.host + window.location.pathname + qs;
  },

  // setup the loader menu in top bar
  setupLoader: function(callback) {
    // pre-populate webstore load form with an example url
    var demoUrl = 'http://thedatahub.org/api/data/b9aae52b-b082-4159-b46f-7bb9c158d013';
    $('form.js-load-url input[name="source"]').val(demoUrl);
  },

  _onLoadURLDialog: function(e) {
    e.preventDefault();
    var $link = $(e.target);
    var $modal = $('.modal.js-load-dialog-url');
    $modal.find('h3').text($link.text());
    $modal.modal('show');
    $modal.find('input[name="source"]').val('');
    $modal.find('input[name="backend_type"]').val($link.attr('data-type'));
    $modal.find('.help-block').text($link.attr('data-help'));
  },
  
  _onLoadSampleQueries: function(e) {
	 var $modal = $('.modal.js-sample-queries');
	 $modal.modal('show');
  },

  _onLoadDemoReVeaLD: function(e) {
	  var $modal = $('.modal.js-reveald-demo');
	  $modal.modal('show');
  },
  
  _onLoadSavedQueries: function(e) {
	  var queryAPI = window.location.protocol + "//" + window.location.host + '/query';
	  $.ajax({
		    url: queryAPI,
		    type: 'GET',
		    dataType : "json",
		    beforeSend : function(data){
	 	    	$('.splashScreenExplorer').show();
	 	    },
	  }).done(function(res){
			$('#savedQueryList').empty();
			for(i in res.query){
				$('#savedQueryList').append('<a href=javascript:viewSavedQuery(\"query-'+res.query[i].resourceID+'\")>'+
						'<h2 class="accordion">'+res.query[i].name+'</h2></a>'+
						'<div class="collapsibleQuery" id="query-'+res.query[i].resourceID+'" style="display:none;">'+
						res.query[i].description + '<br>'+
						'<button onclick="loadQuery(\''+res.query[i].resourceID+'\')" id="loadQuery" class="loadQueryBtn btn btn-primary" value = "Load Query">Load Query</button><br></div>');
			}
			var $modal = $('.modal.js-saved-queries');
			$modal.modal('show');
			$('.splashScreenExplorer').hide();
	  });
	  
  },
  
  _onLoadToolUsage: function(e) {
	  var $modal = $('.modal.js-tool-usage');
	  $modal.modal('show');
  },
  
  _onLoadAboutReVeaLD: function(e) {
	  var $modal = $('.modal.js-about-reveald');
	  $modal.modal('show');
  },
	  
  _onLoadURL: function(e) {
    e.preventDefault();
    $('.modal.js-load-dialog-url').modal('hide');
    var $form = $(e.target);
    var source = $form.find('input[name="source"]').val();
    var datasetInfo = {
      id: 'my-dataset',
      url: source
    };
    var type = $form.find('input[name="backend_type"]').val();
    if (type === 'csv' || type === 'excel') {
      datasetInfo.format = type;
      type = 'dataproxy';
    }
    if (type === 'datahub') {
      // have a full resource url so convert to data API
      if (source.indexOf('dataset') != -1) {
        var parts = source.split('/');
        datasetInfo.url = parts[0] + '/' + parts[1] + '/' + parts[2] + '/api/data/' + parts[parts.length-1];
      }
      type = 'elasticsearch';
    }
    datasetInfo.backend = type;
    var dataset = new recline.Model.Dataset(datasetInfo);
    this.createExplorer(dataset);
  },

  _onLoadFile: function(e) {
    var self = this;
    e.preventDefault();
    var $form = $(e.target);
    $('.modal.js-load-dialog-file').modal('hide');
    var $file = $form.find('input[type="file"]')[0];
    var dataset = new recline.Model.Dataset({
      file: $file.files[0],
      separator : $form.find('input[name="separator"]').val(),
      delimiter : $form.find('input[name="delimiter"]').val(),
      encoding : $form.find('input[name="encoding"]').val(),
      backend: 'csv'
    });
    dataset.fetch().done(function() {
      self.createExplorer(dataset)
    });
  },

  _getSettings: function() {
    var settings = localStorage.getItem('dataexplorer.settings');
    settings = JSON.parse(settings) || {};
    return settings;
  },

  _initializeSettings: function() {
    var settings = this._getSettings();
    $('.modal.js-settings form input[name="datahub_api_key"]').val(settings.datahubApiKey);
  },

  _onSettingsSave: function(e) {
    var self = this;
    e.preventDefault();
    var $form = $(e.target);
    $('.modal.js-settings').modal('hide');
    var datahubKey = $form.find('input[name="datahub_api_key"]').val();
    var settings = this._getSettings();
    settings.datahubApiKey = datahubKey;
    localStorage.setItem('dataexplorer.settings', JSON.stringify(settings));
  }
});

function storeFile (content, fileName, folderId) {
	var queryAPI = window.location.protocol + "//" + window.location.host + '/explorer/saveSmiles';
	var queryMap = {
			'dataset' : content,
			'name' : fileName,
			'desc' : 'Saved SMILES File',
			'folderid' : folderId
		}
	  $.ajax({
		    url: queryAPI,
		    data: queryMap,
		    type: 'POST',
		    dataType : "json",
		    beforeSend : function(data){
	 	    	$('.splashScreenExplorer').show();
	 	    },
	  }).done(function(res, textStatus, jqXHR){
			$('.splashScreenExplorer').hide();
			showNotification(fileName + '.smi');
	  });
}

function generateSMILES(query, results) {
	$('.splashScreenExplorer').hide();
  //  var $modal = $('.smilesFile');
  //  $modal.modal('show');
	var myObject = JSON.parse(results);
	var hasSmiles = false;
	var smilesID = 0, uriID = 0, uriSequence = '';
	for(j = 0; j < myObject.head.vars.length; j ++){
		 if (myObject.head.vars[j].indexOf("SMILESnotation") > -1) {
			 hasSmiles = true;
			 smilesID = j;
			 console.log(smilesID);
			 var queryParts = query.split(myObject.head.vars[j]);
			 if(queryParts.length > 0) {
				 var downQueryParts = queryParts[0].split(/\s+/g);
			//	 console.log(downQueryParts);
				 uriSequence = downQueryParts[downQueryParts.length - 3]; 
			//	 console.log(uriSequence);
				 var uriIDCont = uriSequence.split('_')[0];
				 uriID = uriIDCont.substring(3,uriIDCont.length-1);
			//	 console.log(uriID);
			 } else {
				 hasSmiles = false;
			 }
		 }
	}
	if(hasSmiles) {
		var transformedSequence = "SMILESNotation" + "\t" + "Identifier" + "\t" + "Source" + "\n";
	/*	for(j = 0; j < myObject.head.vars.length; j ++) {
			if(j == uriID || j == smilesID)
				continue;
			else if (j == myObject.head.vars.length-1)
				transformedSequence += myObject.head.vars[j] + "\n" ;
			else
				transformedSequence += myObject.head.vars[j] + "\t" ;
		} */
	//	$("#smilesFileContent").append(transformedSequence); 
		for(i = 0; i < myObject.results.bindings.length; i ++) {
		//	transformedSequence = "";
			var binding = myObject.results.bindings[i];
			if(binding == null) 
				continue;
		//	console.log(binding);
		//	console.log(binding[myObject.head.vars[smilesID]]);
			var smilesString = binding[myObject.head.vars[smilesID]].value.replace(/\s+/g, '');
			var uriConcernedParts = binding[myObject.head.vars[uriID]].value.split(/[:#\/]/);
			var uriConcerned = uriConcernedParts[uriConcernedParts.length-1];
			var source = uriConcernedParts[uriConcernedParts.length-2];
			transformedSequence += smilesString + "\t" + uriConcerned + "\t" + source + "\n";
//			console.log(i);
		/*	for(j = 0; j < myObject.head.vars.length; j ++) {
				if(j == uriID || j == smilesID)
					continue;
				else if (binding[myObject.head.vars[j]].length > 50)
					continue;
				else if (j == myObject.head.vars.length-1)
					transformedSequence += binding[myObject.head.vars[j]] + "\n" ;
				else
					transformedSequence += binding[myObject.head.vars[j]] + "\t" ;
			}*/
		//	$("#smilesFileContent").append(transformedSequence);  
		} 
		return transformedSequence;
	} else {
		return "";
	}
}

function convertToJson(data) {
	var baseDoc = data;
	if ($.browser.mozilla) {
		baseVars = $(baseDoc).find("variable");
		baseResults = $(baseDoc).find("result");
	} else {
		baseVars = $(baseDoc).find("variable");
		baseResults = $(baseDoc).find("result");
	}
	console.log(baseResults);
	var sparqlVariables = [];
	$.each (baseVars, function(key, val){
		if($(val).attr('name') != null) {
			var variable = $(val).attr('name');
			sparqlVariables.push(variable);
			//////// tagny
			console.log(variable)
			///////  tagny end
		}
	});
	var parsedCounter = false;
	var results = [];
	for(var i = 0; i < baseResults.length; i ++){
		if(typeof baseResults[i] !== "undefined"){
			var constBinding = "{";
			for(var j = 0 ; j < baseResults[i].children.length; j ++){
				var binding = baseResults[i].children[j];
				constBinding += "\"" + $(binding).attr('name') + "\": {\"type\": \"" + $(binding)[0].children[0].localName + "\", \"value\": \"" + $.trim($(binding)[0].textContent) + "\"}, ";
			}
			constBinding = constBinding.substring(0, constBinding.length-2) + "}";
			constBinding = constBinding.replace(/[^A-Za-z 0-9 \.,\?""!@#\$%\^&\*\(\)-_=\+;:<>\/\|\}\{\[\]`~]*/g, '') ;
			constBinding = constBinding.replace(/[\\]/g, '\\\\');
			console.log(constBinding);
			results.push(JSON.parse(constBinding));
		}
	}
	var jsonData = {"head" : {"vars" : sparqlVariables}, "results" : {"bindings": results}};
	console.log(jsonData);
	return jsonData;
}

function loadDataSet(results) {
	var myObject = JSON.parse(results);
	var transformedJS = "[ ";
	for(i = 0; i < myObject.results.bindings.length; i ++) {
		transformedJS += "{";
		
		for(j = 0; j < myObject.head.vars.length; j ++) {
			transformedJS += "\"" +myObject.head.vars[j] + "\": ";
			//////// tagny
			//console.log(myObject.head.vars[j]);
			//////// tagny end
			var binding = myObject.results.bindings[i];
			//console.log(binding);
			var mainVariable = binding[myObject.head.vars[j]].value;
			if($.isNumeric(binding[myObject.head.vars[j]].value))
				transformedJS += binding[myObject.head.vars[j]].value + ", ";
			else if (mainVariable.substring(0,4) == "http") {	
				var extractedTitle = "More";
				var spanId = mainVariable.replace(/[:#\/]/gi, '');
				retrievedURIs.push(mainVariable);
				transformedJS += "\"<a href = javascript:popUpInfo(\'"+mainVariable.substring(7,mainVariable.length)+"\')><i class=\'icon-external-link icon-large\'></i></a> "+
			//	"<a href = javascript:popUpInfo(\'"+mainVariable.substring(7,mainVariable.length)+"\')><i class=\'icon-book icon-large\'></i></a> " +
				"<span class=\'"+spanId+"_additionalIcons\'></span><br><span class=\'"+spanId+"\'>" +extractedTitle+ "</span>\", ";
			} else if (myObject.head.vars[j].indexOf("SMILESnotation") > -1) {
				var content = binding[myObject.head.vars[j]].value.replace(/\"/g, "");
				content = content.replace(/[^A-Za-z 0-9 \.,\?""!@#\$%\^&\*\(\)-_=\+;:<>\/\\\|\}\{\[\]`~]*/g, '') ;
				content = encodeURIComponent(content);
				transformedJS += "\"<a href = javascript:getResources(\'"+content+"\')><i class=\'icon-cogs icon-large\'></i> "+
					"ChemSpider References</a>\", ";
			} else {
				var content = binding[myObject.head.vars[j]].value.replace(/\"/g, "");
				content = content.replace(/[^A-Za-z 0-9 \.,\?""!@#\$%\^&\*\(\)-_=\+;:<>\/\\\|\}\{\[\]`~]*/g, '') ;
			//	content = content.replace(/\/\\/g, "")
				var randomId = "abcd" + i + "d" + j;
				if(content.length > 200)
					content = content.substring(0,200) + "<span style=\'display:none\' id=\'"+randomId+"_content\'>" + content.substring(200, content.length-1) + "</span>" +
					" ... " + "<a href = javascript:readMore(\'"+randomId+"_content\')>" + "<span id=\'"+randomId+"_link\'>Read More</span></a>" ;
				transformedJS += "\"" + content + "\", ";
			}		
		}
		transformedJS = transformedJS.substring(0,transformedJS.length-2);
	//	transformedJS += "\"Additional Information\": \"<a href = javascript:popUpInfo(\'"+mainVariable.substring(7,mainVariable.length)+"\')>Click Here </a>\"";
		transformedJS += "},";
	}
	transformedJS = transformedJS.substring(0,transformedJS.length-1) + "]";
	var myRecords = JSON.parse(transformedJS);
	return myRecords;
}

function makeSparqlRequest (fileName, outputFormat, folderId) {
	var queryDisplayValue = $('textarea[name="query"]').val() + " LIMIT " + $('#limitResults').val();
	if(queryDisplayValue != '' && readCookie('disposition') != null){
		var outputSelection = readCookie('disposition');
		var query = queryDisplayValue;
		writeCookie('disposition', '', -1);
		console.log(folderId);
		if(outputSelection == "save" && outputFormat != "SMI") {
			var queryMap = {
				'query' : query,
				'output' : outputFormat,
				'disposition' : 'save',
				'filename' : fileName,
				'folderid' : folderId
			}
			$.ajax({
				type : "POST",
				url : uriStr + "sparql",
				data : queryMap,
				beforeSend : function(data){
		   	    	$('.splashScreenExplorer').show();
		   	    },
				success : function(data, textStatus, jqXHR) {
					$('.splashScreenExplorer').hide();
					var notiMsg = jqXHR.getResponseHeader("X-FileName");
					showNotification(notiMsg);
				},
				error : function(xhr, status, error) {
					$('.splashScreenExplorer').hide();
					var notiMsg = xhr.getResponseHeader("X-FileName");
					showNotification(notiMsg);
				}
			});
		} else if (outputSelection == "save" && outputFormat == "SMI")  {
			  var queryMap = { 'query': query,	'output' : 'json' }
			  $.ajax({
		   	    type : "POST",
		   	    url : uriStr + "sparql",
		   	    data : queryMap,
		   	    dataType : "json",
		   	    beforeSend : function(data){
		   	    	$('.splashScreenExplorer').show();
		   	    },
		   	    success : function(data){
		   	    	var transformedSequence = generateSMILES(query, JSON.stringify(data));
		   	    //	console.log(transformedSequence);
		   	    	if(transformedSequence == ""){
		   	    		$('.taskQuestion').show();
		   	    		$('.evaluateQueryQues').hide();
		   	    		$('#queryResultsRes').append('No SMILES Values found !! <a href="?reset=true">Refresh Interface</a>');
		   	    		$('#queryResultsRes').show();
		   	    	} else {
		   	    		storeFile(transformedSequence, fileName, folderId);
		   	    	}
		   	    }
		   	  });
		} else {
			window.location.href = uriStr + "sparql?disposition=" + outputSelection + "&query="
				+ encodeURIComponent(query) + '&output=' + outputFormat +'&filename=' + fileName; 
		}
	} else 
		alert("No Query Found");
}

function showNotification (notiMsg) {
	$('.taskQuestion').show();
	$('.evaluateQueryQues').hide();
	$('#queryResultsRes').append('File saved as ' + notiMsg + ' under the Invoked Folder in BSCW. <a href="?reset=true">Refresh Interface</a>');
	$('#queryResultsRes').show();
}

function replaceSemantics() {
	for(i in retrievedURIs) {
		$.ajax({
			url: "https://maulikkamdar.cloudant.com/biologicaltitlecorpus/_design/alltitles/_search/titles?stale=ok&q=entityId:\""+retrievedURIs[i]+"\"",
			dataType: "jsonp",
			success: function( data ) {
				if(data.total_rows > 0){
					var counter = 0;
					var additionalIcons = [];
					for(i in data.rows) {
						if(data.rows[i].fields.entity.toLowerCase().indexOf('assay') >= 0) {
							additionalIcons.push("chart");
						}
						if(data.rows[i].fields.entity.toLowerCase().indexOf('molecule') >= 0) {
							additionalIcons.push("molchart");
							additionalIcons.push("molLit");
						}
						if(data.rows[i].fields.property.indexOf('label') >= 0) {
							counter = i;
							break;
						} else if (data.rows[i].fields.property.indexOf('title') >= 0){
							counter = i;
						} else { }
					}
					var retrievedSemantics = data.rows[counter].fields.title;
					var entityId = data.rows[0].fields.entityId;
					var spanId = entityId.replace(/[:#\/]/gi,'');
					var additionalIconsId = entityId.replace(/[:#\/]/gi,'') + "_additionalIcons";
					for(i in document.getElementsByClassName(spanId)){
						document.getElementsByClassName(spanId)[i].innerHTML = retrievedSemantics;
					}
					
					var iconTag = "";
					for(i in additionalIcons) {
						if(additionalIcons[i] == "chart") {
							if(entityId.substring(0,4) == 'http'){
								entityId = entityId.substring(7,entityId.length);
							}
							iconTag += "<a href = \"javascript:renderInfo(\'"+entityId+"\'\,\'AssayResult\')\"><i class=\'icon-bar-chart icon-large\'></i></a>"
						}
						if(additionalIcons[i] == "molchart") {
							if(entityId.substring(0,4) == 'http'){
								entityId = entityId.substring(7,entityId.length);
							}
							iconTag += "<a href = \"javascript:renderInfo(\'"+entityId+"\'\,\'MolResult\')\"><i class=\'icon-bar-chart icon-large\'></i></a>&nbsp;"
						}
						if(additionalIcons[i] == "molLit") {
							if(entityId.substring(0,4) == 'http'){
								entityId = entityId.substring(7,entityId.length);
							}
							iconTag += "<a href = \"javascript:renderInfo(\'"+entityId+"\'\,\'MolLiterature\',\'"+retrievedSemantics+"\')\"><i class=\'icon-book icon-large\'></i></a>"
						}
					}
					for(i in document.getElementsByClassName(additionalIconsId)){
						document.getElementsByClassName(additionalIconsId)[i].innerHTML = iconTag;
					}
				}
			}
		});
	}
}

function readMore(id) {
	if ($('#'+id).is(":hidden")) {    
		$("#" + id).show();
		document.getElementById(id.split("_")[0] + "_link").innerHTML = "Read Less";
	} else {
		$("#" + id).hide();
		document.getElementById(id.split("_")[0] + "_link").innerHTML = "Read More";
	}
}
////// --------------- Render Lens Dialog
function getResources (smilesString) {
	var $modal = $('.modal.js-popup-info');
	var smilesNotation = decodeURIComponent(smilesString).replace(/\s+/g, '');
	var resourceStr = resourceURL + "apiMethod=SimpleSearch&queryParams=" + smilesNotation;
	var resourceID = smilesNotation.replace(/[^A-Za-z0-9]/g, '') ;
	$.ajax({
 	    type : "GET",
 	    url : resourceStr,
 	    dataType : "xml",
 	    beforeSend : function(data){
 	    	$('.splashScreenExplorer').show();
 	    },
 	    success : function(data){
 	    	$modal.modal('show');
 	    	var csIDs = $(data).find("int");
 	    //	console.log(csIDs);
 	    	var retrievedIds = [];
 	    	for (i in csIDs) {
 	    		(csIDs[i].textContent != null)? retrievedIds.push(csIDs[i].textContent) : '';
 	    	}
 	    //	console.log(retrievedIds);
 	    	renderResources(resourceID, smilesNotation, retrievedIds);
 	    //	$('.splashScreenExplorer').hide();
 	    }
 	}) 
	$modal.modal('show');
}

function renderResources(resourceID, label, csIDs){
	if(label.length > 10)
		var truncatedLabel = label.substring(0,8) + "...";
	else 
		var truncatedLabel = label;
	
	var resourceTerms = resourceID.split(/[:#\/]/);
	var resourceTerm = makeid() + resourceTerms[resourceTerms.length-1] ;
	
	var selector = "#" + resourceTerm;
	$infoTabs.tabs('add',selector, truncatedLabel);  
	$('.splashScreenExplorer').hide();
	
	d3.select(selector).html("" +
		"<h4 class='sidebar'>" + label + "</h4>");
	
	d3.select(selector).append("table").attr("border","0px").attr("width", "100%").selectAll("tr").data(csIDs).enter().append("tr").html(function(d){
		var srcUr = "http://www.chemspider.com/Chemical-Structure."+d+".html";
		var src = "http://www.chemspider.com/ImagesHandler.ashx?id=" + d;
		return "<td><img src='"+src+"' width='200px' height='200px'></td><td class='infoTableCell'>" +
						"<b>CSID: </b> "+d+"<br><a href='"+srcUr+"' target='_blank'>Open link in a new window</a></td>";
	});
	
}

function popUpInfo(resource) {
	console.log(resource);
	if(resource.substring(0,4) == 'http'){
		resource = resource.substring(7,resource.length);
	}
	
	renderInfo(resource, "default");
}

function renderInfo(resource, category, resourceTitle) {
	var $modal = $('.modal.js-popup-info');
	console.log(category);
	if(category == "AssayResult") {
		/*
		 *  PREFIX granatum: <http://chem.deri.ie/granatum/>
			SELECT ?mol ?mol_title ?score ?type ?value ?measure WHERE { 
			granatum:2a20c9f4fe76fbad8ea22b855152cf91 granatum:hasResult ?res .
			?res granatum:mentionMolecule ?mol .
			?mol granatum:title ?mol_title .
			?res granatum:outcomeScore ?score .
			?res granatum:outcomeType ?type .
			OPTIONAL {?res granatum:outcomeMeasure ?measure .
			?res granatum:outcomeValue ?value}
			} 
		 */
		var additionalQuery = "PREFIX granatum: <http://chem.deri.ie/granatum/>" +
		"SELECT ?mol ?mol_title ?score ?type ?value ?measure WHERE {" +
	    	"<http://"+ resource +"> granatum:hasResult ?res ." +
	    	"?res granatum:mentionMolecule ?mol ." +
	    	"?mol granatum:title ?mol_title ." +
	    	"?res granatum:outcomeScore ?score ." +
	    	"?res granatum:outcomeType ?type ." +
	    	"OPTIONAL {?res granatum:outcomeMeasure ?measure ." +
	    	"?res granatum:outcomeValue ?value}" +
			"} LIMIT 1000";
	} else if (category == "MolResult"){
		/*
		 *  PREFIX granatum: <http://chem.deri.ie/granatum/>
			SELECT ?mol ?mol_title ?score ?type ?value ?measure WHERE { 
			granatum:2a20c9f4fe76fbad8ea22b855152cf91 granatum:hasResult ?res .
			?res granatum:mentionMolecule ?mol .
			?mol granatum:title ?mol_title .
			?res granatum:outcomeScore ?score .
			?res granatum:outcomeType ?type .
			OPTIONAL {?res granatum:outcomeMeasure ?measure .
			?res granatum:outcomeValue ?value}
			} 
		 */
		var additionalQuery = "PREFIX granatum: <http://chem.deri.ie/granatum/>" +
		"SELECT ?mol ?mol_title ?score ?type ?value ?measure WHERE {" +
	    	"?mol granatum:hasResult ?res ." +
	    	"?res granatum:mentionMolecule <http://"+ resource +"> ." +
	    	"?mol granatum:title ?mol_title ." +
	    	"?res granatum:outcomeScore ?score ." +
	    	"?res granatum:outcomeType ?type ." +
	    	"OPTIONAL {?res granatum:outcomeMeasure ?measure ." +
	    	"?res granatum:outcomeValue ?value}" +
			"} LIMIT 1000";
	} else if (category == "MolLiterature"){
		var additionalQuery = "PREFIX granatum_onto: <http://granatum.ubitech.eu/CancerChemopreventionOntology.owl#>" +
				"SELECT DISTINCT * WHERE {" +
				"?s a granatum_onto:PublishedWork ." +
				"?s granatum_onto:hasAbstract ?o . " +
				" FILTER( regex( <http://www.w3.org/2001/XMLSchema#string>( ?o ), \""+resourceTitle+"\", \"is\" ) ) . " +
				"} LIMIT 100";
	} else {
		var additionalQuery = "SELECT * WHERE { " +
    			"<http://"+ resource +"> ?p ?o . " +
		"} LIMIT 100";
	}
  
	var queryMap = { 'query': additionalQuery, 'output' : 'json', 'endpoint': uriStr};
	$('.splashScreenExplorer').show();
	$.post("makeRequest.php", queryMap, function(xmldata){
		// small function force converting sparql-xml to json (should be done on the server side)
		// this is not required when the SPARQL Engine exposes ReVeaLD as a service (should use AJAX call below)
		var data = convertToJson(xmldata);
		
		$modal.modal('show');
 	    	if(category == "AssayResult") {
 	    	//	console.log(data.results.bindings);
 	    		renderBarChart(data.results.bindings, resource);
 	    	} else if (category == "MolResult") {
 	    		renderBarChart(data.results.bindings, resource);
 	    	} else if (category == "MolLiterature") {
 	    		var infoTokenArray = [];
 	 	    	for(delta in data.results.bindings){
 	 	    		var binding = data.results.bindings[delta];
 	 	    		var predicateTerms = binding["s"].value.split(/[:#\/]/);
 	 	    		var predicateTerm = predicateTerms[predicateTerms.length-1];
 	 	    		var objectTerm = binding["o"].value;
 	 	    		infoToken = {predicate : predicateTerm, object : objectTerm};
 	 	    		infoTokenArray.push(infoToken);
 	 	    	}
 	 	    	console.log(infoTokenArray);
 	    		renderLiterature(infoTokenArray, resource, resourceTitle);
 	    	} else {
 	    		var infoTokenArray = [];
 	 	    	var label = "";
 	 	    	var image = "";
 	 	    	for(delta in data.results.bindings){
 	 	    		var binding = data.results.bindings[delta];
 	 	    		var predicateTerms = binding["p"].value.split(/[:#\/]/);
 	 	    		var predicateTerm = predicateTerms[predicateTerms.length-1];
 	 	    		var objectTerm = binding["o"].value;
 	 	    	//	alert(predicateTerm);
 	 	    		if(predicateTerm == "title" || predicateTerm == "label")
 	 	    			label = objectTerm;
 	 	    		if(predicateTerm == "thumbnail" || predicateTerm == "image")
 	 	    			image = objectTerm;
 	 	    		infoToken = {predicate : predicateTerm, object : objectTerm};
 	 	    		infoTokenArray.push(infoToken);
 	 	    	}
 	 	    	renderPopUp(infoTokenArray,label,image,resource);
 	    	}
   	}).fail(function() {
	   	$('.splashScreenExplorer').hide();
	});  
	// To be used if direct integration with the SPARQL Engine
/*
 	$.ajax({
 	    type : "GET",
 	    url : uriStr + "sparql",
 	    data : queryMap,
 	    dataType : "json",
 	    beforeSend : function(data){
 	    	$('.splashScreenExplorer').show();
 	    },
 	    success : function(data){
 	    	$modal.modal('show');
 	    	if(category == "AssayResult") {
 	    	//	console.log(data.results.bindings);
 	    		renderBarChart(data.results.bindings, resource);
 	    	} else if (category == "MolResult") {
 	    		renderBarChart(data.results.bindings, resource);
 	    	} else if (category == "MolLiterature") {
 	    		var infoTokenArray = [];
 	 	    	for(delta in data.results.bindings){
 	 	    		var binding = data.results.bindings[delta];
 	 	    		var predicateTerms = binding["s"].value.split(/[:#\/]/);
 	 	    		var predicateTerm = predicateTerms[predicateTerms.length-1];
 	 	    		var objectTerm = binding["o"].value;
 	 	    		infoToken = {predicate : predicateTerm, object : objectTerm};
 	 	    		infoTokenArray.push(infoToken);
 	 	    	}
 	 	    	console.log(infoTokenArray);
 	    		renderLiterature(infoTokenArray, resource, resourceTitle);
 	    	} else {
 	    		var infoTokenArray = [];
 	 	    	var label = "";
 	 	    	var image = "";
 	 	    	for(delta in data.results.bindings){
 	 	    		var binding = data.results.bindings[delta];
 	 	    		var predicateTerms = binding["p"].value.split(/[:#\/]/);
 	 	    		var predicateTerm = predicateTerms[predicateTerms.length-1];
 	 	    		var objectTerm = binding["o"].value;
 	 	    	//	alert(predicateTerm);
 	 	    		if(predicateTerm == "title" || predicateTerm == "label")
 	 	    			label = objectTerm;
 	 	    		if(predicateTerm == "thumbnail" || predicateTerm == "image")
 	 	    			image = objectTerm;
 	 	    		infoToken = {predicate : predicateTerm, object : objectTerm};
 	 	    		infoTokenArray.push(infoToken);
 	 	    	}
 	 	    	renderPopUp(infoTokenArray,label,image,resource);
 	    	}
 	    }
 	}) */
}

