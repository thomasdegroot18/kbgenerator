package com.thesis.kbgenerator;

import openllet.owlapi.explanation.PelletExplanation;
import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;
import org.apache.jena.base.Sys;
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
import org.semanticweb.owlapi.reasoner.InferenceType;


/**
 *
 * @author Thomas de Groot
 */


public class App

{
    private static byte[] strToBytes = ("\nNew inconsistency: \n").getBytes();  // Small string to separate different explanations.
    private static int MaxExplanations;                                         // Max explanation Storage.
    private static Random rand = new Random();                                  // Random Instantiation
    private static String[] classLabels;                                        // ClassLabel storage
    private static String[] individualLabels;                                   // InstanceLabel Storage
    private static boolean verbose;                                             // Verbosity storage
    private static List<GeneralisedSubGraph> GeneralGraphs = new ArrayList<>(); // Generalised SubGraph Storage
    private static int InconsistenciesHit = 0;                                  // Counter of inconsistencies
    private static int TotalInconsistenciesBeforeBreak;                         // Break terminator for inconsistencies hit
    private static boolean UnBreakable;                                         // Stores boolean for breaking after amount of Inconsistencies
    private static IsomorphismManager IsoChecker = new IsomorphismManager();    // Start IsomorphismManager


    private static List StreamParser(OWLAxiom InconsistencyExplanationLine){
        // Instantiate a new Array List.
        List<String> NewList = new ArrayList<>();

        // Retrieve the individuals as a array of objects.
        Object[] ListIndividuals = InconsistencyExplanationLine.individualsInSignature().toArray();

        // Retrieve the classes as a array of objects.
        Object[] ListClasses = InconsistencyExplanationLine.classesInSignature().toArray();

        // Instantiate the value.
        int value;
        int prevValue = 0;

        // Loop through the list of individuals.
        for (Object ListItem: ListIndividuals ) {
            // generate the value where the object is found in the line.
            value = InconsistencyExplanationLine.toString().indexOf(ListItem.toString());
            // If not found throw error.
            if (value < 0){
                throw new StackOverflowError("Nothing found in Inconsistency Explanation, This is not possible. check" +
                        " input. " + ListItem.toString() + "  " + InconsistencyExplanationLine.toString());
            }

            // Check if the value is larger or smaller than the previous value. This is needed to know as to which side the item needs to be added.

            // If the value is larger than the previous value it needs to be appended to back.
            if (value > prevValue){
                // add to newList
                NewList.add(ListItem.toString()+"IndividualCheck82910283");
                // Set the next value
                prevValue = value;
            } else if (value < prevValue) {
                // If the value is smaller than the prevValue it has to be added to the front.
                NewList.add(0, ListItem.toString()+"IndividualCheck82910283");
                // Set the next value
                prevValue = value;
                // If it ever hits this check it means that the item occurs twice. It means that the value is equal.
            } else {
                // Shout warning just in case.
                System.out.println("Item occurred twice Check if it problem. "+ InconsistencyExplanationLine.toString());
                NewList.add(ListItem.toString()+"IndividualCheck82910283");
            }
        }


        for (Object ListItem: ListClasses ) {
            // generate the value where the object is found in the line.
            value = InconsistencyExplanationLine.toString().indexOf(ListItem.toString());
            // If not found throw error.
            if (value < 0) {
                throw new StackOverflowError("Nothing found in Inconsistency Explanation, This is not possible. check" +
                        " input. " + ListItem.toString() + "  " + InconsistencyExplanationLine.toString());
            }
            // Check if the value is larger or smaller than the previous value. This is needed to know as to which side the item needs to be added.

            // If the value is larger than the previous value it needs to be appended to back.
            if (value > prevValue){
                // add to newList
                NewList.add(ListItem.toString());
                // Set the next value
                prevValue = value;
            } else if (value < prevValue) {
                // If the value is smaller than the prevValue it has to be added to the front.
                NewList.add(0, ListItem.toString());
                // Set the next value
                prevValue = value;
                // If it ever hits this check it means that the item occurs twice. It means that the value is equal.
            } else {
                // Shout warning just in case.
                System.out.println("Item occurred twice Check if it problem. " + InconsistencyExplanationLine.toString());
                NewList.add(ListItem.toString());
            }

        }
        return NewList;
    }


    private static String AxiomConverter(OWLAxiom InconsistencyExplanationLine, Map<Object, String>  variableMap, int[] iterator){

        // Get the list of variables in the correct order.
        List SetOfVariables = StreamParser(InconsistencyExplanationLine);

        // Gets the axiom type of the line.
        AxiomType RelationType = InconsistencyExplanationLine.getAxiomType();
        // For both elements of the class/instance list.
        for (Object variable: SetOfVariables){
            // Check if the variable map contains the key for the variable(class/Instance)
            if (!variableMap.containsKey(variable)){
                // If there is no key yet made, the key is added to map.
                // Now we check if the variable is an instance or not. By checking if it hits an certain string.
                // TODO: low priority. Change the IndividualCheck82910283 to a less breakable check.
                if ( variable.toString().contains("IndividualCheck82910283")) {
                    // add the variable and new label to the map and increase the value with one
                    variableMap.put(variable, individualLabels[iterator[1]]);  // Iterator on Individual
                    iterator[1] ++;

                } else {
                    // add the variable and new label to the map and increase the value with one.
                    variableMap.put(variable, classLabels[iterator[0]]); // Iterator on Class
                    iterator[0] ++;
                }

            }
        }

        // return the string line with a generalised variable, Takes RelationType + first element + second element.
        return RelationType.toString()+" "+variableMap.get(SetOfVariables.get(0))+" "+variableMap.get(SetOfVariables.get(1));
    }


    private static void InconsistencyStandardizer(Set<OWLAxiom> InconsistencyExplanation, FileOutputStream fileWriter)
    {
        // Instantiate the variable map storing all the variables/labels and the combined Cleaned labels
        // Due being declared in this scope the changes made to the HashMap are also stored in this scoped, and are
        // inherited in deeper layered functions(AxiomConverter and StreamParser).
        Map<Object, String> variableMap = new HashMap<>();

        //Stores the lines of the explanations in a list.
        List<String> ExplanationStringList = new ArrayList<>();


        int[] iterator = new int[2]; // The first element is the Class iterator, the second element is the Individual Iterator.

        // Loop through the Inconsistency line per line to replace the Classes and the instances with the correct values.
        for (OWLAxiom InconsistencyExplanationLine : InconsistencyExplanation){
            // Instantiate the StringLine in correct scope.
            String StringLine;

            // Get the axiom type.
            AxiomType RelationType = InconsistencyExplanationLine.getAxiomType();

            // For each relationTYPE a function is called. At the moment only these 4 RelationTypes are used.
            // TODO Add in more relation types.

            // class assertion: <a1> <rdf:type> <C1>
            if (RelationType == AxiomType.CLASS_ASSERTION){
                // Gets the new clean relation graph with generalised names.
                StringLine = AxiomConverter(InconsistencyExplanationLine, variableMap, iterator);

            // Disjoint class: <C1> <owl:disjointWith> <C2>
            } else if (RelationType == AxiomType.DISJOINT_CLASSES){
                // Gets the new clean relation graph with generalised names.
                StringLine = AxiomConverter(InconsistencyExplanationLine, variableMap, iterator);

            // Subclass of: <C1> <rdf:subclassOf> <C2>
            } else if (RelationType == AxiomType.SUBCLASS_OF){
                // Gets the new clean relation graph with generalised names.
                StringLine = AxiomConverter(InconsistencyExplanationLine, variableMap, iterator);

            // Equivalent Class: <C1> <owl:equivalentWith> <C2>
            }  else if (RelationType == AxiomType.EQUIVALENT_CLASSES){
                // Gets the new clean relation graph with generalised names.
                StringLine = AxiomConverter(InconsistencyExplanationLine, variableMap, iterator);

            // If any other classes are hit Class cast exception is thrown. Throwing the relationType to add.
            } else {
                throw new ClassCastException(RelationType.toString());
            }

            // Adds the new cleaned line to the ExplanationList
            ExplanationStringList.add(StringLine);

        }

        // TRY to check the graph and write if needed to a file.
        try {
            if(verbose ) { // IF verbose write to file.
                // Write out all the complete inconsistencies for examples Paper.
                for (OWLAxiom InconsistencyExplanationLine : InconsistencyExplanation){

                    // Transfer the string to bytes and send to fileWriter.
                    fileWriter.write((InconsistencyExplanationLine.toString()+"\n").getBytes());
                }
                for (String StringLine : ExplanationStringList) { // Write to Output stream for Generalised output.
                    // Generalised output written.
                    fileWriter.write((StringLine + "\n") .getBytes());
                }
            }

            // Takes the Generalised Explanation and makes a generalised subgraph.
            // Adds to the list if the subgraph is not yet recognized.
            GeneralisedSubGraph GeneralGraph = new GeneralisedSubGraph(ExplanationStringList);
            // Checks if accepted to generalised subgraphs. If the subgraph is not found in the list
            // the subgraph is added to the list.

            boolean AcceptedTo = true;
            // Loop through all the subgraphs.
            for (GeneralisedSubGraph AcceptedGraph : GeneralGraphs){

                // Check if the subgraph is equal to the compared graph.
                if (AcceptedTo && IsoChecker.CompareGraph(AcceptedGraph, GeneralGraph) ){
                    AcceptedTo = false;
                }
            }
            // If no subgraph can be found it is added to the list.
            if (AcceptedTo){
                GeneralGraphs.add(GeneralGraph);
                System.out.println("Found A new General Graph");
            }

            if(verbose) { // IF verbose write to file.
                { // Write out all the complete inconsistencies for examples Paper.
                    for (Object InconsistencyExplanationLine : GeneralGraph.getAxioms().toArray())
                        // Transfer the string to bytes and send to fileWriter.
                        fileWriter.write((InconsistencyExplanationLine.toString()+"\n").getBytes());
                }
            }
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
                    // Write to outputStream as bytes.
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
        // Retrieve OWL ontology with a PipeModel. The model pipes the set of Strings from the subModel to the OWL Ontology.
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
            InconsistenciesHit ++;
            // TODO: Do not use the fileWriter here.
            fileWriter.write(strToBytes);

            // Standardize the inconsistency and write to file.
            InconsistencyStandardizer(InconsistencyExplanation, fileWriter);

        }


    }


    private static void WriteInconsistencySubGraph(HDT hdt, String tripleItem, FileOutputStream fileWriter) throws Exception{
        // Calls the GraphExtract which is Extended by Thomas de Groot to use the HDT instead of the JENA graph.
        // The TripleBoundary is set to stopNowhere Such that the model keeps going until my own stop criteria.
        GraphExtractExtended GraphExtract = new GraphExtractExtended(TripleBoundary.stopNowhere);

        // The subgraph is a set of strings. That stores up to 5000 triples.
        Set<String> subGraph = null;
        try{
            // Try to extract a the subgraph from the HDT.
            subGraph = GraphExtract.extractExtend(tripleItem , hdt);
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
        while(it.hasNext() && (InconsistenciesHit < TotalInconsistenciesBeforeBreak || UnBreakable)){

            // As it would not be scalable to use all the triples as starting point, as well as that the expectation is
            // that triples are connected to each other, not every triple needs to be evaluated.
            // A selection of triples is chosen at random.
            // Can be changed later to a selection of triples that meet a certain criteria.

            // at the moment every 1 out of 100 triples is taken.
            if (rand.nextDouble() < 0.01) {
                it.next();
                continue;
            }

            // If the loop is not triggered the next element from the tripleString is taken.
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

            if (InconsistenciesHit % 1000 == 0){
                System.out.println("Inconsistencies Hit: " + InconsistenciesHit);
            }

        }
    }


    private static ResultSet SPARQLQuery(Model model, String SPARQLQuery) {
        // Set the result set to null.
        ResultSet results = null;

        // Try to execute the query. Could throw errors when creating the factory.
        try {

            // Use Jena ARQ to execute the query. Firstly creating the query.
            Query query = QueryFactory.create(SPARQLQuery);

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
        ResultSet results = SPARQLQuery(model, query);

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
         * @params {@code args[0] } location of the HDT  -- necessary
         * @params {@code args[1] } Location to store the output  -- necessary
         * @params {@code args[2] } inconsistency explanations per subgraph  -- not necessary, default 10
         * @params {@code args[3] } Verbose   -- not necessary, default false
         * @params {@code args[4] } Inconsistency break   -- not necessary, default false, needs integer.
         * @returns void
         *
         */

        // Print to the user that the HDT is being loaded. Can take a while.
        System.out.println("Print Loading in HDT");

        // Load HDT file using the hdt-java library
        // TODO: Throw error when incorrect location.
        HDT hdt = HDTManager.mapIndexedHDT(args[0], null);

        // Print to the user that the HDT is finished loading.
        System.out.println("Finished Loading HDT");

        // Set output Writer
        // TODO: Throw error when incorrect location.
        FileOutputStream fileWriter = new FileOutputStream(new File(args[1]));

        // If the third argument is empty the amount of inconsistency explanations per subgraph is set to 10 else use
        // the amount given by the user.
        if (!args[2].isEmpty()){
            try{
                MaxExplanations = Integer.parseInt(args[2]);

            } catch (Exception e){
                System.out.println("Could not parse the third argument using 10 explanations");
                MaxExplanations = 10;
            }
        } else {
            MaxExplanations = 10;
        }

        // If the fourth argument is empty the amount of
        verbose = !args[3].isEmpty() && args[3].equals("true");

        // Sets the break for the Inconsistencies for the graph. This checks for the amount and sets it accordingly.
        // The default is no break.
        if (args.length > 4 && !args[4].isEmpty()){
            try{
                TotalInconsistenciesBeforeBreak = Integer.parseInt(args[4]);
                UnBreakable = false;
            } catch (Exception e){
                System.out.println("Could not parse the fifth argument Will continue without breaking the subgraphs");
                UnBreakable = true;
            }
        } else {
            UnBreakable = true;
        }


        System.out.println("InputLocation : " + args[0]);
        System.out.println("OutputLocation : " + args[1]);
        System.out.println("MaxExplanations : " + MaxExplanations);
        System.out.println("verbose : " + verbose);
        System.out.println("TotalInconsistenciesBeforeBreak : " + TotalInconsistenciesBeforeBreak);
        System.out.println("UnBreakable : " + UnBreakable);

        // Generate labels for the classes and instances.
        ClassLabelGenerator();

        if(verbose) {
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
        }

        // Print to the user that system started finding inconsistencies.
        System.out.println("Start locating the inconsistencies.");

        // Test Inconsistencies
        LocateInconsistencies(hdt, fileWriter);

        // Print to the user that the system finished finding inconsistencies.
        System.out.println("Finished locating inconsistencies");

        System.out.println("Printing Generalised Graphs");
        // Prints the finalised generalised subgraphs
        int GeneralCounter = 0;
        if (verbose) {
            for (GeneralisedSubGraph GeneralGraph : GeneralGraphs) {
                System.out.println(GeneralCounter);

                // Prints the generalGraph with specialised function.
                GeneralGraph.print();
                GeneralCounter ++;
            }
        }

    }

}

