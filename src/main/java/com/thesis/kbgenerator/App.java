package com.thesis.kbgenerator;

import openllet.jena.PelletInfGraph;
import openllet.jena.PelletReasonerFactory;
import org.apache.jena.graph.*;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.triples.TripleString;
import org.rdfhdt.hdtjena.HDTGraph;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import openllet.core.OpenlletOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.annotation.ElementType;


/**
 *
 * @author Thomas de Groot
 */


public class App
{
    public static ResultSet sparqlQuery(Model model, String sparqlQuery) {
        //HDT hdt = HDTManager.mapIndexedHDT(fileHDT, null);
        ResultSet results = null;
        try {

            // Use Jena ARQ to execute the query.
            Query query = QueryFactory.create(sparqlQuery);
            QueryExecution qe = QueryExecutionFactory.create(query, model);

            results = qe.execSelect();
        } catch (Exception e) {
            System.out.println(e);
        }
        return results;
    }

    private static boolean WriteInconsistency(Graph graph, String tripleItem, FileOutputStream fileWriter, NodeFactory NodeFactoryName){




        org.apache.jena.graph.Node testNode = NodeFactoryName.createURI(tripleItem);



        GraphExtractExtended GraphExtr = new GraphExtractExtended(TripleBoundary.stopNowhere);
        Graph subGraph = null;
        try{
            subGraph = GraphExtr.extractExtend(testNode , graph);
        } catch (StackOverflowError e){
            System.out.println(e);
            return false;
        }


        OpenlletOptions.USE_TRACING = true;



        Model subModel = ModelFactory.createModelForGraph(subGraph);

        Model subModelNew = ModelFactory.createOntologyModel( PelletReasonerFactory.THE_SPEC, subModel);

        PelletInfGraph pellet = (PelletInfGraph) subModelNew.getGraph();

        if( !pellet.isConsistent() ) {
            // create an inference model using Pellet reasoner
            Model explanation = pellet.explainInconsistency();// print the explanation
            explanation.write( fileWriter, "TTL");
            System.out.println("Inconsistency Found!");
            return true;
        }
        return false;
    }




    public static void main( String[] args ) throws Exception {
        // Load HDT file using the hdt-java library
        HDT hdt = HDTManager.mapIndexedHDT("/home/thomas/thesis/HDTs/lod-10000T.hdt", null);




        System.out.println("Starting search");


        IteratorTripleString it = hdt.search("", "", "");

        // Create Jena wrapper on top of HDT.
        HDTGraph graph = new HDTGraph(hdt);
        NodeFactory NodeFactoryName = new NodeFactory();


//        String query = "SELECT ?a ?C ?D WHERE { ?a <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?C . ?a <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?D . ?C <http://www.w3.org/2002/07/owl#disjointWith> ?D . } limit 1";
//
//
//        Model model = ModelFactory.createModelForGraph(graph);
//        ResultSet results = sparqlQuery(model, query);
//        while (results.hasNext()){
//            System.out.println(results.next());
//        }
//
//
//        org.apache.jena.graph.Node testNode2 = NodeFactorytest.createURI("http://yago-knowledge.org/resource/SeaWorld_&_Busch_Gardens_Conservation_Fund");
        FileOutputStream fileWriter = new FileOutputStream(new File("../RDFs/myInconsistencies.ttl"));


        int iterator = 0;

        boolean Incons;

        byte[] strToBytes = ("\n New inconsistency: \n").getBytes();
        fileWriter.write(strToBytes);

        while(it.hasNext()){
            if (!(iterator%100000 == 0)){
                iterator += 1;
                it.next();
                continue;
            }
            TripleString item = it.next();

            String subject = item.getSubject().toString();
            // String Predicate = item.getPredicate().toString();
            String object = item.getObject().toString();

            Incons = WriteInconsistency(graph, object, fileWriter, NodeFactoryName);

            if (Incons){
                strToBytes = ("\n New inconsistency: \n").getBytes();
                fileWriter.write(strToBytes);
            }

            Incons = WriteInconsistency(graph, subject, fileWriter, NodeFactoryName);

            if (Incons){
                strToBytes = ("\n New inconsistency: \n").getBytes();
                fileWriter.write(strToBytes);
            }



            iterator += 1;
        }



    }



}

