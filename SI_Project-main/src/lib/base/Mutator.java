package lib.base;

import lib.models.GenotypePair;

@FunctionalInterface
public interface Mutator {
    public String mutator(String genotype, Integer size);
}