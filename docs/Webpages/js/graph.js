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
readTextFile("js/data.json", function(text){
    var sample = JSON.parse(text);
    var graph = sample[getQueryVariable('Graphnumber')-1].Graph;
    var nodeLinks = graph.split(", ");


    const svg = d3.select('svg');
    const svgContainer = d3.select('#container');

   var nodes = []

   var links = []

   xLoc = 300
   yLoc = 300

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
              nodesElem.color = "red";
              nodesElem.name = vertexIn;
              nodesElem.x = xLoc + Math.floor(Math.random() * 400) - 100;
              nodesElem.y = yLoc + Math.floor(Math.random() * 200) - 100;
              nodes.push(nodesElem);
            } else{
              nodes[reuse].color = "red";
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
              nodesElem.x = xLoc + Math.floor(Math.random() * 400) - 100;
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

    var vis = d3.select("#graph")
                .append("svg");

    var width = 1200,
        height = 700;

    vis.attr("width", width)
       .attr("height", height);

   vis.text("The Graph")
      .select("#graph")

      var simulation = d3.forceSimulation()
          .force("forceX", d3.forceX().strength(.1).x(width * .5))
          .force("forceY", d3.forceY().strength(.1).y(height * .5))
          .force("center", d3.forceCenter().x(width * .5).y(height * .5))
          .force("charge", d3.forceManyBody().strength(-15));



   vis.selectAll("circle .nodes")
       .data(nodes)
       .enter()
       .append("svg:circle")
       .attr("class", "nodes")
       .attr("cx", function(d) { return d.x; })
       .attr("cy", function(d) { return d.y; })
       .attr("text", function(d) {return d.name})
       .attr("r", "10px")
       .attr("fill", function(d) {return d.color})

 vis.selectAll(".line")
    .data(links)
    .enter()
    .append("line")
    .attr("x1", function(d) { return d.source.x })
    .attr("y1", function(d) { return d.source.y })
    .attr("x2", function(d) { return d.target.x })
    .attr("y2", function(d) { return d.target.y })
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

simulation.force()

  })
