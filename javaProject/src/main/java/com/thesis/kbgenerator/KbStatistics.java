package com.thesis.kbgenerator;

import org.apache.jena.rdf.model.Model;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.header.Header;
import org.rdfhdt.hdt.triples.IteratorTripleID;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.triples.TripleString;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

class KbStatistics {
    static long InDegreeDistribution(HDT hdt, String input){
        try {
            IteratorTripleString it = hdt.search(input, "", "");
            // Find the estimated inDegree.
            return it.estimatedNumResults();
        } catch (Exception e){
            System.out.println("Could not find the inserted string");
        }

        return 0;
    }

    static long OutDegreeDistribution(HDT hdt, String input){
        try {
            IteratorTripleString it = hdt.search("", "",input);
            // Find the estimated inDegree.
            return it.estimatedNumResults();
        } catch (Exception e){
            System.out.println("Could not find the inserted string");
        }

        return 0;
    }

    static double ClusteringCoef(HDT hdt, String input){
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

    static int KbSize(HDT hdt){
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

        // if the result can not be found in the header a seperate function should be called to find it.
        System.out.println("Could not find indication of the amount of triples in the header.");
        return 0;
    }

    // TODO: Implement Strong and Weak CC
    static int StrongCC(HDT hdt){


        return 100;
    }

    static int WeakCC(HDT hdt){


        return 200;
    }


    static HashSet<String> NameSpaces(HDT hdt){

        Iterator it = hdt.getDictionary().getPredicates().getSortedEntries();
        HashSet<String> NameSpaceSet = new HashSet<>();
        while (it.hasNext()){
            String NameSpaceNotCleaned = it.next().toString();
            int HashNumber = NameSpaceNotCleaned.lastIndexOf("#");
            int SlashNumber = NameSpaceNotCleaned.lastIndexOf("/");
            String NameSpaceCleaned;
            if(HashNumber > SlashNumber ){
                NameSpaceCleaned = NameSpaceNotCleaned.substring(0, HashNumber+1);
            } else{
                NameSpaceCleaned = NameSpaceNotCleaned.substring(0, SlashNumber +1);
            }
            NameSpaceSet.add(NameSpaceCleaned);
        }
        for (String elem : NameSpaceSet){
            System.out.println(elem);
        }
        return NameSpaceSet;
    }

    static Object GetLocalizedScores(HDT hdt){
        Iterator it = hdt.getDictionary().getSubjects().getSortedEntries();
        HashMap<Long, Integer> hashInDegree = new HashMap<>();
        HashMap<Long, Integer> hashOutDegree = new HashMap<>();
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
            double Coef = ClusteringCoef(hdt, SubjectTripleElem );

            Element ++;
        }

        Object localObject = new Object();
        return localObject;
    }



    static Object RunAll(Model model, HDT hdt){
        KbSize(hdt);
        NameSpaces(hdt);
        GetLocalizedScores(hdt);

        Object Test = new Object();
        return Test;
    }

}
