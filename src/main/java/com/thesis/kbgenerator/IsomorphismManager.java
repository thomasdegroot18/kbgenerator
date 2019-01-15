package com.thesis.kbgenerator;

import java.util.ArrayList;
import java.util.HashMap;

final class IsomorphismManager {
    private GeneralisedSubGraph Graph1;
    private GeneralisedSubGraph Graph2;

    // Test Subgraph with other subgraph owlOntologyGraph


    boolean CompareGraph(GeneralisedSubGraph Graph1, GeneralisedSubGraph Graph2){
        this.Graph1 = Graph1;
        this.Graph2 = Graph2;

        // If Size is not equal the two graphs can not be isomorphic.
        if (Graph1.AxiomSize() != Graph2.AxiomSize() ){
            return false;
        }
        if(Graph1.getCountInstances() != Graph2.getCountInstances()){
            return false;
        }
        if(Graph1.getCountClasses() != Graph2.getCountClasses()){
            return false;
        }

        //Builds two array lists that store the used elements.
        HashMap<String, String> emptyList1= new HashMap<>();
        HashMap<String, String> emptyList2= new HashMap<>();

        // Checks it the two graphs are Isomorphic
        boolean IsoCheck = false;
        int iterator1 = 0;

        ArrayList<String> Graph1Vertex = Graph1.GetVerticesSet();
        ArrayList<String> Graph2Vertex = Graph2.GetVerticesSet();

        // Only Check for one graph as starting point. If no points can be met to one point it means that the graph is always not isomorphic.
        while(!IsoCheck && iterator1 < Graph2Vertex.size()){
            IsoCheck = recursiveIsomorphicCheck(Graph1Vertex.get(0), Graph1Vertex.get(iterator1), emptyList1, emptyList2);
            iterator1 ++;
        }

        return IsoCheck;

    }

    private boolean recursiveIsomorphicCheck(String Graph1Vertex, String Graph2Vertex, HashMap<String, String> Used1, HashMap<String, String> Used2 ) {
        // Set boolean IsoCheck to false.
        boolean IsoCheck = false;

        // retrieve all outgoing edge vertex combination graph 1.
        ArrayList<String[]> Graph1TempEdgeVertices = Graph1.GetOutElements(Graph1Vertex);

        // retrieve all outgoing edge vertex combination graph 2.
        ArrayList<String[]> Graph2TempEdgeVertices = Graph2.GetOutElements(Graph2Vertex);

        // if sum of all outgoing edges is in equal return false.
        if (Graph1TempEdgeVertices.size() != Graph2TempEdgeVertices.size()){
            return false;
        }

        //

        // Final return of all checks.
        if ((Used1.size() == Used2.size()) && (Used2.size() >= Graph2.GetVerticesSet().size())){
            return true;
        }

        // loop over the combinations of graph 1.
        // take a outgoing edge vertex combination.
        for (String[] Graph1Out : Graph1TempEdgeVertices){
            // loop over all the combinations of graph 2.
            // take a outgoing edge vertex combination
            for (String[] Graph2Out: Graph2TempEdgeVertices){
                // compare both the edge and the vertex if it is possible to link.



                // ADDED IN CHECK ON INSTANCE VS CLASS TODO: DONE
                if (!(Graph2Out[0].charAt(0) == Graph1Out[0].charAt(0))){
                    continue;
                }

                // ADDED IN CHECK ON EDGES TODO
                if (!(Graph1Out[1].equals(Graph2Out[1]))){
                    continue;
                }

                if (Used1.get(Graph1Out[0]) != null || Used2.get(Graph1Out[0]) != null ){
                    continue;

                }

                if (!IsoCheck) {
                    Used1.put(Graph2Out[0],Graph1Out[0]);
                    Used2.put(Graph1Out[0],Graph2Out[0]);
                    IsoCheck = recursiveIsomorphicCheck(Graph1Out[0], Graph2Out[0], Used1, Used2 );
                }

                if(!IsoCheck){
                    Used1.remove(Graph2Out[0]);
                    Used2.remove(Graph1Out[0]);
                }


                // call in new recursiveIsomorphicCheck for outgoing vertex if not all are used.
                // ELSE return true

            }


        }




        return IsoCheck;

    }

}
