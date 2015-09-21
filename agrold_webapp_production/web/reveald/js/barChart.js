var valueLabelWidth = 40; // space reserved for value labels (right)
var barHeight = 20; // height of one bar
var barLabelWidth = 100; // space reserved for bar labels
var barLabelPadding = 5; // padding between bar and bar labels (left)
var gridLabelHeight = 18; // space reserved for gridline labels
var gridChartOffset = 3; // space between start of grid and first bar
var maxBarWidth = 420; // width of the bar with the max value
 
function renderBarChart(bindings, resource) {
	var dataArray = [];
	for(delta in bindings) {
		var binding = bindings[delta];
		console.log(binding);
		var measure = "", value = "";
		if(binding["measure"] != null)
			measure = binding["measure"].value;
		if(binding["value"] != null)
			value = binding["value"].value;
		var dataObj = {"mol" : binding["mol_title"].value, "type" : binding["type"].value, 
				"score" : binding["score"].value, "measure" : measure, 
				"value" : value, "mol_uri" : binding["mol"].value};
		dataArray.push(dataObj);
	}
	var truncatedLabel = "";
	var resourceTerms = resource.split(/[:#\/]/);
	var resourceTerm = makeid() + resourceTerms[resourceTerms.length-1] ;
	var selector = "#" + resourceTerm;
	$infoTabs.tabs('add',selector, truncatedLabel);  
	
	createBarChart(selector, dataArray);
	$('.splashScreenExplorer').hide();
}

function createBarChart (div, data) {
	var barLabel = function(d) { return d.mol; };
	var barValue = function(d) { return parseFloat(d.score); };
	var barPoints = function(d) { return parseFloat(d.value); };
	
	var sortedData = data.sort(function(a, b) {
	 return d3.descending(barValue(a), barValue(b));
	}); 

	var yScale = d3.scale.ordinal().domain(d3.range(0, sortedData.length)).rangeBands([0, sortedData.length * barHeight]);
	var y = function(d, i) { return yScale(i); };
	var yText = function(d, i) { return y(d, i) + yScale.rangeBand() / 2; };
	var x = d3.scale.linear().domain([0, d3.max(sortedData, barValue)]).range([0, maxBarWidth]);

	var chart = d3.select(div).append("svg")
	  .attr('width', maxBarWidth + barLabelWidth + valueLabelWidth)
	  .attr('height', gridLabelHeight + gridChartOffset + sortedData.length * barHeight);
	
	var tooltip = d3.select(div).append("div")   
    .attr("class", "tooltip")               
    .style("opacity", 0);
	
	var gridContainer = chart.append('g')
	  .attr('transform', 'translate(' + barLabelWidth + ',' + gridLabelHeight + ')'); 
	gridContainer.selectAll("text").data(x.ticks(10)).enter().append("text")
	  .attr("x", x)
	  .attr("dy", -3)
	  .attr("text-anchor", "middle")
	  .text(String);

	gridContainer.selectAll("line").data(x.ticks(10)).enter().append("line")
	  .attr("x1", x)
	  .attr("x2", x)
	  .attr("y1", 0)
	  .attr("y2", yScale.rangeExtent()[1] + gridChartOffset)
	  .style("stroke", "#ccc");

	var labelsContainer = chart.append('g')
	  .attr('transform', 'translate(' + (barLabelWidth - barLabelPadding) + ',' + (gridLabelHeight + gridChartOffset) + ')'); 
	labelsContainer.selectAll('text').data(sortedData).enter().append('text')
	  .attr('y', yText)
	  .attr('stroke', 'none')
	  .attr('fill', 'black')
	  .attr("dy", ".35em") // vertical-align: middle
	  .attr('text-anchor', 'end')
	  .text(barLabel)
	  .on("mouseover", function(d) { console.log(d.mol_uri);
		  })                  
      .on("mouseout", function(d) {console.log("out of here")});

	var barsContainer = chart.append('g')
	  .attr('transform', 'translate(' + barLabelWidth + ',' + (gridLabelHeight + gridChartOffset) + ')'); 
	barsContainer.selectAll("rect").data(sortedData).enter().append("rect")
	  .attr('y', y)
	  .attr('height', yScale.rangeBand())
	  .attr('width', function(d) { return x(barValue(d)); })
	  .attr('stroke', 'white')
	  .attr('fill', function(d) { return ((d.type == "2") ? "green" : "red")})
	  .on("mouseover", function(d) {      
            tooltip.transition()        
                .duration(200)      
                .style("opacity", .9);      
            tooltip .html(d.mol_title + "<br/>"  + d.score)  
                .style("left", (d3.event.pageX) + "px")     
                .style("top", (d3.event.pageY - 28) + "px");    
            })                  
      .on("mouseout", function(d) {       
            tooltip.transition()        
                .duration(500)      
                .style("opacity", 0);   
      });

	barsContainer.selectAll("text").data(sortedData).enter().append("text")
	  .attr("x", function(d) { return x(barValue(d)); })
	  .attr("y", yText)
	  .attr("dx", 3) // padding-left
	  .attr("dy", ".35em") // vertical-align: middle
	  .attr("text-anchor", "start") // text-align: right
	  .attr("fill", "black")
	  .attr("stroke", "none")
	  .text(function(d) { return d3.round(barValue(d), 2); });

	barsContainer.append("line")
	  .attr("y1", -gridChartOffset)
	  .attr("y2", yScale.rangeExtent()[1] + gridChartOffset)
	  .style("stroke", "#000");
}

