package hydraulic;

/**
 * Represents a tap that can interrupt the flow.
 * 
 * The status of the tap is defined by the method
 * {@link #setOpen(boolean) setOpen()}.
 */

public class Tap extends Element {
 
	private double[] flows;
	private Element upstream;
	private Element downstream;
	private boolean open;
    private double maxFlow;

	/**
	 * Constructor
	 * @param name name of the tap element
	 */
	public Tap(String name) {
		super.name = name;
		flows = new double[]{	
			SimulationObserver.NO_FLOW, 
			SimulationObserver.NO_FLOW
		};
		upstream = null;
		downstream = null;
		open = false;
		maxFlow = 0.0;
	}

	/**
	 * Set whether the tap is open or not. The status is used during the simulation.
	 *
	 * @param open opening status of the tap
	 */
	public void setOpen(boolean open){
		
		this.open = open;
		if (!open)
			flows[OUTPUT] = 0.0;
		else
			flows[OUTPUT] = flows[INPUT];
	}
	
	@Override
	public void setFlow(double flow) {
		flows[INPUT] = flow;
		flows[OUTPUT] = flow;
	}

	@Override
	public double getFlow() {
		return open ? flows[INPUT] : 0.0;
	}

	@Override
	public double getOutFlow() {
		return getFlow();
	}

	@Override
	public void connect(Element elem){
		downstream = elem;
		elem.setFlow(getFlow());
		elem.setUpstream(this);
	}

	@Override
	public void setUpstream(Element elem) {
		upstream = elem;
	}

	@Override
	public Element getUpstream() {
		return upstream;
	}

	@Override
	public void setDownstream(Element elem) {
		downstream = elem;
	}

	@Override
	public Element getDownstream() {
		return downstream;
	}

	@Override
	public void setTreeFlow() {
		if (upstream instanceof Multisplit ms) 
			setFlow(ms.getOutFlow(ms.getIndex(this)));
		else
			setFlow(upstream.getOutFlow());
		setOpen(open);

		if (downstream != null)
			downstream.setTreeFlow();
	}

	@Override
	public void print(SimulationObserver observer) {
		observer.notifyFlow("Tap", getName(), flows[INPUT], flows[OUTPUT]);
	}
	
	@Override
	public void printError(SimulationObserver observer) {
		observer.notifyFlowError("Tap", getName(), flows[INPUT], maxFlow);
	}
	
	@Override
	public boolean delete() {
		if (downstream != null) {
			if (upstream != null)
				upstream.connect(downstream);
			downstream.setUpstream(upstream);
		} else 
			upstream.setDownstream(null);
		downstream = null;
		upstream = null;

		return true;
	}

	@Override
	public void setMaxFlow(double maxFlow) {
		this.maxFlow = maxFlow;
	}

	@Override
	public boolean maxFlowCheck() {
		return maxFlow != 0 ? maxFlow >= flows[INPUT] : true;
	}

}
