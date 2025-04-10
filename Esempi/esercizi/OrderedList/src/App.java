
import java.util.Random;

public class App {
    static int MAX_VALUES = 20;
    Random rand = new Random();

    public static void main(String[] args) throws Exception {
        int i;
        int[] added_values = {100, 67, 932, 1023, 193, 14, 23, 10293}, popped_values;
        OrderedList list;
        
        list = new OrderedList();
        list.add(added_values);
        list.print_list();

        popped_values = new int[5];
        popped_values = list.pop(popped_values.length);
        System.out.println("Lista dei valori rimossi: ");
        for (i = 0; i < popped_values.length; i++)
            System.out.print(popped_values[i] + " ");
        System.out.println(); 
        System.out.println();

        list.print_list();
    }
}
