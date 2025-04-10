package hydraulic;

import java.util.ArrayList;

/**
 * Hydraulics system builder providing a fluent API
 */
public class HBuilder {

    private HSystem system = new HSystem();
    private Element next;
    private ArrayList<Element> previous = new ArrayList<>(); 
    private ArrayList<Integer> indexes = new ArrayList<>();

    /**
     * Add a source element with the given name
     * 
     * @param name name of the source element to be added
     * @return the builder itself for chaining 
     */
    public HBuilder addSource(String name) {
        previous.add(new Source(name));
        system.addElement(previous.get(0));
        return this;
    }

    /**
     * returns the hydraulic system built with the previous operations
     * 
     * @return the hydraulic system
     */
    public HSystem complete() {
        return system;
    }

    /**
     * creates a new tap and links it to the previous element
     * 
     * @param name name of the tap
     * @return the builder itself for chaining 
     */
    public HBuilder linkToTap(String name) {
        Element prev = previous.get(previous.size()-1);

        next = new Tap(name);
        system.addElement(next);
        if (!(prev instanceof Split)) {
            prev.connect(next);
            previous.remove(previous.size()-1);
        } else {
            prev.connect(next, indexes.get(indexes.size()-1));
        }
        previous.add(next);

        return this;
    }

    /**
     * creates a sink and link it to the previous element
     * @param name name of the sink
     * @return the builder itself for chaining 
     */
    public HBuilder linkToSink(String name) {
        Element prev = previous.get(previous.size()-1);

        next = new Sink(name);
        system.addElement(next);
        if (!(prev instanceof Split)) {
            prev.connect(next);
            previous.remove(previous.size()-1);
        } else {
            prev.connect(next, indexes.get(indexes.size()-1));
        }
        previous.add(next);

        return this;
    }

    /**
     * creates a split and links it to the previous element
     * @param name of the split
     * @return the builder itself for chaining 
     */
    public HBuilder linkToSplit(String name) {
        Element prev = previous.get(previous.size()-1);
        
        next = new Split(name);
        system.addElement(next);
        if (!(prev instanceof Split)) {
            prev.connect(next);
            previous.remove(previous.size()-1);
        } else {
            prev.connect(next, indexes.get(indexes.size()-1));
        }
        previous.add(next);

        return this;
    }

    /**
     * creates a multisplit and links it to the previous element
     * @param name name of the multisplit
     * @param numOutput the number of output of the multisplit
     * @return the builder itself for chaining 
     */
    public HBuilder linkToMultisplit(String name, int numOutput) {
        Element prev = previous.get(previous.size()-1);
        
        next = new Multisplit(name, numOutput);
        system.addElement(next);
        if (!(prev instanceof Split)) {
            prev.connect(next);
            previous.remove(previous.size()-1);
        } else {
            prev.connect(next, indexes.get(indexes.size()-1));
        }
        previous.add(next);

        return this;
    }

    /**
     * introduces the elements connected to the first output 
     * of the latest split/multisplit.
     * The element connected to the following outputs are 
     * introduced by {@link #then()}.
     * 
     * @return the builder itself for chaining 
     */
    public HBuilder withOutputs() {
        indexes.add(0);
        return this;
    }

    /**
     * inform the builder that the next element will be
     * linked to the successive output of the previous split or multisplit.
     * The element connected to the first output is
     * introduced by {@link #withOutputs()}.
     * 
     * @return the builder itself for chaining 
     */
    public HBuilder then() {
        Element prev = previous.get(previous.size()-1);
        int i = indexes.size()-1;
        indexes.set(i, indexes.get(i)+1);
        while(!(prev instanceof Split)) {
            previous.remove(previous.size()-1);
            prev = previous.get(previous.size()-1);
        }
        return this;
    }

    /**
     * completes the definition of elements connected
     * to outputs of a split/multisplit. 
     * 
     * @return the builder itself for chaining 
     */
    public HBuilder done() {
        Element prev = previous.get(previous.size()-1);

        indexes.remove(indexes.size()-1);
        while(!(prev instanceof Split)) {
            previous.remove(previous.size()-1);
            prev = previous.get(previous.size()-1);
        }
        previous.remove(previous.size()-1);
        return this;
    }

    /**
     * define the flow of the previous source
     * 
     * @param flow flow used in the simulation
     * @return the builder itself for chaining 
     */
    public HBuilder withFlow(double flow) {
        Element prev = previous.get(previous.size()-1);

        prev.setFlow(flow);
        return this;
    }

    /**
     * define the status of a tap as open,
     * it will be used in the simulation
     * 
     * @return the builder itself for chaining 
     */
    public HBuilder open() {
        Element prev = previous.get(previous.size()-1);

        ((Tap)prev).setOpen(true);
        return this;
    }

    /**
     * define the status of a tap as closed,
     * it will be used in the simulation
     * 
     * @return the builder itself for chaining 
     */
    public HBuilder closed() {
        Element prev = previous.get(previous.size()-1);

        ((Tap)prev).setOpen(false);
        return this;
    }

    /**
     * define the proportions of input flow distributed
     * to each output of the preceding a multisplit
     * 
     * @param props the proportions
     * @return the builder itself for chaining 
     */
    public HBuilder withPropotions(double[] props) {
        Element prev = previous.get(previous.size()-1);

        ((Multisplit)prev).setProportions(props);
        return this;
    }

    /**
     * define the maximum flow theshold for the previous element
     * 
     * @param max flow threshold
     * @return the builder itself for chaining 
     */
    public HBuilder maxFlow(double max) {
        Element prev = previous.get(previous.size()-1);
        
        prev.setMaxFlow(max);
        return this;
    }
}
