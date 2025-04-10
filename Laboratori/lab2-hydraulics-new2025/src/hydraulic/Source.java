package hydraulic;

/**
 * Represents a source of water, i.e. the initial element for the simulation.
 *
 * Lo status of the source is defined through the method
 * {@link #setFlow(double) setFlow()}.
 */
public class Source extends Element {

	private double[] flows;
	private Element downstream;

	/**
	 * constructor
	 * @param name name of the source element
	 */
	public Source(String name) {
		super.name = name;
		flows = new double[]{	
			SimulationObserver.NO_FLOW, 
			SimulationObserver.NO_FLOW
		};
		downstream = null;
	}

	/**
	 * Define the flow of the source to be used during the simulation
	 *
	 * @param flow flow of the source (in cubic meters per hour)
	 */
	@Override
	public void setFlow(double flow){
		
		flows[OUTPUT] = flow;
	}

	@Override
	public double getFlow(){
		
		return flows[OUTPUT];
	}

	@Override
	public double getOutFlow() {
		return flows[OUTPUT];
	}

	@Override
	public void connect(Element elem) {
		downstream = elem;
		elem.setFlow(getFlow());
		elem.setUpstream(this);
		setTreeFlow();
	}

	@Override
	public Element getDownstream() {
		return downstream;
	}

	@Override
	public void setTreeFlow() {
		if (downstream != null)
			downstream.setTreeFlow();
	}

	@Override
	public void print(SimulationObserver observer) {
		observer.notifyFlow("Source", getName(), flows[INPUT], flows[OUTPUT]);
	}
	
	@Override
	public void printError(SimulationObserver observer) {
		
	}
	
	@Override
	public boolean delete() {
		double tmp = flows[OUTPUT];
		
		flows[OUTPUT] = SimulationObserver.NO_FLOW;
		setTreeFlow();
		flows[OUTPUT] = tmp;

		downstream.setUpstream(null);

		return true;
	}

}
