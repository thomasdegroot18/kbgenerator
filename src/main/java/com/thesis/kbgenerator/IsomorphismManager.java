package com.thesis.kbgenerator;

import java.util.ArrayList;

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
        ArrayList<String> emptyList1= new ArrayList<>();
        ArrayList<String> emptyList2= new ArrayList<>();

        // Checks it the two graphs are Isomorphic
        boolean IsoCheck = recursiveIsomorphicCheck(Graph1.GetInstancesSet(), Graph2.GetInstancesSet(), emptyList1, emptyList2);
//        System.out.println(IsoCheck);
        return IsoCheck;

    }

    private boolean recursiveIsomorphicCheck(ArrayList<String> Graph1Vertex, ArrayList<String> Graph2Vertex, ArrayList<String> Used1, ArrayList<String> Used2 ) {

        // IF ALL elements of the graph ARE used RETURN TRUE
        if (Used1.size() == Graph1.size() ){
            return true;
        }

        // ALGORITHM

        // Set the validIsoFound as long as no mapping is found yet.
        boolean validIsoFound = false;

        // Loop through the given set of links of GRAPH 1.
        for (String vertexGraph1 : Graph1Vertex) {
            // Retrieve all outgoing links from the chosen vertex From GRAPH 1.
            ArrayList<String> graph1Out = this.Graph1.GetOutvertices(vertexGraph1);

            // Loop through all links of the given set of GRAPH 2.
            for (String vertexGraph2 : Graph2Vertex) {
                // Retrieve all outgoing links from the chosen vertex From GRAPH 2.
                ArrayList<String> graph2Out = this.Graph2.GetOutvertices(vertexGraph2);




                // IF the two outgoing vertices have the same size and are not yet used they are considered equal.
                // TODO: add in check that checks if the outgoing vertices have the same type. "Instance" or "Class"
                // TODO: add in check that checks if the outgoing links have the same type. "subClassOf", "InstanceOf", "DisjointWith", ...
                if (graph1Out.size() == graph2Out.size() && !(Used1.contains(vertexGraph1)) && !(Used2.contains(vertexGraph2))){

                    // Add both vertices to their own respective lists.
                    Used1.add(vertexGraph1);
                    Used2.add(vertexGraph2);

                    // Recursive without chosen values.
                    validIsoFound = recursiveIsomorphicCheck(graph1Out, graph2Out, Used1 , Used2);

                    // IF no isomorphism can be found the both elements are removed from the list such that they can later be combined with different vertices.
                    if (!validIsoFound){
                        Used1.remove(vertexGraph1);
                        Used2.remove(vertexGraph2);
                    }

                }

            }

        }

        // IF Graph1 and Graph2 ARE not equal AND never empty RETURN FALSE ELSE RETURN TRUE
        return validIsoFound;

    }

}
