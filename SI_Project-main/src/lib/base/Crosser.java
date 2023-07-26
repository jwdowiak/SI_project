package lib.base;

import lib.models.GenotypePair;

@FunctionalInterface
public interface Crosser {
    public GenotypePair crossingOpeator(GenotypePair pair);
}
