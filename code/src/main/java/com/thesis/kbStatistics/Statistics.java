package com.thesis.kbStatistics;

import org.apache.jena.query.Dataset;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdt.header.Header;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.triples.TripleString;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Statistics {
    // Setting boolean testing
    private static boolean testing_Bool = true;

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
                else if (line.equals("Stopped")){
                    if (!(key == null )){
                        StoredGraph.put(key, value.toString());
                        value = new StringBuilder();
                    }
                    ConstantLoopBoolean = false;
                }
                else if (!line.startsWith("\n") && !line.startsWith("General")){
                    value.append(line).append(", ");
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

    static void writeJSON(String fileLocation, String[] StringArray){
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


    private static String[] GetDataSets(HDT hdt ){
        TreeMap<Integer, String> map = new TreeMap<Integer, String>();

        int smallestnumber = 0;
        try {
            IteratorTripleString it = hdt.search("", "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", "http://rdfs.org/ns/void#Dataset");
            while(it.hasNext()) {
                TripleString ts = it.next();

                try {
                    IteratorTripleString itNew = hdt.search(ts.getSubject(), "http://rdfs.org/ns/void#triples", "");
                    if (itNew.hasNext()) {
                        Integer triples = Integer.valueOf((itNew.next().getObject().toString().replace("^^<http://www.w3.org/2001/XMLSchema#integer>","").replace("\"","")));

                        if (triples > smallestnumber){
                            map.putIfAbsent(triples, ts.getSubject().toString()) ;
                        }
                        if( map.size() > 20){
                            map.remove(smallestnumber);
                            smallestnumber = map.firstKey();
                        }


                    }

                    itNew = hdt.search(ts.getSubject(), "http://purl.org/HDT/hdt#triplesnumTriples", "");
                    if (itNew.hasNext()) {
                        Integer triples = Integer.valueOf((itNew.next().getObject().toString().replace("^^<http://www.w3.org/2001/XMLSchema#integer>","").replace("\"","")));
                        if (triples > smallestnumber){
                            map.putIfAbsent(triples, ts.getSubject().toString()) ;
                        }
                        if( map.size() > 20){
                            map.remove(smallestnumber);
                            smallestnumber = map.firstKey();
                        }


                    }

                } catch (Exception e){
                }

                }

            } catch (Exception e){
                e.printStackTrace();
        }

        System.out.println("Finished Check");

        return map.values().toArray(new String[0]);

    }

    private static void RetrieveDataSet(HDT hdt, String Dataset){
        try{
            System.out.println(Dataset);
            IteratorTripleString it = hdt.search(Dataset, "", "");
            while(it.hasNext()) {
                TripleString ts = it.next();
                System.out.println(ts.asNtriple());
            }
        } catch (Exception e){

        }

    }



    private static void ConstantLoop( HDT hdt,  String fileLocation, String OutputLocation){



        // Get all the KB statistics
        if(true){// testing_Bool
            System.out.println("knowledge base statistics calculation");
            KbStatistics.RunAll(hdt, OutputLocation+"kbStatistics.json");

            System.out.println("Finished knowledge base statistics calculation");

        }
        String[] Datasets= new String [1];
//        Datasets = GetDataSets(hdt);
//        writeJSON(fileLocation.split("RDFs/")[0]+ "extraFiles/DataSets.txt",Datasets);
        // Create object for Inconsistencies.
//        for (String Dataset: Datasets){
//            RetrieveDataSet(hdt, Dataset);
//        }
        InconsistencyStatistics InconsistencyStats = new InconsistencyStatistics(hdt);

        // Create HashMap for StoredGraphs
        HashMap<String, String> StoredGraphs = new HashMap<>();
        boolean firstLoop = true;
        while(firstLoop ||ConstantLoopBoolean){
            firstLoop = false;
            // Load all Inconsistency graphs.
            GraphsLoader(fileLocation, StoredGraphs);
            // RunAll Inconsistency Statistics:

            System.out.println("Running Inconsistency Statistics");
            for (String Key : StoredGraphs.keySet()){
                if (Datasets.length > 1){
                    System.out.println("datasets Check");
                    InconsistencyStats.RunAll(Key, StoredGraphs.get(Key), Datasets);
                } else{
                    InconsistencyStats.RunAll(Key, StoredGraphs.get(Key));
                }

            }

            System.out.println("Finished Inconsistency Statistics");
            // Write the Inconsistencies until now collected.
            InconsistencyStats.WriteToFile(OutputLocation+"InconsistencyStatistics.json");

            try{
                if(ConstantLoopBoolean){
                    System.out.println("Waiting 60 seconds");
                    Thread.sleep(60000);
                    System.out.println("Resending Requests");
                }

            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) throws Exception {
        /*  Argument 0: input location of the HDT
         *  Argument 1: Input directory of the Inconsistency File.
         *  Argument 2: Output location of the gathered statistics.
         *  Argument 3: Skipping First step of statistics.
         */
        if(args.length > 3){
            if (args[3].equals("1")){
                testing_Bool = false;
            }
        }

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

                if (file.getName().contains("INCONSISTENCIES-"+args[0].split("/")[args[0].split("/").length - 1].replace(".hdt", "").replace("Sample-",""))) {
                    FileInput = file.toString();
                }
                // TODO: DELETE LINK
                FileInput = args[1]+"INCONSISTENCIES-LOD-a-lotUSED.ttl";
            }
        }

        System.out.println(FileInput);
        // Load HDT file using the hdt-java library
        HDT hdt = HDTManager.mapIndexedHDT(args[0], null);
        // Create Jena wrapper on top of HDT.
        System.out.println("Creating model from graph");

        File newFolder = new File(args[2]);
        boolean created = newFolder.mkdir();
        if (!created){
            System.out.println("No OUTPUT DIR CREATED");
        }

        ConstantLoop( hdt, FileInput, args[2]);


    }
}
