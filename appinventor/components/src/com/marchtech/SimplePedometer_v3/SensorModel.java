package com.marchtech.SimplePedometer_v3;

public class SensorModel {
    protected int id;
    protected int simpleStep;
    protected int walkStep;
    protected float distance;
    protected float strideLength;
    protected long time;
    protected long startTime;

    public SensorModel() {}

    public SensorModel(int id, int simpleStep, int walkStep, float distance, float strideLength, long time, long startTime) {
        this.id = id;
        this.simpleStep = simpleStep;
        this.walkStep = walkStep;
        this.distance = distance;
        this.strideLength = strideLength;
        this.time = time;
        this.startTime  = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSimpleStep() {
        return simpleStep;
    }

    public void setSimpleStep(int simpleStep) {
        this.simpleStep = simpleStep;
    }

    public int getWalkStep() {
        return walkStep;
    }

    public void setWalkStep(int walkStep) {
        this.walkStep = walkStep;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getStrideLength() {
        return strideLength;
    }

    public void setStrideLength(float strideLength) {
        this.strideLength = strideLength;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
