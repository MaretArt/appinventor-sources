package com.marchtech.SimplePedometer_v2;

public interface StepListener {
    public void onWalkStep(int steps, float distances);
    public void onSimpleStep(int steps, float distances);
}
