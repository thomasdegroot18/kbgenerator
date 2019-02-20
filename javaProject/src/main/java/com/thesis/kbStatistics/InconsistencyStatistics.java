package com.thesis.kbStatistics;


import com.thesis.SPARQLengine.SPARQLExecutioner;
import org.apache.jena.rdf.model.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InconsistencyStatistics {

    private Model model;
    private HashMap<String, InconsistencyStats> CollectedStatistics;

    InconsistencyStatistics(Model model){
        this.model = model;
        this.CollectedStatistics = new HashMap<>();
    }

    private static int InconsistencySize(String SPARQLString){


        return 5;
    }

    private static String InconsistencyType(String SPARQLString){


        return "Test";
    }

    private static String InconsistencyClasstype(String SPARQLString){


        return "Test";
    }

    private static float TailEffect(String SPARQLString){


        return 0;
    }

    private int InconsistencyCount(String SPARQLString){

        return SPARQLExecutioner.CounterResultPrinter(this.model, SPARQLString);
    }


    @SuppressWarnings("UnusedReturnValue")
    private static HashMap<String, Integer> InconsistencyCheck(String fileLocation, HashMap<String, Integer> StoredValues) {

        File file = new File(fileLocation);
        try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;


            while((line = br.readLine()) != null){
                if (line.startsWith("SELECT")){
                    if(StoredValues.getOrDefault(line, null) == null){
                        StoredValues.put(line, null);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        return StoredValues;
    }



    private static HashMap<String, Integer> InconsistencyCheck(String fileLocation) {
        //Load the SPARQL Query Location
        HashMap<String, Integer> StoredValues = new HashMap<>();

        File file = new File(fileLocation);
        try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;


            while((line = br.readLine()) != null){
                if (line.startsWith("SELECT")){
                    StoredValues.put(line, null);
                }

            }
        } catch (Exception e){
            e.printStackTrace();
        }


        return StoredValues;
    }


    void RunAll(String SPARQLQuery, String StringOfInconsistency){
        // Take information, Check if information is already tested, by checking a "hash"

        // Run tests for single inconsistency
        int Count = InconsistencyCount(SPARQLQuery);
        int Size = InconsistencySize(SPARQLQuery);
        String Type = InconsistencyType(SPARQLQuery);
        String ClassType = InconsistencyClasstype(SPARQLQuery);
        float Tails = TailEffect(SPARQLQuery);



        // Store results in array for this query with a array.
        InconsistencyStats InconsistencyStats = new InconsistencyStats(Count, Size, Type, ClassType, Tails);

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
            float TailEffect = InconsistencyStats.getTailEffect();

            LineToWrite = "{\"id\":"+ IndexNumber +", \"Count\": "+Count+", \"Size\": " + Size + ", \"Type\": \""+ Type +"\", " +
                          "\"ClassType\": \""+ ClassType +"\", \"TailEffect\": "+ TailEffect +"}";


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
