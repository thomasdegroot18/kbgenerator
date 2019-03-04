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

class InconsistencyStatementChecker {
    private HDT hdt;

    InconsistencyStatementChecker(HashMap<String, Integer> Inconsistencies, HDT hdt){
        Model model = ModelFactory.createModelForGraph(new HDTGraph(hdt));
        Model modelResultSet = ModelFactory.createDefaultModel();
        for (String Inconsistency: Inconsistencies.keySet()){
            // Convert SPARQL TO USEFUL OTHER ITEM
            ArrayList<String[]> InconsistencyToList = InconsistencyToList(Inconsistency);

            int MinAmount = Inconsistencies.get(Inconsistency );
            ResultSet results = SPARQLExecutioner.SPARQLQuery(model, Inconsistency+ "LIMIT "+MinAmount*10);
            while(results.hasNext()){
                QuerySolution result = results.next();
                for (String[] elem :InconsistencyToList){
                    // Get the item.
                    modelResultSet.add(result.get(elem[0]).asResource(), ResourceFactory.createProperty(elem[1]), result.get(elem[2]));

                    // Build a random remover adder function.

                }
            }


        }
        try{
            Generator.WriteHDT(modelResultSet, "resources/extraFiles/temp/temporary.hdt");
            this.hdt = HDTManager.mapIndexedHDT("resources/extraFiles/temp/temporary.hdt");
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
