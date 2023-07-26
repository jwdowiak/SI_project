package lib.base;

import lib.individuals.base.IIndividual;
import solution.ResultRecorder;

public interface ICGA {
    IIndividual[] findBest();
    boolean isStopConditionSatisfied();
    int numberOfIndividualsConditionSatisfied();
    void generateNextPopulation();
    boolean doMainStep();
    IIndividual[] getPopulation();

    public ResultRecorder getResultRecorder();
}
