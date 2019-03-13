package com.thesis.testSuite;


import com.thesis.InconsistencyJsonCreator.InconsistencyCreator;
import com.thesis.kbInconsistencyLocator.InconsistencyLocator;
import com.thesis.kbStatistics.Statistics;
import com.thesis.kbgenerator.Generator;

import java.io.IOException;

/**
Testsuite holds a simple implementation of the functionality of the knowledge base generator.

 CREATED by:
 Thomas de Groot
 */
public class testSuite {

    public static void main(String[] args) throws Exception{

        String AbsoluteName = "/home/thomas/thesis/kbgenerator/javaProject/resources/";
        //String AbsoluteName = "D:/Users/Thomas/Documents/thesis/kbgenerator/";
        //String AbsoluteName = "/home/thomasdegroot/local/kbgenerator/javaProject/resources/";

        String hdt;
        String hdtName;

        // Setting Locations
        switch (args[0]){
            case "0":
            // Setting Lod-a-lot here:
            hdt = "/home/wbeek/data/LOD-a-lot/lod-a-lot.hdt";
            hdtName = "lod-a-lot.hdt";
            break;
            case "1":
            // Setting DBpedia here:
            hdt = "/home/dimitris/local/dbpedia2016-04en.hdt";
            hdtName = "dbpedia2016-04en.hdt";
            break;
            case "2":
            // Setting Freebase here:
            hdt = "/home/dimitris/local/freebase-rdf-2013-12-01-00-00.hdt";
            hdtName = "freebase-rdf-2013-12-01-00-00.hdt";
            break;
            case "3":
            //Setting Yago2s here:
            hdt = AbsoluteName+"HDTs/yago2s.hdt";
            hdtName = "yago2s.hdt";
            break;
            case "4":
            //Setting Pizza here:
            hdt = AbsoluteName+"HDTs/pizza.hdt";
            hdtName = "pizza.hdt";
            break;
            default:
            // Default here:
            System.out.println("Not found value.");
            hdt = AbsoluteName+"HDTs/pizza.hdt";
            hdtName = "pizza.hdt";
        }

        String rdf = AbsoluteName+"RDFs/";
        String samples = AbsoluteName+"Samples/";
        String inconsistencyJSON = AbsoluteName+"StatResults/";
        String temp = AbsoluteName+"extraFiles/temp/";

        System.out.println("AbsoluteName: "+AbsoluteName);
        System.out.println("rdf: "+rdf);
        System.out.println("hdt: "+hdt);
        System.out.println("samples: "+samples);
        System.out.println("inconsistencyJSON: "+inconsistencyJSON);
        System.out.println("temp: "+temp);

        // Inconsistency Locator
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Starting Locating Inconsistencies");
        String[] argsInconsistency =  {hdt,
                                       rdf,
                                       "10",
                                       "true"};

        InconsistencyLocator.main(argsInconsistency);
        System.out.println("------------------------------------------------------------------------------------------");
        // Statistics
        System.out.println("Starting Statistics");
        String[] argsStatistics =  {hdt, rdf, inconsistencyJSON};
        Statistics.main(argsStatistics);
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Making inconsistency.json");
        String[] argsInconsistencyCreator = {inconsistencyJSON+"inconsistencies.json"};
        InconsistencyCreator.main(argsInconsistencyCreator);
        promptEnterKey();

        System.out.println("------------------------------------------------------------------------------------------");
        // Generator
        System.out.println("Starting Generation");
        String[] argsGenerator =   {hdt, samples, inconsistencyJSON+"inconsistencies.json", temp, "N-TRIPLES", "0.1"};
        Generator.main(argsGenerator);

        System.out.println("------------------------------------------------------------------------------------------");
        // Statistics Sampled
        System.out.println("Starting Statistics");
        String[] argsStatisticsSampled = {samples+"Sample-"+hdtName, rdf, inconsistencyJSON+"Sampled/"};
        Statistics.main(argsStatisticsSampled);
        System.out.println("------------------------------------------------------------------------------------------");


    }


    @SuppressWarnings("unused")
    private static void promptEnterKey(){
        System.out.println("Press \"ENTER\" to continue...");
        try {
            int read = System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
