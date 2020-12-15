package com.thesis.kbInconsistencyLocator;


import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.FileOutputStream;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author Thomas de Groot
 */

@SuppressWarnings("unused")
public class GeneralisedSubGraph {

    // Make New Subgraph
    private OWLOntology owlOntologyGraph;        // Instantiate OWLOntology for the graph, The bread and butter of this class
    private OWLDataFactory dataFactory;     // Instantiate the DataFactory.
    private HashMap<String, ArrayList<String>> Vertices = new HashMap<>();
    private HashMap<String, String> Edges = new HashMap<>();
    private StringBuilder SPARQLStringB = new StringBuilder();
    private List<String>  ExplanationStringList;
    private boolean EquivalenceType = false;

    private boolean consistency;            // Store the consistency of the subgraph


    // Constructor for the Generalised Sub graph.
    public GeneralisedSubGraph(List<String> Subgraph) {
        this.ExplanationStringList = Subgraph;
        // OWL ontology manager creation.
        int disjoints = 0;
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();

        // try to build generalised subgraph.
        try {
            // Use the owlOntology and dataFactory.
            owlOntologyGraph = man.createOntology();
            dataFactory = owlOntologyGraph.getOWLOntologyManager().getOWLDataFactory();

            // Loop through lines of the subgraph.
            for (String line : Subgraph){

                // Split string into 3 parts. 0. Relation, 1. First class/type , 2. Second class/type
                String[] OWLAXIOMString = line.split(" ");

                Edges.put(OWLAXIOMString[1]+ OWLAXIOMString[2], OWLAXIOMString[0]);
                Edges.put(OWLAXIOMString[2]+ OWLAXIOMString[1], OWLAXIOMString[0]);

                // Different types of possibilities.
                // Relation, class, instance.
                if (OWLAXIOMString[2].contains("a") && OWLAXIOMString[0].contains("ClassAssertion")){
                    // Instantiate Class.
                    OWLClass classC2 = AddDeclarationClass(OWLAXIOMString[1]);

                    // Instantiate Instance and update instance count.
                    OWLIndividual Individual = dataFactory.getOWLNamedIndividual(OWLAXIOMString[2]);

                    // Instantiate Class Assertion/
                    OWLClassAssertionAxiom Assertion = dataFactory.getOWLClassAssertionAxiom(classC2, Individual);

                    // Add triple
                    owlOntologyGraph.add(Assertion);
                    // Add Triple To hashMap
                    addToList(OWLAXIOMString[1], OWLAXIOMString[2]);
                    addToList(OWLAXIOMString[2], OWLAXIOMString[1]);




                // Relation, instance, class.
                } else if (OWLAXIOMString[1].contains("a") && OWLAXIOMString[0].contains("ClassAssertion")){

                    // Instantiate Class.
                    OWLClass classC2 = AddDeclarationClass(OWLAXIOMString[2]);

                    // Instantiate Instance and update instance count.
                    OWLIndividual Individual = dataFactory.getOWLNamedIndividual(OWLAXIOMString[1]);

                    OWLClassAssertionAxiom Assertion = dataFactory.getOWLClassAssertionAxiom(classC2, Individual);

                    // Add triple
                    owlOntologyGraph.add(Assertion);
                    // Add Triple To hashMap
                    addToList(OWLAXIOMString[1], OWLAXIOMString[2]);
                    addToList(OWLAXIOMString[2], OWLAXIOMString[1]);


                // If a mistake is made in the creation of the subgraph this thing checks if there are any problems.
                // This could have happened with incorrect class assertion or other things.
                } else if (OWLAXIOMString[1].contains("a") && OWLAXIOMString[2].contains("a") && OWLAXIOMString[0].contains("DifferentIndividuals")) {
                    // Print out the possibly incorrect string.
                    // Instantiate Instance and update instance count.
                    OWLIndividual Individual1 = dataFactory.getOWLNamedIndividual(OWLAXIOMString[1]);
                    OWLIndividual Individual2 = dataFactory.getOWLNamedIndividual(OWLAXIOMString[2]);
                    OWLDifferentIndividualsAxiom Assertion = dataFactory.getOWLDifferentIndividualsAxiom(Individual1, Individual2);

                    // Add triple
                    owlOntologyGraph.add(Assertion);
                    // Add Triple To hashMap
                    addToList(OWLAXIOMString[1], OWLAXIOMString[2]);
                    addToList(OWLAXIOMString[2], OWLAXIOMString[1]);
                } else if (OWLAXIOMString[2].contains("a") && OWLAXIOMString[0].contains("EquivalentClasses")) {
                    // Add the two classes for the relationship cases.
                    OWLClass classC1 = AddDeclarationClass(OWLAXIOMString[1]); // Subclass
                    OWLClass classC2 = AddDeclarationClass(OWLAXIOMString[2]); // Superclass
                    // Build Subclass Axiom
                    OWLEquivalentClassesAxiom Assertion = dataFactory.getOWLEquivalentClassesAxiom(classC1, classC2);
                    // Add Subclass Axiom
                    owlOntologyGraph.add(Assertion);
                    // Add Triple To hashMap
                    addToList(OWLAXIOMString[1], OWLAXIOMString[2]);
                    addToList(OWLAXIOMString[2], OWLAXIOMString[1]);

                    EquivalenceType = true;

                }  else {
                        OWLIndividual Individual1 = dataFactory.getOWLNamedIndividual(OWLAXIOMString[1]);
                        OWLIndividual Individual2 = dataFactory.getOWLNamedIndividual(OWLAXIOMString[2]);
                        OWLObjectProperty property = dataFactory.getOWLObjectProperty(OWLAXIOMString[1]);
                        OWLDataProperty propertyD = dataFactory.getOWLDataProperty(OWLAXIOMString[1]);
                        OWLLiteral Literal = dataFactory.getOWLLiteral(OWLAXIOMString[2]);
                        OWLDatatype dataType = dataFactory.getOWLDatatype(OWLAXIOMString[2]);
                        // Add the two classes for the relationship cases.
                        OWLClass classC1 = AddDeclarationClass(OWLAXIOMString[1]); // Subclass
                        OWLClass classC2 = AddDeclarationClass(OWLAXIOMString[2]); // Superclass
                        OWLObjectProperty classP1 = dataFactory.getOWLObjectProperty(OWLAXIOMString[1]); // Subclass
                        OWLObjectProperty classP2 = dataFactory.getOWLObjectProperty(OWLAXIOMString[2]); // Superclass

                        // Instantiate Assertion in scope.

                        // TODO: Add more and different cases.

                        // Different Class relations OWLAXIOMString[0] SubClassOf, DisjointClasses, EquivalentClasses
                        switch (OWLAXIOMString[0]) {
                            case "SubClassOf": {

                                // Build Subclass Axiom
                                OWLSubClassOfAxiom Assertion = dataFactory.getOWLSubClassOfAxiom(classC1, classC2);
                                // Add Subclass Axiom
                                owlOntologyGraph.add(Assertion);
                                // Add Triple To hashMap
                                addToList(OWLAXIOMString[1], OWLAXIOMString[2]);
                                addToList(OWLAXIOMString[2], OWLAXIOMString[1]);

                                break;
                            }
                            case "DisjointClasses": {
                                // Build Subclass Axiom
                                OWLDisjointClassesAxiom Assertion = dataFactory.getOWLDisjointClassesAxiom(classC1, classC2);
                                // Add Subclass Axiom
                                owlOntologyGraph.add(Assertion);
                                // Add Triple To hashMap
                                addToList(OWLAXIOMString[1], OWLAXIOMString[2]);
                                addToList(OWLAXIOMString[2], OWLAXIOMString[1]);
                                // Disjoint Counter:
                                disjoints ++;
                                break;
                            }
                            case "DisjointObjectProperties": {
                                // Build Subclass Axiom
                                OWLDisjointObjectPropertiesAxiom Assertion = dataFactory.getOWLDisjointObjectPropertiesAxiom(classP1, classP2);
                                // Add Subclass Axiom
                                owlOntologyGraph.add(Assertion);
                                // Add Triple To hashMap
                                addToList(OWLAXIOMString[1], OWLAXIOMString[2]);
                                addToList(OWLAXIOMString[2], OWLAXIOMString[1]);
                                // Disjoint Counter:
                                disjoints ++;
                                break;
                            }
                            case "ObjectPropertyAssertion": {
                                // Build Subclass Axiom
                                OWLObjectPropertyAssertionAxiom Assertion;
                                if (OWLAXIOMString.length == 4){
                                    property = dataFactory.getOWLObjectProperty(OWLAXIOMString[3]);
                                    Assertion = dataFactory.getOWLObjectPropertyAssertionAxiom(property,Individual1, Individual2);
                                } else {
                                    property = dataFactory.getOWLObjectProperty(OWLAXIOMString[2]);
                                    Assertion = dataFactory.getOWLObjectPropertyAssertionAxiom(property,Individual1, Individual1);
                                }
                                // Add Subclass Axiom
                                owlOntologyGraph.add(Assertion);
                                // Add Triple To hashMap
                                addToList(OWLAXIOMString[1], OWLAXIOMString[2]);
                                addToList(OWLAXIOMString[2], OWLAXIOMString[1]);
                                // Disjoint Counter:

                                break;
                            }
                            case "ObjectPropertyDomain": {
                                // Build Subclass Axiom
                                OWLObjectPropertyDomainAxiom Assertion = dataFactory.getOWLObjectPropertyDomainAxiom(property, classC2);
                                // Add Subclass Axiom
                                owlOntologyGraph.add(Assertion);
                                // Add Triple To hashMap
                                addToList(OWLAXIOMString[1], OWLAXIOMString[2]);
                                addToList(OWLAXIOMString[2], OWLAXIOMString[1]);
                                // Disjoint Counter:
                                disjoints ++;
                                break;
                            }
                            case "ObjectPropertyRange": {
                                // Build Subclass Axiom

                                OWLObjectPropertyRangeAxiom Assertion = dataFactory.getOWLObjectPropertyRangeAxiom(property, classC2);
                                // Add Subclass Axiom
                                owlOntologyGraph.add(Assertion);
                                // Add Triple To hashMap
                                addToList(OWLAXIOMString[1], OWLAXIOMString[2]);
                                addToList(OWLAXIOMString[2], OWLAXIOMString[1]);
                                // Disjoint Counter:
                                disjoints ++;
                                break;
                            }
                            case "DataPropertyAssertion": {
                                // Build Subclass Axiom
                                propertyD = dataFactory.getOWLDataProperty(OWLAXIOMString[3]);
                                OWLDataPropertyAssertionAxiom Assertion = dataFactory.getOWLDataPropertyAssertionAxiom(propertyD,Individual1, Literal);
                                // Add Subclass Axiom
                                owlOntologyGraph.add(Assertion);
                                // Add Triple To hashMap
                                addToList(OWLAXIOMString[1], OWLAXIOMString[2]);
                                addToList(OWLAXIOMString[2], OWLAXIOMString[1]);
                                // Disjoint Counter:
                                disjoints ++;
                                break;
                            }
                            case "DataPropertyDomain": {
                                // Build Subclass Axiom
                                OWLDataPropertyDomainAxiom Assertion = dataFactory.getOWLDataPropertyDomainAxiom(propertyD, classC2);
                                // Add Subclass Axiom
                                owlOntologyGraph.add(Assertion);
                                // Add Triple To hashMap
                                addToList(OWLAXIOMString[1], OWLAXIOMString[2]);
                                addToList(OWLAXIOMString[2], OWLAXIOMString[1]);
                                // Disjoint Counter:
                                disjoints ++;
                                break;
                            }
                            case "DataPropertyRange": {
                                // Build Subclass Axiom
                                OWLDataPropertyRangeAxiom Assertion = dataFactory.getOWLDataPropertyRangeAxiom(propertyD, dataType);
                                // Add Subclass Axiom
                                owlOntologyGraph.add(Assertion);
                                // Add Triple To hashMap
                                addToList(OWLAXIOMString[1], OWLAXIOMString[2]);
                                addToList(OWLAXIOMString[2], OWLAXIOMString[1]);
                                // Disjoint Counter:
                                disjoints ++;
                                break;
                            }
                            case "EquivalentClasses": {
                                // Build Subclass Axiom
                                OWLEquivalentClassesAxiom Assertion = dataFactory.getOWLEquivalentClassesAxiom(classC1, classC2);
                                // Add Subclass Axiom
                                owlOntologyGraph.add(Assertion);
                                // Add Triple To hashMap
                                addToList(OWLAXIOMString[1], OWLAXIOMString[2]);
                                addToList(OWLAXIOMString[2], OWLAXIOMString[1]);
                                EquivalenceType = true;
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
        // TODO: Remove unneeded disjoints:
        // Take the hashMap with vertices generated by the Vertices degree Map.
        // Checked and worked
        if (disjoints > 1) {
            HashMap<String, Integer> VerticesDegree = GetVerticesDegreeMap();

            for (String elem : VerticesDegree.keySet()) {
                ArrayList<String[]> VerticesEdges = GetOutElements(elem);
                boolean allEdge = true;
                for (String[] elements: VerticesEdges){
                    if (!elements[1].equals("DisjointClasses")){
                        allEdge = false;
                    }
                }
                if (allEdge){
                    //TODO: Remove all traces of the disjoint
                    for (String[] elements: VerticesEdges){
                        OWLClass classC1 = AddDeclarationClass(elem); // Subclass
                        OWLClass classC2 = AddDeclarationClass(elements[0]); // Superclass
                        OWLDisjointClassesAxiom Assertion = dataFactory.getOWLDisjointClassesAxiom(classC1, classC2);
                        // Add Subclass Axiom
                        owlOntologyGraph.removeAxiom(Assertion);
                        // remove it from list.
                        RemoveEdge(elem, elements[0]);
                        RemoveEdge(elements[0], elem);
                        RemoveVertex(elem, elements[0]);
                    }
                }

            }
        }


        CheckConsistency();
        RebuildSPARQL();


    }

    // Adds the Vertices to a array list.
    private synchronized void addToList(String Ingoing, String Outgoing) {

        // Finds the array list if the list exists.
        ArrayList<String> itemsList = Vertices.get(Ingoing);

        // if list does not exist create it
        if(itemsList == null) {
            // makes a new list.
            itemsList = new ArrayList<>();

            // Adds the link to the new array list
            itemsList.add(Outgoing);
            //Adds the link to the complete map.
            Vertices.put(Ingoing, itemsList);
        } else {
            // add if item is not already in list
            if(!itemsList.contains(Outgoing)) itemsList.add(Outgoing);
        }
    }

    // Add the class declarations to the graph.
    private OWLClass AddDeclarationClass(String AxiomDeclaration){
        // Add class
        OWLClass classC2 = dataFactory.getOWLClass(AxiomDeclaration);
        // Add Declaration Axiom
        OWLDeclarationAxiom DeclarationAxiom1 = dataFactory.getOWLDeclarationAxiom(classC2);
        // Add the declaration Axiom to the OWL ontology.
        owlOntologyGraph.add(DeclarationAxiom1);
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
    private Stream<OWLAxiom> getAxioms(){
        return owlOntologyGraph.axioms();
    }

    public boolean getEquivalenceType() {
        return EquivalenceType;
    }

    // gets the consistency.
    boolean getConsistency(){ return consistency; }

    // Returns the size of the Axioms
    int AxiomSize(){
        return getAxioms().toArray().length;
    }

    // Returns the size of the graph
    int size(){

        return this.GetClasses().toArray().length + this.GetInstances().toArray().length;
    }

    // Gets the amount of classes
    int getCountClasses(){
        return this.GetClasses().toArray().length;
    }

    // Gets the amount of Instances
    int getCountInstances(){
        return this.GetInstances().toArray().length;
    }

    // Gets the instances as stream
    private Stream<OWLNamedIndividual> GetInstances() { return owlOntologyGraph.individualsInSignature(); }

    // Gets the classes as stream
    private Stream<OWLClass> GetClasses() { return owlOntologyGraph.classesInSignature(); }

    // Retrieves vertices as a Map to iterate over.
    private HashMap<String, Integer> GetVerticesDegreeMap() {
        // Build a new HashMap called Vertices Degree
        HashMap<String, Integer> VerticesDegree = new HashMap<>();
        // For each Vertex in the Set of vertices.
        for (String element: this.GetVerticesSet()){
            // Put the vertex in the map with the outgoing degrees of the element.
            VerticesDegree.put(element , this.GetOutElements(element).size());
        }
        // return the HashMap
        return VerticesDegree;
    }

    // To remove Links with only a single incoming/outgoing link we need to recursively call this function. As it removes
    // all the nodes with only one link.
    public void SinglesRemoval() {
        // Take the hashMap with vertices generated by the Vertices degree Map.
        HashMap<String, Integer> VerticesDegree = GetVerticesDegreeMap();

        // For each vertex in the keySet of the hashMap
        for (String elem: VerticesDegree.keySet() ){
            // Loop through integer other degrees to find if there is a link with only one incoming, outgoing link.
            int degrees = 0;

            // Loop through the keySet.
            for (String elemTemp: VerticesDegree.keySet() ){
                // If there is a link add one to the amount of degrees.
                if(!(GetEdges(elem, elemTemp) == null)){
                    degrees ++;
                }
            }
            // If the amount of links is lower than one. To remove links that also have 0 degrees. Just in case.
            if (degrees <= 1){
                // Get the first element of the list.
                String Class = Vertices.get(elem).get(0);

                // IF the link is of instance type remove so
                // TODO: Make own function to make it easier to add stuff to it.
                if (Class.contains("a") && elem.contains("a")) {
                    // Instantiate Instance and update instance count.
                    OWLIndividual Individual1 = dataFactory.getOWLNamedIndividual(elem);
                    OWLIndividual Individual2 = dataFactory.getOWLNamedIndividual(Class);

                    OWLDifferentIndividualsAxiom Assertion = dataFactory.getOWLDifferentIndividualsAxiom(Individual1, Individual2);
                    owlOntologyGraph.removeAxiom(Assertion);
                }
                else if (elem.contains("a")){
                    // Instantiate Instance and update instance count.
                    OWLIndividual Individual = dataFactory.getOWLNamedIndividual(elem);


                    OWLClass classC2 = dataFactory.getOWLClass(Class);
                    OWLClassAssertionAxiom Assertion = dataFactory.getOWLClassAssertionAxiom(classC2, Individual);
                    owlOntologyGraph.removeAxiom(Assertion);
                } else if(Class.contains("a")) {
                    // Instantiate Instance and update instance count.
                    OWLIndividual Individual = dataFactory.getOWLNamedIndividual(Class);


                    OWLClass classC2 = dataFactory.getOWLClass(elem);
                    OWLClassAssertionAxiom Assertion = dataFactory.getOWLClassAssertionAxiom(classC2, Individual);
                    owlOntologyGraph.removeAxiom(Assertion);
                } else {
                    // Instantiate Class 1
                    OWLClass classC1 = dataFactory.getOWLClass(elem);

                    // Find the matching Edge
                    String Edge = Edges.get(elem + Class);

                    // Retrieve the other class.
                    OWLClass classC2 = dataFactory.getOWLClass(Class);
                    switch (Edge) {

                        case "SubClassOf": {

                            // Build Subclass Axiom
                            OWLSubClassOfAxiom Assertion = dataFactory.getOWLSubClassOfAxiom(classC1, classC2);
                            // Add Subclass Axiom
                            owlOntologyGraph.removeAxiom(Assertion);
                            break;
                        }
                        case "DisjointClasses": {
                            // Build Subclass Axiom
                            OWLDisjointClassesAxiom Assertion = dataFactory.getOWLDisjointClassesAxiom(classC1, classC2);
                            // Add Subclass Axiom
                            owlOntologyGraph.removeAxiom(Assertion);
                            break;
                        }
                        case "EquivalentClasses": {
                            // Build Subclass Axiom
                            OWLEquivalentClassesAxiom Assertion = dataFactory.getOWLEquivalentClassesAxiom(classC1, classC2);
                            // Add Subclass Axiom
                            owlOntologyGraph.removeAxiom(Assertion);

                            break;
                        }
                        default:
                            throw new ClassCastException(Edge);

                    }
                    OWLDeclarationAxiom DeclarationAxiom1 = dataFactory.getOWLDeclarationAxiom(classC1);
                    // Add the declaration Axiom to the OWL ontology.
                    owlOntologyGraph.removeAxiom(DeclarationAxiom1);

                }
                // remove it from list.
                RemoveEdge(elem, Class);
                RemoveEdge(Class, elem);
                RemoveVertex(elem, Class);

                break;

            }
        }

        // Retrieve the again the vertices degree to measure if there is a tail again.
        HashMap<String, Integer> VerticesDegree2 = GetVerticesDegreeMap();

        // Set the recursive boolean to false.
        boolean RecursiveActionBool = false;

        // Loop through keySet again.
        for (String elem: VerticesDegree2.keySet() ){
            int degrees = 0;
            // Check if degrees is there.
            for (String elemTemp: VerticesDegree2.keySet() ){
                if(!(GetEdges(elem, elemTemp) == null)){
                    degrees ++;
                }
            }
            // If only one has more than one link we are going through it again.
            if (degrees <= 1){
                RecursiveActionBool = true;
            }
        }
        // Recursion!
        if (RecursiveActionBool){
            SinglesRemoval();
        }


    }

    // As the singlesRemoval is now needed to regenerate the SPARQL result again.
    public void RebuildSPARQL (){

        // Set up sparql builder.
        SPARQLStringB = new StringBuilder();

        // Loop through the axioms and start rebuilding.
        // TODO: Use this in stead of in the constructor.
        for (Object elem: owlOntologyGraph.axioms().toArray()){
            String[] line = elem.toString().split(("<"));
            if (line.length < 3){
                continue;
            }
            String elem1 = line[1].split(">")[0];
            String elem2 = line[2].split(">")[0];
            // Different Class relations OWLAXIOMString[0] SubClassOf, DisjointClasses, EquivalentClasses
            switch (line[0].substring(0,line[0].length()-1)) {
                case "ClassAssertion": {
                    if(elem1.contains("a")){
                        SPARQLStringB.append("?");
                        SPARQLStringB.append(elem1);
                        SPARQLStringB.append(" rdf:type ?");
                        SPARQLStringB.append(elem2);
                        SPARQLStringB.append(". ");
                    } else{
                        SPARQLStringB.append("?");
                        SPARQLStringB.append(elem2);
                        SPARQLStringB.append(" rdf:type ?");
                        SPARQLStringB.append(elem1);
                        SPARQLStringB.append(". ");
                    }
                    break;
                }

                case "SubClassOf": {

                    SPARQLStringB.append("?");
                    SPARQLStringB.append(elem1);
                    SPARQLStringB.append(" rdfs:subClassOf ?");
                    SPARQLStringB.append(elem2);
                    SPARQLStringB.append(". ");
                    break;
                }
                case "DataPropertyDomain":
                case "ObjectPropertyDomain": {
                    SPARQLStringB.append("?");
                    SPARQLStringB.append(elem2);
                    SPARQLStringB.append(" rdfs:domain ?");
                    SPARQLStringB.append(elem1);
                    SPARQLStringB.append(". ");
                    break;
                }
                case "DataPropertyAssertion":
                    String elem4 = line[2].split("\"")[1];
                    SPARQLStringB.append("?");
                    SPARQLStringB.append(elem1);
                    SPARQLStringB.append(" ?");
                    SPARQLStringB.append(elem2);
                    SPARQLStringB.append(" ?");
                    SPARQLStringB.append(elem4);
                    SPARQLStringB.append(" . ");

                    break;
                case "ObjectPropertyAssertion": {
                    String elem3 = line[3].split(">")[0];
                    SPARQLStringB.append("?");
                    SPARQLStringB.append(elem1);
                    SPARQLStringB.append(" ?");
                    SPARQLStringB.append(elem2);
                    SPARQLStringB.append(" ?");
                    SPARQLStringB.append(elem3);
                    SPARQLStringB.append(" . ");

                    break;
                }
                case "DataPropertyRange":
                case "ObjectPropertyRange": {

                    SPARQLStringB.append("?");
                    SPARQLStringB.append(elem2);
                    SPARQLStringB.append(" rdfs:range ?");
                    SPARQLStringB.append(elem1);
                    SPARQLStringB.append(". ");
                    break;
                }
                case "DisjointObjectProperties":
                case "DisjointClasses": {
                    SPARQLStringB.append("?");
                    SPARQLStringB.append(elem1);
                    SPARQLStringB.append(" owl:disjointWith ?");
                    SPARQLStringB.append(elem2);
                    SPARQLStringB.append(". ");
                    break;
                }
                case "DifferentIndividuals": {
                    SPARQLStringB.append("?");
                    SPARQLStringB.append(elem1);
                    SPARQLStringB.append(" owl:differentFrom ?");
                    SPARQLStringB.append(elem2);
                    SPARQLStringB.append(". ");
                    break;
                }
                case "EquivalentClasses": {
                    SPARQLStringB.append("?");
                    SPARQLStringB.append(elem1);
                    SPARQLStringB.append(" owl:equivalentClass ?");
                    SPARQLStringB.append(elem2);
                    SPARQLStringB.append(". ");
                    break;
                }
                default:
                    System.out.println(line[0].substring(0,line[0].length()-1));
                    throw new ClassCastException();
            }
        }
    }

    // Function to remove a single edge in the from the Vertices.
    private void RemoveVertex(String vertex, String vertexIn){
        // Remove vertex
        Vertices.remove(vertex);
        // Rebuild the new vertices Arraylist.
        ArrayList<String> newInVertices = new ArrayList<>();

        // Get all the SPARQL strings.
        for (String elem : Vertices.get(vertexIn)){
            // Loop through the list And check if they are equal. If they are add them to the NewInVertices.
            if(!elem.equals(vertex)){
                newInVertices.add(elem);
            }

        }
        // Replace vertices.
        Vertices.replace(vertexIn,newInVertices );

    }

    // Remove a single Edge
    private void RemoveEdge(String vertex1, String vertex2){
        Edges.remove(vertex1+vertex2);
    }

    // Get the list of vertices degrees. A cheaper version of the map. Only to check if two lists are isomorphic. Is sorted.
    List<Integer> GetVerticesDegree() {
        List<Integer> VerticesDegree = new ArrayList<>();
        for (String element: this.GetVerticesSet()){
            VerticesDegree.add(this.GetOutElements(element).size());
        }
        Collections.sort(VerticesDegree);
        return VerticesDegree;
    }

    // Gets the instances as set.
    ArrayList<String> GetInstancesSet() {
        ArrayList<String> newList = new ArrayList<>();
        for (String elem : this.GetVertices()){
            if( elem.contains("a")){
                newList.add(elem);
            }
        }
        return newList;
    }

    // Gets all Vertices as set.
    ArrayList<String> GetVerticesSet() {
        return new ArrayList<>(this.GetVertices());
    }

    @SuppressWarnings("WeakerAccess")
    public String GetEdges(String Vertex1, String Vertex2 ) {
        return Edges.get(Vertex1+Vertex2);
    }

    // A GetVertices function that returns all outgoing vertices of the Vertex.
    ArrayList<String> GetOutVertices(String Vertex ) {
        return Vertices.get(Vertex);
    }


    // A Get Out elements that returns a Array comprising of the combining Vertices and Edges for the outgoing links.
    ArrayList<String[]> GetOutElements(String Vertex){
        ArrayList<String[]> TempStorage = new ArrayList<>();
        ArrayList<String> VerticesSet  = Vertices.get(Vertex);
//        System.out.println(VerticesSet.size());

        try {
            for (String elem : VerticesSet) {
                String[] TempString = new String[2];
                TempString[0] = elem;
                TempString[1] = GetEdges(Vertex, elem);
                TempStorage.add(TempString);

            }
        } catch (Exception e) {
            // Caught element with zero links will be skipped.
        }


        return TempStorage;
    }

    // A Get vertices function that returns all outgoing vertices of the Vertex.
    public Set<String> GetVertices() {
        return Vertices.keySet();
    }

    // If no Parameter is found the print uses this version.
    void print(){ print("System.out"); }

    // TODO2: Make pretty print of the graph.
    @SuppressWarnings("SameParameterValue")
    public void print( FileOutputStream printLocation, int number){
        if (printLocation != null){
            try{
                printLocation.write(("\nNew General Graph number: "+ number+ "\n").getBytes());

                for (Object Axiom : getAxioms().toArray()){

                    String AxiomString = Axiom.toString() + "\n";
                    printLocation.write(AxiomString .getBytes());

                    }
            } catch (Exception e ){
                e.printStackTrace();
            }
        } else {
            throw new ExceptionInInitializerError("Not correctly initialized the fileOutputStream.");
        }


    }

    // TODO2: Make pretty print of the graph.
    @SuppressWarnings("SameParameterValue")
    private void print( String printLocation){
        // Print the general Graph uses the print location to write the file. Default is System.out
        if (printLocation.contains("System.out") || printLocation.isEmpty()){
            // Print to System out. By looping through the array and printing out the axioms.
            System.out.println("Graph to system out:");
            for (Object Axiom : getAxioms().toArray()){
                System.out.println(Axiom.toString());
            }
        }

    }

    // Gets a vertex with a degree of one and not yet in the inputted values.
    String GetVertexDegree1(ArrayList<String> UsedElem){
        for (String Vertex : this.GetVerticesSet()){
            if( this.GetOutElements(Vertex).size() == 1){
                if( !(UsedElem.contains(Vertex))){
                    return Vertex;
                }

            }
        }
        return null;
    }


    public String convertSPARQL(){
        // Converts a generalised subgraph to a set of SPARQL lines.

        return "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
               "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
               "prefix owl: <http://www.w3.org/2002/07/owl#>" +
               "SELECT * WHERE {" + SPARQLStringB.toString() + "}";

    }

    List<String> getExplanationStringList() {
        return ExplanationStringList;
    }



    // TODO: Can add in more functions to generalize the graph further:

    public void generalizeFurther() {


    }
}
