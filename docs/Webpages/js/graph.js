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

//usage:
readTextFile("data/data.json", function(text){
    var sample = JSON.parse(text);
    var graph = sample[getQueryVariable('Graphnumber')-1].Graph;
    var nodeLinks = graph.split(", ");
    var sparqlRequest = sample[getQueryVariable('Graphnumber')-1].SparqlRequest.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll(". ", ". </br>").replace("WHERE {", "WHERE { </br>")
    d3.select("body").select("p")
        .append("p")
        .html(sparqlRequest);

    const svg = d3.select('svg');
    const svgContainer = d3.select('#container');

   var nodes = []

   var links = []

   xLoc = 100
   yLoc = 100

    for (elem in nodeLinks) {
        nodesElem = {x: 0, y: 0, name: "", color: "blue"}


          nodeLinks[elem]
          edge = nodeLinks[elem].split("(")[0]
          vertexIn = nodeLinks[elem].split("(")[1]
          if (vertexIn == undefined){
            continue;
          }
          vertexIn = vertexIn.split(" ")[0].replace("<","").replace(">","");
          if (vertexIn == "Class"){
            vertexType = vertexIn;
            vertexIn = nodeLinks[elem].split("(")[2].replaceAll(")","").replace("<","").replace(">","");
            reuse = undefined;
            for (elem in nodes){
              if (nodes[elem].name == vertexIn){
                reuse = elem
              }
            }
            if (reuse == undefined){
              nodesElem.color = "green";
              nodesElem.name = vertexIn;
              nodesElem.x = xLoc + Math.floor(Math.random() * 100) - 50;
              nodesElem.y = yLoc + Math.floor(Math.random() * 100) - 50;
              nodes.push(nodesElem);
            } else{
              nodes[reuse].color = "green";
            }

          } else {
            vertexOut = nodeLinks[elem].split("(")[1].split(" ")[1].replaceAll(")","").replace("<","").replace(">","")
            reuse = undefined;
            for (elem in nodes){
              if (nodes[elem].name == vertexOut){
                reuse = elem
              }
            }
            if (reuse == undefined){
              nodesElem.name = vertexOut;
              nodesElem.x = xLoc + Math.floor(Math.random() * 200) - 100;
              nodesElem.y = yLoc + Math.floor(Math.random() * 200) - 100;


              nodes.push(nodesElem);

            }

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
              nodesElem2.x = xLoc + Math.floor(Math.random() * 400) - 100;
              nodesElem2.y = yLoc + Math.floor(Math.random() * 200) - 100;
              nodes.push(nodesElem2);
              linksElem.target = nodes[nodes.length-1]
            }
            linksElem.type = edge;
            links.push(linksElem);
          }

    }


    var width = 400,
        height = 300;



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
       .attr("r", "10px")
       .attr("fill", function(d) {return d.color})



       //add tick instructions:
       simulation.on("tick", tickActions );

       var link_force =  d3.forceLink(links)
                               .id(function(d) { return d.name; })
                               .distance(50)



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
    .attr("stroke-width", "2px")
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

  })
