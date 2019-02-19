package com.thesis.kbStatistics;


public class InconsistencyStatistics {

    public static int InconsistencySize(){


        return 5;
    }

    public static String InconsistencyType(){


        String type = "Test";
        return type;
    }

    public static String InconsistencyClasstype(){


        String ClassType = "Test";
        return ClassType;
    }

    public static float TailEffect(){


        return 0;
    }

    public static float InconsistencyCount(){


        return 0;
    }

    public static void RunAll(String StringOfInconsistency){
        StringOfInconsistency = "Declaration(Class(<C0>)), Declaration(Class(<C1>)), Declaration(Class(<C4>)), Declaration(Class(<C3>)), SubClassOf(<C0> <C1>), SubClassOf(<C4> <C0>), SubClassOf(<C3> <C4>), DisjointClasses(<C1> <C3>),";


    }

    public void uploadTo(String outputLocation){


    }
}
