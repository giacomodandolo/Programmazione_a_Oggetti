package it.polito.oop.stream.test;

import java.util.LinkedList;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import static it.polito.oop.stream.App.fibonacci;

public class TestApp {
    @Test
    public void testFibonacci(){
        
        Stream<Integer> fib = fibonacci();

        assertNotNull(fib);

        LinkedList<Integer> l = new LinkedList<>();

        fib.limit(5).forEach(l::add);

        assertEquals(5, l.size());
        assertEquals(1,l.get(0).intValue());
        assertEquals(1,l.get(1).intValue());
        assertEquals(2,l.get(2).intValue());
        assertEquals(3,l.get(3).intValue());
        assertEquals(5,l.get(4).intValue());

    }
}
