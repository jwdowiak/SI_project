package solution;

import lib.CGA;
import lib.base.ICGA;
import lib.individuals.Coordinates;
import lib.individuals.base.IIndividual;
import tools.Cross;
import tools.Mutation;
import tools.Tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Solution {
    private static ResultLogger resultLogger  = new ResultLogger("results/coordinatesResults.txt");;
    public static void run() {
        // Ustawienie liczebności populacji
        int n = 64;

        // wygenerowanie populacji początkowej
        List<IIndividual> list = new ArrayList<>();
        Random random = new Random();
        for(int i=0;i<n;i++) {
            var result = Coordinates.create(random.nextInt(1024), random.nextInt(1024));
            list.add(result);
        }

        IIndividual[] initPopulation = Tool.CastListToArray(list);

        // 3. Parametry algorytmu. Wybrane w drodze eksperymentu:
        double maximalDifference = 150; // maksymalna różnica z najlepszym osobnikiem (zbiorem najlepszych)
        double populationCheckCoefficient = 0.5; // procent populacji, który ma spełniać warunek porównania
        double mutationProbability = 0.1;
        double crossProbability = 0.45;
        // Wartości te mają wpływ na warunek stopu. Sprawdzamy dla każdej populacji
        // czy populationCheckCoefficient*100% osobników ma taką własność, że :
        // |najlepszyosobnik - osobnik | <= maximalDifference


        //Tworzenie instancji algorytmu
        ICGA algorithm = new CGA(
                initPopulation,
                Mutation::generateMutation,
                Cross::crossingOperator,
                maximalDifference,
                populationCheckCoefficient,
                mutationProbability,
                crossProbability
        );

        //Wykonanie algorytmu
        int numberOfGenerations = 0;
        while(algorithm.doMainStep()) {
            numberOfGenerations++;

            System.out.println( String.format("Generacja %d, OK/ALL : %d/%d",numberOfGenerations, algorithm.numberOfIndividualsConditionSatisfied() ,algorithm.getPopulation().length ) );
        }

         // Podsumowanie
        System.out.println("\nPopulacja końcowa:");
        for (var item : algorithm.getPopulation()) {
            System.out.println(item);
        }

        System.out.println("\nGrupa maksymalnych rozwiązań: ");
        var theBestOfTheBest = algorithm.findBest();
        for (var item : theBestOfTheBest) {
            System.out.println(item);
        }

        System.out.println("\nOstateczna odpowiedź: ");
        System.out.println( theBestOfTheBest[0].toString() );


        resultLogger.noteResult(algorithm.getResultRecorder().getSummary());
        System.out.println("******************************");
        System.out.println("Best fitness values from all runs: " + resultLogger.getBestFitnessValue());

        System.out.println("\nBest individuals: ");
        resultLogger.printAsCoordinates(resultLogger.getBestIndividuals());
    }
}
