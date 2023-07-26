package tools;

import java.util.Random;

public class Mutation {

    public static Integer generateMutation(Integer fenotype) {
        throw new RuntimeException();
    }

    public static Byte generateMutation(Byte fenotype) {

        throw new RuntimeException();
    }

    public static String generateMutation8(String genotype) {
        return generateMutation(genotype, 8);
    }

    public static String generateMutation32(String genotype) {
        return generateMutation(genotype, 32);
    }

    public static String generateMutation(String genotype, int length) {
        if (genotype == null || genotype.length() != length)
            return null;
        int locus = new Random().nextInt(length);
        StringBuilder sb = new StringBuilder(genotype);
        sb.setCharAt(locus, sb.charAt(locus)== '0'?'1':'0');
            return sb.toString();
    }
}
