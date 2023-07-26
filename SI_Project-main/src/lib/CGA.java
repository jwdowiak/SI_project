package lib;

import lib.base.Crosser;
import lib.base.ICGA;
import lib.base.Mutator;
import lib.individuals.base.IIndividual;
import lib.models.GenotypePair;
import solution.ResultRecorder;
import tools.Tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Michał Horodelski
 * @version 1.0
 */
public class CGA implements ICGA {
    public static final Double DEFAULT_CROSSING_PROBABILITY = 0.5;
    public static final Double DEFAULT_MUTATION_PROBABILITY = 0.15;
    public static final Double DEFAULT_MAX_DIFFERENCE = 100.0;
    public static final Double DEFAULT_COEFFICIENT_POPULATION_CHECK = 0.5;

    private IIndividual[] _nextPopulation;
    private int _populationSize;
    private Mutator _mutationOperator;
    private Crosser _crossingOperator;

    private double _maxDifference; // wartość mocno zależna od rozwiązywanego problemu optymalizacji
    private double _coefficient;
    private double _mutationProbability;
    private double _crossProbability;
    private Random _random;


    private final ResultRecorder resultRecorder = new ResultRecorder();

    public CGA(IIndividual[] initialPopulation, Mutator mutator, Crosser crosser, double maximalDifference, double populationCheckCeofficient, double mutationProbability, double crossProbability) {
        _nextPopulation = initialPopulation;
        _populationSize = _nextPopulation.length;
        _mutationOperator = mutator;
        _crossingOperator = crosser;
        _maxDifference = maximalDifference >= 0.0
                ? maximalDifference
                : DEFAULT_MAX_DIFFERENCE;
        _coefficient = populationCheckCeofficient >= 0.0 && populationCheckCeofficient <= 1.0
                ? populationCheckCeofficient
                : DEFAULT_COEFFICIENT_POPULATION_CHECK;
        _mutationProbability = mutationProbability >= 0.0 && mutationProbability <= 1.0
                ? mutationProbability
                : DEFAULT_MUTATION_PROBABILITY;
        _crossProbability = crossProbability >= 0.0 && crossProbability <= 1.0
                ? crossProbability
                : DEFAULT_CROSSING_PROBABILITY;
        _random = new Random();

        //ewentualne sortowanie:
        //Arrays.sort(_nextPopulation,IIndividual.compare);

        resultRecorder.recordParameters(_populationSize, _maxDifference, _coefficient, _mutationProbability, _crossProbability);
    }

    public void generateNextPopulation(){

        List<IIndividual> newPopulationList = new ArrayList<>();

        IIndividual individualA;
        IIndividual individualB;
        int pairStep = this._populationSize / 2;


        for (int i = 0; i < pairStep; i++) {

            individualA = drawNextIndividualRoulette();
            individualB = drawNextIndividualRoulette();

            if( _random.nextDouble() < _crossProbability ){
                //do crossing
                var resultingPair = this._crossingOperator.crossingOpeator(
                        new GenotypePair(individualA.getGenotype(),individualB.getGenotype())
                );
                individualA.setGenotypeUpdateAll(resultingPair.getGenotypeA());
                individualB.setGenotypeUpdateAll(resultingPair.getGenotypeB());
            }

            if( _random.nextDouble() < _mutationProbability ) {
                // mutation for A
                individualA.setGenotypeUpdateAll(
                        _mutationOperator.mutator( individualA.getGenotype(),individualA.getGenotypeLength() ) );
            }

            if( _random.nextDouble() < _mutationProbability) {
                // mutation for B
                individualA.setGenotypeUpdateAll(
                        _mutationOperator.mutator( individualB.getGenotype(),individualB.getGenotypeLength() ) );
            }

            newPopulationList.add(individualA);
            newPopulationList.add(individualB);
        }

        IIndividual[] resultingArray = Tool.CastListToArray(newPopulationList);
        _nextPopulation = resultingArray;
    }

    private IIndividual drawNextIndividualRoulette() {
        IIndividual chosenIndividual = null;
        Double sum = 0.0;
        for (var individual :
                _nextPopulation) {
            sum += individual.getFitnessValue();
        }
        Double drawnValue = _random.nextDouble()*sum;
        Double cumulation = 0.0;
        for (int i = 0; i < _populationSize; i++) {
            cumulation += _nextPopulation[i].getFitnessValue();
            if( drawnValue <= cumulation ){
                chosenIndividual = _nextPopulation[i].cloneIndividual();
                break;
            }
        }

        if(  chosenIndividual == null){
            chosenIndividual = _nextPopulation[_populationSize-1].cloneIndividual();
        }
        return chosenIndividual;
    }

    @Override
    public boolean doMainStep() {
        //1. Ocena populacji
        // możemy wyznaczyć dla każdego osobnika wartość

        //2. Decyzja
        if( isStopConditionSatisfied() )
            return false;

        //3. Wygenerowanie nowej populacji
        generateNextPopulation();

        //4. ewentualne zostowanie
        //Arrays.sort(_nextPopulation,IIndividual.compare);

        resultRecorder.recordGeneration(numberOfIndividualsConditionSatisfied(), _nextPopulation);

        return true;
    }

    @Override
    public IIndividual[] getPopulation() {
        return _nextPopulation;
    }

    @Override
    public IIndividual[] findBest(){

        List<IIndividual> iIndividuals = new ArrayList<>();

        double bestValue = _nextPopulation[0].getFitnessValue();
        iIndividuals.add(_nextPopulation[0]);

        for (int i = 0; i < _populationSize; i++) {
            if( _nextPopulation[i].getFitnessValue() == bestValue ) {
                iIndividuals.add(_nextPopulation[i]);
            } else if( _nextPopulation[i].getFitnessValue() > bestValue ){
                bestValue = _nextPopulation[i].getFitnessValue();
                iIndividuals.clear();
                iIndividuals.add(_nextPopulation[i]);
            }
        }
        IIndividual[] result = Tool.CastListToArray(iIndividuals);
        resultRecorder.recordBest(result[0]);

        return result;
    }

    /**
     * Metoda sprawdza czy 50% ( gdy ceofficient == 0.5 ) osobników z populacji ma następującą własność:
     * | a - b | <= maxDifference
     * gdzie a to najlepsza znaleziona wartość, b to wartość osobnika.
     * @return true - gdy warunek stopu zachodzi, oraz false w przeciwnym razie
     */
    @Override
    public boolean isStopConditionSatisfied() {
        int partToCheck = (int) (_populationSize * _coefficient);
        var result = numberOfIndividualsConditionSatisfied();
        return result >= partToCheck;
    }

    @Override
    public int numberOfIndividualsConditionSatisfied() {
        int numberOfPositiveIndividuals = 0;
        double bestValue = findBest()[0].getFitnessValue();

        for (int i = 0; i < _populationSize; i++) {
            if( Math.abs(bestValue - _nextPopulation[i].getFitnessValue()) < _maxDifference ) {
                numberOfPositiveIndividuals++;
            }
        }

        return numberOfPositiveIndividuals;
    }

    public ResultRecorder getResultRecorder() {
        return resultRecorder;
    }
}

