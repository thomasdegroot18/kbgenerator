

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
        sample.sort(function (a, b) {
            return a.value < b.value
        });
        sample.sort()


    const svg = d3.select('svg');
    const svgContainer = d3.select('#container');

    const margin = 80;
    const width = 1000 - 2 * margin;
    const height = 600 - 2 * margin;

    const chart = svg.append('g')
      .attr('transform', `translate(${margin}, ${margin})`);

    // Define the div for the tooltip
    var tooltip = d3.select("body").append("div")
        .attr("class", "tooltip")
        .style("opacity", 0);

    const xScale = d3.scaleBand()
      .range([0, width])
      .domain(sample.map((s) => s.Graphnumber))
      .padding(0.4)

    const yScale = d3.scaleLinear()
      .range([height, 0])
      .domain([1, sample[0].value]);


    const makeYLines = () => d3.axisLeft()
      .scale(yScale)

    chart.append('g')
      .attr('transform', `translate(0, ${height})`)
      .call(d3.axisBottom(xScale).tickValues([]));

    chart.append('g')
      .call(d3.axisLeft(yScale));


    chart.append('g')
      .attr('class', 'grid')
      .call(makeYLines()
        .tickSize(-width, 0, 0)
        .tickFormat('')
      )

    const barGroups = chart.selectAll()
      .data(sample)
      .enter()
      .append('g')

    barGroups
      .append('rect')
      .attr('class', 'bar')
      .attr('x', (g) => xScale(g.Graphnumber))
      .attr('y', (g) => yScale(g.value))
      .attr('height', (g) => height - yScale(g.value))
      .attr('width', xScale.bandwidth())
      .on('mouseenter', function (actual, i) {

        d3.select(this)
          .transition()
          .duration(300)
          .attr('opacity', 0.6)
          .attr('x', (a) => xScale(a.Graphnumber) - 5)
          .attr('width', xScale.bandwidth() + 10)

        const y = yScale(actual.value)

        line = chart.append('line')
          .attr('id', 'limit')
          .attr('x1', 0)
          .attr('y1', y)
          .attr('x2', width)
          .attr('y2', y)

      })
      .on('mouseleave', function () {
        d3.selectAll('.value')
          .attr('opacity', 1)

        d3.select(this)
          .transition()
          .duration(300)
          .attr('opacity', 1)
          .attr('x', (a) => xScale(a.Graphnumber))
          .attr('width', xScale.bandwidth())

        chart.selectAll('#limit').remove()
      })
      .on("click", function(d) {

        storedString = d.SparqlRequest.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll(". ", ". </br>").replace("WHERE {", "WHERE { </br>")
        console.log(storedString);
          tooltip.transition()
              .duration(200)
              .style("opacity", .9);
          tooltip.html(storedString)
              .style("left", (d3.event.pageX) + "px")
              .style("top", (d3.event.pageY) + "px");
          })
          .on("mousemove", function (){
            tooltip.transition()
            .duration(100).
            style("opacity", 0)
          })

          .on("dblclick",function(d){
            window.location = "graph.html"+ "?Graphnumber=" + d.Graphnumber;
          });

          barGroups.append("text")
          .attr('x', (a) => xScale(a.Graphnumber))
          .attr('y', (g) => yScale(g.value)+13)
          .attr("text-anchor", "left")
          .text(function(d) { return d.value; })
          .attr('opacity', function(d) {
            if(d.value == 0 ){return 0}
            else { return 1 }})
          .style("fill", "#000000");





    svg
      .append('text')
      .attr('class', 'label')
      .attr('x', -(height / 2) - margin)
      .attr('y', margin / 2.4)
      .attr('transform', 'rotate(-90)')
      .attr('text-anchor', 'middle')
      .text('Inconsistencies')

    svg.append('text')
      .attr('class', 'title')
      .attr('x', width / 2 + margin)
      .attr('y', 40)
      .attr('text-anchor', 'middle')
      .text('Inconsistencies per general graph')

    });
