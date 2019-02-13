package com.thesis.kbgenerator;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.graph.TripleBoundary;
import org.apache.jena.rdf.model.*;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdtjena.HDTGraph;
import rationals.transformations.Star;
import ru.avicomp.ontapi.jena.impl.CachedAnnotationImpl;

import java.util.HashSet;
import java.util.Random;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class Generator {

    private static Random rand = new Random();

    private static boolean ConnectionChecker(Set<Triple> SubjectInput, Set<Triple> ObjectInput, Statement Input){
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
                return true;
            }
        }
        return false;
    }

    private static void RemoveStatements(HDT hdt) {
        // Locates the inconsistencies by looping through the graph over a large selection of triples.
        System.out.println("Creating Jena HDT graph");
        HDTGraph graph = new HDTGraph(hdt);

        // Create Models
        System.out.println("Creating model from graph");
        Model model = ModelFactory.createModelForGraph(graph);

        //Model model = ModelFactory.createDefaultModel();

//        model.add(TotalTriples);

        long StartingSize = model.size();
        long NewSize = model.size();
        long OldSize = 0;
        // While there is a triple the loop continues.
        while (StartingSize*0.1 < NewSize  && OldSize != NewSize){

            OldSize = NewSize;
            double RemovalRate = 0.1*( (double)StartingSize/ (double)OldSize);
            System.out.println(OldSize);
            System.out.println(RemovalRate );

            // Get the Iterator tripleString to loop through.
            Model modelRemovedTriples = ModelFactory.createDefaultModel();

            StmtIterator stmtIt = model.listStatements(null, null ,(RDFNode)null);
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
                Set<Triple> SubjectInput = GetSubGraph(model, subject, 500, modelRemovedTriples);

                // Find all the inconsistencies in the second subgraph(Subject)
                Set<Triple> ObjectInput =  GetSubGraph(model, object, 500, modelRemovedTriples);

                System.out.println(SubjectInput.size());

                System.out.println(ObjectInput.size());
                if ((ObjectInput.size() == 0) || (SubjectInput.size() == 0)){
                    continue;
                }

                boolean CannotbeRemoved = !ConnectionChecker(SubjectInput, ObjectInput, item);

                System.out.println(CannotbeRemoved);
                if (CannotbeRemoved){
                    modelRemovedTriples.remove(item);
                }

                // TODO: Remove Break;
                else{
                    break;
                }

            }

            model.remove(modelRemovedTriples);
            NewSize = model.size();
        }
    }

    static Set<Triple> GetSubGraph(Model model, String tripleItem, int maxValue, Model modelRemovedTriples) {
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



    public static void main(String[] args)  throws Exception {
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

        // Load HDT file using the hdt-java library

        HDT hdt = HDTManager.mapIndexedHDT(args[0]);

        // Print to the user that the HDT is finished loading.
        System.out.println("Finished Loading HDT");

        // Set output Writer
        RemoveStatements(hdt);


    }
}
