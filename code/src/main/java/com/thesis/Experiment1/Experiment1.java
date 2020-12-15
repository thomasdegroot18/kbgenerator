package com.thesis.Experiment1;


import com.thesis.kbInconsistencyLocator.InconsistencyLocator;


/**
 Testsuite holds a simple implementation of the functionality of the knowledge base generator.

 CREATED by:
 Thomas de Groot
 */
public class Experiment1 {

    public static void main(String[] args) throws Exception {
        String AbsoluteName = "/home/thomasdegroot/Documents/kbgenerator/code/resources/";
        //String AbsoluteName = "D:/Users/Thomas/Documents/thesis/kbgenerator/code/resources/";
        //String AbsoluteName = "/home/thomasdegroot/local/kbgenerator/code/resources/";

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
        String[] argsInconsistency = {hdt, rdf, "1", "true", "false", "0", "2000"};
        InconsistencyLocator.main(argsInconsistency);
        int size2000 = InconsistencyLocator.GeneralGraphNumber;
        long time2000 = InconsistencyLocator.timePassed;
        long ProcessTimeCreateSubgraph2000 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
        long ProcessTimeFindContradictions2000 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
        long ProcessTimeCreateAntiPatterns2000 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
        System.out.println("size 2000 Subgraph: "+size2000 + " Time Needed: "+ time2000 + "Subgraphs: " + ProcessTimeCreateSubgraph2000+ "Contradictions: " + ProcessTimeFindContradictions2000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns2000);
        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "3000"};
        InconsistencyLocator.main(argsInconsistency);
        int size3000 = InconsistencyLocator.GeneralGraphNumber;
        long time3000 = InconsistencyLocator.timePassed;
        long ProcessTimeCreateSubgraph3000 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
        long ProcessTimeFindContradictions3000 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
        long ProcessTimeCreateAntiPatterns3000 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
        System.out.println("size 3000 Subgraph: "+size3000 + " Time Needed: "+ time3000 + "Subgraphs: " + ProcessTimeCreateSubgraph3000+ "Contradictions: " + ProcessTimeFindContradictions3000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns3000);
        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "1000"};
        InconsistencyLocator.main(argsInconsistency);
        int size1000 = InconsistencyLocator.GeneralGraphNumber;
        long time1000 = InconsistencyLocator.timePassed;
        long ProcessTimeCreateSubgraph1000 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
        long ProcessTimeFindContradictions1000 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
        long ProcessTimeCreateAntiPatterns1000 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
        System.out.println("size 1000 Subgraph: "+size1000 + " Time Needed: "+ time1000 + "Subgraphs: " + ProcessTimeCreateSubgraph1000+ "Contradictions: " + ProcessTimeFindContradictions1000+"AntiPatterns: " + ProcessTimeCreateAntiPatterns1000);
        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "750"};
        InconsistencyLocator.main(argsInconsistency);
        int size750 = InconsistencyLocator.GeneralGraphNumber;
        long time750 = InconsistencyLocator.timePassed;
        long ProcessTimeCreateSubgraph750 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
        long ProcessTimeFindContradictions750 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
        long ProcessTimeCreateAntiPatterns750 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
        System.out.println("size 750 Subgraph: "+size750 + " Time Needed: "+ time750 + "Subgraphs: " + ProcessTimeCreateSubgraph750+  "Contradictions: " + ProcessTimeFindContradictions750+"AntiPatterns: " + ProcessTimeCreateAntiPatterns750);

        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "500"};
        InconsistencyLocator.main(argsInconsistency);
        int size500 = InconsistencyLocator.GeneralGraphNumber;
        long time500 = InconsistencyLocator.timePassed;
        long ProcessTimeCreateSubgraph500 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
        long ProcessTimeFindContradictions500 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
        long ProcessTimeCreateAntiPatterns500 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500 + "Subgraphs: " + ProcessTimeCreateSubgraph500+ "Contradictions: " + ProcessTimeFindContradictions500+"AntiPatterns: " + ProcessTimeCreateAntiPatterns500);

        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "250"};
        InconsistencyLocator.main(argsInconsistency);
        int size250 = InconsistencyLocator.GeneralGraphNumber;
        long time250 = InconsistencyLocator.timePassed;
        long ProcessTimeCreateSubgraph250 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
        long ProcessTimeFindContradictions250 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
        long ProcessTimeCreateAntiPatterns250 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
        System.out.println("size 250 Subgraph: "+size250 + " Time Needed: "+ time250 + "Subgraphs: " + ProcessTimeCreateSubgraph250+  "Contradictions: " + ProcessTimeFindContradictions250+"AntiPatterns: " + ProcessTimeCreateAntiPatterns250);
        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "100"};
        InconsistencyLocator.main(argsInconsistency);
        int size100 = InconsistencyLocator.GeneralGraphNumber;
        long time100 = InconsistencyLocator.timePassed;
        long ProcessTimeCreateSubgraph100 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
        long ProcessTimeFindContradictions100 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
        long ProcessTimeCreateAntiPatterns100 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
        System.out.println("size 100 Subgraph: "+size100 + " Time Needed: "+ time100 + "Subgraphs: " + ProcessTimeCreateSubgraph100+ "Contradictions: " + ProcessTimeFindContradictions100+"AntiPatterns: " + ProcessTimeCreateAntiPatterns100);
        argsInconsistency = new String[]{hdt, rdf, "500", "true", "false", "0", "50"};
        InconsistencyLocator.main(argsInconsistency);
        int size50 = InconsistencyLocator.GeneralGraphNumber;
        long time50 = InconsistencyLocator.timePassed;
        long ProcessTimeCreateSubgraph50 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
        long ProcessTimeFindContradictions50 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
        long ProcessTimeCreateAntiPatterns50 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
        System.out.println("size 50 Subgraph: "+size50 + " Time Needed: "+ time50 + " Subgraphs: " + ProcessTimeCreateSubgraph50+ " Contradictions: " + ProcessTimeFindContradictions50+" AntiPatterns: " + ProcessTimeCreateAntiPatterns50);
        System.out.println("size 100 Subgraph: "+size100 + " Time Needed: "+ time100 + " Subgraphs: " + ProcessTimeCreateSubgraph100+ " Contradictions: " + ProcessTimeFindContradictions100+" AntiPatterns: " + ProcessTimeCreateAntiPatterns100);
        System.out.println("size 250 Subgraph: "+size250 + " Time Needed: "+ time250 + " Subgraphs: " + ProcessTimeCreateSubgraph250+  " Contradictions: " + ProcessTimeFindContradictions250+" AntiPatterns: " + ProcessTimeCreateAntiPatterns250);
        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500 + " Subgraphs: " + ProcessTimeCreateSubgraph500+ " Contradictions: " + ProcessTimeFindContradictions500+" AntiPatterns: " + ProcessTimeCreateAntiPatterns500);
        System.out.println("size 750 Subgraph: "+size750 + " Time Needed: "+ time750 + " Subgraphs: " + ProcessTimeCreateSubgraph750+  " Contradictions: " + ProcessTimeFindContradictions750+ " AntiPatterns: " + ProcessTimeCreateAntiPatterns750);
        System.out.println("size 1000 Subgraph: "+size1000 + " Time Needed: "+ time1000 + " Subgraphs: " + ProcessTimeCreateSubgraph1000+ " Contradictions: " + ProcessTimeFindContradictions1000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns1000);
        System.out.println("size 2000 Subgraph: "+size2000 + " Time Needed: "+ time2000 + " Subgraphs: " + ProcessTimeCreateSubgraph2000+ " Contradictions: " + ProcessTimeFindContradictions2000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns2000);

    }

}
