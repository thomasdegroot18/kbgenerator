package com.thesis.testSuite;


import com.thesis.kbInconsistencyLocator.InconsistencyLocator;
import com.thesis.kbStatistics.Statistics;
import com.thesis.kbgenerator.Generator;

/**
Testsuite holds a simple implementation of the functionality of the knowledge base generator.

 CREATED by:
 Thomas de Groot
 */
public class testSuite {

    public static void main(String[] args) throws Exception{

        //String AbsoluteName = "/home/thomas/thesis/kbgenerator/";
        //String AbsoluteName = "D:/Users/Thomas/Documents/thesis/kbgenerator/";
        String AbsoluteName = "/home/thomasdegroot/local/kbgenerator/";

        String hdtName;

        // Setting HDT name/Lod-a-lot
        if (args.length == 0){
            // hdtName = "pizza.hdt";
            hdtName = "LOD-a-lot";
        } else{
            hdtName = args[0];
        }

        // TODO: Testing for 4 different hdts
        // Setting Locations

        // Setting Lod-a-lot here:
        //String hdt = AbsoluteName+"javaProject/resources/HDTs/";
        String hdt = "/home/wbeek/data/LOD-a-lot/";

        String rdf = AbsoluteName+"javaProject/resources/RDFs/";
        String samples = AbsoluteName+"javaProject/resources/Samples/";
        String inconsistencyJSON = AbsoluteName+"docs/Webpages/data/";
        String temp = AbsoluteName+"javaProject/resources/extraFiles/temp/";

        System.out.println("AbsoluteName: "+AbsoluteName);
        System.out.println("rdf: "+rdf);
        System.out.println("hdt: "+hdt);
        System.out.println("samples: "+samples);
        System.out.println("inconsistencyJSON: "+inconsistencyJSON);
        System.out.println("temp: "+temp);

        // Inconsistency Locator
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Starting Locating Inconsistencies");
        String[] argsInconsistency =   {hdt+hdtName,
                                        rdf,
                                        "10",
                                        "true"};

        InconsistencyLocator.main(argsInconsistency);
        System.out.println("------------------------------------------------------------------------------------------");
        // Statistics
        System.out.println("Starting Statistics");
        String[] argsStatistics =  {hdt+hdtName, rdf, inconsistencyJSON};
        Statistics.main(argsStatistics);
        System.out.println("------------------------------------------------------------------------------------------");
        // Generator
        System.out.println("Starting Generation");
        String[] argsGenerator =   {hdt+hdtName, samples, inconsistencyJSON+"inconsistencies.json", temp, "N-TRIPLES", "0.1"};

        Generator.main(argsGenerator);




    }


}
