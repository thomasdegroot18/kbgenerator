package com.thesis.kbStatistics;

import com.thesis.kbInconsistencyLocator.InconsistencyLocator;
import openllet.core.KnowledgeBase;
import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;
import openllet.owlapi.explanation.PelletExplanation;
import org.apache.jena.rdf.model.Model;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.header.Header;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.triples.TripleString;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.*;

class KbStatistics {

    private static long InDegreeDistribution(HDT hdt, String input){
        try {
            IteratorTripleString it = hdt.search(input, "", "");
            // Find the estimated inDegree.
            return it.estimatedNumResults();
        } catch (Exception e){
            System.out.println("Could not find the inserted string");
        }

        return 0;
    }

    private static long OutDegreeDistribution(HDT hdt, String input){
        try {
            IteratorTripleString it = hdt.search("", "",input);
            // Find the estimated inDegree.
            return it.estimatedNumResults();
        } catch (Exception e){
            System.out.println("Could not find the inserted string");
        }

        return 0;
    }

    private static double ClusteringCoef(HDT hdt, String input){
        try {
            IteratorTripleString it = hdt.search(input,"", "");
            HashSet<String> Neighbours = new HashSet<>();
            double LocalizedTriangle = 0;
            while(it.hasNext()){
                Neighbours.add(it.next().getObject().toString());
            }

            for (CharSequence Neighbour: Neighbours){

                IteratorTripleString it3 = hdt.search(Neighbour,"", "");

                while(it3.hasNext()){
                    CharSequence NeighbourNeighbour = it3.next().getObject().toString();
                    if (Neighbours.contains(NeighbourNeighbour)){
                        LocalizedTriangle ++;
                    }
                }
            }
            double k = Neighbours.size();
            if (k < 2){
                return 0;
            }
            return LocalizedTriangle/(k*(k-1));
        } catch (Exception e){
            System.out.println("Could not find the inserted string");
        }

        return 0;
    }

    private static int KbSize(HDT hdt){
        Header header = hdt.getHeader();
        int result;
        try {
            IteratorTripleString it = header.search("", "", "");
            while (it.hasNext()) {
                TripleString itElem = it.next();
                if (itElem.getPredicate().toString().contains("http://rdfs.org/ns/void#triples")){
                    result = Integer.parseInt(itElem.getObject().toString().substring(1,itElem.getObject().toString().length()-1));
                    return result;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        // if the result can not be found in the header a separate function should be called to find it.
        System.out.println("Could not find indication of the amount of triples in the header.");
        return 0;
    }

    // TODO: Implement Strong and Weak CC
    private static int StrongCC(HDT hdt){


        return 100;
    }

    private static int WeakCC(HDT hdt){


        return 200;
    }

    private static String getExpressively(HDT hdt) throws Exception {
        IteratorTripleString it = hdt.search("","","");
        // If the loop is not triggered the next element from the tripleString is taken.
        TripleString item = it.next();

        // both the subject and the object are taken to build the subgraph. With the expectation that the subject
        // graph encompasses the object graph. With the exception when the subject graph gets to large and misses
        // some of the depth the object graph does take into account.
        String subject = item.getSubject().toString();

        // The subGraph is a set of strings. That stores up to 5000 triples.
        Set<String> subGraph = InconsistencyLocator.WriteInconsistencySubGraph(hdt, subject, 50000);

        OWLOntology ontology = OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(InconsistencyLocator.PipeModel(subGraph));


        // Starting up the Pellet Explanation module.
        PelletExplanation.setup();

        // Create the reasoner and load the ontology with the open pellet reasoner.
        OpenlletReasoner reasoner = OpenlletReasonerFactory.getInstance().createReasoner(ontology);

        KnowledgeBase kb = reasoner.getKB();

        System.out.println("Expressivity   : " + kb.getExpressivity());


        return kb.getExpressivity().toString();
    }


    private static HashSet<String> NameSpaces(HDT hdt){

        Iterator it = hdt.getDictionary().getPredicates().getSortedEntries();
        HashSet<String> NameSpaceSet = new HashSet<>();
        HashSet<String> Elements = new HashSet<>();
        while (it.hasNext()){
            String NameSpaceNotCleaned = it.next().toString();
            int HashNumber = NameSpaceNotCleaned.lastIndexOf("#");
            int SlashNumber = NameSpaceNotCleaned.lastIndexOf("/");
            String NameSpaceCleaned;
            if(HashNumber > SlashNumber ){
                NameSpaceCleaned = NameSpaceNotCleaned.substring(0, HashNumber + 1);
                if(NameSpaceCleaned.contains("www.w3.org")){
                    Elements.add(NameSpaceNotCleaned);
                }
                if (NameSpaceCleaned.contains("http://dbpedia.org/property/")){
                    NameSpaceCleaned = "http://dbpedia.org/property/";
                }
                if (NameSpaceCleaned.contains("http://dbpedia.org/ontology/")){
                    NameSpaceCleaned = "http://dbpedia.org/ontology/";
                }
            } else{
                NameSpaceCleaned = NameSpaceNotCleaned.substring(0, SlashNumber + 1);
                if(NameSpaceCleaned.contains("www.w3.org")){
                    Elements.add(NameSpaceNotCleaned);
                }
                if (NameSpaceCleaned.contains("http://dbpedia.org/property/")){
                    NameSpaceCleaned = "http://dbpedia.org/property/";
                }
                if (NameSpaceCleaned.contains("http://dbpedia.org/ontology/")){
                    NameSpaceCleaned = "http://dbpedia.org/ontology/";
                }
                if (NameSpaceCleaned.contains("http://wikidata.dbpedia.org/property/")){
                    NameSpaceCleaned = "http://wikidata.dbpedia.org/property/";
                }
                if (NameSpaceCleaned.contains("http://wikidata.dbpedia.org/ontology/")){
                    NameSpaceCleaned = "http://wikidata.dbpedia.org/ontology/";
                }


            }
            NameSpaceSet.add(NameSpaceCleaned);
        }
        for (String elem : NameSpaceSet){
            System.out.println(elem);
        }
        for (String elem2 : Elements){
            System.out.println(elem2);
        }
        return NameSpaceSet;
    }

    private static List<HashMap> GetLocalizedScores(HDT hdt){
        Iterator it = hdt.getDictionary().getSubjects().getSortedEntries();
        HashMap<Long, Integer> hashInDegree = new HashMap<>();
        HashMap<Long, Integer> hashOutDegree = new HashMap<>();
        HashMap<Double, Integer> ClusteringCoefMap = new HashMap<>();
        int Element = 0;
        while (it.hasNext()){
            String SubjectTripleElem = it.next().toString();
            long In = InDegreeDistribution(hdt, SubjectTripleElem );
            if(hashInDegree.containsKey(In)){
                int i = hashInDegree.get(In);
                i++;
                hashInDegree.replace(In, i);
            }else{
                hashInDegree.put(In, 1);
            }

            long Out = OutDegreeDistribution(hdt, SubjectTripleElem );
            if(hashOutDegree.containsKey(Out)){
                int i = hashOutDegree.get(Out);
                i++;
                hashOutDegree.replace(Out, i);
            }else{
                hashOutDegree.put(Out, 1);
            }
            if(Element % 1000000 == 0){
                System.out.println(Element);
            }
            // Calculating the Cluster Coefficient
            Double Coef = ClusteringCoef(hdt, SubjectTripleElem );
            if(ClusteringCoefMap.containsKey(Coef)){
                int i = ClusteringCoefMap.get(Coef);
                i++;
                ClusteringCoefMap.replace(Coef, i);
            }else{
                ClusteringCoefMap.put(Coef, 1);
            }


            Element ++;
        }


        List<HashMap> arrayList = new ArrayList<>();
        arrayList.add(hashInDegree);
        arrayList.add(hashOutDegree);
        arrayList.add(ClusteringCoefMap);

        return arrayList;
    }



    static Object RunAll(Model model, HDT hdt){
        String Expressivity = null;
        try{
            Expressivity = getExpressively(hdt);
        } catch (Exception e){
            e.printStackTrace();
        }
        int Size = KbSize(hdt);
        HashSet<String> NameSpaceSet = NameSpaces(hdt);
        List<HashMap> arrayList = GetLocalizedScores(hdt);

        Object Test = new Object();
        return Test;
    }



}



