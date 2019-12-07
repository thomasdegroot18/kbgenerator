package com.thesis.Experiment1;


import com.thesis.kbInconsistencyLocator.InconsistencyLocator;


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

        hdt = AbsoluteName + "HDTs/yago2s.hdt";
        hdtName = "yago2s.hdt";


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
        String[] argsInconsistency = {hdt, rdf, "1", "true", "false", "0", "100"};
        InconsistencyLocator.main(argsInconsistency);
        int size100 = InconsistencyLocator.GeneralSubGraphFound;
        long time100 = InconsistencyLocator.timePassed;
        System.out.println("size 100 Subgraph: "+size100 + " Time Needed: "+ time100);
        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "500"};
        InconsistencyLocator.main(argsInconsistency);
        int size500 = InconsistencyLocator.GeneralSubGraphFound;
        long time500 = InconsistencyLocator.timePassed;
        System.out.println("size 100 Subgraph: "+size100 + " Time Needed: "+ time100);
        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500);
        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "1000"};
        InconsistencyLocator.main(argsInconsistency);
        int size1000 = InconsistencyLocator.GeneralSubGraphFound;
        long time1000 = InconsistencyLocator.timePassed;
        System.out.println("size 100 Subgraph: "+size100 + " Time Needed: "+ time100);
        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500);
        System.out.println("size 1000 Subgraph: "+size1000 + " Time Needed: "+ time1000);
        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "5000"};
        InconsistencyLocator.main(argsInconsistency);
        int size5000 = InconsistencyLocator.GeneralSubGraphFound;
        long time5000 = InconsistencyLocator.timePassed;
        System.out.println("size 100 Subgraph: "+size100 + " Time Needed: "+ time100);
        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500);
        System.out.println("size 1000 Subgraph: "+size1000 + " Time Needed: "+ time1000);
        System.out.println("size 5000 Subgraph: "+size5000 + " Time Needed: "+ time5000);
        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "10000"};
        InconsistencyLocator.main(argsInconsistency);
        int size10000 = InconsistencyLocator.GeneralSubGraphFound;
        long time10000 = InconsistencyLocator.timePassed;
        System.out.println("size 100 Subgraph: "+size100 + " Time Needed: "+ time100);
        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500);
        System.out.println("size 1000 Subgraph: "+size1000 + " Time Needed: "+ time1000);
        System.out.println("size 5000 Subgraph: "+size5000 + " Time Needed: "+ time5000);
        System.out.println("size 10000 Subgraph: "+size10000 + " Time Needed: "+ time10000);
        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "50000"};
        InconsistencyLocator.main(argsInconsistency);
        int size50000 = InconsistencyLocator.GeneralSubGraphFound;
        long time50000 = InconsistencyLocator.timePassed;
        System.out.println("size 100 Subgraph: "+size100 + " Time Needed: "+ time100);
        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500);
        System.out.println("size 1000 Subgraph: "+size1000 + " Time Needed: "+ time1000);
        System.out.println("size 5000 Subgraph: "+size5000 + " Time Needed: "+ time5000);
        System.out.println("size 10000 Subgraph: "+size10000 + " Time Needed: "+ time10000);
        System.out.println("size 50000 Subgraph: "+size50000 + " Time Needed: "+ time50000);
        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "100000"};
        InconsistencyLocator.main(argsInconsistency);
        int size100000 = InconsistencyLocator.GeneralSubGraphFound;
        long time100000 = InconsistencyLocator.timePassed;
        System.out.println("size 100 Subgraph: "+size100 + " Time Needed: "+ time100);
        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500);
        System.out.println("size 1000 Subgraph: "+size1000 + " Time Needed: "+ time1000);
        System.out.println("size 5000 Subgraph: "+size5000 + " Time Needed: "+ time5000);
        System.out.println("size 10000 Subgraph: "+size10000 + " Time Needed: "+ time10000);
        System.out.println("size 50000 Subgraph: "+size50000 + " Time Needed: "+ time50000);
        System.out.println("size 100000 Subgraph: "+size100000 + " Time Needed: "+ time100000);
        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "50000000"};
        InconsistencyLocator.main(argsInconsistency);
        int size50000000 = InconsistencyLocator.GeneralSubGraphFound;
        long time50000000 = InconsistencyLocator.timePassed;
        System.out.println("size 100 Subgraph: "+size100 + " Time Needed: "+ time100);
        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500);
        System.out.println("size 1000 Subgraph: "+size1000 + " Time Needed: "+ time1000);
        System.out.println("size 5000 Subgraph: "+size5000 + " Time Needed: "+ time5000);
        System.out.println("size 10000 Subgraph: "+size10000 + " Time Needed: "+ time10000);
        System.out.println("size 50000 Subgraph: "+size50000 + " Time Needed: "+ time50000);
        System.out.println("size 100000 Subgraph: "+size100000 + " Time Needed: "+ time100000);
        System.out.println("size 50000000 Subgraph: "+size50000000 + " Time Needed: "+ time50000000);
    }

}
