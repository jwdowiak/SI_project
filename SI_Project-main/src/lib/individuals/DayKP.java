package lib.individuals;

import lib.individuals.base.IIndividual;
import tools.FitnessFunction;

import java.util.Objects;
import java.util.function.Function;

public class DayKP implements IIndividual {
    private String _genotype;
    private Integer _fenotype;
    private Function<Double, Double> _fitnessFunction;

    protected DayKP(Integer day, Function<Double, Double> fitnessFunction) {
        _fitnessFunction = fitnessFunction;
        var result = Integer.toBinaryString(day);
        setGenotypeUpdateAll(result);
    }

    /**
     * Metoda zwraca genotyp dla obiektu
     * Genotyp jest zakodowany jako 8-bitowe słowo.
     * @return
     */
    @Override
    public String getGenotype() {
        return _genotype;
    }

    @Override
    public int getGenotypeLength() {
        return _genotype.length();
    }

    @Override
    public boolean setGenotypeUpdateAll(String genotype) {
        if(genotype == null)
            return false;

        if(!genotype.matches("[01]*") || genotype.length() > 8 )
            return false;

        String newGeno = genotype.length() == 8 ? genotype : "0".repeat(8- genotype.length()) + genotype;

        _genotype = newGeno;

        _fenotype = Integer.parseInt(_genotype,2);

        return true;
    }

    @Override
    public int getId() {
        return this.hashCode();
    }

    @Override
    public Double getFitnessValue() {

        Double argument = (double) _fenotype;

        Double result = this._fitnessFunction.apply(argument);

        return result;
    }

    @Override
    public IIndividual cloneIndividual() {
        return create(this._fenotype);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("G: ").append(_genotype)
                .append(", F: ").append(_fenotype)
                .append(", Fittness: ").append(getFitnessValue())
                .append(", ID: ").append(getId());
        return builder.toString();
    }

    /**
     * Metoda przeznaczona do tworzenia instancji osobnika będącego dniem na Krakowskim przedmieściu
     * day in [0,255]
     * @param day
     * @return
     */
    public static DayKP create(Integer day){
        return new DayKP(day, FitnessFunction::krakowskieFunction);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DayKP dayKP = (DayKP) o;
        return Objects.equals(_genotype, dayKP._genotype) && Objects.equals(_fenotype, dayKP._fenotype) && Objects.equals(_fitnessFunction, dayKP._fitnessFunction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_genotype, _fenotype, _fitnessFunction);
    }
}