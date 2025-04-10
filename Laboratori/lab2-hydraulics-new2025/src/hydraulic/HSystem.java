package hydraulic;

import java.util.ArrayList;

/**
 * Main class that acts as a container of the elements for
 * the simulation of an hydraulics system 
 * 
 */
public class HSystem {

	ArrayList<Element> elements = new ArrayList<>();

// R1
	/**
	 * Adds a new element to the system
	 * 
	 * @param elem the new element to be added to the system
	 */
	public void addElement(Element elem){
		
		elements.add(elem);
	}

	/**
	 * returns the number of element currently present in the system
	 * 
	 * @return count of elements
	 */
	public int size() {
        
		return elements.size();
    }

	/**
	 * returns the element added so far to the system
	 * 
	 * @return an array of elements whose length is equal to 
	 * 							the number of added elements
	 */
	public Element[] getElements(){
		
		Element[] elemArray = new Element[size()];
		int i = 0;

		for (Element e : elements)
			elemArray[i++] = e;

		return elemArray;
	}

// R4
	/**
	 * starts the simulation of the system
	 * 
	 * The notification about the simulations are sent
	 * to an observer object
	 * 
	 * Before starting simulation the parameters of the
	 * elements of the system must be defined
	 * 
	 * @param observer the observer receiving notifications
	 */
	public void simulate(SimulationObserver observer){
		
		for (Element e : elements)
			if (e instanceof Source s)
				s.setTreeFlow();

		for (Element e : elements)
			e.print(observer);
	}


// R6
	/**
	 * Deletes a previously added element 
	 * with the given name from the system
	 */
	public boolean deleteElement(String name) {
		
		boolean done;
		Element elementToDelete = null;

		for (Element e : elements)
			if (name.equals(e.getName())) {
				elementToDelete = e;
				break;
			}

		
		done = elementToDelete.delete();
		if (done)
			elements.remove(elementToDelete);

		return done;
	}

// R7
	/**
	 * starts the simulation of the system; if {@code enableMaxFlowCheck} is {@code true},
	 * checks also the elements maximum flows against the input flow
	 * 
	 * If {@code enableMaxFlowCheck} is {@code false}  a normals simulation as
	 * the method {@link #simulate(SimulationObserver)} is performed
	 * 
	 * Before performing a checked simulation the max flows of the elements in thes
	 * system must be defined.
	 */
	public void simulate(SimulationObserver observer, boolean enableMaxFlowCheck) {
		
		for (Element e : elements)
			if (e instanceof Source s)
				s.setTreeFlow();

		for (Element e : elements) {
			e.print(observer);
			if (!(e instanceof Source) && enableMaxFlowCheck && !(e.maxFlowCheck()))
				e.printError(observer);
		}
	}

// R8
	/**
	 * creates a new builder that can be used to create a 
	 * hydraulic system through a fluent API 
	 * 
	 * @return the builder object
	 */
    public static HBuilder build() {
		return new HBuilder();
    }
}
