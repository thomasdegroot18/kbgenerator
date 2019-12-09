package com.thesis.lodalot;

import com.thesis.kbInconsistencyLocator.InconsistencyLocator;
import com.thesis.kbStatistics.Statistics;

/**
 LODalot holds a simple implementation that can extract inconsistencies from lod-a-lot.

 CREATED by:
 Thomas de Groot
 */
public class LODalot {

    public static void main(String[] args) throws Exception{
        String AbsoluteName = "/home/thomas/thesis/kbgenerator/javaProject/resources/";
        AbsoluteName = "D:/Users/Thomas/Documents/thesis/kbgenerator/javaProject/resources/";
        AbsoluteName = "/home/thomasdegroot/local/kbgenerator/javaProject/resources/";

        String hdt = "/home/wbeek/data/LOD-a-lot/LOD-a-lot.hdt";

        //hdt = AbsoluteName+"HDTs/yago2s.hdt";

        String rdf = AbsoluteName+"RDFs/";
        String samples = AbsoluteName+"Samples/";
        String inconsistencyJSON = AbsoluteName+"StatResults/";
        String temp = AbsoluteName+"extraFiles/temp/";
        String args3 = "0";
        System.out.println("AbsoluteName: "+AbsoluteName);
        System.out.println("rdf: "+rdf);
        System.out.println("hdt: "+hdt);
        System.out.println("samples: "+samples);
        System.out.println("inconsistencyJSON: "+inconsistencyJSON);
        System.out.println("temp: "+temp);

        System.out.println("------------------------------------------------------------------------------------------");
        // Statistics
        System.out.println("Starting Statistics");
        String[] argsStatistics =  {hdt, rdf, inconsistencyJSON, args3};
        Statistics.main(argsStatistics);
        // Inconsistency Locator
//        System.out.println("------------------------------------------------------------------------------------------");
//        System.out.println("Starting Locating Inconsistencies");
//        String[] argsInconsistency =  {hdt,
//                rdf,
//                "20",
//                "true",
//                "false",
//                "2000"
//        };
//
//        InconsistencyLocator.main(argsInconsistency);
        System.out.println("------------------------------------------------------------------------------------------");


        System.out.println("Finished");

    }

}
