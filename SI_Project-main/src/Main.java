import solution.ResultLogger;
import solution.Solution;

public class Main {
    public static void main(String[] args) {

        try{
            Solution.run();
        }catch(RuntimeException ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }catch( Exception ex) {
            System.out.println("Wyjątek! " + ex.toString());
        }

    }
}