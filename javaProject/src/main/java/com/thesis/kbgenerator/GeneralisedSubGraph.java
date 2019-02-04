package com.thesis.kbgenerator;


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
class GeneralisedSubGraph {

    // Make New Subgraph
    private OWLOntology owlOntologyGraph;        // Instantiate OWLOntology for the graph, The bread and butter of this class
    private OWLDataFactory dataFactory;     // Instantiate the DataFactory.
    private HashMap<String, ArrayList<String>> Vertices = new HashMap<>();
    private HashMap<String, String> Edges = new HashMap<>();
    private StringBuilder SPARQLStringB = new StringBuilder();

    private boolean consistency;            // Store the consistency of the subgraph


    // Constructor for the Generalised Sub graph.
    GeneralisedSubGraph(List<String> Subgraph) {

        // OWL ontology manager creation.
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

                    SPARQLStringB.append("?");
                    SPARQLStringB.append(OWLAXIOMString[2]);
                    SPARQLStringB.append(" <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?");
                    SPARQLStringB.append(OWLAXIOMString[1]);
                    SPARQLStringB.append(". ");



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


                    SPARQLStringB.append("?");
                    SPARQLStringB.append(OWLAXIOMString[1]);
                    SPARQLStringB.append(" <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?");
                    SPARQLStringB.append(OWLAXIOMString[2]);
                    SPARQLStringB.append(". ");

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
                            // Add Triple To hashMap
                            addToList(OWLAXIOMString[1], OWLAXIOMString[2]);
                            addToList(OWLAXIOMString[2], OWLAXIOMString[1]);

                            SPARQLStringB.append("?");
                            SPARQLStringB.append(OWLAXIOMString[1]);
                            SPARQLStringB.append(" <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?");
                            SPARQLStringB.append(OWLAXIOMString[2]);
                            SPARQLStringB.append(". ");
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
                            SPARQLStringB.append("?");
                            SPARQLStringB.append(OWLAXIOMString[1]);
                            SPARQLStringB.append(" <http://www.w3.org/2002/07/owl#disjointWith> ?");
                            SPARQLStringB.append(OWLAXIOMString[2]);
                            SPARQLStringB.append(". ");
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
                            SPARQLStringB.append("?");
                            SPARQLStringB.append(OWLAXIOMString[1]);
                            SPARQLStringB.append(" <http://www.w3.org/2002/07/owl#equivalentClass> ?");
                            SPARQLStringB.append(OWLAXIOMString[2]);
                            SPARQLStringB.append(". ");
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
    Stream<OWLAxiom> getAxioms(){
        return owlOntologyGraph.axioms();
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

    HashMap<String, Integer> GetVerticesDegreeMap() {
        HashMap<String, Integer> VerticesDegree = new HashMap<>();
        for (String element: this.GetVerticesSet()){
            VerticesDegree.put(element , this.GetOutElements(element).size());
        }
        return VerticesDegree;
    }

    void SinglesRemoval() {
        HashMap<String, Integer> VerticesDegree = GetVerticesDegreeMap();

        for (String elem: VerticesDegree.keySet() ){
            Integer degrees = 0;
            for (String elemTemp: VerticesDegree.keySet() ){
                if(!(GetEdges(elem, elemTemp) == null)){
                    degrees ++;
                }
            }

            if (degrees <= 1){
                String Class = Vertices.get(elem).get(0);
                if (elem.contains("a")){
                    // Instantiate Instance and update instance count.
                    OWLIndividual Individual = dataFactory.getOWLNamedIndividual(elem);


                    OWLClass classC2 = dataFactory.getOWLClass(Class);
                    OWLClassAssertionAxiom Assertion = dataFactory.getOWLClassAssertionAxiom(classC2, Individual);
                    owlOntologyGraph.removeAxiom(Assertion);
                } else {
                    // Instantiate Instance and update instance count.
                    OWLClass classC1 = dataFactory.getOWLClass(elem);
                    String Edge = Edges.get(elem + Class);
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

        HashMap<String, Integer> VerticesDegree2 = GetVerticesDegreeMap();
        Boolean RecursiveActionBool = false;
        for (String elem: VerticesDegree2.keySet() ){
            Integer degrees = 0;
            for (String elemTemp: VerticesDegree2.keySet() ){
                if(!(GetEdges(elem, elemTemp) == null)){
                    degrees ++;
                }
            }

            if (degrees <= 1){
                RecursiveActionBool = true;
            }
        }
        if (RecursiveActionBool){
            SinglesRemoval();
        }


    }

    void RebuildSPARQL (){
        SPARQLStringB = new StringBuilder();
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
                    SPARQLStringB.append("?");
                    SPARQLStringB.append(elem1);
                    SPARQLStringB.append(" <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?");
                    SPARQLStringB.append(elem2);
                    SPARQLStringB.append(". ");
                    break;
                }

                case "SubClassOf": {

                    SPARQLStringB.append("?");
                    SPARQLStringB.append(elem1);
                    SPARQLStringB.append(" <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?");
                    SPARQLStringB.append(elem2);
                    SPARQLStringB.append(". ");
                    break;
                }
                case "DisjointClasses": {
                    SPARQLStringB.append("?");
                    SPARQLStringB.append(elem1);
                    SPARQLStringB.append(" <http://www.w3.org/2002/07/owl#disjointWith> ?");
                    SPARQLStringB.append(elem2);
                    SPARQLStringB.append(". ");
                    break;
                }
                case "EquivalentClasses": {
                    SPARQLStringB.append("?");
                    SPARQLStringB.append(elem1);
                    SPARQLStringB.append(" <http://www.w3.org/2002/07/owl#equivalentClass> ?");
                    SPARQLStringB.append(elem2);
                    SPARQLStringB.append(". ");
                    break;
                }
                default:
                    throw new ClassCastException();
            }
        }
    }

    void RemoveVertex(String vertex, String vertexIn){
        Vertices.remove(vertex);
        ArrayList<String> newInVertices = new ArrayList<>();
        for (String elem : Vertices.get(vertexIn)){
            if(!elem.equals(vertex)){
                newInVertices.add(elem);
            }

        }
        Vertices.replace(vertexIn,newInVertices );

    }

    void RemoveEdge(String vertex1, String vertex2){
        Edges.remove(vertex1+vertex2);
    }

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
    @SuppressWarnings("WeakerAccess")
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
    public void print( String printLocation){
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


    String convertSPARQL(){
        // Converts a generalised subgraph to a set of SPARQL lines.

        return "SELECT * WHERE {" + SPARQLStringB.toString() + "}";

    }

}
