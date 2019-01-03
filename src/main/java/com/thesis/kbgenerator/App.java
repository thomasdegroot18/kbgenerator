package com.thesis.kbgenerator;

import com.ctc.wstx.util.InternCache;
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
import java.lang.reflect.Array;
import java.util.*;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLOntology;


/**
 *
 * @author Thomas de Groot
 */


public class App

{
    private static byte[] strToBytes = ("\n New inconsistency: \n").getBytes();
    private static int MaxExplanations;
    private static Random rand = new Random();
    private static String[] classLabels;
    private static String[] individualLabels;




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


    private static void InconsistencyStandardizer(Set<OWLAxiom> InconsistencyExplanation, FileOutputStream fileWriter)
    {

        Map<Object, String> variableMap = new HashMap<>();
        List<String> ExplanationStringList = new ArrayList<>();



        int[] iterator = new int[2]; // The first element is the Class iterator, the second element is the Individual Iterator.
        for (OWLAxiom InconsistencyExplanationLine : InconsistencyExplanation){

            String StringLine;
            AxiomType RelationType = InconsistencyExplanationLine.getAxiomType();
            if (RelationType == AxiomType.CLASS_ASSERTION){
                StringLine = AxiomConverter(InconsistencyExplanationLine, variableMap, iterator);

            } else if (RelationType == AxiomType.DISJOINT_CLASSES){
                StringLine = AxiomConverter(InconsistencyExplanationLine, variableMap, iterator);

            } else if (RelationType == AxiomType.SUBCLASS_OF){
                StringLine = AxiomConverter(InconsistencyExplanationLine, variableMap, iterator);

            }  else if (RelationType == AxiomType.EQUIVALENT_CLASSES){
                StringLine = AxiomConverter(InconsistencyExplanationLine, variableMap, iterator);

            } else {
                throw new ClassCastException(RelationType.toString());
            }
            ExplanationStringList.add(StringLine);



        }
        try {
//            for (OWLAxiom InconsistencyExplanationLine : InconsistencyExplanation) { // Write out all the complete inconsistencies for examples Paper.
//                fileWriter.write((InconsistencyExplanationLine.toString()+"\n").getBytes());
//            }
            for (String StringLine : ExplanationStringList) { // Write to Output stream for Generalised output.
                fileWriter.write((StringLine + "\n") .getBytes());
            }
            GeneralisedSubGraph GeneralGraph = new GeneralisedSubGraph(ExplanationStringList);
            GeneralGraph.CompareGraph(GeneralGraph);
        } catch(IOException io) {
            io.printStackTrace();
        }
    }


    private static String AxiomConverter(OWLAxiom InconsistencyExplanationLine, Map<Object, String>  variableMap, int[] iterator){
        List SetOfVariables = StreamParser(InconsistencyExplanationLine);
        AxiomType RelationType = InconsistencyExplanationLine.getAxiomType();
        for (Object variable: SetOfVariables){
            if (!variableMap.containsKey(variable)){
                if ( variable.toString().contains("IndividualCheck82910283")) {
                    variableMap.put(variable, individualLabels[iterator[1]]);  // Iterator on Individual
                    iterator[1] ++;
                } else {
                    variableMap.put(variable, classLabels[iterator[0]]);
                    iterator[0] ++;
                }

            }
        }


        return RelationType.toString()+" "+variableMap.get(SetOfVariables.get(0))+" "+variableMap.get(SetOfVariables.get(1));
    }


    private static List StreamParser(OWLAxiom InconsistencyExplanationLine){
        List<String> NewList = new ArrayList<>();

        Object[] ListIndividuals = InconsistencyExplanationLine.individualsInSignature().toArray();
        for (Object ListItem: ListIndividuals ) {
            NewList.add(ListItem.toString()+"IndividualCheck82910283");
        }
        Object[] ListClasses = InconsistencyExplanationLine.classesInSignature().toArray();
        for (Object ListItem: ListClasses ) {
            NewList.add(ListItem.toString());
        }
        return NewList;
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
        // Running a set of test queries to check quickly if this graph can be susceptible to INCONSISTENCIES.

        String query = "SELECT ?a ?C ?D WHERE { ?a <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?C . ?a <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?D . ?C <http://www.w3.org/2002/07/owl#disjointWith> ?D . } LIMIT 5";
        String query2 = "SELECT ?a ?C WHERE { ?a <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?C . ?C <http://www.w3.org/2002/07/owl#disjointWith> ?C . } LIMIT 5";
        String query3 = "SELECT ?D ?C WHERE { ?C <http://www.w3.org/2002/07/owl#disjointWith> ?D . } LIMIT 5";

        QueryResultPrinter(model, query);
        QueryResultPrinter(model, query2);
        QueryResultPrinter(model, query3);


    }

    private static void LocateInconsistencies(HDT hdt, FileOutputStream fileWriter) throws Exception {

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


    private static void ClassLabelGenerator() // Generated Labels for the classes and the instances that can be used to map them.
    // LABELS are stored in the program itself.
    {

        int FinalLength = 100;  // ARBITRARY VALUE OF 100 instance and class labels are chosen.
        classLabels = new String[FinalLength ]; // storage for class labels
        individualLabels = new String[FinalLength ]; // storage for instance labels

        int iteratorLabels = 0;

        // labels are generated to be used by the algorithm to generalise the subgraphs to be able to compare them.
        while (iteratorLabels < FinalLength ){
            classLabels[iteratorLabels] = "C" + iteratorLabels; // CLASS LABELS start with a 'C'
            individualLabels[iteratorLabels] = "a" + iteratorLabels; // INSTANCE LABELS start with a 'a'
            iteratorLabels ++;
        }

    }


    public static void main( String[] args ) throws Exception { // args[0] = location of the HDT, args[1] = Location to store the output. args[2] = inconsistency explanations per subgraph

        // If the third argument is empty the amount of inconsistency explanations per subgraph is set to 10 else use
        // the amount given bij the user.
        if (!args[2].isEmpty()){
            MaxExplanations = Integer.parseInt(args[2]);
        } else{
            MaxExplanations = 10;
        }

        // Generate labels for the classes and instances.
        ClassLabelGenerator();

        // Print to the user that the HDT is being loaded. Can take a while.
        System.out.println("Print Loading in HDT");

        // Load HDT file using the hdt-java library
        HDT hdt = HDTManager.mapIndexedHDT(args[0], null);

        // Print to the user that the HDT is finished loading.
        System.out.println("Finished Loading HDT");

        // Set output Writer
        FileOutputStream fileWriter = new FileOutputStream(new File(args[1]));


//        // Create Jena wrapper on top of HDT.
        System.out.println("Creating Jena HDT graph");
//        HDTGraph graph = new HDTGraph(hdt);
//
//
//        // Create Models
//        System.out.println("Creating model from graph");
//        Model model = ModelFactory.createModelForGraph(graph);
//
//        System.out.println("Creating model from n3");
//        Model N3Model = ModelFactory.createDefaultModel().read("http://lov.okfn.org/dataset/lov/vocabs/veo/versions/2014-09-01.n3");
//
//        System.out.println("Running test Queries");
//        // Run test Queries
//        testQueries(model);
//        testQueries(N3Model);


        // Print to the user that system started finding inconsistencies.
        System.out.println("Start locating the inconsistencies.");

        // Test Inconsistencies
        LocateInconsistencies(hdt, fileWriter);

        // Print to the user that the system finished finding inconsistencies.
        System.out.println("Finished locating inconsistencies");







    }



}

