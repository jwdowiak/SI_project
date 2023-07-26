package tools;

import lib.models.GenotypePair;
public class Cross {
    public static GenotypePair crossingOperator(GenotypePair pair){
        if (pair == null) return null;
        String a = pair.getGenotypeA();
        String b = pair.getGenotypeB();

        if (a == null || b == null || a.length() != b.length())
            return null;

        int n = a.length();
        int locus = n / 2;

        var result1 = a.substring(0, locus) + b.substring(locus, n);
        var result2 = b.substring(0, locus) + a.substring(locus, n);

        return new GenotypePair(result1, result2);
    }

    }

