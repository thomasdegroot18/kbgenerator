package com.thesis.kbgenerator;

public class IsomorphismManager {

    // Test Subgraph with other subgraph owlOntologyGraph

    boolean CompareGraph(GeneralisedSubGraph Graph1, GeneralisedSubGraph Graph2){

        // If Size is not equal the two graphs can not be isomorphic.
        if (Graph1.size() != Graph2.size() ){
            return false;
        }
        if(Graph1.getCountInstances() != Graph2.getCountInstances()){
            return false;
        }
        if(Graph1.getCountClasses() != Graph2.getCountClasses()){
            return false;
        }
        boolean IsIsomorphic = recursiveIsomorphicCheck(Graph1, Graph2);

        return IsIsomorphic;

    }

    private boolean recursiveIsomorphicCheck(GeneralisedSubGraph Graph1, GeneralisedSubGraph Graph2) {


        // TODO:
        //  PROCEDURE Match(s)
        //    INPUT: an intermediate state s; the initial state s0 has M(s0)=
        //    OUTPUT: the mappings between the two graphs
        //    IF M(s) covers all the nodes of G2 THEN
        //        OUTPUT M(s)
        //    ELSE
        //        Compute the set P(s) of the pairs candidate for inclusion in M(s)
        //        FOREACH (n, m) P(s)
        //            IF F(s, n, m) THEN
        //                Compute the state s´ obtained by adding (n, m) to M(s)
        //                CALL Match(s )
        //            END IF
        //        END FOREACH
        //         Restore data structures
        //    END IF
        //  END PROCEDURE


        for (Object axiomsGraph1 : Graph1.getAxioms().toArray()) {
            String Specialized1  = axiomsGraph1.toString();
            if (Specialized1.contains("Declaration")){
                continue;
            }
            String GeneralString1 = Specialized1.replaceAll("<a[0-9]+>","Instance").replaceAll("<C[0-9]+>","Class");

            for (Object axiomsGraph2 : Graph2.getAxioms().toArray()) {
                String Specialized2  = axiomsGraph2.toString();
                if (Specialized2.contains("Declaration")){
                    continue;
                }
                String GeneralString2 = Specialized2.replaceAll("<a[0-9]+>","Instance").replaceAll("<C[0-9]+>","Class");

            }


        }







        return true;
    }

}
