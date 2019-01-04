package com.thesis.kbgenerator;


import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author Thomas de Groot
 */


class GeneralisedSubGraph {

    // Make New Subgraph
    private OWLOntology owlOntology;        // Instantiate OWLOntology for the graph
    private OWLDataFactory dataFactory;     // Instantiate the Datafactory.
    private List<String> SubGraphStoredasStrings;

    private boolean consistency;            // Store the consistency of the subgraph


    // Constructor for the Generalised Sub graph.
    GeneralisedSubGraph(List<String> Subgraph)
        {
            // Storing the subgraph as String:
            SubGraphStoredasStrings = Subgraph;

            // OWL ontology manager creation.
            OWLOntologyManager man = OWLManager.createOWLOntologyManager();

            // try to build generalised subgraph.
            try {
                // Use the owlOntology and datafactory.
                owlOntology = man.createOntology();
                dataFactory = owlOntology.getOWLOntologyManager().getOWLDataFactory();

                // Loop through lines of the subgraph.
                for (String line : Subgraph){

                    // Split string into 3 parts. 0. Relation, 1. First class/type , 2. Second class/type
                    String[] OWLAXIOMString = line.split(" ");

                    // Different types of possibilities.
                    // Relation, class, instance.
                    if (OWLAXIOMString[2].contains("a") && OWLAXIOMString[0].contains("ClassAssertion")){

                        // Instantiate Class.
                        OWLClass classC2 = AddDeclarationClass(OWLAXIOMString[1]);

                        // Instantiate Instance
                        OWLIndividual Individual = dataFactory.getOWLNamedIndividual(OWLAXIOMString[2]);

                        // Instantiate Class Assertion/
                        OWLClassAssertionAxiom Assertion = dataFactory.getOWLClassAssertionAxiom(classC2, Individual);

                        // Add triple
                        owlOntology.add(Assertion);

                    // Relation, instance, class.
                    } else if (OWLAXIOMString[1].contains("a") && OWLAXIOMString[0].contains("ClassAssertion")){

                        // Instantiate Class.
                        OWLClass classC2 = AddDeclarationClass(OWLAXIOMString[2]);

                        // Instantiate Instance
                        OWLIndividual Individual = dataFactory.getOWLNamedIndividual(OWLAXIOMString[1]);

                        OWLClassAssertionAxiom Assertion = dataFactory.getOWLClassAssertionAxiom(classC2, Individual);

                        // Add triple
                        owlOntology.add(Assertion);

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
                                owlOntology.add(Assertion);

                                break;
                            }
                            case "DisjointClasses": {
                                // Build Subclass Axiom
                                OWLDisjointClassesAxiom Assertion = dataFactory.getOWLDisjointClassesAxiom(classC1, classC2);
                                // Add Subclass Axiom
                                owlOntology.add(Assertion);

                                break;
                            }
                            case "EquivalentClasses": {
                                // Build Subclass Axiom
                                OWLEquivalentClassesAxiom Assertion = dataFactory.getOWLEquivalentClassesAxiom(classC1, classC2);
                                // Add Subclass Axiom
                                owlOntology.add(Assertion);

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
        owlOntology.add(DeclarationAxiom1);
        // Return the classC2
        return classC2;
    }

    // Checks the consistency of the small graph.
    private void CheckConsistency(){
        // Starts the reasoner
        OpenlletReasoner reasoner = OpenlletReasonerFactory.getInstance().createReasoner(owlOntology);
        // Sets the consistency of the graph.
        consistency = reasoner.isConsistent();
        // Disposes of the reasoner. To get garbage collection done.
        reasoner.dispose();
    }

    // Returns a stream of Axioms to the user.
    Stream<OWLAxiom> Axioms(){
        return owlOntology.axioms();
    }

    // gets the consistency.
    boolean getConsistency(){ return consistency; }

    // Returns the size of the graph
    int size(){
        return Axioms().toArray().length;
    }

    // If no Parameter is found the print uses this version.
    void print(){ print(""); }

    // TODO: Make pretty print of the graph.
    void print(String printlocation){
        // Print the general Graph uses the print location to write the file. Default is System.out
        if (printlocation.contains("System.out") || printlocation.isEmpty()){
            // Print to System out.
            System.out.println(Axioms().toArray().toString());
        } else{
            // Print to location
            System.out.println(Axioms().toArray().toString());
        }



    }


    // Test Subgraph with other subgraph

    boolean CompareGraph(GeneralisedSubGraph otherGraph)
        {
            if (otherGraph.size() != size() ){
                return false;
            } else{
                return true;
            }
        }




}
