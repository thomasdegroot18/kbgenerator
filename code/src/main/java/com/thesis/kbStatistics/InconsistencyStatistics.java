package com.thesis.kbStatistics;


import com.thesis.SPARQLengine.SPARQLExecutioner;
import com.thesis.kbInconsistencyLocator.GeneralisedSubGraph;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdtjena.HDTGraph;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class InconsistencyStatistics {

    private Model model;
    private HashMap<String, InconsistencyStats> CollectedStatistics;

    InconsistencyStatistics(HDT hdt){
        HDTGraph Graph = new HDTGraph(hdt);
        this.model = ModelFactory.createModelForGraph(Graph );
        this.CollectedStatistics = new HashMap<>();
    }

    private static int InconsistencySize(GeneralisedSubGraph Subgraph){
        return Subgraph.GetVertices().size();
    }

    @SuppressWarnings("unused")
    private static String InconsistencyType(String SPARQLString, GeneralisedSubGraph Subgraph){
        try {
            int SizeBefore = Subgraph.GetVertices().size();
            Subgraph.SinglesRemoval();
            int SizeAfter = Subgraph.GetVertices().size();
            boolean Equivalence = Subgraph.getEquivalenceType();
            if (SizeBefore == SizeAfter && !Equivalence) {
                return "LoopWithoutEquivalence";
            }

            if (SizeBefore > SizeAfter && !Equivalence) {
                return "KiteWithoutEquivalence";
            }

            if (SizeBefore == SizeAfter) {
                return "LoopWithEquivalence";
            }

            if (SizeBefore > SizeAfter) {
                return "KiteWithEquivalence";
            }
            return "NoClassFound";
        } catch (Exception e){
            return "domainRange";
        }
    }

    @SuppressWarnings("unused")
    private static String InconsistencyClassType(String SPARQLString){

        return "Test";
    }

    private double TailEffect(GeneralisedSubGraph Subgraph, Integer number, String outputLocation){
        try{
            Subgraph.SinglesRemoval();
            Subgraph.RebuildSPARQL();
            String NewSPARQLQuery = Subgraph.convertSPARQL();
            int CountTail = InconsistencyCount(NewSPARQLQuery, number, outputLocation);

            return (double)CountTail;
        } catch (Exception e){
            System.out.println("Could not remove Tail");
            return (double)0;
        }
    }



    private int InconsistencyCount(String SPARQLString, Integer number, String outputLocation){
        return SPARQLExecutioner.CounterResultPrinter(this.model, SPARQLString, number, outputLocation);
    }

    private int InconsistencyCountperDataset(String SPARQLString, String DataSet, Integer number, String outputLocation){
        SPARQLString = SPARQLString.replace("SELECT * WHERE", "SELECT * FROM <"+DataSet+"> WHERE");
        System.out.println(SPARQLString);

        return SPARQLExecutioner.CounterResultPrinter(this.model, SPARQLString, number, outputLocation);
    }

    private int InconsistencyExistsChecker(String SPARQLString){
        return SPARQLExecutioner.ExistsPrinter(this.model, SPARQLString);
    }

    void RunAll(String SPARQLQuery, String StringOfInconsistency, Integer number, String outputLocation){
        if(CollectedStatistics.containsKey(SPARQLQuery)){
            return;
        }

        // Take information, Check if information is already tested, by checking a "hash"
        List<String> Subgraph = new ArrayList<>();
        for (String s: StringOfInconsistency.split(", ")){
            if(s.length() > 1){
                Subgraph.add(s);
            }
        }

        GeneralisedSubGraph GeneralGraph = new GeneralisedSubGraph(Subgraph);
        // Run tests for single inconsistency
        int Count = InconsistencyCount(SPARQLQuery, number, outputLocation);
        int Size = InconsistencySize(GeneralGraph);
        int[] CountArray = new int[1];
        String Type = InconsistencyType(SPARQLQuery, GeneralGraph);
        String ClassType = InconsistencyClassType(SPARQLQuery);
        double TailEffect = 0;
        System.out.println(Type);

        // Store results in array for this query with a array.
        InconsistencyStats InconsistencyStats = new InconsistencyStats(Count, Size, Type, ClassType, TailEffect, CountArray );

        CollectedStatistics.put(SPARQLQuery, InconsistencyStats);
    }

    void RunAll(String SPARQLQuery, String StringOfInconsistency, String[] Datasets, Integer number, String outputLocation){
        if(CollectedStatistics.containsKey(SPARQLQuery)){
            return;
        }

        // Take information, Check if information is already tested, by checking a "hash"
        List<String> Subgraph = new ArrayList<>();
        for (String s: StringOfInconsistency.split(", ")){
            if(s.length() > 1){
                Subgraph.add(s);
            }
        }

        GeneralisedSubGraph GeneralGraph = new GeneralisedSubGraph(Subgraph);
        // Run tests for single inconsistency
        int[] CountArray = new int[Datasets.length];
        int i = 0;
        for (String DataSet : Datasets){
            CountArray[i] = InconsistencyCountperDataset(SPARQLQuery,DataSet, number, outputLocation);
            System.out.println(DataSet+ " : "+ CountArray[i]);
            i ++;
        }
        int Count = InconsistencyCount(SPARQLQuery, number, outputLocation);
        int Size = InconsistencySize(GeneralGraph);

        String Type = InconsistencyType(SPARQLQuery, GeneralGraph);
        String ClassType = InconsistencyClassType(SPARQLQuery);
        System.out.println(Type);
        double TailEffect = 0; //TailEffect(GeneralGraph);



        // Store results in array for this query with a array.
        InconsistencyStats InconsistencyStats = new InconsistencyStats(Count, Size, Type, ClassType, TailEffect, CountArray);

        CollectedStatistics.put(SPARQLQuery, InconsistencyStats);
    }

    void RunExists(String SPARQLQuery, String StringOfInconsistency) {
        if (CollectedStatistics.containsKey(SPARQLQuery)) {
            return;
        }

        // Run tests for single inconsistency
        int Count = InconsistencyExistsChecker(SPARQLQuery);
        int Size = 0;
        double TailEffect = 0;
        int[] CountArray = new int[1];
        String Type = "";
        String ClassType = "";
        // Store results in array for this query with a array.
        InconsistencyStats InconsistencyStats = new InconsistencyStats(Count, Size, Type, ClassType, TailEffect, CountArray);

        CollectedStatistics.put(SPARQLQuery, InconsistencyStats);
    }


    void WriteToFile(String OutputLocation){

        // take all information We have now. Converting the files.

        String LineToWrite = "[";

        List<String> StringLines = new ArrayList<>();
        StringLines.add(LineToWrite);
        try {
            FileOutputStream fileWriter232 = new FileOutputStream(new File(OutputLocation.replace("InconsistencyStatistics", "inconsistencies")));

        int IndexNumber = 1;
        for (String SPARQLQueryKey : this.CollectedStatistics.keySet()){
            InconsistencyStats InconsistencyStats = CollectedStatistics.get(SPARQLQueryKey);

            int Count = InconsistencyStats.getCount();
            int Size = InconsistencyStats.getSize();
            String Type = InconsistencyStats.getType();
            String ClassType = InconsistencyStats.getClassType();
            double TailEffect = InconsistencyStats.getTailEffect();
            int[] CountArray = InconsistencyStats.getCountDataSet();
            try {
                if (IndexNumber == 1) {
                    if (Count > 0){
                        String text = "{\"SPARQLQuery\": \"" + SPARQLQueryKey + "\", \"Amount\" : " + Math.round(Count*0.2) + " }\n";
                        fileWriter232.write((text).getBytes());
                    }
                    LineToWrite = "{\"id\":" + IndexNumber + ", \"SPARQLQuery\": \"" + SPARQLQueryKey + "\", \"Count\": " + Count + ", \"Size\": " + Size + ", \"Type\": \"" + Type + "\", " +
                            "\"ClassType\": \"" + ClassType + "\", \"TailEffect\": " + TailEffect + ", \"PerDatasetCount\": " + Arrays.toString(CountArray) + "}";
                } else {
                    if (Count > 0){
                        String text2 = "{\"SPARQLQuery\": \"" + SPARQLQueryKey + "\", \"Amount\" : " + Math.round(Count*0.2) + " }\n";
                        fileWriter232.write((text2).getBytes());
                    }
                    LineToWrite = ",{\"id\":" + IndexNumber + ", \"SPARQLQuery\": \"" + SPARQLQueryKey + "\", \"Count\": " + Count + ", \"Size\": " + Size + ", \"Type\": \"" + Type + "\", " +
                            "\"ClassType\": \"" + ClassType + "\", \"TailEffect\": " + TailEffect + ", \"PerDatasetCount\": " + Arrays.toString(CountArray) + "}";
                }
            } catch (Exception e){

            }


            StringLines.add(LineToWrite);

            LineToWrite = "";
            IndexNumber ++;
        }

        LineToWrite = LineToWrite + "]";
        StringLines.add(LineToWrite);
        // Writes file to JSON location.
        Statistics.writeJSON(OutputLocation, StringLines);
        } catch (Exception e){

        }
    }
}
