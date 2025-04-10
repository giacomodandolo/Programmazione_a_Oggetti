package hydraulic;

/**
 * Represents a split element, a.k.a. T element
 * 
 * During the simulation each downstream element will
 * receive a stream that is half the input stream of the split.
 */

public class Split extends Element {

	protected double[] flows;
	protected Element upstream;
	protected Element[] downstream;
	protected int numberDownstream;
	private final double EQUAL_FLOW = 0.5;
	protected double maxFlow;
	
	/**
	 * Constructor
	 * @param name name of the split element
	 */
	public Split(String name) {
		super.name = name;
		flows = new double[] {
			SimulationObserver.NO_FLOW,
			SimulationObserver.NO_FLOW
		};
		upstream = null;
		downstream = new Element[] {
			null,
			null
		};
		numberDownstream = 0;
		maxFlow = 0.0;
	}

	@Override
	public void setFlow(double flow) {
		flows[INPUT] = flow;
		flows[OUTPUT] = flow*EQUAL_FLOW;
	}

	@Override
	public double getFlow() {
		return flows[INPUT];
	}

	@Override
	public double getOutFlow() {
		return flows[OUTPUT];
	}

	@Override
	public void setUpstream(Element elem) {
		upstream = elem;
	}

	public void setDownstream(Element elem, int index) {
		downstream[index] = elem;
		if (elem == null)
			numberDownstream--;
	}

	@Override
	public void connect(Element elem, int index){
		downstream[index] = elem;
		numberDownstream++;
		elem.setFlow(getFlow()*EQUAL_FLOW);
		elem.setUpstream(this);
	}
	
	@Override
	public Element[] getOutputs(){
		Element[] outputs = new Element[downstream.length];
		int i = 0;

		if (numberDownstream == 0)
			return null;

		for (Element e : downstream)
			outputs[i++] = e;

		return outputs;
	}

	public int getIndex(Element elem) {
		int i = 0;

		for (Element e : downstream) {
			if (e != null && e.equals(elem))
				return i;
			i++;
		}

		return -1;
	}

	@Override
	public void setTreeFlow() {
		if (upstream instanceof Multisplit ms) 
			setFlow(ms.getOutFlow(ms.getIndex(this)));
		else
			setFlow(upstream.getOutFlow());

		for (Element e : downstream)
			if (e != null)
				e.setTreeFlow();
	}

	@Override
	public void print(SimulationObserver observer) {
		double[] outFlows = new double[]{
			flows[OUTPUT],
			flows[OUTPUT]
		};
		
		observer.notifyFlow("Split", getName(), flows[INPUT], outFlows);
	}

	@Override
	public void printError(SimulationObserver observer) {
		observer.notifyFlowError("Split", getName(), flows[INPUT], maxFlow);
	}

	@Override
	public boolean delete() {
		Element downstreamElement = null;
		int i = 0;

		if (numberDownstream > 1)
			return false;
			
		for (Element e : downstream) {
			if (e != null) {
				downstreamElement = e;
				break;
			}
			i++;
		}

		if (downstreamElement != null) {
			if (upstream != null)
				upstream.connect(downstreamElement);
			downstreamElement.setUpstream(upstream);
		} else 
			upstream.setDownstream(null);
		
		if (i < numberDownstream)
			downstream[i] = null;
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
