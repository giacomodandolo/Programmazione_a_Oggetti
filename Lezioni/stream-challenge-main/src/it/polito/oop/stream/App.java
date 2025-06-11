package it.polito.oop.stream;

import java.util.function.IntSupplier;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ){
        


    }

    private static class FibonacciSupplier implements IntSupplier {
        
        int current = 1;
        int previous = 0;

        @Override
        public int getAsInt() {
            int tmp = previous;
            previous = current;
            current = current + tmp;
            return current;
        }

    }

    public static Stream<Integer> fibonacci() {
        Stream<Integer> stream = Stream
            .iterate(
                new Integer[] {0, 1}, 
                p -> new Integer[] {p[1], p[0] + p[1]}
            )
            .map(p -> p[1]);    // ho solo bisogno del secondo elemento 
                                // generato per ogni coppia (identifica 
                                // l'effettivo n-esimo numero di Fibonacci)
        
        return stream;
    }
}
