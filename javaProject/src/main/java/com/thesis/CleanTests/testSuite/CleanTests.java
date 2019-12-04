package com.thesis.CleanTests.testSuite;


import com.thesis.kbStatistics.Statistics;
import com.thesis.InconsistencyJsonCreator.InconsistencyCreator;
import com.thesis.kbInconsistencyLocator.InconsistencyLocator;

import java.io.IOException;

/**
Testsuite holds a simple implementation of the functionality of the knowledge base generator.

 CREATED by:
 Thomas de Groot
 */
public class CleanTests {

    public static void main(String[] args) throws Exception{
        String AbsoluteName = "D:/Users/Thomas/Documents/thesis/kbgenerator/javaProject/resources/";
        //String AbsoluteName = "/home/thomasdegroot/local/kbgenerator/javaProject/resources/";

        String hdt;
        String hdtName;
        String args3 = "0";
        boolean CheckingInconsistency = true;
        // Setting Locations
        switch (args[0]){
            case "0":
                // Setting Lod-a-lot here:
                hdt = "/home/wbeek/data/LOD-a-lot/lod-a-lot.hdt";
                hdtName = "lod-a-lot.hdt";
                break;
            case "3":
                //Setting Yago2s here:
                hdt = AbsoluteName+"HDTs/yago2s.hdt";
                hdtName = "yago2s.hdt";
                args3 = "1";
                break;
            default:
                // Default here:
                System.out.println("Not found value.");
                hdt = AbsoluteName+"HDTs/pizza.hdt";
                hdtName = "pizza.hdt";
            }

            String rdf = AbsoluteName+"RDFs/";
            String samples = AbsoluteName+"Samples/";
            String inconsistencyJSON = AbsoluteName+"StatResults/" +hdtName.replace(".hdt", "")+"/";
            String temp = AbsoluteName+"extraFiles/temp/"+hdtName.replace(".hdt", "")+"/";
            String SampledLocationStats = AbsoluteName+"StatResults/Sampled/" +hdtName.replace(".hdt", "")+"/";

            System.out.println("AbsoluteName: "+AbsoluteName);
            System.out.println("rdf: "+rdf);
            System.out.println("hdt: "+hdt);
            System.out.println("samples: "+samples+"Sample-"+hdtName);
            System.out.println("inconsistencyJSON: "+inconsistencyJSON);
            System.out.println("temp: "+temp);

        // Inconsistency Locator
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Starting Locating Inconsistencies");
        String[] argsInconsistency =  {hdt,
                                       rdf,
                                       "500",
                                       "true",
                                        "false",
                                        "0"};
        if (CheckingInconsistency ){
            InconsistencyLocator.main(argsInconsistency);
        }
        System.out.println("------------------------------------------------------------------------------------------");
        // Statistics
        System.out.println("Starting Statistics");
        String[] argsStatistics =  {hdt, rdf, inconsistencyJSON, args3};
        Statistics.main(argsStatistics);
        System.out.println("------------------------------------------------------------------------------------------");
//        System.out.println("Making inconsistency.json");
        String FileInput = rdf+"INCONSISTENCIES-LOD-a-lotUSED.ttl";
        String[] argsInconsistencyCreator = {inconsistencyJSON+"inconsistencies.json", FileInput, inconsistencyJSON+"InconsistencyStatistics.json"};
        InconsistencyCreator.main(argsInconsistencyCreator);
        promptEnterKey();

        System.out.println("------------------------------------------------------------------------------------------");
        // Generator

//        File newFolder = new File(temp);
//        boolean created = newFolder.mkdir();
//        if (!created){
//            System.out.println("No temp DIR CREATED");
//        }
//        System.out.println("Starting Generation");
//        String[] argsGenerator = {hdt, samples, inconsistencyJSON+"inconsistencies.json", temp, "N-TRIPLES", "0.2"};
//        Generator.main(argsGenerator);

        System.out.println("------------------------------------------------------------------------------------------");
        // Statistics Sampled
        System.out.println("Starting Statistics");
        String[] argsStatisticsSampled = {samples+"Sample-"+hdtName, rdf, SampledLocationStats};
        Statistics.main(argsStatisticsSampled);
        System.out.println("------------------------------------------------------------------------------------------");



    }


    @SuppressWarnings("unused")
    public static void promptEnterKey(){
        System.out.println("Press \"ENTER\" to continue...");
        try {
            int read = System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
