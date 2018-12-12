package com.thesis.kbgenerator;

import openllet.jena.PelletInfGraph;
import openllet.jena.PelletReasonerFactory;
import org.apache.jena.base.Sys;
import org.apache.jena.graph.*;
import org.apache.jena.util.iterator.ExtendedIterator;
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

    private static boolean WriteInconsistencySubGraph(Graph graph, String tripleItem, FileOutputStream fileWriter, NodeFactory NodeFactoryName){

        org.apache.jena.graph.Node testNode = NodeFactoryName.createURI(tripleItem);


        GraphExtractExtended GraphExtr = new GraphExtractExtended(TripleBoundary.stopNowhere);
        Graph subGraph = null;
        try{
            subGraph = GraphExtr.extractExtend(testNode , graph);
        } catch (StackOverflowError e){
            System.out.println(e);
            return false;
        }

        return  WriteInconsistencyGraph(subGraph, fileWriter);

    }

    private static boolean WriteInconsistencyGraph(Graph graph, FileOutputStream fileWriter){
        System.out.println(graph.size());

        Model subModel = ModelFactory.createModelForGraph(graph);
        return WriteInconsistencyModel(subModel, fileWriter);

    }

    private static boolean WriteInconsistencyModel(Model subModel, FileOutputStream fileWriter){
        OpenlletOptions.USE_TRACING = true;
        Model subModelNew = ModelFactory.createOntologyModel( PelletReasonerFactory.THE_SPEC, subModel);

        PelletInfGraph pellet = (PelletInfGraph) subModelNew.getGraph();

//        subModel.write(System.out, "TTL");

        if( !pellet.isConsistent() ) {
            // create an inference model using Pellet reasoner
            Model explanation = pellet.explainInconsistency();// print the explanation
            explanation.write( fileWriter, "TTL");
            System.out.println("Inconsistency Found!");
            return true;
        }
        return false;
    }


    public static void QueryResultPrinter(Model model, String query){
        ResultSet results = sparqlQuery(model, query);
        while (results.hasNext()){
            System.out.println(results.next());
        }
    }

    public static void testQueries(Model model){

        String query = "SELECT ?a ?C ?D WHERE { ?a <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?C . ?a <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?D . ?C <http://www.w3.org/2002/07/owl#disjointWith> ?D . } LIMIT 5";
        String query2 = "SELECT ?a ?C WHERE { ?a <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?C . ?C <http://www.w3.org/2002/07/owl#disjointWith> ?C . } LIMIT 5";
        String query3 = "SELECT ?D ?C WHERE { ?C <http://www.w3.org/2002/07/owl#disjointWith> ?D . } LIMIT 5";

        QueryResultPrinter(model, query);
        QueryResultPrinter(model, query2);
        QueryResultPrinter(model, query3);


    }

    public static void testConsistency(IteratorTripleString it, Graph graph, FileOutputStream fileWriter) throws Exception {


        byte[] strToBytes = ("\n New inconsistency: \n").getBytes();
        fileWriter.write(strToBytes);

        NodeFactory NodeFactoryName = new NodeFactory();

        int iterator = 0;
        boolean Incons;
        while(it.hasNext()){
            if (!(iterator%10500 == 0)){
                iterator += 1;
                it.next();
                continue;
            }
            TripleString item = it.next();

            String subject = item.getSubject().toString();
            String object = item.getObject().toString();


            Incons = WriteInconsistencySubGraph(graph, object, fileWriter, NodeFactoryName);

            if (Incons){
                strToBytes = ("\n New inconsistency: \n").getBytes();
                fileWriter.write(strToBytes);
            }

            Incons = WriteInconsistencySubGraph(graph, subject, fileWriter, NodeFactoryName);

            if (Incons){
                strToBytes = ("\n New inconsistency: \n").getBytes();
                fileWriter.write(strToBytes);
            }



            iterator += 1;
        }
    }



    public static void main( String[] args ) throws Exception {
        // Load HDT file using the hdt-java library
        System.out.println("Print Loading in HDT");
        HDT hdt = HDTManager.mapIndexedHDT("/home/thomas/thesis/HDTs/dblp-20170124.hdt", null);


        // Create Jena wrapper on top of HDT.
        HDTGraph graph = new HDTGraph(hdt);

        // Set output Writer
        FileOutputStream fileWriter = new FileOutputStream(new File("../RDFs/myInconsistencies.ttl"));

        // Create Models
        Model model = ModelFactory.createModelForGraph(graph);

        Model NewModel = ModelFactory.createDefaultModel().read("http://lov.okfn.org/dataset/lov/vocabs/veo/versions/2014-09-01.n3");

        // Run test Queries
        testQueries(model);
        //testQueries(NewModel);



        // Test Inconsistencies
        System.out.println(WriteInconsistencyModel(NewModel, fileWriter));
        //testConsistency(hdt.search("","",""), graph, fileWriter);








    }



}

