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
    private OWLOntology owlOntology;
    private OWLDataFactory dataFactory;



    GeneralisedSubGraph(List<String> Subgraph)
        {

            OWLOntologyManager man = OWLManager.createOWLOntologyManager();

            try {
                owlOntology = man.createOntology();
                dataFactory = owlOntology.getOWLOntologyManager().getOWLDataFactory();

                for (String line : Subgraph){


                    String[] OWLAXIOMString = line.split(" ");
                    if (OWLAXIOMString[2].contains("a") && OWLAXIOMString[0].contains("ClassAssertion")){
                        OWLClass classC2 = AddDeclarationClass(OWLAXIOMString[1]);
                        OWLIndividual Individual = dataFactory.getOWLNamedIndividual(OWLAXIOMString[2]);
                        OWLClassAssertionAxiom Assertion = dataFactory.getOWLClassAssertionAxiom(classC2, Individual);
                        owlOntology.add(Assertion);
                    } else if (OWLAXIOMString[1].contains("a") && OWLAXIOMString[0].contains("ClassAssertion")){
                        OWLClass classC2 = AddDeclarationClass(OWLAXIOMString[2]);
                        OWLIndividual Individual = dataFactory.getOWLNamedIndividual(OWLAXIOMString[1]);
                        OWLClassAssertionAxiom Assertion = dataFactory.getOWLClassAssertionAxiom(classC2, Individual);
                        owlOntology.add(Assertion);
                    } else if (OWLAXIOMString[1].contains("a") || OWLAXIOMString[2].contains("a")){
                        System.out.println(OWLAXIOMString[0]);
                    } else {
                        OWLClass classC2 = AddDeclarationClass(OWLAXIOMString[1]);
                        OWLClass classC1 = AddDeclarationClass(OWLAXIOMString[2]);


                        if different OWLAXIOMString[0] cases
                            owlOntology.add(Assertion);


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





    // Test Subgraph with other subgraph

    boolean CompareGraph(GeneralisedSubGraph otherGraph)
        {






            if(1 < 0.5){
                return true;
            }
            else {
                return false;
            }
        }




}
