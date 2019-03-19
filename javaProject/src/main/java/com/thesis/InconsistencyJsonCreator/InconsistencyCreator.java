package com.thesis.InconsistencyJsonCreator;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class InconsistencyCreator {


    private static HashMap<String, Integer> WriteInconsistency(String OutputLocation, String InputLocationGraphs, String InputLocationAnalytics){
        HashMap<String, Integer> Inconsistencies = new HashMap<>();
        Path path = Paths.get(OutputLocation);
        try{
            List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line: allLines){
                String[] lineSplit = line.split(", \"Amount\" : ");
                Inconsistencies.put(lineSplit[0].replace("{\"Inconsistency\" : ","").replace("\"", "") , Integer.parseInt(lineSplit[1].replace("\"","").replace("}","").trim()));
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        return Inconsistencies;
    }


    public static void main(String[] args){
        /* main method that generates Inconsistencies when found.
         * @params {@code args[0] } Location of the amount and types of inconsistencies.
         * @returns void
         */
        System.out.println("Skipping");
        //WriteInconsistency(args[0], InputLocationGraphs, InputLocationAnalytics);




    }
}

