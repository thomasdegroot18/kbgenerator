package com.thesis.SamplingTest;

import com.thesis.kbStatistics.Statistics;
import com.thesis.kbgenerator.Generator;

import java.io.File;

import static com.thesis.testSuite.testSuite.promptEnterKey;


public class samplingtest {

    /**
     Testsuite holds a simple implementation of the functionality of the knowledge base generator.

     CREATED by:
     Thomas de Groot
     */

    public static void main(String[] args) throws Exception{
        String AbsoluteName = "/home/thomas/thesis/kbgenerator/javaProject/resources/";
        AbsoluteName = "D:/Users/Thomas/Documents/thesis/kbgenerator/";
        AbsoluteName = "/home/thomasdegroot/local/kbgenerator/javaProject/resources/";

        String hdt;
        String hdtName;

        String TestInconsistency = AbsoluteName+"extraFiles/InconsistencyFile/SampledInconsistency.json";
        // Setting Locations
        switch (args[0]){
            case "0":
                // Setting Lod-a-lot here:
                hdt = "/home/wbeek/data/LOD-a-lot/lod-a-lot.hdt";
                hdtName = "lod-a-lot.hdt";
                break;
            case "1":
                // Setting DBpedia here:
                hdt = AbsoluteName+"HDTs/dbpedia2016-04en.hdt";
                hdtName = "dbpedia2016-04en.hdt";
                break;
            case "2":
                // Setting Freebase here:
                hdt = AbsoluteName+"HDTs/freebase-rdf-2013-12-01-00-00.hdt";
                hdtName = "freebase-rdf-2013-12-01-00-00.hdt";
                break;
            case "3":
                //Setting Yago2s here:
                hdt = AbsoluteName+"HDTs/yago2s.hdt";
                hdtName = "yago2s.hdt";
                TestInconsistency = AbsoluteName+"extraFiles/InconsistencyFile/SampledInconsistencyYAGO.json";
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
        String inconsistencyJSON = AbsoluteName+"StatResults/" +hdtName.replace(".hdt", "")+"/";
        String temp = AbsoluteName+"extraFiles/temp/"+hdtName.replace(".hdt", "")+"/";
        String SampledLocationStats = AbsoluteName+"StatResults/Sampled/" +hdtName.replace(".hdt", "")+"/";




        System.out.println("AbsoluteName: "+AbsoluteName);
        System.out.println("rdf: "+rdf);
        System.out.println("hdt: "+hdt);
        System.out.println("samples: "+samples);
        System.out.println("inconsistencyJSON: "+inconsistencyJSON);
        System.out.println("temp: "+temp);


        System.out.println("------------------------------------------------------------------------------------------");
        // Generator

        File newFolder = new File(temp);
        boolean created = newFolder.mkdir();
        if (!created){
            System.out.println("No temp DIR CREATED");
        }
        System.out.println("Starting Generation");
        String[] argsGenerator =   {hdt, samples,TestInconsistency, temp, "N-TRIPLES", "0.2"};
        Generator.main(argsGenerator);

        System.out.println("------------------------------------------------------------------------------------------");
        // Statistics Sampled
        promptEnterKey();
        System.out.println("Starting Statistics");
        String[] argsStatisticsSampled = {samples+"Sample-"+hdtName, rdf, SampledLocationStats};
        Statistics.main(argsStatisticsSampled);
        System.out.println("------------------------------------------------------------------------------------------");


    }




}
