package com.thesis.InconsistencyJsonCreator;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class InconsistencyCreator {


    private static HashMap<String, Integer> WriteInconsistency(String OutputLocation, String InputLocationGraphs, String InputLocationAnalytics){
        HashMap<String, Integer> Inconsistencies = new HashMap<>();
        Path path2 = Paths.get(InputLocationGraphs);
        Path path3 = Paths.get(InputLocationAnalytics);
        System.out.println(InputLocationGraphs);
        System.out.println(InputLocationAnalytics);
        try{
            FileOutputStream fileWriter = new FileOutputStream(new File(OutputLocation));
            List<String> allLines2 = Files.readAllLines(path2, StandardCharsets.UTF_8);
            List<String> allLines3 = Files.readAllLines(path3, StandardCharsets.UTF_8);
            boolean x = false;
            for (String line: allLines2){
                if (x) {
                    String elem = "{\"Inconsistency\" : \"" + line + "\", \"Amount\" : 14000 } \n";
                    fileWriter.write((elem).getBytes());
                    x = false;
                }
                if (line.startsWith("General graph number:")){
                    x = true;
                }
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
        //System.out.println("Skipping");
        WriteInconsistency(args[0], args[1], args[2]);




    }
}

