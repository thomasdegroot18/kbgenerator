
json_file = open("Inconsistencies.json", 'w')
json_file.write('[')
number = ""
ttl_file = open("INCONSISTENCIES-LOD-a-lot.ttl", 'r')
for line in ttl_file.read().split("New general inconsistency: "):
    i = 1
    Graph = line.split("New general inconsistency:")
    for elem in Graph:
        codex = []
        for elemelem in elem.split("\n"):
            if len(elemelem) == 0:
                pass
            elif "General graph" in elemelem:
                 number = elemelem.split(": ")[1]
            elif "ELECT" in elemelem:
                Sparql = elemelem
            elif "Stopped" in elemelem:
                pass
            else:
                codex.append(elemelem)

        codexStr = ", ".join(codex)
        if number == "1":
            i += 1
            json_file.write('{"Graphnumber" : '+str(i)+', "SparqlRequest" :"' + Sparql +'", "Graph" :"' + codexStr+'"}')
        elif len(number) > 0:
            i += 1
            json_file.write(', {"Graphnumber" : '+str(i)+', "SparqlRequest" :"' + Sparql +'", "Graph" :"' + codexStr+'"}')
json_file.write(']')
