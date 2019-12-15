package com.thesis.kbStatistics;

class InconsistencyStats {

    private int Size;
    private int Count;
    private String Type;
    private String ClassType;
    private double TailEffect;
    private int[] CountDataSet;


    InconsistencyStats(int Count, int Size, String Type, String ClassType, double TailEffect, int[] CountDataSet){
        this.Size = Size;
        this.Count = Count;
        this.Type = Type;
        this.ClassType = ClassType;
        this.TailEffect = TailEffect;
        this.CountDataSet = CountDataSet;

    }

    int[] getCountDataSet() { return CountDataSet; }

    int getCount() {
        return Count;
    }

    int getSize() {
        return Size;
    }

    double getTailEffect() {
        return TailEffect;
    }

    String getClassType() {
        return ClassType;
    }

    String getType() {
        return Type;
    }
}
