package com.thesis.kbgenerator;

import org.apache.jena.base.Sys;

import java.util.ArrayList;
import java.util.Set;

public class IsomorphismManager {
    private GeneralisedSubGraph Graph1;
    private GeneralisedSubGraph Graph2;

    // Test Subgraph with other subgraph owlOntologyGraph


    boolean CompareGraph(GeneralisedSubGraph Graph1, GeneralisedSubGraph Graph2){
        this.Graph1 = Graph1;
        this.Graph2 = Graph2;


        // If Size is not equal the two graphs can not be isomorphic.
        if (Graph1.Axiomsize() != Graph2.Axiomsize() ){
            return false;
        }
        if(Graph1.getCountInstances() != Graph2.getCountInstances()){
            return false;
        }
        if(Graph1.getCountClasses() != Graph2.getCountClasses()){
            return false;
        }
        ArrayList<String> emptyList1= new ArrayList<String>();
        ArrayList<String> emptyList2= new ArrayList<String>();
        return recursiveIsomorphicCheck(Graph1.GetInstancesSet(), Graph2.GetInstancesSet(), emptyList1, emptyList2);

    }

    private boolean recursiveIsomorphicCheck(ArrayList<String> Graph1Vertex, ArrayList<String> Graph2Vertex, ArrayList<String> Used1, ArrayList<String> Used2 ) {

        // IF Graph1 and Graph2 ARE empty RETURN TRUE
        if (Used1.size() == Graph1.size() ){
            return true;
        }

        // ALGORITHM
        boolean validIsoFound = false;


        for (String vertexGraph1 : Graph1Vertex) {
            ArrayList<String> graph1Out = this.Graph1.GetOutVertexes(vertexGraph1);


            for (String vertexGraph2 : Graph2Vertex) {
                ArrayList<String> graph2Out = this.Graph2.GetOutVertexes(vertexGraph2);

                if (graph1Out.size() == graph2Out.size() && !(Used1.contains(vertexGraph1)) && !(Used2.contains(vertexGraph2))){


                    Used1.add(vertexGraph1);
                    Used2.add(vertexGraph2);
                    // Recursive without chosen values.
                    validIsoFound = recursiveIsomorphicCheck(graph1Out, graph2Out, Used1 , Used2);

                }


            }


        }


        // IF Graph1 and Graph2 ARE not equal AND never empty RETURN FALSE ELSE RETURN TRUE
        return validIsoFound;

    }

}
