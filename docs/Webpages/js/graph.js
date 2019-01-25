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

    var nodes = [{x: 30, y: 50},
                 {x: 50, y: 80},
                 {x: 90, y: 120}]

   var links = [
     {source: nodes[0], target: nodes[1]},
     {source: nodes[2], target: nodes[1]}
   ]

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

            linksElem = {source: undefined, target: undefined}
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

            links.push(linksElem);
          }

    }

    var vis = d3.select("#graph")
                .append("svg");

    var w = 900,
        h = 400;

    vis.attr("width", w)
       .attr("height", h);

   vis.text("The Graph")
      .select("#graph")





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
    .style("stroke", "rgb(6,120,155)");

  })
