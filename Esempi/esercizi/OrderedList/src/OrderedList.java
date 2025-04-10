public class OrderedList {
    
    int[] list;
    int n;
    static int MAX_N = 100;
    static int MAX_ROW = 10;

    /* Constructor */
    public OrderedList() {
        this.list = new int[MAX_N];
        this.n = 0;
    }

    /* Print list */
    public void print_list() {
        int i;
        System.out.println("Lista attuale: ");
        for (i = 0; i < n; i++) {
            if (i%MAX_ROW == MAX_ROW-1)
                System.out.println(list[i]);
            else
                System.out.print(list[i] + " ");
        }
        System.out.println();
    }

    private void swap(int i, int j) {
        int tmp;

        tmp = list[i];
        list[i] = list[j];
        list[j] = tmp;
    }

    /* Add elements */
    public void add(int[] val) {
        int i, j, k;

        for (i = 0; i < val.length; i++) {
            list[n] = val[i];
            n++;
            for (j = 0; j < n-1; j++)
                if (val[i] < list[j]) {
                    for (k = n-1; k > j; k--)
                        swap(k, k-1);
                    break;
                }
            //print_list();
        }
    }


    /* Remove elements in last position */
    public int[] pop(int n) {
        int i;
        int[] popped;
        
        popped = new int[n];
        for (i = 0; i < n; i++) {
            this.n--;
            popped[i] = this.list[this.n];
        }

        return popped;
    }

}
