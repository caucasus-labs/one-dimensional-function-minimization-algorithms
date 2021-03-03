package com.caucasus.optimization;

import com.caucasus.optimization.algos.entities.minfinder.*;
import com.caucasus.optimization.algos.entities.util.Interval;
import com.caucasus.optimization.algos.entities.util.Solution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SectionTester {

    private static class SolutionWithName {
        String name;
        Solution solution;

        public SolutionWithName(String name, Solution solution) {
            this.name = name;
            this.solution = solution;
        }
    }
    public static void main(String[] args) {
        final Interval borders = new Interval(0, 1);
        final Double epsilon = 1e-9;
        final Function<Double, Double> function = x -> Math.exp(3.0D * x) + 5 * Math.exp(-2.0D * x);
        Path outputFile;
        final String folderName = "tables" + File.separator;
        for (SolutionWithName solution : calculateSolutions(function, borders, epsilon)) {
            try {
                outputFile = Paths.get(folderName + solution.name + ".dat");
            } catch (final InvalidPathException e) {
                System.err.println("Invalid path: " + e.getMessage());
                return;
            }
            try (final BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
                List<Interval> intervals = solution.solution.getIntervals();
                List<Double> approxMinPoints = solution.solution.getApproximatelyMinimums();
                try {
                    for (int i = 0; i < Math.min(intervals.size(), approxMinPoints.size()); i++) {
                        //section, len, x, f(x)
                        final Interval curInterval = intervals.get(i);
                        final Double point = approxMinPoints.get(i);
                        writer.write(String.format("[%.9f; %.9f] & %.9f & %.9f & %.9f", curInterval.getLeftBorder(), curInterval.getRightBorder(),
                                curInterval.getRightBorder() - curInterval.getLeftBorder(), point, function.apply(point)));
                        writer.newLine();
                    }
                } catch (final IOException e) {
                    System.err.println("Error printing to file " + outputFile + ": " + e.getMessage());
                }
            } catch (final IOException e) {
                System.err.println("Error creating writer: " + e.getMessage());
            }
        }
    }

    private static List<SolutionWithName> calculateSolutions(Function<Double, Double> function,
                                                           Interval interval,
                                                           Double eps) {
        List<SolutionWithName> list = new ArrayList<>();
        list.add(new SolutionWithName("dichotomy", new Dichotomy(function, interval, eps).getSolution()));
        list.add(new SolutionWithName("goldensection", new GoldenSection(function, interval, eps).getSolution()));
        list.add(new SolutionWithName("fibonacci", new Fibonacci(function, interval, eps).getSolution()));
        list.add(new SolutionWithName("brent", new Brent(function, interval, eps).getSolution()));
        list.add(new SolutionWithName("parabola", new Paraboloid(function, interval, eps).getParaboloidSolution()));
        return list;
    }
}
