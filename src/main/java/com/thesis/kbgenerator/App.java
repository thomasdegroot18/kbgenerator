package com.thesis.kbgenerator;

import openllet.owlapi.explanation.PelletExplanation;
import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;
import org.apache.jena.graph.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.triples.TripleString;
import org.rdfhdt.hdtjena.HDTGraph;

import java.io.*;
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


    private static OWLOntology PipeModel(Set<String> subModel) throws Exception{
        // Start an Input stream that uses a pipe to stream the input to an output stream, this gives the possibility
        // to convert a set of strings easy to an a OWL Ontology.
        PipedInputStream in = new PipedInputStream();
        // Start an PipedOutputStream with input the PipedInputStream.
        PipedOutputStream out = new PipedOutputStream(in);

        // Lambda Runnable task to convert the set of strings to an output stream.
        Runnable task = () -> {
            try {
                for (String Test: subModel){
                    // Write to outputstream as bytes.
                    out.write(Test.getBytes());
                }
                out.close();
            }
            //When Error happens catch it and print stacktrace.
            catch(IOException io) {
                io.printStackTrace();
            }
        };

        // Starts the new tread. to push the strings to the OWLOntology.
        new Thread(task).start();


        // Create a new OWLOntology and return the filled OWL ontology with the in stream.
        return OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(in);
    }


    private static void WriteInconsistencyModel(Set<String> subModel, FileOutputStream fileWriter) throws Exception {
        // Retrieve OWL ontology with a PipeModel. The model pipes the set of Strings from the submodel to the OWL Ontology.
        OWLOntology ontology = PipeModel(subModel);

        // Starting up the Pellet Explanation module.
        PelletExplanation.setup();

        // Create the reasoner and load the ontology with the open pellet reasoner.
        OpenlletReasoner reasoner = OpenlletReasonerFactory.getInstance().createReasoner(ontology);

        // Create an Explanation reasoner with the Pellet Explanation and the Openllet Reasoner modules.
        PelletExplanation expGen = new PelletExplanation(reasoner);

        // Find the Set of the Explanations that show that the SubGraph is inconsistent.
        Set<Set<OWLAxiom>> exp = expGen.getInconsistencyExplanations(MaxExplanations);

        // Loop through the set of explanations and standardize the Inconsistencies.
        for(Set<OWLAxiom> InconsistencyExplanation: exp){

            // WIL BE DEPRECATED IN FUTURE VERSIONS: Writing to file split between inconsistencies. strToBytes.
            fileWriter.write(strToBytes);

            // Standardize the inconsistency and write to file.
            InconsistencyStandardizer(InconsistencyExplanation, fileWriter);

        }

    }


    private static void WriteInconsistencySubGraph(HDT hdt, String tripleItem, FileOutputStream fileWriter) throws Exception{
        // Calls the GraphExtract which is Extended by Thomas de Groot to use the HDT instead of the JENA graph.
        // The TripleBoundary is set to stopNowhere Such that the model keeps going until my own stop criteria.
        GraphExtractExtended GraphExtr = new GraphExtractExtended(TripleBoundary.stopNowhere);

        // The subgraph is a set of strings. That stores up to 5000 triples.
        Set<String> subGraph = null;
        try{
            // Try to extract a the subgraph from the HDT.
            subGraph = GraphExtr.extractExtend(tripleItem , hdt);
        } catch (StackOverflowError e){
            // Can print the error if overflow happens.
            e.printStackTrace();
        }

        // Go to next step. Find (if any) Inconsistencies.
        WriteInconsistencyModel(subGraph, fileWriter);

    }


    private static void LocateInconsistencies(HDT hdt, FileOutputStream fileWriter) throws Exception {
        // Locates the inconsistencies by looping through the graph over a large selection of triples.


        // Get the Iterator tripleString to loop through.
        IteratorTripleString it = hdt.search("","","");

        // While there is a triple the loop continues.
        while(it.hasNext()){

            // As it would not be scalable to use all the triples as starting point, as well as that the expectation is
            // that triples are connected to each other, not every triple needs to be evaluated.
            // A selection of triples is chosen at random.
            // Can be changed later to a selection of triples that meet a certain criteria.

            // at the moment every 1 out of 100 triples is taken.
            if (rand.nextDouble() < 0.01) {
                it.next();
                continue;
            }

            // If the loop is not triggered the next element from the triplestring is taken.
            TripleString item = it.next();

            // both the subject and the object are taken to build the subgraph. With the expectation that the subject
            // graph encompasses the object graph. With the exception when the subject graph gets to large and misses
            // some of the depth the object graph does take into account.
            String subject = item.getSubject().toString();
            String object = item.getObject().toString();

            // Find all the inconsistencies in the first subgraph(Object)
            WriteInconsistencySubGraph(hdt, object, fileWriter);

            // Find all the inconsistencies in the second subgraph(Subject)
            WriteInconsistencySubGraph(hdt, subject, fileWriter);


        }
    }


    private static ResultSet sparqlQuery(Model model, String sparqlQuery) {
        // Set the result set to null.
        ResultSet results = null;

        // Try to execute the query. Could throw errors when creating the factory.
        try {

            // Use Jena ARQ to execute the query. Firstly creating the query.
            Query query = QueryFactory.create(sparqlQuery);

            // Executing the query.
            QueryExecution qe = QueryExecutionFactory.create(query, model);

            //Get the selection of the executed query.
            results = qe.execSelect();
        } catch (Exception e) {
            // Catch Exception and print th message.
            e.printStackTrace();
        }
        return results;
    }


    private static void QueryResultPrinter(Model model, String query){
        //  Run query  and retrieve the results as ResultSet.
        ResultSet results = sparqlQuery(model, query);

        // Print for every result the result line.
        while (results.hasNext()){
            System.out.println(results.next());
        }
    }

    private static void testQueries(Model model){
        // Running a set of test queries to check quickly if this graph can be susceptible to INCONSISTENCIES.

        // Check an inconsistency. If the graph has this pattern the graph is inconsistent.
        String query = "SELECT ?a ?C ?D WHERE { ?a <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?C . ?a <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?D . ?C <http://www.w3.org/2002/07/owl#disjointWith> ?D . } LIMIT 5";

        // Check a second inconsistency. If the graph has this pattern the graph is inconsistent.
        String query2 = "SELECT ?a ?C WHERE { ?a <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?C . ?C <http://www.w3.org/2002/07/owl#disjointWith> ?C . } LIMIT 5";

        // Final check, if the query has disjoints the graph can be inconsistent. If the query does not have disjoints it is still possible to be inconsistent. It will only be less plausible to happen.
        String query3 = "SELECT ?D ?C WHERE { ?C <http://www.w3.org/2002/07/owl#disjointWith> ?D . } LIMIT 5";

        // test all three queries.
        QueryResultPrinter(model, query);
        QueryResultPrinter(model, query2);
        QueryResultPrinter(model, query3);


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


    public static void main( String[] args ) throws Exception {
        /* main method that generates Inconsistencies when found.
         * @params {@code args[0] } location of the HDT
         * @params {@code args[1]} Location to store the output
         * @params {@code args[2] } inconsistency explanations per subgraph
         *
         * @returns void
         *
         */

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


        // Create Jena wrapper on top of HDT.
        System.out.println("Creating Jena HDT graph");
        HDTGraph graph = new HDTGraph(hdt);


        // Create Models
        System.out.println("Creating model from graph");
        Model model = ModelFactory.createModelForGraph(graph);

        System.out.println("Creating model from n3");
        Model N3Model = ModelFactory.createDefaultModel().read("http://lov.okfn.org/dataset/lov/vocabs/veo/versions/2014-09-01.n3");

        System.out.println("Running test Queries");
        // Run test Queries
        testQueries(model);
        testQueries(N3Model);


        // Print to the user that system started finding inconsistencies.
        System.out.println("Start locating the inconsistencies.");

        // Test Inconsistencies
        LocateInconsistencies(hdt, fileWriter);

        // Print to the user that the system finished finding inconsistencies.
        System.out.println("Finished locating inconsistencies");

    }

}

