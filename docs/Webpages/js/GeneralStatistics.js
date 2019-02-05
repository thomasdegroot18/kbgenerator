
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

//usage
// CoeffandDegree.json
readTextFile("data/CoeffandDegree.json", function(text){
  var CoeffandDegree = JSON.parse(text);
  // freebasenotails.json
  readTextFile("data/OccurencesTail.json", function(text){
    var OccurencesTail = JSON.parse(text);
    // freebasetails.json
    readTextFile("data/occurencesNoTail.json", function(text){
      var occurencesNoTail = JSON.parse(text);
      // kbstat.json
      readTextFile("data/kbstat.json", function(text){
        var kbstat = JSON.parse(text);
        // freebaseOcc.json
        readTextFile("data/Graphnumber.json", function(text){
          var Graphnumber = JSON.parse(text);


          console.log(CoeffandDegree)
          console.log(OccurencesTail)
          console.log(occurencesNoTail)
          console.log(kbstat)
          console.log(Graphnumber)


              var origin = [480, 250], j = 16, points = [], alpha = 0, beta = 0, startAngle = Math.PI/4;
              var svg    = d3.select('svg').call(d3.drag().on('drag', dragged).on('start', dragStart).on('end', dragEnd)).append('g');
              var mx, my, mouseX, mouseY;

              var surface = d3._3d()
                  .scale(10)
                  .x(function(d){ return d.x; })
                  .y(function(d){ return d.y; })
                  .z(function(d){ return d.z; })
                  .origin(origin)
                  .rotateY(startAngle)
                  .rotateX(-startAngle)
                  .shape('SURFACE', j*2);

              var color = d3.scaleLinear();

              function processData(data, tt){

                  var planes = svg.selectAll('path').data(data, function(d){ return d.plane; });

                  planes
                      .enter()
                      .append('path')
                      .attr('class', '_3d')
                      .attr('fill', colorize)
                      .attr('opacity', 0)
                      .attr('stroke-opacity', 0.1)
                      .merge(planes)
                      .attr('stroke', 'black')
                      .transition().duration(tt)
                      .attr('opacity', 1)
                      .attr('fill', colorize)
                      .attr('d', surface.draw);

                  planes.exit().remove();

                  d3.selectAll('._3d').sort(surface.sort);

              }

              function colorize(d){
                  var _y = (d[0].y + d[1].y + d[2].y + d[3].y)/4;
                  return d.ccw ? d3.interpolateSpectral(color(_y)) : d3.color(d3.interpolateSpectral(color(_y))).darker(2.5);
              }

              function dragStart(){
                  mx = d3.event.x;
                  my = d3.event.y;
              }

              function dragged(){
                  mouseX = mouseX || 0;
                  mouseY = mouseY || 0;
                  beta   = (d3.event.x - mx + mouseX) * Math.PI / 230 ;
                  alpha  = (d3.event.y - my + mouseY) * Math.PI / 230  * (-1);
                  processData(surface.rotateY(beta + startAngle).rotateX(alpha - startAngle)(points), 0);
              }

              function dragEnd(){
                  mouseX = d3.event.x - mx + mouseX;
                  mouseY = d3.event.y - my + mouseY;
              }

              function init(eq){
                  points = [];

                  for(var z = -j; z < j; z++){
                      for(var x = -j; x < j; x++){
                          points.push({x: x, y: eq(x, z), z: z});
                      }
                  }

                  var yMin = d3.min(points, function(d){ return d.y; });
                  var yMax = d3.max(points, function(d){ return d.y; });

                  color.domain([yMin, yMax]);
                  processData(surface(points), 1000);
              }

              function change(){
                  var rn1 = Math.floor(d3.randomUniform(1, 12)());
                  var eqa = function(x, z){
                      return Math.cos(Math.sqrt(x*x+z*z)/5*Math.PI)*rn1;
                  };
                  init(eqa);
              }

              d3.selectAll('button').on('click', change);

              change();


        });
      });
    });
  });
});
