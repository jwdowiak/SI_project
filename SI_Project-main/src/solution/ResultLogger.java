package solution;

import lib.individuals.Coordinates;
import lib.individuals.base.IIndividual;

import java.io.*;
import java.util.List;

public class ResultLogger {
    private File log;

    public ResultLogger(String log) {
        this.log = new File(log);
    }

    public void noteResult(String result) {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(log, true))) {
            bufferedWriter.newLine();
            bufferedWriter.write(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int getRecordAmount() {
        int numberOfRecords = 0;
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(log))) {
            numberOfRecords = (int) bufferedReader.lines().filter(line -> line.equals("{")).count();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return numberOfRecords;
    }

    private List<Double> getFitnessValues() {
        List<Double> fitnessValues;
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(log))) {
            fitnessValues = bufferedReader.lines()
                    .filter(line -> line.contains("Fitness"))
                    .map(line -> {
                        String[] array = line.split(",");
                        String fitness = array[2];
                        return fitness.substring(10);
                    })
                    .map(value -> Double.parseDouble(value))
                    .toList();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fitnessValues;
    }

    public Double getBestFitnessValue() {
        return getFitnessValues().stream().mapToDouble(value -> value).max().orElseThrow();
    }

    public List<IIndividual> getBestIndividuals() {
        List<IIndividual> bestIndividuals;
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(log))) {
            bestIndividuals = bufferedReader.lines()
                    .filter(line -> line.contains("Best individual"))
                    .filter(line -> line.contains(getBestFitnessValue().toString()))
                    .map(line -> line.substring(16))
                    .map(line -> {
                        String[] fields = line.split(",");
                        return (IIndividual)Coordinates.getCoordinates(fields[0].substring(4));
                    })
                    .toList();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bestIndividuals;
    }

    public void printAsCoordinates(List<IIndividual> individuals) {
        individuals.stream()
                .distinct()
                .map(individual -> ((Coordinates)individual).toStringWithCoordinates())
                .forEach(System.out::println);
    }
}
