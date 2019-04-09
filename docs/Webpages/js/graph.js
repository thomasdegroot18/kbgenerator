function getQueryVariable(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split('&');
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split('=');
        if (decodeURIComponent(pair[0]) == variable) {
            return decodeURIComponent(pair[1]);
        }
    }
    console.log('Query variable %s not found', variable);
}

function readTextFile(file, callback) {
    var rawFile = new XMLHttpRequest();
    rawFile.overrideMimeType("application/json");
    rawFile.open("GET", file, true);
    rawFile.onreadystatechange = function() {
        if (rawFile.readyState === 4 && rawFile.status == "200") {
            callback(rawFile.responseText);
        }
    }
    rawFile.send(null);
}

String.prototype.replaceAll = function(search, replacement) {
    var target = this;
    return target.split(search).join(replacement);
};


function RetrieveNodes(nodeLinks){

   var nodes = []

   var links = []

   xLoc = 100
   yLoc = 100

    for (elem in nodeLinks) {
        nodesElem = {x: 0, y: 0, name: "", color: "blue"}



          edge = nodeLinks[elem].split(" ")[0]
          vertexIn = nodeLinks[elem].split(" ")[1]
          if (vertexIn == undefined){
            continue;
          }
          vertexOut = nodeLinks[elem].split(" ")[2];
          console.log(vertexOut)
          vertexIn = vertexIn.split(" ")[0];
          if (vertexIn.includes("C")){
            vertexType = "Class";
            reuse = undefined;
            for (elem in nodes){
              if (nodes[elem].name == vertexIn){
                reuse = elem
              }
            }
            if (reuse == undefined){
              nodesElem.color = "green";
              nodesElem.name = vertexIn;
              nodesElem.x = xLoc + Math.floor(Math.random() * 50) - 20;
              nodesElem.y = yLoc + Math.floor(Math.random() * 50) - 20;
              nodes.push(nodesElem);
            } else{
              nodes[reuse].color = "green";
            }

          }
          if (vertexOut.includes("C")){
            vertexType = "Class";
            reuse = undefined;
            for (elem in nodes){
              if (nodes[elem].name == vertexOut){
                reuse = elem
              }
            }
            if (reuse == undefined){
              nodesElem.color = "green";
              nodesElem.name = vertexIn;
              nodesElem.x = xLoc + Math.floor(Math.random() * 50) - 20;
              nodesElem.y = yLoc + Math.floor(Math.random() * 50) - 20;
              nodes.push(nodesElem);
            } else{
              nodes[reuse].color = "green";
            }

          }
            reuse = undefined;
            for (elem in nodes){
              if (nodes[elem].name == vertexOut){
                reuse = elem
              }
            }
            if (reuse == undefined){
              nodesElem.name = vertexOut;
              nodesElem.x = xLoc + Math.floor(Math.random() * 50) - 50;
              nodesElem.y = yLoc + Math.floor(Math.random() * 50) - 50;


              nodes.push(nodesElem);



            linksElem = {source: undefined, target: undefined, type: undefined}
            for (elem in nodes){
              if (nodes[elem].name == vertexOut){
                linksElem.source = nodes[elem]
              }
              if (nodes[elem].name == vertexIn){
                linksElem.target = nodes[elem]
              }
            }
            if (linksElem.target == undefined){
              nodesElem2 = {x: 0, y: 0, name: "", color: "blue"}
              nodesElem2.name = vertexIn;
              nodesElem2.x = xLoc + Math.floor(Math.random() * 100) - 50;
              nodesElem2.y = yLoc + Math.floor(Math.random() * 100) - 50;
              nodes.push(nodesElem2);
              linksElem.target = nodes[nodes.length-1]
            }
            linksElem.type = edge;
            links.push(linksElem);
          }

        }
        return [nodes, links]
    }



function build(nodes, links, divSection) {
    const svg = divSection.append('svg').attr("width","300px").attr("height","300px");

    var width = 200,
        height = 200;



      var simulation = d3.forceSimulation()
          .nodes(nodes);
    simulation.force("charge", d3.forceManyBody())
              .force("center", d3.forceCenter(width / 2, height / 2));

//draw circles for the nodes
var node = svg.append("g")
        .attr("class", "nodes")
        .selectAll("circle")
       .data(nodes)
       .enter()
       .append("circle")
       .attr("cx", function(d) { return d.x; })
       .attr("cy", function(d) { return d.y; })
       .attr("text", function(d) {return d.name})
       .attr("r", "5px")
       .attr("fill", function(d) {return d.color})



       //add tick instructions:
       simulation.on("tick", tickActions );

       var link_force =  d3.forceLink(links)
                               .id(function(d) { return d.name; })
                               .distance(10)



       simulation.force("links",link_force)


 //draw lines for the links
 var link = svg.append("g")
       .attr("class", "links")
     .selectAll("line")
    .data(links)
    .enter()
    .append("line")
    .attr("x1", function(d) { return d.source.x })
    .attr("y1", function(d) { return d.source.y })
    .attr("x2", function(d) { return d.target.x })
    .attr("y2", function(d) { return d.target.y })
    .attr("stroke-width", "1px")
    .style("stroke", function(d) {
      if(d.type == "SubClassOf"){
        return "black"
      }
      if (d.type == "DisjointClasses"){
        return "red"
      }
      if(d.type == "ClassAssertion"){
        return "blue"
      }
      else{
        return "black"
      }
    });

    function tickActions() {
        //update circle positions each tick of the simulation
        node
            .attr("cx", function(d) { return d.x; })
            .attr("cy", function(d) { return d.y; });

        //update link positions
        //simply tells one end of the line to follow one node around
        //and the other end of the line to follow the other node around
        link
            .attr("x1", function(d) { return d.source.x; })
            .attr("y1", function(d) { return d.source.y; })
            .attr("x2", function(d) { return d.target.x; })
            .attr("y2", function(d) { return d.target.y; });

      }
    }

function tablebuild(columns, data, bodySection){
    var table = bodySection.append('table')
    		var	tbody = table.append('tbody');

        // create a row for each object in the data
    		var rows = tbody.selectAll('td')
    		  .data(data)
    		  .enter()
    		  .append('td');

        // create a cell in each row for each column
    		var cells = rows.selectAll('tr')
    		  .data(function (row) {
    		    return columns.map(function (column) {
    		      return {column: column, value: row[column]};
    		    });
    		  })
    		  .enter()
          .append('tr')
            .text(function (d) { return d.column; })
    		  .append('td')
    		    .text(function (d) { return d.value; });

    	  return table;
    	}



//usage:
readTextFile("data/Inconsistencies.json", function(text1){
  readTextFile("data/Graphnumber.json", function(text2){
    var sample = JSON.parse(text1);
    var GraphNumberItems = JSON.parse(text2);
    for (elem in sample){
      var graph = sample[elem].Graph
      var graphInfo = GraphNumberItems[elem]
      var nodeLinks = graph.split(", ");
      var nodesEdges = RetrieveNodes(nodeLinks)
      nodes = nodesEdges[0]
      links = nodesEdges[1]
    // var graph = sample[getQueryVariable('Graphnumber')-1].Graph;
    //var sparqlRequest = sample[getQueryVariable('Graphnumber')-1].SparqlRequest.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll(". ", ". </br>").replace("WHERE {", "WHERE { </br>")
    var sparqlRequest = sample[elem].SparqlRequest.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll(". ", ". </br>").replace("WHERE {", "WHERE { </br>")
    var bodySection = d3.select("body").append("div");
    bodySection.append("p").attr("width", "600px").html(sparqlRequest);
    build(nodes, links, bodySection)

    values = []
    values.push(graphInfo);
    tablebuild(Object.keys(graphInfo), values, bodySection)
  }
  })
  })
