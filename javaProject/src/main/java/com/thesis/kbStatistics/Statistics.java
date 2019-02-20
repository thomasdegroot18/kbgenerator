package com.thesis.kbStatistics;

import com.thesis.SPARQLengine.SPARQLExecutioner;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdtjena.HDTGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Statistics {

    private static boolean ConstantLoopBoolean = true;


    @SuppressWarnings("UnusedReturnValue")
    private static HashMap<String, String> GraphsLoader (String fileLocation, HashMap<String, String> StoredGraph) {
        //Load the SPARQL Query Location

        File file = new File(fileLocation);
        try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;

            String key = null;
            StringBuilder value = new StringBuilder();


            while((line = br.readLine()) != null){
                if (line.startsWith("SELECT")){
                    key = line;
                }
                else if (line.startsWith("New general inconsistency:")){
                    if (!(key == null )){
                        StoredGraph.put(key, value.toString());
                        value = new StringBuilder();
                    }
                }
                else if (!line.startsWith("\n") && !line.startsWith("General")){
                    value.append(line).append(", ");
                }

                if (line.equals("Stopped")){
                    ConstantLoopBoolean = false;
                }


            }
        } catch (Exception e){
            e.printStackTrace();
        }


        return StoredGraph;
    }


    static void writeJSON(String fileLocation, List<String> StringArray){
        try{
            FileOutputStream fileWriter = new FileOutputStream(new File(fileLocation));
            for (String line : StringArray){
                fileWriter.write(line.getBytes());
            }
            fileWriter.close();

        } catch (Exception e){
            e.printStackTrace();
        }

    }



    private static void ConstantLoop(Model model, HDT hdt,  String fileLocation, String OutputLocation){



        // Get all the KB statistics
        KbStatistics.RunAll(hdt, OutputLocation);

        // Create object for Inconsistencies.
        InconsistencyStatistics InconsistencyStats = new InconsistencyStatistics(model);

        // Create Hashmap for StoredGraphs
        HashMap<String, String> StoredGraphs = new HashMap<>();

        while(ConstantLoopBoolean){

            // Load all Inconsistency graphs.
            GraphsLoader(fileLocation, StoredGraphs);

            // RunAll Inconsistency Statistics:
            for (String Key : StoredGraphs.keySet()){
                InconsistencyStats.RunAll(Key, StoredGraphs.get(Key));
            }
            // Write the Inconsistencies until now collected.
            InconsistencyStats.WriteToFile(OutputLocation);

            try{
                Thread.sleep(60000);
                System.out.println("Resending Requests");

            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) throws Exception {
        /*  Argument 0: input location of the HDT
         *  Argument 1: Input directory of the turtle.
         *  Argument 2: Output location of the gathered statistics.
         */


        // Print to the user that the HDT is being loaded. Can take a while.
        System.out.println("Print Loading in HDT");
        if (args.length == 0){
            throw new IllegalArgumentException("Did not input the correct locations of the output or the input locations.");
        }
        // Check if HDT Exists
        boolean InputFileExists = new File(args[0]).isFile();

        // Check if output dir exists
        Path path = Paths.get(args[1]);
        String parentDirName = path.toString();

        File Folder = new File(parentDirName);
        boolean OutputDirExists = Folder.exists();

        // Throws error when incorrect location.
        if ( (!OutputDirExists  )|| (!InputFileExists)){
            throw new IllegalArgumentException("Did not input the correct locations of the output or the input locations.");
        }
        String FileInput = "";
        File[] directoryListing = Folder.listFiles();
        if (directoryListing != null) {
            for (File file : directoryListing) {
                if (file.getName().contains(args[0].split("/")[args[0].split("/").length - 1].replace(".hdt", ""))) {
                    FileInput = file.toString();
                }
            }
        }
        // Load HDT file using the hdt-java library
        HDT hdt = HDTManager.mapIndexedHDT(args[0], null);
        // Create Jena wrapper on top of HDT.
        System.out.println("Creating Jena HDT graph");
        System.out.println("Creating model from graph");
        Model model = ModelFactory.createModelForGraph(new HDTGraph(hdt));

        ConstantLoop(model, hdt, FileInput, args[2]);


    }
}
