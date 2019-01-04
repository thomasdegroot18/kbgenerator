package com.thesis.kbgenerator;


import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 *
 * @author Thomas de Groot
 */


class GeneralisedSubGraph {

    // Make New Subgraph
    private OWLOntology owlOntologyGraph;        // Instantiate OWLOntology for the graph, The bread and butter of this class
    private OWLDataFactory dataFactory;     // Instantiate the Datafactory.
    private List<String> SubGraphStoredasStrings;
    private Map<Object, String[]> HashMapStorage = new HashMap<>();

    private int instances;
    private int classes;

    private boolean consistency;            // Store the consistency of the subgraph


    // Constructor for the Generalised Sub graph.
    GeneralisedSubGraph(List<String> Subgraph) {
        // Storing the subgraph as String:
        SubGraphStoredasStrings = Subgraph;
        instances = 0;
        classes = 0;

        // OWL ontology manager creation.
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();

        // try to build generalised subgraph.
        try {
            // Use the owlOntology and datafactory.
            owlOntologyGraph = man.createOntology();
            dataFactory = owlOntologyGraph.getOWLOntologyManager().getOWLDataFactory();

            // Loop through lines of the subgraph.
            for (String line : Subgraph){

                // Split string into 3 parts. 0. Relation, 1. First class/type , 2. Second class/type
                String[] OWLAXIOMString = line.split(" ");

                // Different types of possibilities.
                // Relation, class, instance.
                if (OWLAXIOMString[2].contains("a") && OWLAXIOMString[0].contains("ClassAssertion")){

                    // Instantiate Class.
                    OWLClass classC2 = AddDeclarationClass(OWLAXIOMString[1]);

                    // Instantiate Instance and update instance count.
                    OWLIndividual Individual = dataFactory.getOWLNamedIndividual(OWLAXIOMString[2]);
                    instances ++;

                    // Instantiate Class Assertion/
                    OWLClassAssertionAxiom Assertion = dataFactory.getOWLClassAssertionAxiom(classC2, Individual);

                    // Add triple
                    owlOntologyGraph.add(Assertion);
                    // Add Triple To hashMap
                    // TODO Make It a String ARRAY
                    HashMapStorage.put(OWLAXIOMString[1],OWLAXIOMString[0] + " " + OWLAXIOMString[1]);

                // Relation, instance, class.
                } else if (OWLAXIOMString[1].contains("a") && OWLAXIOMString[0].contains("ClassAssertion")){

                    // Instantiate Class.
                    OWLClass classC2 = AddDeclarationClass(OWLAXIOMString[2]);

                    // Instantiate Instance and update instance count.
                    OWLIndividual Individual = dataFactory.getOWLNamedIndividual(OWLAXIOMString[1]);
                    instances ++;

                    OWLClassAssertionAxiom Assertion = dataFactory.getOWLClassAssertionAxiom(classC2, Individual);

                    // Add triple
                    owlOntologyGraph.add(Assertion);



                // If a mistake is made in the creation of the subgraph this thing checks if there are any problems.
                // This could have happened with incorrect class assertion or other things.
                } else if (OWLAXIOMString[1].contains("a") || OWLAXIOMString[2].contains("a")){
                    // Print out the possibly incorrect string.
                    System.out.println(OWLAXIOMString[0]);

                // All instances where two classes are found.
                } else {

                    // Add the two classes for the relationship cases.
                    OWLClass classC1 = AddDeclarationClass(OWLAXIOMString[1]); // Subclass
                    OWLClass classC2 = AddDeclarationClass(OWLAXIOMString[2]); // Superclass

                    // Instantiate Assertion in scope.

                    // TODO: Add more and different cases.

                    // Different Class relations OWLAXIOMString[0] SubClassOf, DisjointClasses, EquivalentClasses
                    switch (OWLAXIOMString[0]) {
                        case "SubClassOf": {

                            // Build Subclass Axiom
                            OWLSubClassOfAxiom Assertion = dataFactory.getOWLSubClassOfAxiom(classC1, classC2);
                            // Add Subclass Axiom
                            owlOntologyGraph.add(Assertion);

                            break;
                        }
                        case "DisjointClasses": {
                            // Build Subclass Axiom
                            OWLDisjointClassesAxiom Assertion = dataFactory.getOWLDisjointClassesAxiom(classC1, classC2);
                            // Add Subclass Axiom
                            owlOntologyGraph.add(Assertion);

                            break;
                        }
                        case "EquivalentClasses": {
                            // Build Subclass Axiom
                            OWLEquivalentClassesAxiom Assertion = dataFactory.getOWLEquivalentClassesAxiom(classC1, classC2);
                            // Add Subclass Axiom
                            owlOntologyGraph.add(Assertion);

                            break;
                        }
                        default:
                            throw new ClassCastException(OWLAXIOMString[0]);
                    }
                }
            }
        // If at any time an error pops up the catch will throw the stacktrace
        } catch (OWLOntologyCreationException e)
        {
            e.printStackTrace();
        }
        // Check if the consistency still holds
        CheckConsistency();
    }

    // Add the class declarations to the graph.
    private OWLClass AddDeclarationClass(String AxiomDeclaration){
        // Add class
        OWLClass classC2 = dataFactory.getOWLClass(AxiomDeclaration);
        // Add Declaration Axiom
        OWLDeclarationAxiom DeclarationAxiom1 = dataFactory.getOWLDeclarationAxiom(classC2);
        // Add the declaration Axiom to the OWL ontology.
        owlOntologyGraph.add(DeclarationAxiom1);
        // Update classes count.
        classes ++;
        // Return the classC2
        return classC2;
    }

    // Checks the consistency of the small graph.
    private void CheckConsistency(){
        // Starts the reasoner
        OpenlletReasoner reasoner = OpenlletReasonerFactory.getInstance().createReasoner(owlOntologyGraph);
        // Sets the consistency of the graph.
        consistency = reasoner.isConsistent();
        // Disposes of the reasoner. To get garbage collection done.
        reasoner.dispose();
    }

    // Returns a stream of Axioms to the user.
    Stream<OWLAxiom> getAxioms(){
        return owlOntologyGraph.axioms();
    }

    // gets the consistency.
    boolean getConsistency(){ return consistency; }

    // Returns the size of the graph
    int size(){
        return getAxioms().toArray().length;
    }

    // Returns the size of the graph
    int getCountClasses(){
        return classes;
    }

    int getCountInstances(){
        return instances;
    }

    Object[] GetInstances() { return owlOntologyGraph.individualsInSignature().toArray(); }

    Object[] GetClasses() { return owlOntologyGraph.classesInSignature().toArray(); }


    // TODO: Make a GetEdges function that returns all outgoing edges of the Vertex.
    Object[] GetEdges(String Vertex ) {


    }

    // If no Parameter is found the print uses this version.
    void print(){ print(""); }

    // TODO: Make pretty print of the graph.
    void print(String printlocation){
        // Print the general Graph uses the print location to write the file. Default is System.out
        if (printlocation.contains("System.out") || printlocation.isEmpty()){
            // Print to System out. By looping through the array and printing out the axioms.
            System.out.println("Graph to system out:");
            for (Object Axiom : getAxioms().toArray()){
                System.out.println(Axiom.toString());
            }
        } else{
            // Print to location.
            FileOutputStream fileWriter = null;
            try{
                // Start the fileWriter OutputStream
                fileWriter = new FileOutputStream(printlocation);

            } catch (Exception e){
                //No file yet created.
                try {
                    fileWriter = new FileOutputStream(new File(printlocation));
                } catch (Exception e2){
                    e2.printStackTrace();
                }

            }

            for (Object Axiom : getAxioms().toArray()){
                try{
                    if (fileWriter != null){
                        fileWriter.write(Axiom.toString().getBytes());
                    } else {
                        throw new ExceptionInInitializerError("Not correctly initialized the fileOutputStream.");
                    }
                } catch (Exception e ){
                    e.printStackTrace();
                }
            }

        }

    }

}
