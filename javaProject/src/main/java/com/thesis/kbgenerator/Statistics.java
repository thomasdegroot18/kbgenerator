package com.thesis.kbgenerator;

import org.apache.jena.graph.TripleIterator;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdt.header.Header;
import org.rdfhdt.hdt.triples.IteratorTripleString;
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

    private static HashMap<String, Integer> InconsistencySPARQL(Model model, HashMap<String, Integer> StoredValues){
        for (String key: StoredValues.keySet()){
            if (StoredValues.get(key) == null){
                StoredValues.replace(key, SPARQLExecutioner.CounterResultPrinter(model, key));
            }
        }
        return StoredValues;
    }


    private static HashMap<String, Integer> InconsistencyCheck(String fileLocation, HashMap<String, Integer> StoredValues) {

        File file = new File(fileLocation);
        try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;


            while((line = br.readLine()) != null){
                if (line.startsWith("SELECT")){
                    if(StoredValues.getOrDefault(line, null) == null){
                        StoredValues.put(line, null);
                    }
                }
                if (line.equals("Stopped")){
                    ConstantLoopBoolean = false;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        return StoredValues;
    }



    private static HashMap<String, Integer> InconsistencyCheck(String fileLocation) {
        //Load the SPARQL Query Location
        HashMap<String, Integer> StoredValues = new HashMap<>();

        File file = new File(fileLocation);
        try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;


            while((line = br.readLine()) != null){
                if (line.startsWith("SELECT")){
                    StoredValues.put(line, null);
                }

                if (line.equals("Stopped")){
                    ConstantLoopBoolean = false;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        return StoredValues;
    }

    private static HashMap<String, String> GraphsLoader (String fileLocation, HashMap<String, String> StoredValues) {
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
                        StoredValues.put(key, value.toString());
                        value = new StringBuilder();
                    }
                }
                else if (!line.startsWith("\n") && !line.startsWith("General")){
                    value.append(line).append(", ");
                }


            }
        } catch (Exception e){
            e.printStackTrace();
        }


        return StoredValues;
    }

    // Retrieves the found inconsistencies from the inconsistent dataset.
    private static HashMap<String, String> GraphsLoader(String fileLocation) {
        //Load the SPARQL Query Location
        HashMap<String, String> StoredValues = new HashMap<>();

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
                        StoredValues.put(key, value.toString());
                        value = new StringBuilder();
                    }
                }
                else if (!line.startsWith("\n") && !line.startsWith("General")){
                    value.append(line).append(", ");
                }


            }
            StoredValues.put(key, value.toString());
        } catch (Exception e){
            e.printStackTrace();
        }


        return StoredValues;
    }

    private static void writeJSON(String fileLocation, List<String> StringArray){
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



    private static void ConstantLoop(Model model, HDT hdt,  String fileLocation){
        HashMap<String, String> StoredGraphs = GraphsLoader(fileLocation);
        HashMap<String, Integer> StoredValues = InconsistencyCheck(fileLocation);
        // RunAll Knowledge base Statistics:
        Object KbInfo = KbStatistics.RunAll(model, hdt);

        while(ConstantLoopBoolean){
            GraphsLoader(fileLocation, StoredGraphs);
            InconsistencyCheck(fileLocation, StoredValues);



            InconsistencySPARQL(model, StoredValues);


            String LineToWrite = "[";
            List<String> StringLines = new ArrayList<>();
            int IndexNumber = 1;
            for (String key : StoredValues.keySet()){
                StringLines.add(LineToWrite);

                LineToWrite = "{\"Graphnumber\" : " + IndexNumber + ", \"value\" : " + StoredValues.get(key) + ", \"SparqlRequest\" : \"" + key + "\", \"Graph\" : \"" + StoredGraphs.get(key) + "\"} ,";
                IndexNumber ++;
            }
            LineToWrite = LineToWrite.substring(0, LineToWrite.length() -1);
            LineToWrite = LineToWrite + "]";
            StringLines.add(LineToWrite);
            // Writes file to JSON location.
            writeJSON(fileLocation.split("javaProject")[0]+"/docs/Webpages/js/data.json", StringLines);

            try{
                Thread.sleep(60000);
                System.out.println("Resending Requests");

            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) throws Exception {
        /** Argument 0: input location of the HDT
         *  Argument 1: Input directory of the ttl.
         *
         *
         *
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

        KbStatistics.RunAll(model, hdt);


        //ConstantLoop(model, hdt, FileInput);


    }
}
