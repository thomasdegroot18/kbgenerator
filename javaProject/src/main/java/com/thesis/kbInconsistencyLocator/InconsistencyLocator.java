package com.thesis.kbInconsistencyLocator;

import com.clarkparsia.owlapi.explanation.TransactionAwareSingleExpGen;
import openllet.core.KnowledgeBase;
import openllet.jena.PelletInfGraph;
import openllet.jena.PelletReasonerFactory;
import openllet.owlapi.OWL;
import openllet.owlapi.PelletReasoner;
import openllet.owlapi.explanation.GlassBoxExplanation;
import openllet.owlapi.explanation.PelletExplanation;
import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;
import openllet.shared.tools.Log;
import org.apache.jena.graph.*;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.rulesys.builtins.Max;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.triples.TripleString;
import openllet.owlapi.parser.OWLFunctionalSyntaxParser;
import org.semanticweb.owlapi.debugging.BlackBoxOWLDebugger;
import org.semanticweb.owlapi.debugging.OWLDebugger;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.io.StringDocumentSource;

import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.Collectors;


import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLParser;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAPIPreconditions;
import ru.avicomp.ontapi.OntManagers;
import ru.avicomp.ontapi.OntologyManager;
import ru.avicomp.ontapi.OntologyModel;

import javax.annotation.Nonnegative;


/**
 *
 * @author Thomas de Groot
 */


public class InconsistencyLocator

{
    // Setting boolean testing
    private static boolean testing_Bool = true;

    // Setting other variables
    private static byte[] strToBytes = ("New general inconsistency: \n").getBytes();  // Small string to separate different explanations.
    private static int MaxExplanations;                                             // Max explanation Storage.
    private static Random rand = new Random();                                      // Random Instantiation
    private static String[] classLabels;                                            // ClassLabel storage
    private static String[] individualLabels;                                       // InstanceLabel Storage
    private static boolean verbose;                                                 // Verbosity storage
    private static List<GeneralisedSubGraph> GeneralGraphs = new ArrayList<>();     // Generalised SubGraph Storage
    private static Map<GeneralisedSubGraph, Integer> SortingListMap = new HashMap<>(); // Generalised SubGraph Storage
    private static int maxValueExperiment = 500;
    // Setting the new Array: 500. TODO: get it better fixed.
    private static int[] TempStorageGenGraph = new int[500];
    private static int InconsistenciesHit = 11;                                      // Counter of inconsistencies
    private static int TripleGap = 0;
    private static int TotalInconsistenciesBeforeBreak;                             // Break terminator for inconsistencies hit
    private static boolean UnBreakable;                                             // Stores boolean for breaking after amount of Inconsistencies
    private static IsomorphismManager IsoChecker = new IsomorphismManager();        // Start IsomorphismManager
    public static int GeneralGraphNumber = 0;
    public static int GeneralSubGraphFound = 0;
    public static int subgraphs = 0;
    public static long timePassed = 0;
    private static String NameFile;
    private static long ProcessTimeFindContradictionsStart;
    private static long ProcessTimeCreateSubgraph = 0;
    private static long ProcessTimeFindContradictions = 0;
    private static long ProcessTimeCreateAntiPatterns = 0;
    public static long ProcessTimeCreateSubgraphTotal = 0;
    public static long ProcessTimeFindContradictionsTotal = 0;
    public static long ProcessTimeCreateAntiPatternsTotal = 0;

    private static List StreamParser(OWLAxiom InconsistencyExplanationLine){
        // Instantiate a new Array List.
        List<String> NewList = new ArrayList<>();

        // Retrieve the individuals as a array of objects.
        Object[] ListIndividuals = InconsistencyExplanationLine.individualsInSignature().toArray();
        Object[] ListIndividualsAnon = InconsistencyExplanationLine.anonymousIndividuals().toArray();
        // Retrieve the classes as a array of objects.
        Object[] ListClasses = InconsistencyExplanationLine.classesInSignature().toArray();

        // Retrieve the classes as a array of objects.
        Object[] ListProperties = InconsistencyExplanationLine.objectPropertiesInSignature().toArray();
        Object[] ListDProperties = InconsistencyExplanationLine.dataPropertiesInSignature().toArray();

        Object[] ListDtypes = InconsistencyExplanationLine.datatypesInSignature().toArray();
        // Instantiate the value.
        int value;
        int prevValue = 0;

        // Loop through the list of individuals.
        for (Object ListItem: ListIndividuals ) {
            // generate the value where the object is found in the line.
            value = InconsistencyExplanationLine.toString().indexOf(ListItem.toString());
            // If not found throw error.
            if (value < 0){
                throw new StackOverflowError("Nothing found in Inconsistency Explanation, This is not possible. check" +
                        " input. " + ListItem.toString() + "  " + InconsistencyExplanationLine.toString());
            }

            // Check if the value is larger or smaller than the previous value. This is needed to know as to which side the item needs to be added.

            // If the value is larger than the previous value it needs to be appended to back.
            if (value > prevValue){
                // add to newList
                NewList.add(ListItem.toString()+"IndividualCheck82910283");
                // Set the next value
                prevValue = value;
            } else if (value < prevValue) {
                // If the value is smaller than the prevValue it has to be added to the front.
                NewList.add(0, ListItem.toString()+"IndividualCheck82910283");
                // Set the next value
                prevValue = value;
                // If it ever hits this check it means that the item occurs twice. It means that the value is equal.
            } else {
                // Shout warning just in case.
                System.out.println("Item occurred twice Check if it problem. "+ InconsistencyExplanationLine.toString());
                NewList.add(ListItem.toString()+"IndividualCheck82910283");
            }
        }
        // Loop through the list of individuals.
        for (Object ListItem: ListIndividualsAnon ) {
            // generate the value where the object is found in the line.
            value = InconsistencyExplanationLine.toString().indexOf(ListItem.toString());
            // If not found throw error.
            if (value < 0){
                throw new StackOverflowError("Nothing found in Inconsistency Explanation, This is not possible. check" +
                        " input. " + ListItem.toString() + "  " + InconsistencyExplanationLine.toString());
            }

            // Check if the value is larger or smaller than the previous value. This is needed to know as to which side the item needs to be added.

            // If the value is larger than the previous value it needs to be appended to back.
            if (value > prevValue){
                // add to newList
                NewList.add(ListItem.toString()+"IndividualCheck82910283");
                // Set the next value
                prevValue = value;
            } else if (value < prevValue) {
                // If the value is smaller than the prevValue it has to be added to the front.
                NewList.add(0, ListItem.toString()+"IndividualCheck82910283");
                // Set the next value
                prevValue = value;
                // If it ever hits this check it means that the item occurs twice. It means that the value is equal.
            } else {
                // Shout warning just in case.
                System.out.println("Item occurred twice Check if it problem. "+ InconsistencyExplanationLine.toString());
                NewList.add(ListItem.toString()+"IndividualCheck82910283");
            }
        }
        for (Object ListItem: ListDProperties ) {
            // generate the value where the object is found in the line.
            value = InconsistencyExplanationLine.toString().indexOf(ListItem.toString());
            // If not found throw error.
            if (value < 0) {
                throw new StackOverflowError("Nothing found in Inconsistency Explanation, This is not possible. check" +
                        " input. " + ListItem.toString() + "  " + InconsistencyExplanationLine.toString());
            }
            // Check if the value is larger or smaller than the previous value. This is needed to know as to which side the item needs to be added.

            // If the value is larger than the previous value it needs to be appended to back.
            if (value > prevValue){
                // add to newList
                NewList.add(ListItem.toString());
                // Set the next value
                prevValue = value;
            } else if (value < prevValue) {
                // If the value is smaller than the prevValue it has to be added to the front.
                NewList.add(0, ListItem.toString());
                // Set the next value
                prevValue = value;
                // If it ever hits this check it means that the item occurs twice. It means that the value is equal.
            } else {
                // Shout warning just in case.
                System.out.println("Item occurred twice Check if it problem. " + InconsistencyExplanationLine.toString());
                NewList.add(ListItem.toString());
            }

        }

        for (Object ListItem: ListDtypes ) {
            // generate the value where the object is found in the line.
            if(ListItem.toString().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#langString")){
                value = 9999999;
            }
            else {
                value = InconsistencyExplanationLine.toString().indexOf(ListItem.toString());
            }
            // If not found throw error.
            if (value < 0) {
                throw new StackOverflowError("Nothing found in Inconsistency Explanation, This is not possible. check" +
                        " input. " + ListItem.toString() + "  " + InconsistencyExplanationLine.toString());
            }
            // Check if the value is larger or smaller than the previous value. This is needed to know as to which side the item needs to be added.

            // If the value is larger than the previous value it needs to be appended to back.
            if (value > prevValue){
                // add to newList
                NewList.add(ListItem.toString());
                // Set the next value
                prevValue = value;
            } else if (value < prevValue) {
                // If the value is smaller than the prevValue it has to be added to the front.
                NewList.add(0, ListItem.toString());
                // Set the next value
                prevValue = value;
                // If it ever hits this check it means that the item occurs twice. It means that the value is equal.
            } else {
                // Shout warning just in case.
                System.out.println("Item occurred twice Check if it problem. " + InconsistencyExplanationLine.toString());
                NewList.add(ListItem.toString());
            }

        }

        for (Object ListItem: ListProperties ) {
            // generate the value where the object is found in the line.
            value = InconsistencyExplanationLine.toString().indexOf(ListItem.toString());
            // If not found throw error.
            if (value < 0) {
                throw new StackOverflowError("Nothing found in Inconsistency Explanation, This is not possible. check" +
                        " input. " + ListItem.toString() + "  " + InconsistencyExplanationLine.toString());
            }
            // Check if the value is larger or smaller than the previous value. This is needed to know as to which side the item needs to be added.

            // If the value is larger than the previous value it needs to be appended to back.
            if (value > prevValue){
                // add to newList
                NewList.add(ListItem.toString());
                // Set the next value
                prevValue = value;
            } else if (value < prevValue) {
                // If the value is smaller than the prevValue it has to be added to the front.
                NewList.add(0, ListItem.toString());
                // Set the next value
                prevValue = value;
                // If it ever hits this check it means that the item occurs twice. It means that the value is equal.
            } else {
                // Shout warning just in case.
                System.out.println("Item occurred twice Check if it problem. " + InconsistencyExplanationLine.toString());
                NewList.add(ListItem.toString());
            }

        }

        for (Object ListItem: ListClasses ) {
            // generate the value where the object is found in the line.
            value = InconsistencyExplanationLine.toString().indexOf(ListItem.toString());
            // If not found throw error.
            if (value < 0) {
                throw new StackOverflowError("Nothing found in Inconsistency Explanation, This is not possible. check" +
                        " input. " + ListItem.toString() + "  " + InconsistencyExplanationLine.toString());
            }
            // Check if the value is larger or smaller than the previous value. This is needed to know as to which side the item needs to be added.

            // If the value is larger than the previous value it needs to be appended to back.
            if (value > prevValue){
                // add to newList
                NewList.add(ListItem.toString());
                // Set the next value
                prevValue = value;
            } else if (value < prevValue) {
                // If the value is smaller than the prevValue it has to be added to the front.
                NewList.add(0, ListItem.toString());
                // Set the next value
                prevValue = value;
                // If it ever hits this check it means that the item occurs twice. It means that the value is equal.
            } else {
                // Shout warning just in case.
                System.out.println("Item occurred twice Check if it problem. " + InconsistencyExplanationLine.toString());
                NewList.add(ListItem.toString());
            }

        }
        return NewList;
    }


    private static String AxiomConverter(OWLAxiom InconsistencyExplanationLine, Map<Object, String>  variableMap, int[] iterator){
        // Get the list of variables in the correct order.
        List SetOfVariables = StreamParser(InconsistencyExplanationLine);
        // Gets the axiom type of the line.
        AxiomType RelationType = InconsistencyExplanationLine.getAxiomType();
        // For both elements of the class/instance list.
        for (Object variable: SetOfVariables){
            // Check if the variable map contains the key for the variable(class/Instance)
            if (!variableMap.containsKey(variable)){
                // If there is no key yet made, the key is added to map.
                // Now we check if the variable is an instance or not. By checking if it hits an certain string.
                // TODO: low priority. Change the IndividualCheck82910283 to a less breakable check.
                if ( variable.toString().contains("IndividualCheck82910283")) {
                    // add the variable and new label to the map and increase the value with one
                    variableMap.put(variable, individualLabels[iterator[1]]);  // Iterator on Individual
                    iterator[1] ++;

                } else {
                    // add the variable and new label to the map and increase the value with one.
                    variableMap.put(variable, classLabels[iterator[0]]); // Iterator on Class
                    iterator[0] ++;
                }

            }
        }

        // return the string line with a generalised variable, Takes RelationType + first element + second element.
        if (SetOfVariables.size() == 2){
            return RelationType.toString() +" "+variableMap.get(SetOfVariables.get(0))+" "+variableMap.get(SetOfVariables.get(1));
        } else {
            return RelationType.toString()+" "+variableMap.get(SetOfVariables.get(0))+" "+variableMap.get(SetOfVariables.get(1))+" "+variableMap.get(SetOfVariables.get(2));
        }
    }

    private static List<String>  AxiomConverterMultiline(OWLAxiom InconsistencyExplanationLine, Map<Object, String>  variableMap, int[] iterator) {

        // Get the list of variables in the correct order.
        List SetOfVariables = StreamParser(InconsistencyExplanationLine);


        // Gets the axiom type of the line.
        AxiomType RelationType = InconsistencyExplanationLine.getAxiomType();
        // For both elements of the class/instance list.
        for (Object variable : SetOfVariables) {
            // Check if the variable map contains the key for the variable(class/Instance)
            if (!variableMap.containsKey(variable)) {
                // If there is no key yet made, the key is added to map.
                // Now we check if the variable is an instance or not. By checking if it hits an certain string.
                // TODO: low priority. Change the IndividualCheck82910283 to a less breakable check.
                if (variable.toString().contains("IndividualCheck82910283")) {
                    // add the variable and new label to the map and increase the value with one
                    variableMap.put(variable, individualLabels[iterator[1]]);  // Iterator on Individual
                    iterator[1]++;

                } else {
                    // add the variable and new label to the map and increase the value with one.
                    variableMap.put(variable, classLabels[iterator[0]]); // Iterator on Class
                    iterator[0]++;
                }

            }
        }
        int i = 0;
        int j = 0;
        List<String> StringLines = new ArrayList<>();
        if (RelationType.toString().equals("EquivalentClasses")){
            while ((j < SetOfVariables.size())) {
                if (i < j && variableMap.get(SetOfVariables.get(j)).contains("a") ) {
                    StringLines.add(RelationType.toString() + " " + variableMap.get(SetOfVariables.get(0)) + " " + variableMap.get(SetOfVariables.get(j)));
                }
                j++;
            }
        } else{
            while (i < SetOfVariables.size()){
                while ((j < SetOfVariables.size())) {
                    if (i < j) {
                        StringLines.add(RelationType.toString() + " " + variableMap.get(SetOfVariables.get(i)) + " " + variableMap.get(SetOfVariables.get(j)));
                    }
                    j++;
                }
                i++;
                j = 0;
            }
        }




        // return the string line with a generalised variable, Takes RelationType + first element + second element.
        return StringLines;
    }


    private static void InconsistencyStandardizer(Set<OWLAxiom> InconsistencyExplanation, FileOutputStream fileWriter)
    {
        // Instantiate the variable map storing all the variables/labels and the combined Cleaned labels
        // Due being declared in this scope the changes made to the HashMap are also stored in this scoped, and are
        // inherited in deeper layered functions(AxiomConverter and StreamParser).
        Map<Object, String> variableMap = new HashMap<>();
        //Stores the lines of the explanations in a list.
        List<String> ExplanationStringList = new ArrayList<>();

        int[] iterator = new int[2]; // The first element is the Class iterator, the second element is the Individual Iterator.
        // Loop through the Inconsistency line per line to replace the Classes and the instances with the correct values.
        for (OWLAxiom InconsistencyExplanationLine : InconsistencyExplanation){
            // Instantiate the StringLine in correct scope.
            String StringLine = "";
            List<String> StringLines = new ArrayList<>();

            // Get the axiom type.
            AxiomType RelationType = InconsistencyExplanationLine.getAxiomType();

            boolean MoreInformation = (StreamParser(InconsistencyExplanationLine).size() > 2);

            // For each relationTYPE a function is called. At the moment only these 4 RelationTypes are used.
            // TODO Add in more relation types.

            // class assertion: <a1> <rdf:type> <C1>
            if (RelationType == AxiomType.CLASS_ASSERTION){
                // Gets the new clean relation graph with generalised names.
                if(MoreInformation){
                    StringLines = AxiomConverterMultiline(InconsistencyExplanationLine, variableMap, iterator);
                } else {
                    StringLine = AxiomConverter(InconsistencyExplanationLine, variableMap, iterator);
                }

            // Disjoint class: <C1> <owl:disjointWith> <C2>
            } else if (RelationType == AxiomType.DISJOINT_CLASSES){
                // Gets the new clean relation graph with generalised names.
                if(MoreInformation){
                    StringLines = AxiomConverterMultiline(InconsistencyExplanationLine, variableMap, iterator);
                } else {
                    StringLine = AxiomConverter(InconsistencyExplanationLine, variableMap, iterator);
                }

            // Subclass of: <C1> <rdf:subclassOf> <C2>
            } else if (RelationType == AxiomType.SUBCLASS_OF){
                // Gets the new clean relation graph with generalised names.
                if(MoreInformation){
                    StringLines = AxiomConverterMultiline(InconsistencyExplanationLine, variableMap, iterator);
                } else {
                    StringLine = AxiomConverter(InconsistencyExplanationLine, variableMap, iterator);
                }

            // Equivalent Class: <C1> <owl:equivalentWith> <C2>.
                // Or Class: <C1> <owl:equivalentWith> <a0>, <a1>.
            }  else if (RelationType == AxiomType.EQUIVALENT_CLASSES){
                // Gets the new clean relation graph with generalised names.
                if(MoreInformation){
                    StringLines = AxiomConverterMultiline(InconsistencyExplanationLine, variableMap, iterator);
                } else {
                    StringLine = AxiomConverter(InconsistencyExplanationLine, variableMap, iterator);
                }

            // Equivalent Class: <a1> <owl:DifferentIndividuals> <a2>
            }else if (RelationType == AxiomType.DIFFERENT_INDIVIDUALS){
                // Gets the new clean relation graph with generalised names.
                if(MoreInformation){
                    StringLines = AxiomConverterMultiline(InconsistencyExplanationLine, variableMap, iterator);
                } else {
                    StringLine = AxiomConverter(InconsistencyExplanationLine, variableMap, iterator);
                }

                // If any other classes are hit Class cast exception is thrown. Throwing the relationType to add.
            } else if (RelationType == AxiomType.OBJECT_PROPERTY_ASSERTION){
                // Gets the new clean relation graph with generalised names.
                MoreInformation = (StreamParser(InconsistencyExplanationLine).size() > 3);
                if(MoreInformation){
                    StringLines = AxiomConverterMultiline(InconsistencyExplanationLine, variableMap, iterator);
                } else {
                    StringLine = AxiomConverter(InconsistencyExplanationLine, variableMap, iterator);
                }

                // If any other classes are hit Class cast exception is thrown. Throwing the relationType to add.
            } else if (RelationType == AxiomType.DATA_PROPERTY_ASSERTION){
                // Gets the new clean relation graph with generalised names.
                MoreInformation = (StreamParser(InconsistencyExplanationLine).size() > 3);
                if(MoreInformation){
                    StringLines = AxiomConverterMultiline(InconsistencyExplanationLine, variableMap, iterator);
                } else {
                    StringLine = AxiomConverter(InconsistencyExplanationLine, variableMap, iterator);
                }

                // If any other classes are hit Class cast exception is thrown. Throwing the relationType to add.
            }else if (RelationType == AxiomType.OBJECT_PROPERTY_DOMAIN){
                // Gets the new clean relation graph with generalised names.
                if(MoreInformation){
                    StringLines = AxiomConverterMultiline(InconsistencyExplanationLine, variableMap, iterator);
                } else {
                    StringLine = AxiomConverter(InconsistencyExplanationLine, variableMap, iterator);
                }

                // If any other classes are hit Class cast exception is thrown. Throwing the relationType to add.
            }else if (RelationType == AxiomType.DATA_PROPERTY_DOMAIN){
                // Gets the new clean relation graph with generalised names.
                if(MoreInformation){
                    StringLines = AxiomConverterMultiline(InconsistencyExplanationLine, variableMap, iterator);
                } else {
                    StringLine = AxiomConverter(InconsistencyExplanationLine, variableMap, iterator);
                }

                // If any other classes are hit Class cast exception is thrown. Throwing the relationType to add.
            } else if (RelationType == AxiomType.OBJECT_PROPERTY_RANGE){
                // Gets the new clean relation graph with generalised names.
                if(MoreInformation){
                    StringLines = AxiomConverterMultiline(InconsistencyExplanationLine, variableMap, iterator);
                } else {
                    StringLine = AxiomConverter(InconsistencyExplanationLine, variableMap, iterator);
                }

                // If any other classes are hit Class cast exception is thrown. Throwing the relationType to add.
            }else if (RelationType == AxiomType.DATA_PROPERTY_RANGE){
                // Gets the new clean relation graph with generalised names.
                if(MoreInformation){
                    StringLines = AxiomConverterMultiline(InconsistencyExplanationLine, variableMap, iterator);
                } else {
                    StringLine = AxiomConverter(InconsistencyExplanationLine, variableMap, iterator);
                }

                // If any other classes are hit Class cast exception is thrown. Throwing the relationType to add.
            }else {
                System.out.println(RelationType.toString());
            }

            // Adds the new cleaned line to the ExplanationList
            if(MoreInformation){
                ExplanationStringList.addAll(StringLines);
            } else{
                ExplanationStringList.add(StringLine);
            }

        }
        // SORT the ExplanationStringList to make sure the list handy to have it sorted.
        Collections.sort(ExplanationStringList);

        // TRY to check the graph and write if needed to a file.

        try {

            // Takes the Generalised Explanation and makes a generalised subgraph.
            // Adds to the list if the subgraph is not yet recognized.

            GeneralisedSubGraph GeneralGraph = new GeneralisedSubGraph(ExplanationStringList);
            // Checks if accepted to generalised subgraphs. If the subgraph is not found in the list
            // the subgraph is added to the list.


            // TODO: THIS IS USED TO REMOVE TAILS CAN BE DELETED.
//            if (GeneralGraph.GetVerticesDegree().contains(1)){
//                GeneralGraph.SinglesRemoval();
//                GeneralGraph.RebuildSPARQL();
//            }
//
//          GeneralGraph.generalizeFurther();


            boolean AcceptedTo = true;
            int counter = 0;
            // Loop through all the subgraphs.
            for (GeneralisedSubGraph AcceptedGraphs : GeneralGraphs){
                // Check if the subgraph is equal to the compared graph.
                if (AcceptedTo ){
                    if( IsoChecker.CompareGraph(AcceptedGraphs, GeneralGraph)){
                        TempStorageGenGraph[counter] ++;


                        AcceptedTo = false;
                    }
                }
                counter ++;
            }

            // If no subgraph can be found it is added to the list.
            if (AcceptedTo){
                GeneralGraphs.add(GeneralGraph);
                try{
                    TempStorageGenGraph[GeneralGraphNumber] = 1;

                } catch (Exception e){

                    List<Integer> list11 =Arrays.stream(TempStorageGenGraph).boxed().collect(Collectors.toList());
                    int[] TempStorageGenGraph = new int[list11.size()];
                    Iterator<Integer> IntegerIterator = list11.iterator();
                    for (int i = 0; i < TempStorageGenGraph.length; i++)
                    {
                        TempStorageGenGraph[i] = IntegerIterator.next();
                    }

                }


                GeneralGraphNumber ++;

                System.out.println("Found A new General Graph, number " + GeneralGraphNumber);
                GeneralSubGraphFound = 0;

            }
            else{
                GeneralSubGraphFound ++;
            }

            if(verbose && AcceptedTo ) {
                fileWriter.write(strToBytes);
                fileWriter.write(("General graph number: "+ GeneralGraphNumber + "\n").getBytes());
                fileWriter.write((GeneralGraph.convertSPARQL()+"\n").getBytes());
            }

//            if(verbose && AcceptedTo ) { // IF verbose write to file.
//                // Write out all the complete inconsistencies for examples Paper.
//                for (OWLAxiom InconsistencyExplanationLine : InconsistencyExplanation){
//
//                    // Transfer the string to bytes and send to fileWriter.
//                    fileWriter.write((InconsistencyExplanationLine.toString()+"\n").getBytes());
//                }
//
//            }
//
//            if (verbose && AcceptedTo ) {
//                for (String StringLine : ExplanationStringList) { // Write to Output stream for Generalised output.
//                    // Generalised output written.
//                    fileWriter.write((StringLine + "\n") .getBytes());
//                }
//            }

            if(verbose && AcceptedTo ) { // IF verbose write to file.
                { // Write out all the complete inconsistencies for examples Paper.

                    for (Object InconsistencyExplanationLine : GeneralGraph.getExplanationStringList())
                        // Transfer the string to bytes and send to fileWriter.
                        fileWriter.write((InconsistencyExplanationLine.toString()+"\n").getBytes());
                }
                fileWriter.write("\n".getBytes());
            }
        } catch(Exception io) {
            io.printStackTrace();
        }
    }


    public static PipedInputStream PipeModel(Set<String> subModel) throws Exception{
        // Start an Input stream that uses a pipe to stream the input to an output stream, this gives the possibility
        // to convert a set of strings easy to an a OWL Ontology.
        PipedInputStream in = new PipedInputStream();
        // Start an PipedOutputStream with input the PipedInputStream.
        PipedOutputStream out = new PipedOutputStream(in);
        try {
            // Lambda Runnable task to convert the set of strings to an output stream.
            Runnable task = () -> {
                try {
                    for (String elem : subModel) {
                        // Write to outputStream as bytes.
                        if (elem.contains("http://www.w3.org/2002/07/owl#Axiom")) continue;
                        if (elem.contains("http://www.w3.org/2002/07/owl#imports")) continue;
                        if (elem.split(">")[0].contains("http://www.w3.org/2000/01/rdf-schema#domain")) continue;
                        if (elem.split(">")[2].contains("http://www.w3.org/2000/01/rdf-schema#domain")) continue;
                        if (elem.split(">")[0].contains("http://www.w3.org/2000/01/rdf-schema#range")) continue;
                        if (elem.split(">")[2].contains("http://www.w3.org/2000/01/rdf-schema#range")) continue;
                        if (elem.lastIndexOf('"') > -1) continue;
                        if (elem.lastIndexOf("^") > -1) {
                            if (elem.lastIndexOf("^") > elem.lastIndexOf("<")) {
                                elem = elem.substring(0, elem.lastIndexOf("^")) + '-' + elem.substring(elem.lastIndexOf("^") + 1);
                            }
                        }
                        out.write(elem.getBytes());
                    }
                    out.close();
                }
                //When Error happens catch it and print stacktrace.
                catch (IOException io) {
                    io.printStackTrace();
                    return;
                }
            };

            // Starts the new tread. to push the strings to the OWLOntology.
            new Thread(task).start();
        } catch (Exception e){
            return in;
        }

        // Create a new OWLOntology and return the filled OWL ontology with the in stream.
        return in;
    }

//    private static Set<Set<OWLAxiom>> WriteInconsistencyModel(Set<String> subModel)  {
//        // Retrieve OWL ontology with a PipeModel. The model pipes the set of Strings from the subModel to the OWL Ontology.
//        Set<Set<OWLAxiom>> exp = Collections.emptySet();
//
//        final OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
//        try{
//            model.read(PipeModel(subModel), "","N3");
//        } catch (Exception e){
//            return exp;
//        }
//
//        try{
//
//            model.prepare();
//        } catch (Exception e){
//            return exp;
//        }
//
//        // Get the KnowledgeBase object
//        PelletInfGraph pellet = ((PelletInfGraph) model.getGraph());
//        // create an inferencing model using Pellet reasoner
//        if( !pellet.isConsistent() ) {
//            // create an inferencing model using Pellet reasoner
//            Model explanation = pellet.explainInconsistency();
//            // print the explanation
//            explanation.write(System.out);
//            StmtIterator statements =  explanation.listStatements();
//            while (statements.hasNext()){
//                System.out.println(statements.next());
//
//            }
//
//        }
////
////        // perform initial consistency check
////        long s = System.currentTimeMillis();
////        boolean consistent = kb.isConsistent();
////        long etime = System.currentTimeMillis();
////        System.out.println("Consistent? " + consistent + " (" + (etime - s) + "ms) Expressivity: "+kb.getExpressivity());
//
////        OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
////        model.read(PipeModel(subModel), "","N3");
//
//        // Find the Set of the Explanations that show that the SubGraph is inconsistent.
//
////        OWLOntology ontology;
////        try{
////            ontology = OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(PipeModel(subModel));
////        } catch (Exception e){
////            return exp;
////        }
//        // Create an Explanation reasoner with the Pellet Explanation and the Openllet Reasoner modules.
//
////        try{
////            OpenlletReasoner reasoner = OpenlletReasonerFactory.getInstance().createReasoner(ontology );
////            TransactionAwareSingleExpGen singleExpGen = new GlassBoxExplanation(reasoner);
////            HSTExplanationGeneratorExtended _expGen = new HSTExplanationGeneratorExtended(singleExpGen);
////            // Also Setting TimeOut
////            long timeOut = maxValueExperiment*2 < 2000 ? 2000 : maxValueExperiment *4;
////            exp = _expGen.getExplanations(OWL.Thing, MaxExplanations, timeOut+System.currentTimeMillis());
////
////        } catch (Exception e){
////            System.out.println("FailedReasoning");
////            return exp;
////        }
//
//
//
//        return exp;
//
//    }
    private static Set<Set<OWLAxiom>> WriteInconsistencyModel(Set<String> subModel) throws OWLOntologyCreationException {
        // Retrieve OWL ontology with a PipeModel. The model pipes the set of Strings from the subModel to the OWL Ontology.
        Set<Set<OWLAxiom>> exp = Collections.emptySet();

        // Find the Set of the Explanations that show that the SubGraph is inconsistent.
        OWLOntologyManager owlManager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = owlManager.createOntology();
        StringBuilder SPARQLStringB = new StringBuilder();
        for (String elem : subModel) {
            if (elem.contains("http://www.w3.org/2002/07/owl#Axiom")) continue;
            if (elem.contains("http://www.w3.org/2002/07/owl#imports")) continue;
            if (elem.lastIndexOf("^") > -1) continue;
            if (elem.lastIndexOf('"') > -1) continue;
            if (elem.lastIndexOf("^") > -1) {
                if (elem.lastIndexOf("^") > elem.lastIndexOf("<")) {
                    elem = elem.substring(0, elem.lastIndexOf("^")) + '-' + elem.substring(elem.lastIndexOf("^") + 1);
                }
            }

            SPARQLStringB.append(elem);
        }

        try{
            OWLOntologyLoaderConfiguration config = new OWLOntologyLoaderConfiguration();
            config = config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
            owlManager.setOntologyLoaderConfiguration(config);

            StringDocumentSource ontologyStrning = new StringDocumentSource(SPARQLStringB.toString());
            ontology = owlManager.loadOntologyFromOntologyDocument(ontologyStrning);
        } catch (Exception IllegalStateException){
            System.out.println("IllegalStateException");
            return exp;

        }

        try{
            // Create an Explanation reasoner with the Pellet Explanation and the Openllet Reasoner modules.
            OpenlletReasoner reasoner = OpenlletReasonerFactory.getInstance().createReasoner(ontology );
            TransactionAwareSingleExpGen singleExpGen = new GlassBoxExplanation(reasoner);
            HSTExplanationGeneratorExtended _expGen = new HSTExplanationGeneratorExtended(singleExpGen);
            // Also Setting TimeOut
            long timeOut = maxValueExperiment*2 < 2000 ? 2000 : maxValueExperiment *4;
            exp = _expGen.getExplanations(OWL.Thing, MaxExplanations, timeOut+System.currentTimeMillis());
        } catch (Exception e){
            System.out.println("FailedReasoning");
            return exp;
        }
        owlManager.removeOntology(ontology);


        return exp;

    }

    // NOT USED IN THE ALGORITHM!!
    @SuppressWarnings("SameParameterValue")
    public static Set<String> WriteInconsistencySubGraph(HDT hdt, String tripleItem, int maxValue) throws Exception{
        // Calls the GraphExtract which is Extended by Thomas de Groot to use the HDT instead of the JENA graph.
        // The TripleBoundary is set to stopNowhere Such that the model keeps going until my own stop criteria.
        GraphExtractExtended GraphExtract = new GraphExtractExtended(TripleBoundary.stopNowhere);

        // The subgraph is a set of strings. That stores up to 5000 triples.
        Set<String> subGraph = null;
        try{
            // Try to extract a the subgraph from the HDT.
//            subGraph = GraphExtract.extractExtend(tripleItem, hdt, maxValue);
            subGraph = GraphExtract.extractExtendBothClean(tripleItem , hdt, maxValue);



        } catch (StackOverflowError e){
            // Can print the error if overflow happens.
            e.printStackTrace();
        }

        return subGraph;
    }

    private static void SortGeneralList(){
        int counter = 0;
        for (GeneralisedSubGraph AcceptedGraph : GeneralGraphs){


            int foundAlready = SortingListMap.getOrDefault(AcceptedGraph, 0);
            if(foundAlready == 0){
                SortingListMap.put(AcceptedGraph, 0);
            }
            foundAlready += TempStorageGenGraph[counter];
            SortingListMap.replace(AcceptedGraph, foundAlready);
            counter ++;
        }


        SortingListMap = SortingListMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,LinkedHashMap::new));


        // Setting the new Array. Max value plus 50. TODO: get it better fixed.
        TempStorageGenGraph = new int[GeneralGraphNumber+50];

    }


    private static Set<Set<OWLAxiom>>  WriteInconsistencyGraph(HDT hdt) throws Exception{
        // Calls the GraphExtract which is Extended by Thomas de Groot to use the HDT instead of the JENA graph.
        // The TripleBoundary is set to stopNowhere Such that the model keeps going until my own stop criteria.
        GraphExtractExtended GraphExtract = new GraphExtractExtended(TripleBoundary.stopNowhere);

        Set<String> Graph = null;
        try{
            Graph = GraphExtract.extractAll(hdt);
        } catch (StackOverflowError e){
            // Can print the error if overflow happens.
            e.printStackTrace();
        }

        // Go to next step. Find (if any) Inconsistencies.
        return WriteInconsistencyModel(Graph);

    }



    private static Set<Set<OWLAxiom>>  WriteInconsistencySubGraph(HDT hdt, String tripleItem) throws Exception{
        // Calls the GraphExtract which is Extended by Thomas de Groot to use the HDT instead of the JENA graph.
        // The TripleBoundary is set to stopNowhere Such that the model keeps going until my own stop criteria.
        long ProcessTimeCreateSubgraphStart = System.currentTimeMillis();
        GraphExtractExtended GraphExtract = new GraphExtractExtended(TripleBoundary.stopNowhere);

        // The subgraph is a set of strings. That stores up to 5000 triples.
        Set<String> subGraph = null;
        try{
            // Retrieve subgraph from HDT single way 5000 triples takes as long as 250 both ways.
            //subGraph = GraphExtract.extractExtend(tripleItem, hdt, maxValueExperiment);
            //TODO: SPEED UP BOTH WAYS
            subGraph = GraphExtract.extractExtendBothClean(tripleItem , hdt, maxValueExperiment);
        } catch (StackOverflowError e){
            // Can print the error if overflow happens.
            e.printStackTrace();
        }
        ProcessTimeCreateSubgraph += System.currentTimeMillis() - ProcessTimeCreateSubgraphStart ;
        ProcessTimeFindContradictionsStart = System.currentTimeMillis();
        // Go to next step. Find (if any) Inconsistencies.
        return WriteInconsistencyModel(subGraph);

    }


    private static void LocateInconsistencies(HDT hdt, FileOutputStream fileWriter) throws Exception {
        // Locates the inconsistencies by looping through the graph over a large selection of triples.
        // AbsoluteName = "/home/thomasdegroot/Documents/kbgenerator/javaProject/resources/";
        String AbsoluteName = "D:/Users/Thomas/Documents/thesis/kbgenerator/javaProject/resources/";
        // String AbsoluteName = "/home/thomasdegroot/local/kbgenerator/javaProject/resources/";
        FileOutputStream fileWriter2 = new FileOutputStream(new File(AbsoluteName+"extraFiles/timeKeepingSmall"+NameFile+".txt"));

        fileWriter2.write(("Starting Split File \n").getBytes());

        // Get the Iterator tripleString to loop through.
        String subject = "";
        IteratorTripleString it = hdt.search("","","");
        long size = it.estimatedNumResults();
        System.out.println("HDT size : "+ size);
        long counterTriples = 0;
        List<String> triples;
        ProcessTimeCreateSubgraphTotal =0;
        ProcessTimeFindContradictionsTotal =0;
        ProcessTimeCreateAntiPatternsTotal = 0;
        subgraphs = 0;

        // Setting the AMOUNT OF THREADS: TODO: DO THE CONCURRENCY
        int numberThreads = 1;
        ExecutorService executor = Executors.newFixedThreadPool(numberThreads);
        // Skipping part of the hdt search as this has already been parsed: TODO: SKIP A SET
        //long valueLoop = 400000000;
        long valueLoop = 0;

        System.out.println("Skipping part of the loop to: " + valueLoop);
        while(it.hasNext() && counterTriples < valueLoop){
            counterTriples ++;
            it.next();
        }
        // While there is a triple the loop continues.
        System.out.println("Start the loop");
        long startTime = System.currentTimeMillis();
        long Triple10000 = System.currentTimeMillis();

        fileWriter2.write(("StartTime: "+ 0 + "\n").getBytes());
        int counterI = 0;
        while (counterI < 1) {
            System.out.println("Loop number: "+counterI);
            GeneralGraphNumber = 0;
            GeneralSubGraphFound = 0;
            GeneralGraphs = new ArrayList<>();     // Generalised SubGraph Storage
            SortingListMap = new HashMap<>(); // Generalised SubGraph Storage
            // Setting the new Array: 500. TODO: get it better fixed.
            TempStorageGenGraph = new int[500];
            while (it.hasNext() && (InconsistenciesHit < TotalInconsistenciesBeforeBreak || UnBreakable)) {

                triples = new ArrayList<>();
                // As it would not be scalable to use all the triples as starting point, as well as that the expectation is
                // that triples are connected to each other, not every triple needs to be evaluated.
                // A selection of triples is chosen at random.
                // Can be changed later to a selection of triples that meet a certain criteria.

                while (it.hasNext() && triples.size() < numberThreads) {
                    TripleString item = it.next();
                    counterTriples++;
                    if (counterTriples % 10000 == 0) {
                        long estimatedTime = System.currentTimeMillis() - startTime;
                        long Triple10000Time = System.currentTimeMillis() - Triple10000;
                        System.out.println("Amount of triples: " + counterTriples + " with max of: " + size + " Time passed: " + estimatedTime);
                        System.out.println("Time total: " + Triple10000Time);
                        System.out.println("Time passed create Subgraphs: " + ProcessTimeCreateSubgraphTotal);
                        System.out.println("Time passed Find Contradictions: " + ProcessTimeFindContradictionsTotal);
                        System.out.println("Time passed Create AntiPatterns: " + ProcessTimeCreateAntiPatternsTotal);
                        Triple10000 = System.currentTimeMillis();
                    }
                    // at the moment every 1 out of 2000 triples is taken.
                    // If the loop is not triggered the next element from the tripleString is taken.
                    if (subject.equals(item.getSubject().toString()) || counterTriples % (maxValueExperiment/5) != 0) {
                        continue;
                    }
                    subject = item.getSubject().toString();
                    triples.add(subject);


                }

                ProcessTimeCreateSubgraphTotal +=ProcessTimeCreateSubgraph;
                ProcessTimeFindContradictionsTotal +=ProcessTimeFindContradictions;
                ProcessTimeCreateAntiPatternsTotal +=ProcessTimeCreateAntiPatterns;
                ProcessTimeCreateSubgraph = 0;
                ProcessTimeFindContradictions = 0;
                ProcessTimeCreateAntiPatterns = 0;

                // both the subject and the object are taken to build the subgraph. With the expectation that the subject
                // graph encompasses the object graph. With the exception when the subject graph gets to large and misses
                // some of the depth the object graph does take into account.


                // Find all the inconsistencies in the second subgraph(Subject)

                // Setting the AMOUNT OF THREADS: TODO: DO THE CONCURRENCY
                Set<Set<OWLAxiom>> exp = new HashSet<>();

                for (String subjectString : triples) {
                    subgraphs += 1;
                    exp.addAll(WriteInconsistencySubGraph(hdt, subjectString));
                }

                ProcessTimeFindContradictions += (System.currentTimeMillis() - ProcessTimeFindContradictionsStart);
                // Process all the Inconsistencies.
                // Loop through the set of explanations and standardize the Inconsistencies.
                long ProcessTimeCreateAntiPatternsStart = System.currentTimeMillis();
                for (Set<OWLAxiom> InconsistencyExplanation : exp) {
                    InconsistenciesHit++;
                    // Loop through the set of explanations and standardize the Inconsistencies.
                    // Standardize the inconsistency and write to file.
                    InconsistencyStandardizer(InconsistencyExplanation, fileWriter);

                }
                ProcessTimeCreateAntiPatterns += (System.currentTimeMillis()- ProcessTimeCreateAntiPatternsStart);
                if (InconsistenciesHit % 5000 <= 10) {
                    System.out.println("Inconsistencies Hit: " + InconsistenciesHit);
                    System.out.println("Amount of triples: " + counterTriples + " with max of: " + size);

                    SortGeneralList();
                }

                if (counterTriples > 50000000) {
                    UnBreakable = false;
                }

            }
            counterI ++;
        }
        timePassed = System.currentTimeMillis() - startTime;
        fileWriter2.write((" TimeEnd: "+ timePassed + "\n").getBytes());
    }

    private static void LocateInconsistenciesAll(HDT hdt, FileOutputStream fileWriter) throws Exception {
        // Locates the inconsistencies by looping through the graph over a large selection of triples.

        // Setting the AMOUNT OF THREADS: TODO: DO THE CONCURRENCY
        // Set output Writer
        // String AbsoluteName = "/home/thomasdegroot/Documents/kbgenerator/javaProject/resources/";
        String AbsoluteName = "D:/Users/Thomas/Documents/thesis/kbgenerator/javaProject/resources/";
        //String AbsoluteName = "/home/thomasdegroot/local/kbgenerator/javaProject/resources/";
        FileOutputStream fileWriter2 = new FileOutputStream(new File(AbsoluteName+"extraFiles/timeKeeping"+NameFile+".txt"));

        fileWriter2.write(("Starting Complete File\n").getBytes());
        Set<Set<OWLAxiom>> exp = new HashSet<>();
        long startTime = System.currentTimeMillis();
        fileWriter2.write(("StartTime: "+ startTime + "\n").getBytes());
        MaxExplanations = 1000;
        exp.addAll(WriteInconsistencyGraph(hdt));

        // Process all the Inconsistencies.
        // Loop through the set of explanations and standardize the Inconsistencies.
        for(Set<OWLAxiom> InconsistencyExplanation: exp){
            InconsistenciesHit ++;
            // Loop through the set of explanations and standardize the Inconsistencies.
            // Standardize the inconsistency and write to file.
            InconsistencyStandardizer(InconsistencyExplanation, fileWriter);
        }
        long estimatedTime = System.currentTimeMillis() - startTime;
        fileWriter2.write((" TimeEnd: "+ estimatedTime + "\n").getBytes());
    }


    private static void ClassLabelGenerator() // Generated Labels for the classes and the instances that can be used to map them.
    // LABELS are stored in the program itself.
    {

        int FinalLength = 100;  // ARBITRARY VALUE OF 100 instance and class labels are chosen.
        classLabels = new String[FinalLength ]; // storage for class labels
        individualLabels = new String[FinalLength ]; // storage for instance labels

        int iteratorLabels = 0;

        // labels are generated to be used by the algorithm to generalise the subgraphs to be able to compare them.
        while (iteratorLabels < FinalLength ){
            classLabels[iteratorLabels] = "C" + iteratorLabels; // CLASS LABELS start with a 'C'
            individualLabels[iteratorLabels] = "a" + iteratorLabels; // INSTANCE LABELS start with a 'a'
            iteratorLabels ++;
        }

    }


    public static void main( String[] args ) throws Exception {
        /* main method that generates Inconsistencies when found.
         * @params {@code args[0] } location of the HDT  -- necessary
         * @params {@code args[1] } Location to store the output  -- necessary
         * @params {@code args[2] } inconsistency explanations per subgraph  -- not necessary, default 10
         * @params {@code args[3] } Verbose   -- not necessary, default false
         * @params {@code args[4] } Inconsistency break   -- not necessary, default false, needs integer.
         * @params {@code args[5] } TripleSplit   -- not necessary, default false, needs integer.
         * @returns void
         *
         */
        maxValueExperiment = Integer.parseInt(args[6]);

        // Check if HDT Exists
        boolean InputFileExists = new File(args[0]).isFile();

        // Check if output dir exists
        Path path = Paths.get(args[1]);
        String parentDirName = path.getParent().toString();
        boolean OutputDirExists = new File(parentDirName).exists();

        // Throws error when incorrect location.
        if ( (!OutputDirExists  )|| (!InputFileExists)){
            throw new IllegalArgumentException("Did not input the correct locations of the output or the input locations.");
        }
        args[1] = args[1]+"INCONSISTENCIES" +"-"+ args[0].split("/")[args[0].split("/").length-1].replace(".hdt","")+".ttl";

        NameFile = args[0].split("/")[args[0].split("/").length-1].replace(".hdt","");

        // Set output Writer
        FileOutputStream fileWriter = new FileOutputStream(new File(args[1]));

        // If the third argument is empty the amount of inconsistency explanations per subgraph is set to 10 else use
        // the amount given by the user.

        if (args.length > 2){
            try{
                MaxExplanations = Integer.parseInt(args[2]);

            } catch (Exception e){
                System.out.println("Could not parse the third argument using 10 explanations");
                MaxExplanations = 10;
            }
        } else {
            MaxExplanations = 10;
        }
        // If the fourth argument is empty the amount of
        verbose = (args.length > 3) && args[3].equals("true");

        // Sets the break for the Inconsistencies for the graph. This checks for the amount and sets it accordingly.
        // The default is no break.
        if (args.length > 4 && !args[4].isEmpty()){
            try{
                TotalInconsistenciesBeforeBreak = Integer.parseInt(args[4]);
                UnBreakable = false;
            } catch (Exception e){
                System.out.println("Could not parse the fifth argument Will continue without breaking the subgraphs");
                UnBreakable = true;
            }
        } else {
            UnBreakable = true;
        }
        if (args.length > 5 && !args[5].isEmpty()){
            try{
                TripleGap = Integer.parseInt(args[5]);
            } catch (Exception e){
                System.out.println("Could not parse the sixth argument Will continue without inconsistencyGap");
            }
        } else {
            TripleGap = 100;
        }

        System.out.println("InputLocation : " + args[0]);
        System.out.println("OutputLocation : " + args[1]);
        System.out.println("MaxExplanations : " + MaxExplanations);
        System.out.println("verbose : " + verbose);
        System.out.println("TotalInconsistenciesBeforeBreak : " + TotalInconsistenciesBeforeBreak);
        System.out.println("UnBreakable : " + UnBreakable);
        System.out.println("TripleGap : " + TripleGap);
        // Generate labels for the classes and instances.
        ClassLabelGenerator();

        // Print to the user that the HDT is being loaded. Can take a while.
        System.out.println("Print Loading in HDT");

        // Load HDT file using the hdt-java library

        HDT hdt = HDTManager.mapIndexedHDT(args[0]);

        // Print to the user that the HDT is finished loading.
        System.out.println("Finished Loading HDT");

        // Print to the user that system started finding inconsistencies.
        System.out.println("Start locating the inconsistencies.");

        // Test Inconsistencies
        LocateInconsistencies(hdt, fileWriter);
        //LocateInconsistenciesAll(hdt, fileWriter);
        // Print to the user that the system finished finding inconsistencies.
        System.out.println("Finished locating inconsistencies");

        fileWriter.write(("Stopped").getBytes());

    }

}

