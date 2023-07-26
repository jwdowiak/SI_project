package lib.models;

public class GenotypePair {
    private String genotypeA;
    private String genotypeB;

    public GenotypePair(String genotypeA, String genotypeB) {
        this.genotypeA = genotypeA;
        this.genotypeB = genotypeB;
    }

    public String getGenotypeA() {
        return genotypeA;
    }

    public void setGenotypeA(String genotypeA) {
        this.genotypeA = genotypeA;
    }

    public String getGenotypeB() {
        return genotypeB;
    }

    public void setGenotypeB(String genotypeB) {
        this.genotypeB = genotypeB;
    }
}
