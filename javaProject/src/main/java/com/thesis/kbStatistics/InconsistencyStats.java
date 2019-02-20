package com.thesis.kbStatistics;

class InconsistencyStats {

    private int Size;
    private int Count;
    private String Type;
    private String ClassType;
    private float TailEffect;


    InconsistencyStats(int Size, int Count, String Type, String ClassType, float TailEffect){
        this.Size = Size;
        this.Count = Count;
        this.Type = Type;
        this.ClassType = ClassType;
        this.TailEffect = TailEffect;

    }

    int getCount() {
        return Count;
    }

    int getSize() {
        return Size;
    }

    float getTailEffect() {
        return TailEffect;
    }

    String getClassType() {
        return ClassType;
    }

    String getType() {
        return Type;
    }
}
