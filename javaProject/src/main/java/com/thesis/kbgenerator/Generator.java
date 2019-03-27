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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

// TODO: Needs to add in a function that measures the amount of Inconsistencies and makes sure that the amount of Inconsistencies
//  stays below the correct size.

public class Generator {


    // Setting boolean testing
    private static boolean testing_Bool = true;

    // Other global variables.

    private static Random rand = new Random();
    private static InconsistencyStatementChecker InChecker;

    private static boolean DeletionChecker(Set<Triple> SubjectInput, Set<Triple> ObjectInput, Statement Input){
        // Check if item is mandatory. If the model
        if (InChecker.checkMandatory(Input)){
            return true;
        }

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

    private static Model RemoveStatements(HDT hdt, double SampleSize ) {
        // Locates the inconsistencies by looping through the graph over a large selection of triples.
        System.out.println("Creating Jena HDT graph");
        HDTGraph graph = new HDTGraph(hdt);

        // Create Models
        System.out.println("Creating model from graph");
        Model model = ModelFactory.createModelForGraph(graph);

        Model FinalSampledModel = ModelFactory.createDefaultModel();
        Model SubSampledModel = ModelFactory.createDefaultModel();
        StmtIterator stmtIt = model.listStatements(null, null ,(RDFNode)null);

        double partitions = 10000.0;
        int Iterator = 0;

        long maxSize = Math.round(model.size()*(1.0/partitions));
        while (stmtIt.hasNext()){
            System.out.println("Started Sampling From HDT");
            Model NormalModel = HDTtoSubgraph(model, stmtIt , maxSize );

            System.out.println("Started Sampling From regular Model" );
            Model SampledDownModel = DownSampling(NormalModel , SampleSize, false);

            System.out.println("Finished First SubSampling part: "+ Iterator);
            FinalSampledModel.add(SampledDownModel);
            SampledDownModel.close();
            Iterator ++;
//            if(Iterator % 1000 == 0 ){
//                FinalSampledModel.add(DownSampling(SubSampledModel , SampleSize, false));
//                System.out.println("Finished Combined SubSampling part: "+ Iterator);
//                SubSampledModel.close();
//                SubSampledModel = ModelFactory.createDefaultModel();
//            }
        }

        DownSampling(FinalSampledModel, SampleSize, true);

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
        long StartIterator = 0;
        while (stmtIt.hasNext() && modelCStorage.size() < maxSize ) {
            StartIterator ++;


            // As it would not be scalable to use all the triples as starting point, as well as that the expectation is
            // that triples are connected to each other, not every triple needs to be evaluated.
            // A selection of triples is chosen at random.
            // Can be changed later to a selection of triples that meet a certain criteria.

            // at the moment every 1 out of 10 triples is taken.
            if (rand.nextDouble() > 0.5) {
                Statement item = stmtIt.next();
                if(InChecker.checkMandatory(item)){
                    modelCStorage.add(item);
                    continue;
                }
                RDFNode object = item.getObject();

                Set<Triple> ObjectInput =  GetSubGraph(hdtModel, object, 2, modelRemovedTriples);
                if ((ObjectInput.size() <= 1)){
                    continue;
                }
                modelCStorage.add(item);
                continue;
            }

            // If the loop is not triggered the next element from the tripleString is taken.
            Statement item = stmtIt.next();

            // both the subject and the object are taken to build the subgraph. With the expectation that the subject
            // graph encompasses the object graph. With the exception when the subject graph gets to large and misses
            // some of the depth the object graph does take into account.
            RDFNode subject = item.getSubject();
            RDFNode object = item.getObject();
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
        System.out.println("MaxSize: " + maxSize + " Amount Of Triples Used to reach: " + StartIterator);
        return modelCStorage;
    }

    private static Model DownSampling(Model LargeModel, double DownSamplingSize, boolean FinalSampling){

        long StartingSize = LargeModel.size();
        long NewSize = LargeModel.size();
        long OldSize = 0;
        boolean FinalPass = true;
        double RemovalRate;
        System.out.println("Starting Sample size: "+ NewSize);
        // While there is a triple the loop continues.
        while (StartingSize*DownSamplingSize < NewSize  && (OldSize != NewSize || FinalPass)){
            if( NewSize > 0.95*OldSize ){
                RemovalRate = 1.0;
                OldSize = NewSize;
                FinalPass = false;
            } else{
                FinalPass = true;
                OldSize = NewSize;
                RemovalRate = 0.5;
            }

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
                RDFNode subject = item.getSubject();
                RDFNode object = item.getObject();


                modelRemovedTriples.add(item);
                // Find all the inconsistencies in the first subgraph(Object)
                Set<Triple> SubjectInput = GetSubGraph(LargeModel, subject, 50, modelRemovedTriples);

                // Find all the inconsistencies in the second subgraph(Subject)
                Set<Triple> ObjectInput =  GetSubGraph(LargeModel, object, 50, modelRemovedTriples);

                if (FinalSampling && (ObjectInput.size() == 0) || (SubjectInput.size() == 0)){
                    continue;
                }
                // Takes to Subgraphs and checks for matching nodes.
                boolean CannotBeRemoved = DeletionChecker(SubjectInput, ObjectInput, item);



                if (CannotBeRemoved){
                    modelRemovedTriples.remove(item);
                }

            }

            LargeModel.remove(modelRemovedTriples);
            NewSize = LargeModel.size();
        }
        if (FinalSampling){
            System.out.println("Final Sample size: "+ NewSize);
        } else{
            System.out.println("Final Sample size of partition: "+ NewSize);
        }

        return LargeModel;
    }


    private static Set<Triple> GetSubGraph(Model model, RDFNode tripleItem, int maxValue, Model modelRemovedTriples) {
        // Calls the GraphExtract which is Extended by Thomas de Groot to use the HDT instead of the JENA graph.
        // The TripleBoundary is set to stopNowhere Such that the model keeps going until my own stop criteria.
        GraphExtractExtended GraphExtract = new GraphExtractExtended(TripleBoundary.stopNowhere);

        // The subgraph is a set of strings. That stores up to 5000 triples.
        Set<Triple> subGraph = null;
        try{
            // Try to extract a the subgraph from the HDT.
            subGraph = GraphExtract.extractExtendBoth(tripleItem , model, maxValue, modelRemovedTriples);
            // To improve speed This filter can be used. TODO: CHANGE IT OUT.
            //subGraph = GraphExtract.extractExtendSingle(tripleItem , model, maxValue, modelRemovedTriples);
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


    static boolean WriteHDT(Model model,String outputDirectory,String tempDirectory) throws Exception {
        String TempDirectory = tempDirectory+"temp.hdt";
        try{
            WriteRDF(model, TempDirectory);
        } catch (Exception e){
            System.out.println("Could not write to rdfFile");
            return false;
        }

        String baseURI = "a";
        String rdfInput = TempDirectory.replace(".hdt",".nt");
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

    private static HashMap<String, Integer> ReadInconsistency(String InputLocation){
        HashMap<String, Integer> Inconsistencies = new HashMap<>();
        Path path = Paths.get(InputLocation);
        try{
            List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line: allLines){
                String[] lineSplit = line.split(", \"Amount\" : ");
                Inconsistencies.put(lineSplit[0].replace("{\"Inconsistency\" : ","").replace("\"", "") , Integer.parseInt(lineSplit[1].replace("\"","").replace("}","").trim()));

            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return Inconsistencies;
    }

    private static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    boolean deleteChecker = f.delete();
                    if(!deleteChecker){
                        System.out.println("Failed to delete folder, Check for reason why.");
                    }
                }
            }
        }
    }

    public static void main(String[] args)  throws Exception {
        /* main method that generates Inconsistencies when found.
         * @params {@code args[0] } location of the HDT  -- necessary
         * @params {@code args[1] } Location to store the output  -- necessary
         * @params {@code args[2] } Location of the amount and types of inconsistencies.
         * @params {@code args[3] } Location of the temporary Directory.
         * @params {@code args[4] } Return Type HDT or N-TRIPLES
         * @params {@code args[5] } Return Sample Size, default is 0.1
         * @returns void
         */


        // Read in the inconsistencies
        // Check if inconsistencies dir exists
        HashMap<String, Integer> Inconsistencies;
        if(Paths.get(args[2]).toFile().exists()){
            Inconsistencies = ReadInconsistency(args[2]);
        } else {
            Inconsistencies = new HashMap<>();
        }


        // Setting Env Variables
        double SampleSize;

        if(args.length > 5 ){
            try {
                SampleSize = Double.parseDouble(args[5]);
            } catch (Exception e){
                SampleSize = 0.1;
            }
        } else{
            SampleSize = 0.1;
        }


        // Print to the user that the HDT is being loaded. Can take a while.
        System.out.println("Print Loading in HDT");

        // Check if HDT Exists
        boolean InputFileExists = new File(args[0]).isFile();

        // Check if output dir exists
        Path path = Paths.get(args[1]);
        String parentDirName = path.getParent().toString();
        boolean OutputDirExists = new File(parentDirName).exists();

        // Check if output dir exists
        Path pathTemp = Paths.get(args[3]);
        String parentDirNameTemp = pathTemp.getParent().toString();
        boolean TempDirExists = new File(parentDirNameTemp).exists();

        // Throws error when incorrect location.
        if ( (!OutputDirExists  )){

            throw new IllegalArgumentException("Did not input the correct locations of the OutputDirectory.");
        }
        if((!InputFileExists)){
            throw new IllegalArgumentException("Did not input the correct locations of the InputDirectory.");
        }
        if ((!TempDirExists )){
            System.out.println(args[3]);
            throw new IllegalArgumentException("Did not input the correct locations of the TempDirectory.");
        }
        args[1] = args[1]+"Sample" +"-"+ args[0].split("/")[args[0].split("/").length-1];
        // Load HDT file using the hdt-java library
        HDT hdt = HDTManager.mapIndexedHDT(args[0]);

        // Print to the user that the HDT is finished loading.
        System.out.println("Finished Loading HDT");

        // Setting Env Variables
        InChecker = new InconsistencyStatementChecker(Inconsistencies, hdt, args[3]);

        // Sampling by Removing Statements
        Model FinalSampledModel = RemoveStatements(hdt, SampleSize);

        // Set output Writer
        System.out.println("Finished Working Sampled Model");
        boolean emitSuccess = false;
        if (args[4].equals("HDT")){
            emitSuccess = WriteHDT(FinalSampledModel, args[1], args[3]);
        }else if (args[4].equals("N-TRIPLES")){
            emitSuccess = WriteRDF(FinalSampledModel, args[1]);
        } else {
            System.out.println("Could not understand the type specification, writing the file as N-Triples");
        }


        if(emitSuccess){
            System.out.println("Successfully finished writing the model to file.");
        } else{
            System.out.println("Unsuccessfully written the model.");
        }


        deleteFolder(new File(args[3]));



    }
}
