package com.thesis.Experiment1;


import com.thesis.kbInconsistencyLocator.InconsistencyLocator;


/**
 Testsuite holds a simple implementation of the functionality of the knowledge base generator.

 CREATED by:
 Thomas de Groot
 */
public class Experiment3 {

    public static void main(String[] args) throws Exception {
        //String AbsoluteName = "/home/thomasdegroot/Documents/kbgenerator/code/resources/";
//        String AbsoluteName = "D:/Users/Thomas/Documents/thesis/kbgenerator/code/resources/";
        String AbsoluteName = "/home/thomasdegroot/local/kbgenerator/code/resources/";

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
        int size500 = 0;
        long time500 = 0;
        long ProcessTimeCreateSubgraph500 = 0;
        long ProcessTimeFindContradictions500 = 0;
        long ProcessTimeCreateAntiPatterns500 = 0;
        long subgraphs500 = 0;
        int size1000 = 0;
        long time1000 = 0;
        long ProcessTimeCreateSubgraph1000 = 0;
        long ProcessTimeFindContradictions1000 = 0;
        long ProcessTimeCreateAntiPatterns1000 = 0;
        long subgraphs1000 = 0;
        int size2500 = 0;
        long time2500 = 0;
        long ProcessTimeCreateSubgraph2500 = 0;
        long ProcessTimeFindContradictions2500 = 0;
        long ProcessTimeCreateAntiPatterns2500 =0;
        long subgraphs2500 = 0;
        int size5000 = 0;
        long time5000 = 0;
        long ProcessTimeCreateSubgraph5000 = 0;
        long ProcessTimeFindContradictions5000 = 0;
        long ProcessTimeCreateAntiPatterns5000 = 0;
        long subgraphs5000 = 0;
        int size7500 = 0;
        long time7500 = 0;
        long ProcessTimeCreateSubgraph7500 = 0;
        long ProcessTimeFindContradictions7500 = 0;
        long ProcessTimeCreateAntiPatterns7500 = 0;
        long subgraphs7500 = 0;
        int size10000 = 0;
        long time10000 = 0;
        long ProcessTimeCreateSubgraph10000 = 0;
        long ProcessTimeFindContradictions10000 =0;
        long ProcessTimeCreateAntiPatterns10000 =0;
        long subgraphs10000 =0;
        int size25000 = 0;
        long time25000 = 0;
        long ProcessTimeCreateSubgraph25000 = 0;
        long ProcessTimeFindContradictions25000 = 0;
        long ProcessTimeCreateAntiPatterns25000 = 0;
        long subgraphs25000 = 0;
        int size50000 = 0;
        long time50000 = 0;
        long ProcessTimeCreateSubgraph50000 = 0;
        long ProcessTimeFindContradictions50000 = 0;
        long ProcessTimeCreateAntiPatterns50000 = 0;
        long subgraphs50000 = 0;
        int size100000 = 0;
        long time100000 = 0;
        long ProcessTimeCreateSubgraph100000 = 0;
        long ProcessTimeFindContradictions100000 = 0;
        long ProcessTimeCreateAntiPatterns100000 = 0;
        long subgraphs100000 = 0;
        // Inconsistency Locator
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Starting Locating Inconsistencies");
        String[] argsInconsistency = {hdt, rdf, "20", "true", "false", "0", "500"};
//        InconsistencyLocator.main(argsInconsistency);
//         size500 = InconsistencyLocator.GeneralGraphNumber;
//         time500 = InconsistencyLocator.timePassed;
//         ProcessTimeCreateSubgraph500 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
//         ProcessTimeFindContradictions500 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
//         ProcessTimeCreateAntiPatterns500 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
//         subgraphs500 = InconsistencyLocator.subgraphs;
//        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500 + " Subgraphs: " + ProcessTimeCreateSubgraph500+ " Contradictions: " + ProcessTimeFindContradictions500+" AntiPatterns: " + ProcessTimeCreateAntiPatterns500 + " Total Amount of Subgraphs: "+ subgraphs500);
//        argsInconsistency = new String[]{hdt, rdf, "20", "true", "false", "0", "1000"};
//        InconsistencyLocator.main(argsInconsistency);
//         size1000 = InconsistencyLocator.GeneralGraphNumber;
//         time1000 = InconsistencyLocator.timePassed;
//         ProcessTimeCreateSubgraph1000 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
//         ProcessTimeFindContradictions1000 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
//         ProcessTimeCreateAntiPatterns1000 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
//         subgraphs1000 = InconsistencyLocator.subgraphs;
//        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500 + " Subgraphs: " + ProcessTimeCreateSubgraph500+ " Contradictions: " + ProcessTimeFindContradictions500+" AntiPatterns: " + ProcessTimeCreateAntiPatterns500 + " Total Amount of Subgraphs: "+ subgraphs500);
//        System.out.println("size 1000 Subgraph: "+size1000 + " Time Needed: "+ time1000 + " Subgraphs: " + ProcessTimeCreateSubgraph1000+ " Contradictions: " + ProcessTimeFindContradictions1000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns1000 + " Total Amount of Subgraphs: "+ subgraphs1000);
        argsInconsistency = new String[]{hdt, rdf, "20", "true", "false", "0", "2500"};
        InconsistencyLocator.main(argsInconsistency);
        size2500 = InconsistencyLocator.GeneralGraphNumber;
        time2500 = InconsistencyLocator.timePassed;
        ProcessTimeCreateSubgraph2500 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
        ProcessTimeFindContradictions2500 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
        ProcessTimeCreateAntiPatterns2500 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
        subgraphs2500 = InconsistencyLocator.subgraphs;
        System.out.println("size 500 Subgraph: "+size2500 + " Time Needed: "+ time2500 + " Subgraphs: " + ProcessTimeCreateSubgraph2500+ " Contradictions: " + ProcessTimeFindContradictions2500+" AntiPatterns: " + ProcessTimeCreateAntiPatterns2500 + " Total Amount of Subgraphs: "+ subgraphs2500);
        argsInconsistency = new String[]{hdt, rdf, "20", "true", "false", "0", "7500"};
        InconsistencyLocator.main(argsInconsistency);
        size7500 = InconsistencyLocator.GeneralGraphNumber;
        time7500 = InconsistencyLocator.timePassed;
        ProcessTimeCreateSubgraph7500 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
        ProcessTimeFindContradictions7500 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
        ProcessTimeCreateAntiPatterns7500 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
        subgraphs7500 = InconsistencyLocator.subgraphs;
        System.out.println("size 2500 Subgraph: "+size2500 + " Time Needed: "+ time2500 + " Subgraphs: " + ProcessTimeCreateSubgraph2500+ " Contradictions: " + ProcessTimeFindContradictions2500+" AntiPatterns: " + ProcessTimeCreateAntiPatterns2500 + " Total Amount of Subgraphs: "+ subgraphs2500);
        System.out.println("size 7500 Subgraph: "+size7500 + " Time Needed: "+ time7500 + " Subgraphs: " + ProcessTimeCreateSubgraph7500+ " Contradictions: " + ProcessTimeFindContradictions7500+" AntiPatterns: " + ProcessTimeCreateAntiPatterns7500 + " Total Amount of Subgraphs: "+ subgraphs7500);





//        argsInconsistency = new String[]{hdt, rdf, "20", "true", "false", "0", "5000"};
//        InconsistencyLocator.main(argsInconsistency);
//         size5000 = InconsistencyLocator.GeneralGraphNumber;
//         time5000 = InconsistencyLocator.timePassed;
//         ProcessTimeCreateSubgraph5000 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
//         ProcessTimeFindContradictions5000 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
//         ProcessTimeCreateAntiPatterns5000 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
//         subgraphs5000 = InconsistencyLocator.subgraphs;
//        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500 + " Subgraphs: " + ProcessTimeCreateSubgraph500+ " Contradictions: " + ProcessTimeFindContradictions500+" AntiPatterns: " + ProcessTimeCreateAntiPatterns500 + " Total Amount of Subgraphs: "+ subgraphs500);
//        System.out.println("size 1000 Subgraph: "+size1000 + " Time Needed: "+ time1000 + " Subgraphs: " + ProcessTimeCreateSubgraph1000+ " Contradictions: " + ProcessTimeFindContradictions1000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns1000 + " Total Amount of Subgraphs: "+ subgraphs1000);
//        System.out.println("size 2500 Subgraph: "+size2500 + " Time Needed: "+ time2500 + " Subgraphs: " + ProcessTimeCreateSubgraph2500+  " Contradictions: " + ProcessTimeFindContradictions2500+" AntiPatterns: " + ProcessTimeCreateAntiPatterns2500 + " Total Amount of Subgraphs: "+ subgraphs2500);
//        System.out.println("size 5000 Subgraph: "+size5000 + " Time Needed: "+ time5000 + " Subgraphs: " + ProcessTimeCreateSubgraph5000+ " Contradictions: " + ProcessTimeFindContradictions5000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns5000 + " Total Amount of Subgraphs: "+ subgraphs5000);
//         argsInconsistency = new String[]{hdt, rdf, "20", "true", "false", "0", "10000"};
//        InconsistencyLocator.main(argsInconsistency);
//         size10000 = InconsistencyLocator.GeneralGraphNumber;
//         time10000 = InconsistencyLocator.timePassed;
//         ProcessTimeCreateSubgraph10000 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
//         ProcessTimeFindContradictions10000 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
//         ProcessTimeCreateAntiPatterns10000 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
//         subgraphs10000 = InconsistencyLocator.subgraphs;
//        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500 + " Subgraphs: " + ProcessTimeCreateSubgraph500+ " Contradictions: " + ProcessTimeFindContradictions500+" AntiPatterns: " + ProcessTimeCreateAntiPatterns500 + " Total Amount of Subgraphs: "+ subgraphs500);
//        System.out.println("size 1000 Subgraph: "+size1000 + " Time Needed: "+ time1000 + " Subgraphs: " + ProcessTimeCreateSubgraph1000+ " Contradictions: " + ProcessTimeFindContradictions1000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns1000 + " Total Amount of Subgraphs: "+ subgraphs1000);
//        System.out.println("size 2500 Subgraph: "+size2500 + " Time Needed: "+ time2500 + " Subgraphs: " + ProcessTimeCreateSubgraph2500+  " Contradictions: " + ProcessTimeFindContradictions2500+" AntiPatterns: " + ProcessTimeCreateAntiPatterns2500 + " Total Amount of Subgraphs: "+ subgraphs2500);
//        System.out.println("size 5000 Subgraph: "+size5000 + " Time Needed: "+ time5000 + " Subgraphs: " + ProcessTimeCreateSubgraph5000+ " Contradictions: " + ProcessTimeFindContradictions5000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns5000 + " Total Amount of Subgraphs: "+ subgraphs5000);
//        System.out.println("size 7500 Subgraph: "+size7500 + " Time Needed: "+ time7500 + " Subgraphs: " + ProcessTimeCreateSubgraph7500+  " Contradictions: " + ProcessTimeFindContradictions7500+ " AntiPatterns: " + ProcessTimeCreateAntiPatterns7500 + " Total Amount of Subgraphs: "+ subgraphs7500);
//        System.out.println("size 10000 Subgraph: "+size10000 + " Time Needed: "+ time10000 + " Subgraphs: " + ProcessTimeCreateSubgraph10000+ " Contradictions: " + ProcessTimeFindContradictions10000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns10000 + " Total Amount of Subgraphs: "+ subgraphs10000);
//        argsInconsistency = new String[]{hdt, rdf, "20", "true", "false", "0", "25000"};
//        InconsistencyLocator.main(argsInconsistency);
//         size25000 = InconsistencyLocator.GeneralGraphNumber;
//         time25000 = InconsistencyLocator.timePassed;
//         ProcessTimeCreateSubgraph25000 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
//         ProcessTimeFindContradictions25000 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
//         ProcessTimeCreateAntiPatterns25000 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
//         subgraphs25000 = InconsistencyLocator.subgraphs;
//        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500 + " Subgraphs: " + ProcessTimeCreateSubgraph500+ " Contradictions: " + ProcessTimeFindContradictions500+" AntiPatterns: " + ProcessTimeCreateAntiPatterns500 + " Total Amount of Subgraphs: "+ subgraphs500);
//        System.out.println("size 1000 Subgraph: "+size1000 + " Time Needed: "+ time1000 + " Subgraphs: " + ProcessTimeCreateSubgraph1000+ " Contradictions: " + ProcessTimeFindContradictions1000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns1000 + " Total Amount of Subgraphs: "+ subgraphs1000);
//        System.out.println("size 2500 Subgraph: "+size2500 + " Time Needed: "+ time2500 + " Subgraphs: " + ProcessTimeCreateSubgraph2500+  " Contradictions: " + ProcessTimeFindContradictions2500+" AntiPatterns: " + ProcessTimeCreateAntiPatterns2500 + " Total Amount of Subgraphs: "+ subgraphs2500);
//        System.out.println("size 5000 Subgraph: "+size5000 + " Time Needed: "+ time5000 + " Subgraphs: " + ProcessTimeCreateSubgraph5000+ " Contradictions: " + ProcessTimeFindContradictions5000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns5000 + " Total Amount of Subgraphs: "+ subgraphs5000);
//        System.out.println("size 7500 Subgraph: "+size7500 + " Time Needed: "+ time7500 + " Subgraphs: " + ProcessTimeCreateSubgraph7500+  " Contradictions: " + ProcessTimeFindContradictions7500+ " AntiPatterns: " + ProcessTimeCreateAntiPatterns7500 + " Total Amount of Subgraphs: "+ subgraphs7500);
//        System.out.println("size 10000 Subgraph: "+size10000 + " Time Needed: "+ time10000 + " Subgraphs: " + ProcessTimeCreateSubgraph10000+ " Contradictions: " + ProcessTimeFindContradictions10000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns10000 + " Total Amount of Subgraphs: "+ subgraphs10000);
//        System.out.println("size 25000 Subgraph: "+size25000 + " Time Needed: "+ time25000 + " Subgraphs: " + ProcessTimeCreateSubgraph25000+ " Contradictions: " + ProcessTimeFindContradictions25000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns25000 + " Total Amount of Subgraphs: "+ subgraphs25000);
//        argsInconsistency = new String[]{hdt, rdf, "20", "true", "false", "0", "50000"};
//        InconsistencyLocator.main(argsInconsistency);
//         size50000 = InconsistencyLocator.GeneralGraphNumber;
//         time50000 = InconsistencyLocator.timePassed;
//         ProcessTimeCreateSubgraph50000 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
//         ProcessTimeFindContradictions50000 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
//         ProcessTimeCreateAntiPatterns50000 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
//         subgraphs50000 = InconsistencyLocator.subgraphs;
//        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500 + " Subgraphs: " + ProcessTimeCreateSubgraph500+ " Contradictions: " + ProcessTimeFindContradictions500+" AntiPatterns: " + ProcessTimeCreateAntiPatterns500 + " Total Amount of Subgraphs: "+ subgraphs500);
//        System.out.println("size 1000 Subgraph: "+size1000 + " Time Needed: "+ time1000 + " Subgraphs: " + ProcessTimeCreateSubgraph1000+ " Contradictions: " + ProcessTimeFindContradictions1000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns1000 + " Total Amount of Subgraphs: "+ subgraphs1000);
//        System.out.println("size 2500 Subgraph: "+size2500 + " Time Needed: "+ time2500 + " Subgraphs: " + ProcessTimeCreateSubgraph2500+  " Contradictions: " + ProcessTimeFindContradictions2500+" AntiPatterns: " + ProcessTimeCreateAntiPatterns2500 + " Total Amount of Subgraphs: "+ subgraphs2500);
//        System.out.println("size 5000 Subgraph: "+size5000 + " Time Needed: "+ time5000 + " Subgraphs: " + ProcessTimeCreateSubgraph5000+ " Contradictions: " + ProcessTimeFindContradictions5000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns5000 + " Total Amount of Subgraphs: "+ subgraphs5000);
//        System.out.println("size 7500 Subgraph: "+size7500 + " Time Needed: "+ time7500 + " Subgraphs: " + ProcessTimeCreateSubgraph7500+  " Contradictions: " + ProcessTimeFindContradictions7500+ " AntiPatterns: " + ProcessTimeCreateAntiPatterns7500 + " Total Amount of Subgraphs: "+ subgraphs7500);
//        System.out.println("size 10000 Subgraph: "+size10000 + " Time Needed: "+ time10000 + " Subgraphs: " + ProcessTimeCreateSubgraph10000+ " Contradictions: " + ProcessTimeFindContradictions10000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns10000 + " Total Amount of Subgraphs: "+ subgraphs10000);
//        System.out.println("size 25000 Subgraph: "+size25000 + " Time Needed: "+ time25000 + " Subgraphs: " + ProcessTimeCreateSubgraph25000+ " Contradictions: " + ProcessTimeFindContradictions25000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns25000 + " Total Amount of Subgraphs: "+ subgraphs25000);
//        System.out.println("size 50000 Subgraph: "+size50000 + " Time Needed: "+ time50000 + " Subgraphs: " + ProcessTimeCreateSubgraph50000+ " Contradictions: " + ProcessTimeFindContradictions50000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns50000 + " Total Amount of Subgraphs: "+ subgraphs50000);
//
//        argsInconsistency = new String[]{hdt, rdf, "20", "true", "false", "0", "100000"};
//        InconsistencyLocator.main(argsInconsistency);
//         size100000 = InconsistencyLocator.GeneralGraphNumber;
//         time100000 = InconsistencyLocator.timePassed;
//         ProcessTimeCreateSubgraph100000 = InconsistencyLocator.ProcessTimeCreateSubgraphTotal;
//         ProcessTimeFindContradictions100000 = InconsistencyLocator.ProcessTimeFindContradictionsTotal;
//         ProcessTimeCreateAntiPatterns100000 = InconsistencyLocator.ProcessTimeCreateAntiPatternsTotal;
//         subgraphs100000 = InconsistencyLocator.subgraphs;
//        System.out.println("size 500 Subgraph: "+size500 + " Time Needed: "+ time500 + " Subgraphs: " + ProcessTimeCreateSubgraph500+ " Contradictions: " + ProcessTimeFindContradictions500+" AntiPatterns: " + ProcessTimeCreateAntiPatterns500 + " Total Amount of Subgraphs: "+ subgraphs500);
//        System.out.println("size 1000 Subgraph: "+size1000 + " Time Needed: "+ time1000 + " Subgraphs: " + ProcessTimeCreateSubgraph1000+ " Contradictions: " + ProcessTimeFindContradictions1000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns1000 + " Total Amount of Subgraphs: "+ subgraphs1000);
//        System.out.println("size 2500 Subgraph: "+size2500 + " Time Needed: "+ time2500 + " Subgraphs: " + ProcessTimeCreateSubgraph2500+  " Contradictions: " + ProcessTimeFindContradictions2500+" AntiPatterns: " + ProcessTimeCreateAntiPatterns2500 + " Total Amount of Subgraphs: "+ subgraphs2500);
//        System.out.println("size 5000 Subgraph: "+size5000 + " Time Needed: "+ time5000 + " Subgraphs: " + ProcessTimeCreateSubgraph5000+ " Contradictions: " + ProcessTimeFindContradictions5000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns5000 + " Total Amount of Subgraphs: "+ subgraphs5000);
//        System.out.println("size 7500 Subgraph: "+size7500 + " Time Needed: "+ time7500 + " Subgraphs: " + ProcessTimeCreateSubgraph7500+  " Contradictions: " + ProcessTimeFindContradictions7500+ " AntiPatterns: " + ProcessTimeCreateAntiPatterns7500 + " Total Amount of Subgraphs: "+ subgraphs7500);
//        System.out.println("size 10000 Subgraph: "+size10000 + " Time Needed: "+ time10000 + " Subgraphs: " + ProcessTimeCreateSubgraph10000+ " Contradictions: " + ProcessTimeFindContradictions10000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns10000 + " Total Amount of Subgraphs: "+ subgraphs10000);
//        System.out.println("size 25000 Subgraph: "+size25000 + " Time Needed: "+ time25000 + " Subgraphs: " + ProcessTimeCreateSubgraph25000+ " Contradictions: " + ProcessTimeFindContradictions25000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns25000 + " Total Amount of Subgraphs: "+ subgraphs25000);
//        System.out.println("size 50000 Subgraph: "+size50000 + " Time Needed: "+ time50000 + " Subgraphs: " + ProcessTimeCreateSubgraph50000+ " Contradictions: " + ProcessTimeFindContradictions50000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns50000 + " Total Amount of Subgraphs: "+ subgraphs50000);
//        System.out.println("size 100000 Subgraph: "+size100000 + " Time Needed: "+ time100000 + " Subgraphs: " + ProcessTimeCreateSubgraph100000+ " Contradictions: " + ProcessTimeFindContradictions100000+" AntiPatterns: " + ProcessTimeCreateAntiPatterns100000 + " Total Amount of Subgraphs: "+ subgraphs100000);
//

    }

}
