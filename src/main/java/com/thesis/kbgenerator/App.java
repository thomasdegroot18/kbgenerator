package com.thesis.kbgenerator;

import openllet.jena.PelletInfGraph;
import openllet.jena.PelletReasonerFactory;
import openllet.owlapi.explanation.PelletExplanation;
import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;
import org.apache.jena.base.Sys;
import org.apache.jena.graph.*;
import org.apache.jena.riot.SysRIOT;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.triples.TripleString;
import org.rdfhdt.hdtjena.HDTGraph;

import java.io.*;
import java.util.Random;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.Set;


/**
 *
 * @author Thomas de Groot
 */


public class App

{
    private static byte[] strToBytes = ("\n New inconsistency: \n").getBytes();
    private static int MaxExplanations;
    private static Random rand = new Random();
    private static ResultSet sparqlQuery(Model model, String sparqlQuery) {

        //HDT hdt = HDTManager.mapIndexedHDT(fileHDT, null);
        ResultSet results = null;
        try {

            // Use Jena ARQ to execute the query.
            Query query = QueryFactory.create(sparqlQuery);
            QueryExecution qe = QueryExecutionFactory.create(query, model);

            results = qe.execSelect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return results;
    }

    private static void WriteInconsistencySubGraph(HDT hdt, String tripleItem, FileOutputStream fileWriter) throws Exception{

        GraphExtractExtended GraphExtr = new GraphExtractExtended(TripleBoundary.stopNowhere);
        Set<String> subGraph = null;
        try{
            subGraph = GraphExtr.extractExtend(tripleItem , hdt);
        } catch (StackOverflowError e){
            System.out.println(e.getMessage());
        }

        WriteInconsistencyModel(subGraph, fileWriter);

    }

    private static OWLOntology PipeModel(Set<String> subModel) throws Exception{


        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);

        // Lambda Runnable
        Runnable task = () -> {
            try {
                for (String Test: subModel){
                    out.write(Test.getBytes());
                }
                out.close();
            }
            catch(IOException io) {
                io.printStackTrace();
            }
        };

        new Thread(task).start();



        return OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(in);
    }


    private static void InconsistencyStandardizer(Set<OWLAxiom> InconsistencyExplanation, FileOutputStream fileWriter) throws Exception
    {

        for (OWLAxiom InconsistencyExplanationLine : InconsistencyExplanation){
            fileWriter.write((InconsistencyExplanationLine.toString()+"\n").getBytes());
        }
    }


    private static void WriteInconsistencyModel(Set<String> subModel, FileOutputStream fileWriter) throws Exception {


        OWLOntology ontology = PipeModel(subModel);

        PelletExplanation.setup();
        // Create the reasoner and load the ontology
        OpenlletReasoner reasoner = OpenlletReasonerFactory.getInstance().createReasoner(ontology);


        // Create an clashExplanation generator
        PelletExplanation expGen = new PelletExplanation(reasoner);


        Set<Set<OWLAxiom>> exp = expGen.getInconsistencyExplanations(MaxExplanations);

        for(Set<OWLAxiom> InconsistencyExplanation: exp){
            fileWriter.write(strToBytes);
            InconsistencyStandardizer(InconsistencyExplanation, fileWriter);

        }

    }


    private static void QueryResultPrinter(Model model, String query){
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

    private static void testConsistency(HDT hdt, FileOutputStream fileWriter) throws Exception {

        int iterator = 0;
        IteratorTripleString it = hdt.search("","","");

        while(it.hasNext()){
            if (!(iterator%(10*(rand.nextInt(50))+1) == 0)){
                iterator ++;
                it.next();
                continue;
            }
            TripleString item = it.next();

            String subject = item.getSubject().toString();
            String object = item.getObject().toString();


            WriteInconsistencySubGraph(hdt, object, fileWriter);


            WriteInconsistencySubGraph(hdt, subject, fileWriter);

            iterator ++;

        }
    }



    public static void main( String[] args ) throws Exception {
        // Load HDT file using the hdt-java library
        if (!args[2].isEmpty()){
            MaxExplanations = Integer.parseInt(args[2]);
        } else{
            MaxExplanations = 10;
        }

        System.out.println("Print Loading in HDT");
        HDT hdt = HDTManager.mapIndexedHDT(args[0], null);

        // Set output Writer
        FileOutputStream fileWriter = new FileOutputStream(new File(args[1]));

        System.out.println("Finished Loading HDT");

//        // Create Jena wrapper on top of HDT.
//        HDTGraph graph = new HDTGraph(hdt);
//
//
//        // Create Models
//        Model model = ModelFactory.createModelForGraph(graph);
//
//        Model NewModel = ModelFactory.createDefaultModel().read("http://lov.okfn.org/dataset/lov/vocabs/veo/versions/2014-09-01.n3");
//
//        // Run test Queries
//        testQueries(model);
        //testQueries(NewModel);



        // Test Inconsistencies
        testConsistency(hdt, fileWriter);







    }



}

