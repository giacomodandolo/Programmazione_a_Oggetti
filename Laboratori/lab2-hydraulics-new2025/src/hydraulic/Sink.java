package hydraulic;

/**
 * Represents the sink, i.e. the terminal element of a system
 *
 */
public class Sink extends Element {

	private double[] flows;
	private Element upstream;
	private double maxFlow;

	/**
	 * Constructor
	 * @param name name of the sink element
	 */
	public Sink(String name) {
		super.name = name;
		flows = new double[]{	
			SimulationObserver.NO_FLOW, 
			SimulationObserver.NO_FLOW
		};
		upstream = null;
		maxFlow = 0.0;
	}

	@Override
	public void setFlow(double flow) {
		flows[INPUT] = flow;
	}

	@Override
	public double getFlow() {
		return flows[INPUT];
	}

	@Override
	public void setUpstream(Element elem) {
		upstream = elem;
	}
	
	@Override
	public void setTreeFlow() {
		if (upstream instanceof Multisplit ms) 
			setFlow(ms.getOutFlow(ms.getIndex(this)));
		else
			setFlow(upstream.getOutFlow());
	}

	@Override
	public void print(SimulationObserver observer) {
		observer.notifyFlow("Sink", getName(), flows[INPUT], flows[OUTPUT]);
	}

	@Override
	public void printError(SimulationObserver observer) {
		observer.notifyFlowError("Sink", getName(), flows[INPUT], maxFlow);
	}

	
	@Override
	public boolean delete() {
		if (upstream != null) {
			if (upstream instanceof Split s)
				s.setDownstream(null, s.getIndex(this));
			else
				upstream.setDownstream(null);
		}
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
