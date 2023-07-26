package tools;

import java.util.function.Function;

public class FitnessFunction {
    public static Double krakowskieFunction(Double x) {
        return x*x*x - 420*x*x + 43700*x + 100000;
    }


    public static Double populationLevelFunction(Double fenotype) {

        int x = fenotype.intValue() / 1024;
        int y = fenotype.intValue() % 1024;

        return (-((475-x)*(475-x))-((500-y)*(500-y))+600_000)*1.0;
    }
}
