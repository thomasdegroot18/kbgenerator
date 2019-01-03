package com.thesis.kbgenerator;




import org.apache.jena.base.Sys;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.rdf.model.ModelFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.util.List;
import java.util.Set;


class GeneralisedSubGraph {

    // Make New Subgraph
    private OWLOntology owlOntology;        // Instantiate OWLOntology for the graph
    private OWLDataFactory dataFactory;     // Instantiate the Datafactory.


    // Constructor for the Generalised Sub graph.
    GeneralisedSubGraph(List<String> Subgraph)
        {
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

                        // Instantiate Class Assertion/
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
                        OWLClass classC2 = AddDeclarationClass(OWLAXIOMString[1]); // Subclass
                        OWLClass classC1 = AddDeclarationClass(OWLAXIOMString[2]); // Superclass

                        // Instantiate Assertion in scope.

                        // TODO: Add more and different cases.

                        // Different Class relations OWLAXIOMString[0] SubClassOf, DisjointClasses
                        if (OWLAXIOMString[0].equals("SubClassOf")){

                            // Build Subclass Axiom
                            OWLSubClassOfAxiom Assertion = dataFactory.getOWLSubClassOfAxiom(classC2,  classC1);
                            // Add Subclass Axiom
                            owlOntology.add(Assertion);

                        } else if (OWLAXIOMString[0].equals("DisjointClasses")){
                            // Build Subclass Axiom
                            OWLDisjointClassesAxiom Assertion = dataFactory.getOWLDisjointClassesAxiom(classC2,  classC1);
                            // Add Subclass Axiom
                            owlOntology.add(Assertion);

                        } else {
                            throw new ClassCastException( OWLAXIOMString[0] );
                        }

                    }


                }
            } catch (OWLOntologyCreationException e)
            {
                e.printStackTrace();
            }



        }

    private OWLClass AddDeclarationClass(String AxiomDeclaration){
        OWLClass classC2 = dataFactory.getOWLClass(AxiomDeclaration);
        OWLDeclarationAxiom DeclarationAxiom1 = dataFactory.getOWLDeclarationAxiom(classC2);
        owlOntology.add(DeclarationAxiom1);
        return classC2;
    }

    private boolean CheckConsistency(){
        // Checks if the consistency still holds or not.
        boolean consistency = true;



        return consistency;

    }



    // Test Subgraph with other subgraph

    boolean CompareGraph(GeneralisedSubGraph otherGraph)
        {




            if(1 < 0.5){
                return false;
            }
            else {
                return true;
            }
        }




}
