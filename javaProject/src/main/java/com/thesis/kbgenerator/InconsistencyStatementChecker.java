package com.thesis.kbgenerator;

import com.thesis.SPARQLengine.SPARQLExecutioner;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.*;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdtjena.HDTGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class InconsistencyStatementChecker {
    private HDT hdt;
    private static Random rand = new Random();

    InconsistencyStatementChecker(HashMap<String, Integer> Inconsistencies, HDT hdt, String tempDir){
        Model model = ModelFactory.createModelForGraph(new HDTGraph(hdt));
        Model modelResultSet = ModelFactory.createDefaultModel();
        long oldSize = 0;
        for (String Inconsistency: Inconsistencies.keySet()){
            // Convert SPARQL TO USEFUL OTHER ITEM
            ArrayList<String[]> InconsistencyToList = InconsistencyToList(Inconsistency);

            int MinAmount = Inconsistencies.get(Inconsistency );
            int offsetVal = 0;
            long TimeOutDelay = 0;
            while (modelResultSet.size() < MinAmount){
                ResultSet results = SPARQLExecutioner.SPARQLQuery(model, Inconsistency+ "LIMIT "+MinAmount*5 + " OFFSET "+MinAmount*5*offsetVal, 20000+TimeOutDelay);
                int LIMITc = 0;
                try{
                    while(results.hasNext() && modelResultSet.size() < MinAmount){
                        QuerySolution result = results.next();
                        if(rand.nextDouble() < 0.42 ){
                            for (String[] elem :InconsistencyToList){
                                modelResultSet.add(result.get(elem[0]).asResource(), ResourceFactory.createProperty(elem[1]), result.get(elem[2]));

                            }

                        }
                        LIMITc ++;
                    }
                    if(oldSize == modelResultSet.size()) {
                        break;
                    }
                } catch (Exception e){
                    offsetVal = 0;
                    TimeOutDelay += 20000;
                    if (TimeOutDelay > 60000){
                        System.out.println("Could not find the minimal amount of Inconsistencies for this type:" + Inconsistency);
                        break;
                    }
                }
                oldSize = modelResultSet.size();
                //Making sure we always get results from the next query.
                if(LIMITc >= (MinAmount * 5 - 1)){
                    offsetVal ++;
                } else if(LIMITc <= MinAmount){
                    offsetVal = 0;
                }
            }


        }
        model.close();
        try{
            Generator.WriteHDT(modelResultSet, tempDir + "temporary.hdt", tempDir);
            this.hdt = HDTManager.mapIndexedHDT(tempDir +"temporary.hdt");
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    private ArrayList<String[]> InconsistencyToList(String Inconsistency){
        ArrayList<String[]> InconsistencyList = new ArrayList<>();
        for (String elem : Inconsistency.substring(Inconsistency.indexOf("{")+1, Inconsistency.indexOf("}")).split("\\. ")){
            String[] CleanString = new String[3];
            String[] ArrayElem = elem.split(" ");
            CleanString[0] = ArrayElem[0].substring(1);
            CleanString[1] = ArrayElem[1].substring(1, ArrayElem[1].length()-1);
            CleanString[2] = ArrayElem[2].substring(1);

            InconsistencyList.add(CleanString);
        }

        return InconsistencyList;

    }

    boolean checkMandatory(Statement toCheck){
        /* Returns true When the Statement is found in the hdt,
        Els it returns false.
         */
        String sub = toCheck.getSubject().toString();
        String predicate = toCheck.getPredicate().toString();
        String obj = toCheck.getObject().toString();
        try{
            IteratorTripleString it = this.hdt.search(sub, predicate, obj);
            return it.estimatedNumResults() > 0;

        } catch (Exception e){
            return false;
        }

    }

}
