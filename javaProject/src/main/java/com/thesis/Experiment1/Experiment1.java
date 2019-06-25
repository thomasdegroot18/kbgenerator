package com.thesis.Experiment1;


import com.thesis.InconsistencyJsonCreator.InconsistencyCreator;
import com.thesis.kbInconsistencyLocator.InconsistencyLocator;
import com.thesis.kbStatistics.Statistics;
import com.thesis.kbgenerator.Generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 Testsuite holds a simple implementation of the functionality of the knowledge base generator.

 CREATED by:
 Thomas de Groot
 */
public class Experiment1 {

    public static void main(String[] args) throws Exception {
        //String AbsoluteName = "/home/thomasdegroot/Documents/kbgenerator/javaProject/resources/";
        String AbsoluteName = "D:/Users/Thomas/Documents/thesis/kbgenerator/javaProject/resources/";
        //String AbsoluteName = "/home/thomasdegroot/local/kbgenerator/javaProject/resources/";

        String hdt;
        String hdtName;

        hdt = AbsoluteName + "HDTs/pizza.hdt";
        hdtName = "pizza.hdt";


        String rdf = AbsoluteName + "RDFs/";
        String samples = AbsoluteName + "Samples/";
        String inconsistencyJSON = AbsoluteName + "StatResults/" + hdtName.replace(".hdt", "") + "/";
        String temp = AbsoluteName + "extraFiles/temp/" + hdtName.replace(".hdt", "") + "/";

        System.out.println("AbsoluteName: " + AbsoluteName);
        System.out.println("rdf: " + rdf);
        System.out.println("hdt: " + hdt);
        System.out.println("samples: " + samples + "Sample-" + hdtName);
        System.out.println("inconsistencyJSON: " + inconsistencyJSON);
        System.out.println("temp: " + temp);

        // Inconsistency Locator
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Starting Locating Inconsistencies");
        String[] argsInconsistency = {hdt, rdf, "500", "true", "false", "0", "100"};
//        InconsistencyLocator.main(argsInconsistency);
//        int size100 = InconsistencyLocator.GeneralSubGraphFound;
//        long time100 = InconsistencyLocator.timePassed;
//        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "250"};
//        InconsistencyLocator.main(argsInconsistency);
//        int size250 = InconsistencyLocator.GeneralSubGraphFound;
//        long time250 = InconsistencyLocator.timePassed;
//        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "500"};
//        InconsistencyLocator.main(argsInconsistency);
//        int size500 = InconsistencyLocator.GeneralSubGraphFound;
//        long time500 = InconsistencyLocator.timePassed;
//        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "1000"};
//        InconsistencyLocator.main(argsInconsistency);
//        int size1000 = InconsistencyLocator.GeneralSubGraphFound;
//        long time1000 = InconsistencyLocator.timePassed;
        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "2000"};
        InconsistencyLocator.main(argsInconsistency);
        int size2000 = InconsistencyLocator.GeneralSubGraphFound;
        long time2000 = InconsistencyLocator.timePassed;

//        System.out.println("size 100 Subgraph: "+size100 + " Time Needed: "+ time100);
//        System.out.println("size 250 Subgraph: "+size250 + " Time Needed: "+ time250);
//        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500);
//        System.out.println("size 1000 Subgraph: "+size1000 + " Time Needed: "+ time1000);
        System.out.println("size 2000 Subgraph: "+size2000 + " Time Needed: "+ time2000);
    }

}
