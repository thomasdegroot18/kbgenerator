package com.thesis.kbgenerator;

import com.thesis.kbInconsistencyLocator.GraphExtractExtended;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.graph.TripleBoundary;
import org.apache.jena.rdf.model.*;
import org.rdfhdt.hdt.enums.RDFNotation;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdt.options.HDTSpecification;
import org.rdfhdt.hdtjena.HDTGraph;

import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Random;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;


public class Generator {

    private static Random rand = new Random();

    private static boolean DeletionChecker(Set<Triple> SubjectInput, Set<Triple> ObjectInput, Statement Input){
        Set<Node> SubjectInputCleanList = new HashSet<>();
        Set<Node> ObjectInputCleanList = new HashSet<>();
        for (Triple elem : SubjectInput){
            SubjectInputCleanList.add(elem.getSubject());
            SubjectInputCleanList.add(elem.getObject());
        }
        SubjectInputCleanList.remove(Input.getObject().asNode());

        for (Triple elem : ObjectInput){
            ObjectInputCleanList.add(elem.getSubject());
            ObjectInputCleanList.add(elem.getObject());
        }
        ObjectInputCleanList.remove(Input.getSubject().asNode());

        for (Node elem: ObjectInputCleanList){
            if(SubjectInputCleanList.contains(elem)){
                return false;
            }
        }
        return true;
    }

    private static Model RemoveStatements(HDT hdt, double SampleSize) {
        // Locates the inconsistencies by looping through the graph over a large selection of triples.
        System.out.println("Creating Jena HDT graph");
        HDTGraph graph = new HDTGraph(hdt);

        // Create Models
        System.out.println("Creating model from graph");
        Model model = ModelFactory.createModelForGraph(graph);

        Model FinalSampledModel = ModelFactory.createDefaultModel();

        StmtIterator stmtIt = model.listStatements(null, null ,(RDFNode)null);
        int Iterator = 0;

        long maxSize = Math.round(model.size()*0.05);

        while (stmtIt.hasNext()){
            System.out.println("Started Sampling From HDT");
            Model NormalModel = HDTtoSubgraph(model, stmtIt , maxSize );

            System.out.println("Started Sampling From regular Model" );
            Model SampledDownModel = DownSampling(NormalModel , SampleSize);

            System.out.println("Finished First SubSampling: "+ Iterator);
            FinalSampledModel.add(SampledDownModel);
        }

        DownSampling(FinalSampledModel, SampleSize);

        return FinalSampledModel;
    }

    private static Model HDTtoSubgraph(Model hdtModel, StmtIterator stmtIt, long maxSize){
        /* Generating a subgraph from the hdt
         *
         *
         *
         *
         */


        // Get the Iterator tripleString to loop through.
        Model modelCStorage = ModelFactory.createDefaultModel();
        // Get the Iterator tripleString to loop through.
        Model modelRemovedTriples = ModelFactory.createDefaultModel();

        while (stmtIt.hasNext() && modelCStorage.size() < maxSize ) {



            // As it would not be scalable to use all the triples as starting point, as well as that the expectation is
            // that triples are connected to each other, not every triple needs to be evaluated.
            // A selection of triples is chosen at random.
            // Can be changed later to a selection of triples that meet a certain criteria.

            // at the moment every 1 out of 10 triples is taken.
            if (rand.nextDouble() > 0.1) {
                Statement item = stmtIt.next();
                modelCStorage.add(item);
                continue;
            }

            // If the loop is not triggered the next element from the tripleString is taken.
            Statement item = stmtIt.next();

            // both the subject and the object are taken to build the subgraph. With the expectation that the subject
            // graph encompasses the object graph. With the exception when the subject graph gets to large and misses
            // some of the depth the object graph does take into account.
            String subject = item.getSubject().toString();
            String object = item.getObject().toString();
            modelRemovedTriples.add(item);
            // Find all the inconsistencies in the first subgraph(Object)
            Set<Triple> SubjectInput = GetSubGraph(hdtModel, subject, 50, modelRemovedTriples);

            // Find all the inconsistencies in the second subgraph(Subject)
            Set<Triple> ObjectInput =  GetSubGraph(hdtModel, object, 50, modelRemovedTriples);

            if ((ObjectInput.size() == 0) || (SubjectInput.size() == 0)){
                continue;
            }

            boolean CannotBeRemoved = DeletionChecker(SubjectInput, ObjectInput, item);

            if (CannotBeRemoved){
                modelRemovedTriples.remove(item);
                modelCStorage.add(item);
            }

        }

        return modelCStorage;
    }

    private static Model DownSampling(Model LargeModel, double DownSamplingSize){

        long StartingSize = LargeModel.size();
        long NewSize = LargeModel.size();
        long OldSize = 0;
        // While there is a triple the loop continues.
        while (StartingSize*DownSamplingSize< NewSize  && OldSize != NewSize){

            OldSize = NewSize;
            double RemovalRate = 0.1*( (double)StartingSize/ (double)OldSize);
            System.out.println(OldSize);
            System.out.println(RemovalRate );

            // Get the Iterator tripleString to loop through.
            Model modelRemovedTriples = ModelFactory.createDefaultModel();

            StmtIterator stmtIt = LargeModel.listStatements(null, null ,(RDFNode)null);

            while (stmtIt.hasNext() ) {

                // As it would not be scalable to use all the triples as starting point, as well as that the expectation is
                // that triples are connected to each other, not every triple needs to be evaluated.
                // A selection of triples is chosen at random.
                // Can be changed later to a selection of triples that meet a certain criteria.

                // at the moment every 1 out of 10 triples is taken.
                if (rand.nextDouble() > RemovalRate) {
                    stmtIt.next();
                    continue;
                }

                // If the loop is not triggered the next element from the tripleString is taken.
                Statement item = stmtIt.next();

                // both the subject and the object are taken to build the subgraph. With the expectation that the subject
                // graph encompasses the object graph. With the exception when the subject graph gets to large and misses
                // some of the depth the object graph does take into account.
                String subject = item.getSubject().toString();
                String object = item.getObject().toString();
                modelRemovedTriples.add(item);
                // Find all the inconsistencies in the first subgraph(Object)
                Set<Triple> SubjectInput = GetSubGraph(LargeModel, subject, 100, modelRemovedTriples);

                // Find all the inconsistencies in the second subgraph(Subject)
                Set<Triple> ObjectInput =  GetSubGraph(LargeModel, object, 100, modelRemovedTriples);

                boolean CannotBeRemoved = DeletionChecker(SubjectInput, ObjectInput, item);

                if (CannotBeRemoved){
                    modelRemovedTriples.remove(item);
                }

            }

            LargeModel.remove(modelRemovedTriples);
            NewSize = LargeModel.size();
        }


        return LargeModel;
    }


    private static Set<Triple> GetSubGraph(Model model, String tripleItem, int maxValue, Model modelRemovedTriples) {
        // Calls the GraphExtract which is Extended by Thomas de Groot to use the HDT instead of the JENA graph.
        // The TripleBoundary is set to stopNowhere Such that the model keeps going until my own stop criteria.
        GraphExtractExtended GraphExtract = new GraphExtractExtended(TripleBoundary.stopNowhere);

        // The subgraph is a set of strings. That stores up to 5000 triples.
        Set<Triple> subGraph = null;
        try{
            // Try to extract a the subgraph from the HDT.
            subGraph = GraphExtract.extractExtendBoth(tripleItem , model, maxValue, modelRemovedTriples);
        } catch (StackOverflowError e){
            // Can print the error if overflow happens.
            e.printStackTrace();
        }

        return subGraph;
    }

    private static boolean WriteRDF(Model model, String outputDirectory) {
        try{
            FileOutputStream fileWriter = new FileOutputStream(new File(outputDirectory.replace(".hdt",".nt")));
            model.write(fileWriter,"N-TRIPLE" );
        } catch (Exception e){
            System.out.println("Could not write to N-triple");
            return false;
        }

        return true;
    }


    private static boolean WriteHDT(Model model,String outputDirectory) throws Exception {

        try{
            WriteRDF(model, outputDirectory);
        } catch (Exception e){
            System.out.println("Could not write to rdfFile");
            return false;
        }

        String baseURI = "";
        String rdfInput = outputDirectory.replace(".hdt",".nt");
        String inputType = "ntriples";

        // Create HDT from RDF file
        HDT hdt = HDTManager.generateHDT(
                rdfInput,         // Input RDF File
                baseURI,          // Base URI
                RDFNotation.parse(inputType), // Input Type
                new HDTSpecification(),   // HDT Options
                null              // Progress Listener
        );


        // Save generated HDT to a file
        hdt.saveToHDT(outputDirectory, null);


        return true;
    }


    public static void main(String[] args)  throws Exception {
        /* main method that generates Inconsistencies when found.
         * @params {@code args[0] } location of the HDT  -- necessary
         * @params {@code args[1] } Location to store the output  -- necessary
         * @params {@code args[2] } Return Type HDT or N-TRIPLES
         * @returns void
         */


        double SampleSize = 0.1;

        // Print to the user that the HDT is being loaded. Can take a while.
        System.out.println("Print Loading in HDT");

        // Check if HDT Exists
        boolean InputFileExists = new File(args[0]).isFile();

        // Check if output dir exists
        Path path = Paths.get(args[1]);
        String parentDirName = path.getParent().toString();
        boolean OutputDirExists = new File(parentDirName).exists();

        // Throws error when incorrect location.
        if ( (!OutputDirExists  )|| (!InputFileExists)){
            throw new IllegalArgumentException("Did not input the correct locations of the output or the input locations.");
        }
        args[1] = args[1]+"Sample" +"-"+ args[0].split("/")[args[0].split("/").length-1];
        System.out.println(args[1]);
        // Load HDT file using the hdt-java library

        HDT hdt = HDTManager.mapIndexedHDT(args[0]);

        // Print to the user that the HDT is finished loading.
        System.out.println("Finished Loading HDT");

        // Set output Writer
        Model FinalSampledModel = RemoveStatements(hdt, SampleSize);

        System.out.println("Finished Working Sampled Model");
        boolean emitSuccess = false;
        if (args[2].equals("HDT")){
            emitSuccess = WriteHDT(FinalSampledModel, args[1]);
        }else if (args[2].equals("N-TRIPLES")){
            emitSuccess = WriteRDF(FinalSampledModel, args[1]);
        } else {
            System.out.println("Could not understand the type specification, writing the file as N-Triples");
        }


        if(emitSuccess){
            System.out.println("Successfully finished writing the model to HDT");
        } else{
            System.out.println("Unsuccessfully written the model.");
        }
    }
}
