package com.thesis.kbStatistics;


import com.thesis.SPARQLengine.SPARQLExecutioner;
import com.thesis.kbInconsistencyLocator.GeneralisedSubGraph;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdtjena.HDTGraph;

import java.util.ArrayList;
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
    private static String InconsistencyType(String SPARQLString){

        return "Test";
    }

    @SuppressWarnings("unused")
    private static String InconsistencyClassType(String SPARQLString){

        return "Test";
    }

    private double TailEffect(GeneralisedSubGraph Subgraph){
        try{
            Subgraph.SinglesRemoval();
            Subgraph.RebuildSPARQL();
            String NewSPARQLQuery = Subgraph.convertSPARQL();
            int CountTail = InconsistencyCount(NewSPARQLQuery);

            return (double)CountTail;
        } catch (Exception e){
            System.out.println("Could not remove Tail");
            return (double)0;
        }
    }



    private int InconsistencyCount(String SPARQLString){
        return SPARQLExecutioner.CounterResultPrinter(this.model, SPARQLString);
    }


    void RunAll(String SPARQLQuery, String StringOfInconsistency){
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
        int Count = InconsistencyCount(SPARQLQuery);
        int Size = InconsistencySize(GeneralGraph);
        double TailEffect = TailEffect(GeneralGraph);

        String Type = InconsistencyType(SPARQLQuery);
        String ClassType = InconsistencyClassType(SPARQLQuery);

        // Store results in array for this query with a array.
        InconsistencyStats InconsistencyStats = new InconsistencyStats(Count, Size, Type, ClassType, TailEffect);

        CollectedStatistics.put(SPARQLQuery, InconsistencyStats);
    }


    void WriteToFile(String OutputLocation){

        // take all information We have now. Converting the files.

        String LineToWrite = "[";

        List<String> StringLines = new ArrayList<>();
        StringLines.add(LineToWrite);

        int IndexNumber = 1;
        for (String SPARQLQueryKey : this.CollectedStatistics.keySet()){
            InconsistencyStats InconsistencyStats = CollectedStatistics.get(SPARQLQueryKey);

            int Count = InconsistencyStats.getCount();
            int Size = InconsistencyStats.getSize();
            String Type = InconsistencyStats.getType();
            String ClassType = InconsistencyStats.getClassType();
            double TailEffect = InconsistencyStats.getTailEffect();
            if(IndexNumber == 1){
                LineToWrite = "{\"id\":"+ IndexNumber +", \"Count\": "+Count+", \"Size\": " + Size + ", \"Type\": \""+ Type +"\", " +
                        "\"ClassType\": \""+ ClassType +"\", \"TailEffect\": "+ TailEffect +"}";
            } else{
                LineToWrite = ",{\"id\":"+ IndexNumber +", \"Count\": "+Count+", \"Size\": " + Size + ", \"Type\": \""+ Type +"\", " +
                        "\"ClassType\": \""+ ClassType +"\", \"TailEffect\": "+ TailEffect +"}";
            }


            StringLines.add(LineToWrite);

            LineToWrite = "";
            IndexNumber ++;
        }

        LineToWrite = LineToWrite + "]";
        StringLines.add(LineToWrite);
        // Writes file to JSON location.
        Statistics.writeJSON(OutputLocation, StringLines);

    }
}
