package com.caucasus.optimization.algos;

import java.util.ArrayList;
import java.util.function.Function;

public class Dichotomy extends AbstractIntervalMinFinder {

    public Dichotomy(Function<Double, Double> function, double leftBorder, double rightBorder, double eps, double delta) {
        super(function, leftBorder, rightBorder, eps, delta);
    }

    public Dichotomy(Function<Double, Double> function, Interval domain, double eps, double delta) {
        super(function, domain, eps, delta);
    }

    @Override
    Solution calculateSolution() {
        double leftBorder = getLeftBorder();
        double rightBorder = getRightBorder();
        ArrayList<Interval> intervals = new ArrayList<>();
        intervals.add(new Interval(leftBorder, rightBorder));
        while (!validateAccuracy(leftBorder, rightBorder)) {
            final double x1 = (leftBorder + rightBorder - getDelta()) * 0.5;
            final double x2 = (leftBorder + rightBorder + getDelta()) * 0.5;
            if (compare(getFunction().apply(x1), getFunction().apply(x2)) <= 0) {
                rightBorder = x2;
            } else {
                leftBorder = x1;
            }
            intervals.add(new Interval(leftBorder, rightBorder));
        }
        double endPoint = (leftBorder + rightBorder) * 0.5;

        return new Solution(intervals, endPoint);
    }

    private double calcNthEps(double lb, double rb) {
        return (rb - lb) * 0.5;
    }

    private boolean validateAccuracy(double lb, double rb) {
        return compare(calcNthEps(lb, rb), getEps()) <= 0;
    }
}
