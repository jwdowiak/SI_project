package lib.individuals;

import lib.individuals.base.IIndividual;
import tools.FitnessFunction;

import java.util.Objects;
import java.util.function.Function;

public class Coordinates implements IIndividual {
    private String _genotype;
    private Integer _fenotype;
    private Function<Double, Double> _fitnessFunction;

    protected Coordinates(Integer x, Integer y, Function<Double, Double> fitnessFunction) {
        _fitnessFunction = fitnessFunction;
        setGenotype(Integer.toBinaryString(x), Integer.toBinaryString(y));
        setGenotypeUpdateAll(_genotype);
    }

    private Coordinates(String genotype) {
        _fitnessFunction = FitnessFunction::populationLevelFunction;
        _genotype = genotype;
        setGenotypeUpdateAll(_genotype);
    }


    /**
     * Metoda zwraca genotyp dla obiektu
     * Genotyp jest zakodowany jako 20-bitowe słowo.
     * 10 pierwszych bitów reprezentuje koordynatę x, kolejne 10 koordynatę y
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
        _genotype = genotype;
        _fenotype = Integer.parseInt(_genotype,2);

        return true;
    }

    public boolean setGenotype(String genotype_x, String genotype_y) {
        if(genotype_x == null || genotype_y == null)
            return false;

        if(!genotype_x.matches("[01]*") || genotype_x.length() > 10 )
            return false;

        if(!genotype_y.matches("[01]*") || genotype_y.length() > 10 )
            return false;

        String newGeno_x = genotype_x.length() == 10 ? genotype_x : "0".repeat(10- genotype_x.length()) + genotype_x;
        String newGeno_y = genotype_y.length() == 10 ? genotype_y : "0".repeat(10- genotype_y.length()) + genotype_y;

        _genotype = newGeno_x + newGeno_y;

        return true;
    }

    @Override
    public int getId() {
        return this.hashCode();
    }

    @Override
    public Double getFitnessValue() {

        return this._fitnessFunction.apply(((double)_fenotype));

    }

    @Override
    public IIndividual cloneIndividual() {

        int x = _fenotype.intValue() / 1024;
        int y = _fenotype.intValue() % 1024;
        return create(x, y);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("G: ").append(_genotype)
                .append(", F: ").append(_fenotype)
                .append(", Fitness: ").append(getFitnessValue())
                .append(", ID: ").append(getId());
        return builder.toString();
    }

    public String toStringWithCoordinates() {
        int x = _fenotype.intValue() / 1024;
        int y = _fenotype.intValue() % 1024;

        return new StringBuilder("x: ").append(x).append(", y: ").append(y).toString();
    }

    /**
     * Metoda tworząca obiekt na podstawie koordynatów x i y
     * @param x, y
     * @return
     */
    public static Coordinates create(Integer x, Integer y){
        return new Coordinates(x, y, FitnessFunction::populationLevelFunction);
    }

    public static Coordinates getCoordinates(String genotype) {
        return new Coordinates(genotype);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Objects.equals(_genotype, that._genotype) && Objects.equals(_fenotype, that._fenotype) && Objects.equals(_fitnessFunction, that._fitnessFunction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_genotype, _fenotype, _fitnessFunction);
    }


}
