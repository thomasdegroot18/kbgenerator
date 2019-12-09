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
        long ProcessTimeCreateSubgraph100 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
        long ProcessTimeFindContradictions100 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
        long ProcessTimeCreateAntiPatterns100 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
        System.out.println("size 100 Subgraph: "+size100 + " Time Needed: "+ time100 + "Subgraphs: " + ProcessTimeCreateSubgraph100+ "Contradictions: " + ProcessTimeFindContradictions100+"AntiPatterns: " + ProcessTimeCreateAntiPatterns100);
        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "500"};
        InconsistencyLocator.main(argsInconsistency);
        int size500 = InconsistencyLocator.GeneralSubGraphFound;
        long time500 = InconsistencyLocator.timePassed;
        long ProcessTimeCreateSubgraph500 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
        long ProcessTimeFindContradictions500 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
        long ProcessTimeCreateAntiPatterns500 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
        System.out.println("size 100 Subgraph: "+size100 + " Time Needed: "+ time100 + "Subgraphs: " + ProcessTimeCreateSubgraph100+ "Contradictions: " + ProcessTimeFindContradictions100+"AntiPatterns: " + ProcessTimeCreateAntiPatterns100);
        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500 + "Subgraphs: " + ProcessTimeCreateSubgraph500+ "Contradictions: " + ProcessTimeFindContradictions500+"AntiPatterns: " + ProcessTimeCreateAntiPatterns500);
        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "1000"};
        InconsistencyLocator.main(argsInconsistency);
        int size1000 = InconsistencyLocator.GeneralSubGraphFound;
        long time1000 = InconsistencyLocator.timePassed;
        long ProcessTimeCreateSubgraph1000 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
        long ProcessTimeFindContradictions1000 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
        long ProcessTimeCreateAntiPatterns1000 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
        System.out.println("size 100 Subgraph: "+size100 + " Time Needed: "+ time100 + "Subgraphs: " + ProcessTimeCreateSubgraph100+ "Contradictions: " + ProcessTimeFindContradictions100+"AntiPatterns: " + ProcessTimeCreateAntiPatterns100);
        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500 + "Subgraphs: " + ProcessTimeCreateSubgraph500+ "Contradictions: " + ProcessTimeFindContradictions500+"AntiPatterns: " + ProcessTimeCreateAntiPatterns500);
        System.out.println("size 1000 Subgraph: "+size1000 + " Time Needed: "+ time1000 + "Subgraphs: " + ProcessTimeCreateSubgraph1000+ "Contradictions: " + ProcessTimeFindContradictions1000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns1000);

        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "5000"};
        InconsistencyLocator.main(argsInconsistency);
        int size5000 = InconsistencyLocator.GeneralSubGraphFound;
        long time5000 = InconsistencyLocator.timePassed;
        long ProcessTimeCreateSubgraph5000 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
        long ProcessTimeFindContradictions5000 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
        long ProcessTimeCreateAntiPatterns5000 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
        System.out.println("size 100 Subgraph: "+size100 + " Time Needed: "+ time100 + "Subgraphs: " + ProcessTimeCreateSubgraph100+ "Contradictions: " + ProcessTimeFindContradictions100+"AntiPatterns: " + ProcessTimeCreateAntiPatterns100);
        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500 + "Subgraphs: " + ProcessTimeCreateSubgraph500+ "Contradictions: " + ProcessTimeFindContradictions500+"AntiPatterns: " + ProcessTimeCreateAntiPatterns500);
        System.out.println("size 1000 Subgraph: "+size1000 + " Time Needed: "+ time1000 + "Subgraphs: " + ProcessTimeCreateSubgraph1000+ "Contradictions: " + ProcessTimeFindContradictions1000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns1000);
        System.out.println("size 5000 Subgraph: "+size5000 + " Time Needed: "+ time5000 + "Subgraphs: " + ProcessTimeCreateSubgraph5000+ "Contradictions: " + ProcessTimeFindContradictions5000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns5000);

        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "10000"};
        InconsistencyLocator.main(argsInconsistency);
        int size10000 = InconsistencyLocator.GeneralSubGraphFound;
        long time10000 = InconsistencyLocator.timePassed;
        long ProcessTimeCreateSubgraph10000 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
        long ProcessTimeFindContradictions10000 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
        long ProcessTimeCreateAntiPatterns10000 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
        System.out.println("size 100 Subgraph: "+size100 + " Time Needed: "+ time100 + "Subgraphs: " + ProcessTimeCreateSubgraph100+ "Contradictions: " + ProcessTimeFindContradictions100+"AntiPatterns: " + ProcessTimeCreateAntiPatterns100);
        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500 + "Subgraphs: " + ProcessTimeCreateSubgraph500+ "Contradictions: " + ProcessTimeFindContradictions500+"AntiPatterns: " + ProcessTimeCreateAntiPatterns500);
        System.out.println("size 1000 Subgraph: "+size1000 + " Time Needed: "+ time1000 + "Subgraphs: " + ProcessTimeCreateSubgraph1000+ "Contradictions: " + ProcessTimeFindContradictions1000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns1000);
        System.out.println("size 5000 Subgraph: "+size5000 + " Time Needed: "+ time5000 + "Subgraphs: " + ProcessTimeCreateSubgraph5000+ "Contradictions: " + ProcessTimeFindContradictions5000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns5000);
        System.out.println("size 10000 Subgraph: "+size10000 + " Time Needed: "+ time10000 + "Subgraphs: " + ProcessTimeCreateSubgraph10000+ "Contradictions: " + ProcessTimeFindContradictions10000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns10000);

        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "50000"};
        InconsistencyLocator.main(argsInconsistency);
        int size50000 = InconsistencyLocator.GeneralSubGraphFound;
        long time50000 = InconsistencyLocator.timePassed;
        long ProcessTimeCreateSubgraph50000 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
        long ProcessTimeFindContradictions50000 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
        long ProcessTimeCreateAntiPatterns50000 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
        System.out.println("size 100 Subgraph: "+size100 + " Time Needed: "+ time100 + "Subgraphs: " + ProcessTimeCreateSubgraph100+ "Contradictions: " + ProcessTimeFindContradictions100+"AntiPatterns: " + ProcessTimeCreateAntiPatterns100);
        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500 + "Subgraphs: " + ProcessTimeCreateSubgraph500+ "Contradictions: " + ProcessTimeFindContradictions500+"AntiPatterns: " + ProcessTimeCreateAntiPatterns500);
        System.out.println("size 1000 Subgraph: "+size1000 + " Time Needed: "+ time1000 + "Subgraphs: " + ProcessTimeCreateSubgraph1000+ "Contradictions: " + ProcessTimeFindContradictions1000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns1000);
        System.out.println("size 5000 Subgraph: "+size5000 + " Time Needed: "+ time5000 + "Subgraphs: " + ProcessTimeCreateSubgraph5000+ "Contradictions: " + ProcessTimeFindContradictions5000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns5000);
        System.out.println("size 10000 Subgraph: "+size10000 + " Time Needed: "+ time10000 + "Subgraphs: " + ProcessTimeCreateSubgraph10000+ "Contradictions: " + ProcessTimeFindContradictions10000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns10000);
        System.out.println("size 50000 Subgraph: "+size50000 + " Time Needed: "+ time50000 + "Subgraphs: " + ProcessTimeCreateSubgraph50000+ "Contradictions: " + ProcessTimeFindContradictions50000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns50000);
        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "100000"};
        InconsistencyLocator.main(argsInconsistency);
        int size100000 = InconsistencyLocator.GeneralSubGraphFound;
        long time100000 = InconsistencyLocator.timePassed;
        long ProcessTimeCreateSubgraph100000 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
        long ProcessTimeFindContradictions100000 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
        long ProcessTimeCreateAntiPatterns100000 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
        System.out.println("size 100 Subgraph: "+size100 + " Time Needed: "+ time100 + "Subgraphs: " + ProcessTimeCreateSubgraph100+ "Contradictions: " + ProcessTimeFindContradictions100+"AntiPatterns: " + ProcessTimeCreateAntiPatterns100);
        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500 + "Subgraphs: " + ProcessTimeCreateSubgraph500+ "Contradictions: " + ProcessTimeFindContradictions500+"AntiPatterns: " + ProcessTimeCreateAntiPatterns500);
        System.out.println("size 1000 Subgraph: "+size1000 + " Time Needed: "+ time1000 + "Subgraphs: " + ProcessTimeCreateSubgraph1000+ "Contradictions: " + ProcessTimeFindContradictions1000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns1000);
        System.out.println("size 5000 Subgraph: "+size5000 + " Time Needed: "+ time5000 + "Subgraphs: " + ProcessTimeCreateSubgraph5000+ "Contradictions: " + ProcessTimeFindContradictions5000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns5000);
        System.out.println("size 10000 Subgraph: "+size10000 + " Time Needed: "+ time10000 + "Subgraphs: " + ProcessTimeCreateSubgraph10000+ "Contradictions: " + ProcessTimeFindContradictions10000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns10000);
        System.out.println("size 50000 Subgraph: "+size50000 + " Time Needed: "+ time50000 + "Subgraphs: " + ProcessTimeCreateSubgraph50000+ "Contradictions: " + ProcessTimeFindContradictions50000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns50000);
        System.out.println("size 100000 Subgraph: "+size100000 + " Time Needed: "+ time100000 + "Subgraphs: " + ProcessTimeCreateSubgraph100000+ "Contradictions: " + ProcessTimeFindContradictions100000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns100000);
        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "50000000"};
        InconsistencyLocator.main(argsInconsistency);
        int size50000000 = InconsistencyLocator.GeneralSubGraphFound;
        long time50000000 = InconsistencyLocator.timePassed;
        long ProcessTimeCreateSubgraph50000000 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
        long ProcessTimeFindContradictions50000000 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
        long ProcessTimeCreateAntiPatterns50000000 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
        System.out.println("size 100 Subgraph: "+size100 + " Time Needed: "+ time100 + "Subgraphs: " + ProcessTimeCreateSubgraph100+ "Contradictions: " + ProcessTimeFindContradictions100+"AntiPatterns: " + ProcessTimeCreateAntiPatterns100);
        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500 + "Subgraphs: " + ProcessTimeCreateSubgraph500+ "Contradictions: " + ProcessTimeFindContradictions500+"AntiPatterns: " + ProcessTimeCreateAntiPatterns500);
        System.out.println("size 1000 Subgraph: "+size1000 + " Time Needed: "+ time1000 + "Subgraphs: " + ProcessTimeCreateSubgraph1000+ "Contradictions: " + ProcessTimeFindContradictions1000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns1000);
        System.out.println("size 5000 Subgraph: "+size5000 + " Time Needed: "+ time5000 + "Subgraphs: " + ProcessTimeCreateSubgraph5000+ "Contradictions: " + ProcessTimeFindContradictions5000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns5000);
        System.out.println("size 10000 Subgraph: "+size10000 + " Time Needed: "+ time10000 + "Subgraphs: " + ProcessTimeCreateSubgraph10000+ "Contradictions: " + ProcessTimeFindContradictions10000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns10000);
        System.out.println("size 50000 Subgraph: "+size50000 + " Time Needed: "+ time50000 + "Subgraphs: " + ProcessTimeCreateSubgraph50000+ "Contradictions: " + ProcessTimeFindContradictions50000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns50000);
        System.out.println("size 100000 Subgraph: "+size100000 + " Time Needed: "+ time100000 + "Subgraphs: " + ProcessTimeCreateSubgraph100000+ "Contradictions: " + ProcessTimeFindContradictions100000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns100000);
        System.out.println("size 50000000 Subgraph: "+size50000000 + " Time Needed: "+ time50000000 + "Subgraphs: " + ProcessTimeCreateSubgraph50000000+ "Contradictions: " + ProcessTimeFindContradictions50000000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns50000000);
    }

}
