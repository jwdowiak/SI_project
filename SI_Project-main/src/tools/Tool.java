package tools;

import lib.individuals.base.IIndividual;

import java.util.List;

public class Tool {
    public static <T> T[] CastListToArray(List<T> list) {
        T[] array = (T[]) new IIndividual[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }
}
