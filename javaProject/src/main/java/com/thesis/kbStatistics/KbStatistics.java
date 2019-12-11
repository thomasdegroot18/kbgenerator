package com.thesis.kbStatistics;

import com.thesis.kbInconsistencyLocator.InconsistencyLocator;
import openllet.core.KnowledgeBase;
import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;
import openllet.owlapi.explanation.PelletExplanation;
import org.apache.jena.rdf.model.*;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.header.Header;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.triples.TripleString;
import org.rdfhdt.hdtjena.HDTGraph;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.*;

import static org.apache.jena.rdf.model.ResourceFactory.createResource;

@SuppressWarnings("unused")
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

    private static double ClusteringCoefficient(Model hdt, String input){
        try {
            Resource ResourceElem = createResource(input);
            StmtIterator it = hdt.listStatements(ResourceElem, null ,(RDFNode)null);
            HashSet<RDFNode> Neighbours = new HashSet<>();
            double LocalizedTriangle = 0;
            while(it.hasNext()){
                RDFNode Object = it.next().getObject();
//                if (Object.isLiteral()){
//                    continue;
//                }
                Neighbours.add(it.next().getObject());
            }

            for (RDFNode Neighbour: Neighbours){

                StmtIterator it3 = hdt.listStatements(Neighbour.asResource(), null ,(RDFNode)null);

                while(it3.hasNext()){
                    RDFNode NeighbourNeighbour = it3.next().getObject();
//                    if (NeighbourNeighbour.isLiteral()){
//                        continue;
//                    }
                    if (Neighbours.contains(NeighbourNeighbour)){
                        LocalizedTriangle ++;
                    }
                }
            }
            double k = Neighbours.size();
            if (k < 2){
                return 0;
            }

            System.out.println(k);
            System.out.println(LocalizedTriangle);

            return LocalizedTriangle/(k*(k-1));
        } catch (Exception e){
            System.out.println("Could not find the inserted string");
        }

        return 0;
    }

    private static double ClusteringCoefficient(HDT hdt, String input){
        try {
            IteratorTripleString it = hdt.search(input,"", "");
            HashSet<String> Neighbours = new HashSet<>();
            double LocalizedTriangle = 0;
            while(it.hasNext()){
                Neighbours.add(it.next().getObject().toString());

            }
            if (Neighbours.size() < 1){
                return 0;
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


    private static String getExpressively(HDT hdt) throws Exception {
        IteratorTripleString it = hdt.search("","","");
        // If the loop is not triggered the next element from the tripleString is taken.
        TripleString item = it.next();

        // both the subject and the object are taken to build the subgraph. With the expectation that the subject
        // graph encompasses the object graph. With the exception when the subject graph gets to large and misses
        // some of the depth the object graph does take into account.
        String subject = item.getSubject().toString();

        // The subGraph is a set of strings. That stores up to 50000 triples.
        Set<String> subGraph = InconsistencyLocator.WriteInconsistencySubGraph(hdt, subject, 50000);

        OWLOntology ontology = OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(InconsistencyLocator.PipeModel(subGraph));


        // Starting up the Pellet Explanation module.
        PelletExplanation.setup();

        // Create the reasoner and load the ontology with the open pellet reasoner.
        OpenlletReasoner reasoner = OpenlletReasonerFactory.getInstance().createReasoner(ontology);

        KnowledgeBase kb = reasoner.getKB();


        return kb.getExpressivity().toString();
    }


    private static ArrayList<HashSet<String>>  NameSpaces(HDT hdt){

        Iterator it = hdt.getDictionary().getPredicates().getSortedEntries();
        HashSet<String> NameSpaceSet = new HashSet<>();
        HashSet<String> Elements = new HashSet<>();
        while (it.hasNext()){
            String NameSpaceNotCleaned = it.next().toString();
            int HashNumber = NameSpaceNotCleaned.lastIndexOf("#");
            int SlashNumber = NameSpaceNotCleaned.lastIndexOf("/");
            String NameSpaceCleaned;

            // Clean a set of namespaces such that the overlap a bit better.
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
                if (NameSpaceCleaned.startsWith("http://www.w3.org/1999/02/22-rdf-")){
                    NameSpaceCleaned = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
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
                if (NameSpaceCleaned.startsWith("http://www.w3.org/1999/02/22-rdf-")){
                    NameSpaceCleaned = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
                }


            }
            NameSpaceSet.add(NameSpaceCleaned);
        }
        ArrayList<HashSet<String>> Namespace = new ArrayList<>();
        Namespace.add(NameSpaceSet);
        Namespace.add(Elements);

        System.out.println("NameSpaces Clean: "+ NameSpaceSet.size());
        System.out.println("NameSpaces unClean: "+ Elements.size());
        return Namespace;
    }

    private static List<HashMap> GetLocalizedScores(HDT hdt){
        Iterator it = hdt.getDictionary().getSubjects().getSortedEntries();

//        // Locates the inconsistencies by looping through the graph over a large selection of triples.
//        System.out.println("Creating Jena HDT graph");
//        HDTGraph graph = new HDTGraph(hdt);
//
//        // Create Models
//        System.out.println("Creating model from graph");
//        Model model = ModelFactory.createModelForGraph(graph);

        HashMap<Long, Integer> hashInDegree = new HashMap<>();
        HashMap<Long, Integer> hashOutDegree = new HashMap<>();
        HashMap<Double, Integer> ClusteringCoefficientMap = new HashMap<>();
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
            if(Element % 10000 == 0){
                System.out.println("Element: " + Element);

            }
            // Calculating the Cluster Coefficient
            Double Coefficient = ClusteringCoefficient(hdt, SubjectTripleElem );
            if(ClusteringCoefficientMap.containsKey(Coefficient)){
                int i = ClusteringCoefficientMap.get(Coefficient);
                i++;
                ClusteringCoefficientMap.replace(Coefficient, i);
            }else{
                ClusteringCoefficientMap.put(Coefficient, 1);
            }


            Element ++;
        }

        it = hdt.getDictionary().getShared().getSortedEntries();
        Element = 0;
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
            if(Element % 10000 == 0){
                System.out.println("Element: " + Element);

            }
            // Calculating the Cluster Coefficient
            Double Coefficient = ClusteringCoefficient(hdt, SubjectTripleElem );
            if(ClusteringCoefficientMap.containsKey(Coefficient)){
                int i = ClusteringCoefficientMap.get(Coefficient);
                i++;
                ClusteringCoefficientMap.replace(Coefficient, i);
            }else{
                ClusteringCoefficientMap.put(Coefficient, 1);
            }


            Element ++;
        }
        System.out.println("Finished Searching Element");

        List<HashMap> arrayList = new ArrayList<>();
        arrayList.add(hashInDegree);
        arrayList.add(hashOutDegree);
        arrayList.add(ClusteringCoefficientMap);

        return arrayList;
    }


    @SuppressWarnings({"unchecked", "unused"})
    static void RunAll(HDT hdt){
        String Expressivity = null;
        try{
            Expressivity = getExpressively(hdt);
        } catch (Exception e){
            e.printStackTrace();
        }
        int Size = KbSize(hdt);
        ArrayList<HashSet<String>> NameSpaceSet = NameSpaces(hdt);
        List<HashMap> arrayList = GetLocalizedScores(hdt);

        System.out.println("The expressivity of the KB is: " + Expressivity);
        System.out.println("The size of the KB is: " + Size);

        for (String cleanNameSpace: NameSpaceSet.get(0)){
            System.out.println(cleanNameSpace);
        }

        for (String UncleanNameSpace: NameSpaceSet.get(1)){
            System.out.println(UncleanNameSpace);
        }

        // Print the hashes of the maps.
        HashMap<Long, Integer> hashInDegree = arrayList.get(0);
        HashMap<Long, Integer> hashOutDegree = arrayList.get(1);
        HashMap<Double, Integer> ClusteringCoefficientMap = arrayList.get(2);

        for(Long key: hashInDegree.keySet()){
            System.out.println(key+ " : "+  hashInDegree.get(key));
        }

        for(Long key: hashOutDegree.keySet()){
            System.out.println(key+ " : "+  hashOutDegree.get(key));
        }

        for(Double key: ClusteringCoefficientMap.keySet()){
            System.out.println(key+ " : "+  ClusteringCoefficientMap.get(key));
        }
    }
    @SuppressWarnings("unused")
    static void RunAll(HDT hdt, String UploadLocation){
        String Expressivity = "null";
//        try{
//            Expressivity = getExpressively(hdt);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        long Size = KbSize(hdt);

        ArrayList<HashSet<String>> NameSpaceSet = NameSpaces(hdt);
        List<HashMap> arrayList = new ArrayList<>();
//        arrayList = GetLocalizedScores(hdt);

        System.out.println("Start Writing");
        uploadTo(UploadLocation, Expressivity, 0, NameSpaceSet, arrayList);

    }
    @SuppressWarnings("unchecked")
    private static void uploadTo(String uploadLocation, String Expressivity, long Size, ArrayList<HashSet<String>> NameSpaceSet, List<HashMap> arrayList  ){
        List<String> StringArray = new ArrayList<>();

        StringArray.add("{\"Expressivity\" : \"" + Expressivity +"\",");
        StringArray.add("\"Size\" : " + Size +",");

        StringBuilder CleanNameSpace = new StringBuilder();
        CleanNameSpace.append("\"Namespaces\" : [ ");
        int i = 0;

        for (String cleanNameSpaceIns: NameSpaceSet.get(0)){
            if (i > 0 ){
                String Final = " , \"" + cleanNameSpaceIns + "\"";
                CleanNameSpace.append(Final);
            } else{
                String Final = "\""+ cleanNameSpaceIns + "\"";
                CleanNameSpace.append(Final);
                i = 1;
            }
        }

        CleanNameSpace.append("] , ");
        StringArray.add(CleanNameSpace.toString());

        StringBuilder Predicates = new StringBuilder();
        Predicates.append("\"Predicates\" : [ ");
        i = 0;

        for (String PredicatesString: NameSpaceSet.get(1)){
            if (i > 0 ){
                String Final = " , \"" + PredicatesString + "\"";
                Predicates.append(Final);
            } else{
                String Final = "\""+ PredicatesString + "\"";
                Predicates.append(Final);
                i = 1;
            }
        }

        Predicates.append("], ");
        StringArray.add(Predicates.toString());

//        // Print the hashes of the maps.
//        HashMap<Long, Integer> hashInDegree = arrayList.get(0);
//        HashMap<Long, Integer> hashOutDegree = arrayList.get(1);
//        HashMap<Double, Integer> ClusteringCoefficientMap = arrayList.get(2);
//
//
//        StringBuilder InDegree = new StringBuilder();
//        i = 0;
//        InDegree.append("\"InDegree\": {");
//        for(Long key: hashInDegree.keySet()){
//            if (i > 0 ){
//                String input = ",\""+ key+ "\" : "+  hashInDegree.get(key);
//                InDegree.append(input);
//            } else{
//                String input ="\""+ key+ "\" : "+  hashInDegree.get(key);
//                InDegree.append(input);
//                i = 1;
//            }
//        }
//        InDegree.append("},");
//        StringArray.add(InDegree.toString());
//
//
//        StringBuilder OutDegree = new StringBuilder();
//        i = 0;
//        OutDegree.append("\"OutDegree\": {");
//
//        for(Long key: hashOutDegree.keySet()){
//            if (i > 0 ){
//                String input = ",\""+ key+ "\" : "+  hashOutDegree.get(key);
//                OutDegree.append(input);
//            } else{
//                String input = "\""+key+ "\" : "+  hashOutDegree.get(key);
//                OutDegree.append(input);
//                i = 1;
//            }
//        }
//        OutDegree.append("},");
//        StringArray.add(OutDegree.toString());
//
//
//        StringBuilder ClusteringCoefficientString = new StringBuilder();
//        i = 0;
//        ClusteringCoefficientString.append("\"ClusteringCoefficient\": {");
//
//        for(Double key: ClusteringCoefficientMap.keySet()){
//            if (i > 0 ){
//                String input = ",\""+ key+ "\" : "+  ClusteringCoefficientMap.get(key);
//                ClusteringCoefficientString.append(input);
//            } else{
//                String input = "\""+ key+ "\" : "+  ClusteringCoefficientMap.get(key);
//                ClusteringCoefficientString.append(input);
//                i = 1;
//            }
//        }
//
//        ClusteringCoefficientString.append("}}");
//
//
//        StringArray.add(ClusteringCoefficientString.toString());


        Statistics.writeJSON(uploadLocation, StringArray);



    }


}



