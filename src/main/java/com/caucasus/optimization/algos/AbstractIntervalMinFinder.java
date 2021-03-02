package com.caucasus.optimization.algos;


import java.util.function.Function;

abstract public class AbstractIntervalMinFinder extends AbstractMinFinder implements IntervalMinFinder {
    private final Solution solution;

    public AbstractIntervalMinFinder(Function<Double, Double> function, double leftBorder, double rightBorder, double eps, double delta) {
        this(function, new Interval(leftBorder, rightBorder), eps, delta);
    }

    public AbstractIntervalMinFinder(Function<Double, Double> function, Interval domain, double eps, double delta) {
        super(function, domain, eps, delta);
        this.solution = calculateSolution();
    }

    @Override
    public Solution getSolution() {
        return solution;
    }

    abstract Solution calculateSolution();
}
