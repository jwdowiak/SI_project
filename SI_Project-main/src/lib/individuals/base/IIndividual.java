package lib.individuals.base;

import java.util.Comparator;

public interface IIndividual {
    int getId();
    String getGenotype();
    int getGenotypeLength();
    boolean setGenotypeUpdateAll(String genotype);
    Double getFitnessValue();
    IIndividual cloneIndividual();

    public static Comparator<IIndividual> compare = (IIndividual a, IIndividual b) -> {
        if (a.getFitnessValue() < b.getFitnessValue())
            return 1;
        else if (a.getFitnessValue() > b.getFitnessValue())
            return -1;
        else
            return 0;
    };
}
