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


function RetrieveNodes(nodeLinks) {

  var nodes = []

  var links = []

  xLoc = 100
  yLoc = 100

  for (linksetelem of nodeLinks) {
    nodesElemIn = {
      x: 0,
      y: 0,
      name: "",
      color: "blue",
      radius:  "5px"
    }
    nodesElemOut = {
      x: 0,
      y: 0,
      name: "",
      color: "blue",
      radius:  "5px"
    }
    vertexPred = linksetelem.split(" ")[0]
    vertexIn = linksetelem.split(" ")[1]
    vertexOut = linksetelem.split(" ")[2];
    if (vertexIn[0] == "C") {
      vertexType = "Class";
      reuse = undefined;
      for (elem in nodes) {
        if (nodes[elem].name == vertexIn) {
          reuse = elem
        }
      }
      if (reuse == undefined) {
        nodesElemIn.color = "green";
        nodesElemIn.y = yLoc + Math.floor(Math.random() * 50) - 20;
        nodesElemIn.name = vertexIn;
        nodesElemIn.x = xLoc + Math.floor(Math.random() * 50) - 20;
        nodes.push(nodesElemIn);
      } else {
        nodes[reuse].color = "green";
      }

    }
    if (vertexOut[0] == "C") {
      vertexType = "Class";
      reuse = undefined;
      for (elem in nodes) {
        if (nodes[elem].name == vertexOut) {
          reuse = elem
        }
      }
      if (reuse == undefined) {
        nodesElemOut.color = "green";
        nodesElemOut.name = vertexOut;
        nodesElemOut.x = xLoc + Math.floor(Math.random() * 50) - 20;
        nodesElemOut.y = yLoc + Math.floor(Math.random() * 50) - 20;
        nodes.push(nodesElemOut);
      } else {
        nodes[reuse].color = "green";
      }

    }
    if (vertexOut[0] == "a") {
      reuse = undefined;
      for (elem in nodes) {
        if (nodes[elem].name == vertexOut) {
          reuse = elem
        }
      }
      if (reuse == undefined) {
        nodesElemOut.name = vertexOut;
        nodesElemOut.x = xLoc + Math.floor(Math.random() * 50) - 50;
        nodesElemOut.y = yLoc + Math.floor(Math.random() * 50) - 50;
        nodesElemOut.color = "aqua"

        nodes.push(nodesElemOut);
      }

    }
    if (vertexIn[0] == "a") {
      reuse = undefined;
      for (elem in nodes) {
        if (nodes[elem].name == vertexIn) {
          reuse = elem
        }
      }
      if (reuse == undefined) {
        nodesElemIn.name = vertexIn;
        nodesElemIn.x = xLoc + Math.floor(Math.random() * 50) - 50;
        nodesElemIn.y = yLoc + Math.floor(Math.random() * 50) - 50;
        nodesElemIn.color = "aqua"

        nodes.push(nodesElemIn);
      }
    }



    if (vertexPred[0] == "p") {
      vertexType = "predicate";
      reuse = undefined;
      for (elem in nodes) {
        if (nodes[elem].name == vertexPred) {
          reuse = elem
        }
      }
      if (reuse == undefined) {
        nodesElemIn.color = "orange";
        nodesElemIn.name = vertexPred;
        nodesElemIn.x = xLoc + Math.floor(Math.random() * 50) - 20;
        nodesElemIn.y = yLoc + Math.floor(Math.random() * 50) - 20;
        nodesElemIn.radius =  "2px"
        // nodes.push(nodesElemIn);
      } else {
        nodes[reuse].color = "orange";
      }
      nodes.push(nodesElemIn);

    }





    if (vertexOut[0] == "p") {
      vertexType = "predicate";
      reuse = undefined;
      for (elem in nodes) {
        if (nodes[elem].name == vertexOut) {
          reuse = elem
        }
      }
      if (reuse == undefined) {
        nodesElemOut.color = "orange";
        nodesElemOut.name = vertexOut;
        nodesElemOut.x = xLoc + Math.floor(Math.random() * 50) - 20;
        nodesElemOut.y = yLoc + Math.floor(Math.random() * 50) - 20;
        // nodes.push(nodesElemOut);
                nodesElemOut.radius =  "2px"
      } else {
        nodes[reuse].color = "orange";
      }
      nodes.push(nodesElemOut);
    }
    if (vertexIn[0] == "p") {
      vertexType = "predicate";
      reuse = undefined;
      for (elem in nodes) {
        if (nodes[elem].name == vertexOut) {
          reuse = elem
        }
      }
      if (reuse == undefined) {
        nodesElemOut.color = "red";
        nodesElemOut.name = vertexOut;
        nodesElemOut.x = xLoc + Math.floor(Math.random() * 50) - 20;
        nodesElemOut.y = yLoc + Math.floor(Math.random() * 50) - 20;
                nodesElemOut.radius =  "2px"
        // nodes.push(nodesElemOut);
      } else {
        nodes[reuse].color = "red";
      }
      nodes.push(nodesElemOut);

    }
  }
  names = []
  nodesC = []
  for (node of nodes) {
    if (!names.includes(node.name)) {
      nodesC.push(node)
      names.push(node.name)
    }
  }

  for (elem of nodeLinks) {
    edge = elem.split(" ")[0]
    vertexIn = elem.split(" ")[1]
    vertexOut = elem.split(" ")[2];
      if (edge[0] == "p") {
        linksElem1 = {
          source: nodesC[vertexOut],
          target: nodesC[vertexIn],
          type: edge
        }
        linksElem2 = {
          source: nodesC[vertexOut],
          target: nodesC[vertexIn],
          type: edge
        }
        for (counter of nodesC) {
          if (counter.name == vertexOut) {
            linksElem1.source = counter
          }
          if (counter.name == edge) {
            linksElem1.target = counter
          }
        }
        for (counter of nodesC) {
          if (counter.name == edge) {
            linksElem2.source = counter
          }
          if (counter.name == vertexIn) {
            linksElem2.target = counter
          }
        }
        linksElem1.type = edge;
        linksElem2.type = edge;
        links.push(linksElem1);
        links.push(linksElem2);

      } else {
        linksElem = {
          source: nodesC[vertexOut],
          target: nodesC[vertexIn],
          type: edge
        }
        for (counter of nodesC) {
          if (counter.name == vertexOut) {
            linksElem.source = counter
          }
          if (counter.name == vertexIn) {
            linksElem.target = counter
          }
        }
        linksElem.type = edge;
        links.push(linksElem);
      }
  }
  return [nodesC, links]
}



function build(nodes, links, divSection) {
  const svg = divSection.append('svg').attr("width", "600px").attr("height", "300px");
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
    .attr("cx", function(d) {
      return d.x;
    })
    .attr("cy", function(d) {
      return d.y;
    })
    .text( function(d) {
      return d.name
    })
    .attr("r", function(d) {
      return d.radius
    })
    .attr("fill", function(d) {
      return d.color
    })


var text = svg.append("g")
  .attr("class", "texts")
  .selectAll("text")
  .data(nodes)
  .enter()
  .append("text")
  .attr("x", function(d) {
    return d.x;
  })
  .attr("y", function(d) {
    return d.y;
  })
  .text( function(d) {
    return d.name
  })
  .attr("font-family", "sans-serif")
  .attr("font-weight","bold")
  .attr("font-size", "10px")



  //add tick instructions:
  simulation.on("tick", tickActions);

  var link_force = d3.forceLink(links)
    .id(function(d) {
      return d.name;
    })
    .distance(10)



  simulation.force("links", link_force)


  //draw lines for the links
  var link = svg.append("g")
    .attr("class", "links")
    .selectAll("line")
    .data(links)
    .enter()
    .append("line")
    .attr("x1", function(d) {
      return d.source.x
    })
    .attr("y1", function(d) {
      return d.source.y
    })
    .attr("x2", function(d) {
      return d.target.x
    })
    .attr("y2", function(d) {
      return d.target.y
    })
    .attr("stroke-width", "2px")
    .attr("data-legend",function(d) { return d.type})
    .style("stroke", function(d) {
      if (d.type == "SubClassOf") {
        return "black"
      }
      if (d.type == "DisjointClasses") {
        return "red"
      }
      if (d.type == "ClassAssertion") {
        return "blue"
      }
      if (d.type == "Domain") {
        return "orange"
      }
      if (d.type == "Range") {
        return "orange"
      } else {
        return "purple"
      }
    });

var ordinal = d3.scaleOrdinal()
  .domain(["SubClassOf", "DisjointClasses", "ClassAssertion", "Domain", "Range", "Class", "Instance", "Property"])
  .range([ "black", "red", "blue", "orange", "orange","green","aqua","purple"]);

    svg.append("g")
      .attr("class", "legendOrdinal")
      .attr("transform", "translate(450,20)")
      .attr("overflow","visible")

    var legendOrdinal = d3.legendColor()
    	.scale(ordinal);

    svg.select(".legendOrdinal")
	  	.call(legendOrdinal);


  function tickActions() {
    //update circle positions each tick of the simulation
    node
      .attr("cx", function(d) {
        return d.x;
      })
      .attr("cy", function(d) {
        return d.y;
      });

    text
      .attr("x", function(d) {
        return d.x+4;
      })
      .attr("y", function(d) {
        return d.y-4;
      });

    //update link positions
    //simply tells one end of the line to follow one node around
    //and the other end of the line to follow the other node around
    link
      .attr("x1", function(d) {
        return d.source.x;
      })
      .attr("y1", function(d) {
        return d.source.y;
      })
      .attr("x2", function(d) {
        return d.target.x;
      })
      .attr("y2", function(d) {
        return d.target.y;
      });

  }
}

function tablebuild(columns, data, bodySection) {
  var table = bodySection.append('table')
  var tbody = table.append('tbody');

  // create a row for each object in the data
  var rows = tbody.selectAll('td')
    .data(data)
    .enter()
    .append('td');

  // create a cell in each row for each column
  var cells = rows.selectAll('tr')
    .data(function(row) {
      return columns.map(function(column) {
        return {
          column: column,
          value: row[column]
        };
      });
    })
    .enter()
    .append('tr')
    .text(function(d) {
      return d.column;
    })
    .append('td')
    .text(function(d) {
      return d.value;
    });

  return table;
}
