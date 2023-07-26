package solution;


import lib.individuals.base.IIndividual;

import java.util.ArrayList;
import java.util.List;

public class ResultRecorder {
    private AlgorithmParameters algorithmParameters;

    private final List<Generation> generations;

    private IIndividual best;

    public ResultRecorder() {
       this.generations = new ArrayList<>();
    }

    public void recordParameters(int populationSize, double maximalDifference, double populationCheckCoefficient, double mutationProbability, double crossProbability) {
        algorithmParameters = new AlgorithmParameters(populationSize, maximalDifference, populationCheckCoefficient, mutationProbability, crossProbability);
    }

    public void recordGeneration(int satisfiedAmount, IIndividual[] generation) {
        generations.add(new Generation(satisfiedAmount, algorithmParameters.populationSize, generation));
    }

    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n{\n")
                .append("Number of generations: ").append(generations.size()).append("\n")
                .append(algorithmParameters).append("\n")
                .append("Best individual: ").append(best)
                .append("\n}");

        return sb.toString();
    }

    public void recordBest(IIndividual iIndividual) {
        best = iIndividual;
    }

    private static class AlgorithmParameters {
        private final int populationSize;
        private final double maximalDifference;
        private final double coefficient;
        private final double mutationProbability;
        private final double crossProbability;

        public AlgorithmParameters(int populationSize, double maxDifference, double coefficient, double mutationProbability, double crossProbability) {
            this.populationSize = populationSize;
            this.maximalDifference = maxDifference;
            this.coefficient = coefficient;
            this.mutationProbability = mutationProbability;
            this.crossProbability = crossProbability;
        }

        public String toString() {
            return new StringBuilder().append("Algorithm parameters:")
                    .append("\n\tpopulation size: ").append(populationSize)
                    .append("\n\tmaximalDifference: ").append(maximalDifference)
                    .append("\n\tpopulationCheckCoefficient: ").append(coefficient)
                    .append("\n\tmutationProbability: ").append(mutationProbability)
                    .append("\n\tcrossProbability: ").append(crossProbability).append("\n").toString();
        }
    }

    private static class Generation {
        IIndividual[] individuals;
        private final int satisfiedAmount;
        private final int populationSize;
        public Generation(int satisfiedAmount, int populationSize, IIndividual[] generation) {
            this.individuals = generation;
            this.satisfiedAmount = satisfiedAmount;
            this.populationSize = populationSize;
        }

        public String toString() {
            return new StringBuilder()
                    .append("OK/ALL: "). append(satisfiedAmount).append("/").append(populationSize).toString();
        }
    }
}
