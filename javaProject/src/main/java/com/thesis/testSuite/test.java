package com.thesis.testSuite;


import com.thesis.kbInconsistencyLocator.InconsistencyLocator;
import com.thesis.kbStatistics.Statistics;
import com.thesis.kbgenerator.Generator;

/**
Testsuite holds a simple implementation of the functionality of the knowledge base generator.

 CREATED by:
 Thomas de Groot
 */
public class test {

    public static void main(String[] args) throws Exception{

        String hdtName;

        // Setting HDT name
        if (args.length == 0){
            hdtName = "pizza.hdt";
        } else{
            hdtName = args[0];
        }

        // Setting Locations
        String hdt = "/home/thomas/thesis/kbgenerator/javaProject/resources/HDTs/";
        String rdf = "/home/thomas/thesis/kbgenerator/javaProject/resources/RDFs/";
        String samples = "/home/thomas/thesis/kbgenerator/javaProject/resources/Samples/";
        String inconsistencyJSON = "/home/thomas/thesis/kbgenerator/docs/Webpages/data/";
        String temp = "/home/thomas/thesis/kbgenerator/javaProject/resources/extraFiles/temp/";


        // Inconsistency Locator
        System.out.println("Starting Locating Inconsistencies");
        String[] argsInconsistency =   {hdt+hdtName,
                                        rdf,
                                        "10",
                                        "true"};

        InconsistencyLocator.main(argsInconsistency);

        // Statistics
        System.out.println("Starting Statistics");
        String[] argsStatistics =  {hdt+hdtName, rdf, inconsistencyJSON};
        Statistics.main(argsStatistics);

        // Generator
        System.out.println("Starting Generation");
        String[] argsGenerator =   {hdt+hdtName, samples, inconsistencyJSON+"inconsistencies.json", temp, "N-TRIPLES", "0.1"};

        Generator.main(argsGenerator);




    }


}
