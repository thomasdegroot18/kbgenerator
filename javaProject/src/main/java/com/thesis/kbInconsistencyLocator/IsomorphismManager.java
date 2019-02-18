package com.thesis.kbInconsistencyLocator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class IsomorphismManager {
    private GeneralisedSubGraph Graph1;
    private GeneralisedSubGraph Graph2;

    // Test Subgraph with other subgraph owlOntologyGraph


    boolean CompareGraph(GeneralisedSubGraph Graph1, GeneralisedSubGraph Graph2){
        this.Graph1 = Graph1;
        this.Graph2 = Graph2;



        // If Size is not equal the two graphs can not be isomorphic.
        if (this.Graph1.AxiomSize() != this.Graph2.AxiomSize() ){
            return false;
        }


        //Get vertices sorted on degree and check if these match.
        if (!this.Graph2.GetVerticesDegree().equals(this.Graph1.GetVerticesDegree())){
            return false;
        }


        //TODO: CHECK INSTANCES AND CLASSES
//        // Check if instances match
//        if(this.Graph1 .getCountInstances() != this.Graph2.getCountInstances()){
//            return false;
//        }
//        // Check if classes match
//        if(this.Graph1 .getCountClasses() != this.Graph2.getCountClasses()){
//            return false;
//        }


        ArrayList<String> Graph1Vertex = this.Graph1.GetVerticesSet();
        ArrayList<String> Graph2Vertex = this.Graph2.GetVerticesSet();

        //Builds two array lists that store the used elements.
        ArrayList<String> emptyList1= new ArrayList<>();
        ArrayList<String> emptyList2= new ArrayList<>();

        // Checks it the two graphs are Isomorphic
        boolean IsoCheck = false;
        // Only Check for one graph as starting point. If no points can be met to one point it means that the graph is always not isomorphic.
        for (String VertexGraph1 : Graph1Vertex) {
            emptyList1.add(VertexGraph1);
            for (String VertexGraph2 : Graph2Vertex){
                if(IsoCheck){
                    continue;
                }

                emptyList2.add(VertexGraph2);
                IsoCheck = recursiveIsomorphicCheck(VertexGraph1, VertexGraph2, emptyList1, emptyList2);
                emptyList2.clear();
            }

            emptyList1.clear();
        }

        return IsoCheck;

    }

    private boolean recursiveIsomorphicCheck(String Graph1Vertex, String Graph2Vertex, ArrayList<String> Used1, ArrayList<String> Used2 ) {
        // Set boolean IsoCheck to false.
        boolean IsoCheck = false;

        // retrieve all outgoing edge vertex combination graph 1.
        ArrayList<String[]> Graph1TempEdgeVertices = Graph1.GetOutElements(Graph1Vertex);


        // retrieve all outgoing edge vertex combination graph 2.
        ArrayList<String[]> Graph2TempEdgeVertices = Graph2.GetOutElements(Graph2Vertex);


        // Check if second step neighbours have the same amount of outgoing edges.

        // Loop Through the first graph to find the neighbour list
        List<Integer> graph1Neighbours = new ArrayList<>();
        for (String[] Graph1Out: Graph1TempEdgeVertices){
            // Add the vertices count.
            graph1Neighbours.add(Graph1.GetOutElements(Graph1Out[0]).size());
        }

        // Loop through the second list set.
        List<Integer> graph2Neighbours = new ArrayList<>();
        for (String[] Graph2Out: Graph2TempEdgeVertices){
            graph2Neighbours.add(Graph2.GetOutElements(Graph2Out[0]).size());
        }

        // Sort the graphs to before matching them.
        Collections.sort(graph1Neighbours);
        Collections.sort(graph2Neighbours);


        // check if second step neighbours are equal.
        if (!graph1Neighbours.equals(graph2Neighbours)){
            return false;
        }

        // if sum of all outgoing edges is in equal return false.
        if (Graph1TempEdgeVertices.size() != Graph2TempEdgeVertices.size()){
            return false;
        }
        // Final return of all checks.
        if ((Used1.size() == Used2.size()) && (Used2.size() >= Graph2.GetVerticesSet().size())){
            return true;
        }

        // loop over the combinations of graph 1.
        // take a outgoing edge vertex combination.
        for (String[] Graph1Out : Graph1TempEdgeVertices){
            // loop over all the combinations of graph 2.
            // take a outgoing edge vertex combination
            if ((Used1.contains(Graph1Out[0])) ){
                continue;
            }

            for (String[] Graph2Out: Graph2TempEdgeVertices){
                // compare both the edge and the vertex if it is possible to link.
                if ( (Used2.contains(Graph2Out[0])) ){
                    continue;
                }

                // ADDED IN CHECK ON INSTANCE VS CLASS TODO: DONE
//                if (!(Graph2Out[0].charAt(0) == Graph1Out[0].charAt(0))){
//                    continue;
//                }

                // ADDED IN CHECK ON EDGES TODO: DONE
                try {
                    if (!(Graph1Out[1].equals(Graph2Out[1]))){
                        continue;
                    }
                } catch (Exception e ){
                    continue;
                }



                if (!IsoCheck) {
                    Used1.add(Graph1Out[0]);
                    Used2.add(Graph2Out[0]);
                    IsoCheck = recursiveIsomorphicCheck(Graph1Out[0], Graph2Out[0], Used1, Used2 );
                }

                if(!IsoCheck){
                    Used1.remove(Graph1Out[0]);
                    Used2.remove(Graph2Out[0]);
                }


                // call in new recursiveIsomorphicCheck for outgoing vertex if not all are used.
                // ELSE return true

            }


        }

        // Loop over all vertex that only have 1 edge and jump to that vertex

        if((Used1.size() < Graph1.GetVerticesSet().size()) && Graph1TempEdgeVertices.size() == 1 )
        {
            // Find the correct vertex with edges 1.


            String StringIDGraph1Vertex = Graph1.GetVertexDegree1(Used1);
            String StringIDGraph2Vertex = Graph2.GetVertexDegree1(Used2);
            if (StringIDGraph1Vertex == null || StringIDGraph2Vertex == null){
                return false;
            }
            Used1.add(StringIDGraph1Vertex);
            Used2.add(StringIDGraph2Vertex);

            IsoCheck = recursiveIsomorphicCheck(StringIDGraph1Vertex, StringIDGraph2Vertex, Used1, Used2 );
            if(!IsoCheck){
                Used1.remove(StringIDGraph1Vertex);
                Used2.remove(StringIDGraph2Vertex);
            }
        }

        return IsoCheck;

    }

}
