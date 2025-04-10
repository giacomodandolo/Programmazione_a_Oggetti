package hydraulic;

/**
 * Represents a multisplit element, an extension of the Split that allows many outputs
 * 
 * During the simulation each downstream element will
 * receive a stream that is determined by the proportions.
 */

public class Multisplit extends Split {

	private double[] outFlowProportions;

	/**
	 * Constructor
	 * @param name the name of the multi-split element
	 * @param numOutput the number of outputs
	 */
	public Multisplit(String name, int numOutput) {
		super(name);
		outFlowProportions = new double[numOutput];
		upstream = null;
		downstream = new Element[numOutput];
		numberDownstream = 0;
	}
	
	@Override
	public void setFlow(double flow) {
		flows[INPUT] = flow;
	}

	@Override
	public double getFlow() {
		return flows[INPUT];
	}

	public double getOutFlow(int index) {
		return flows[INPUT] * outFlowProportions[index];
	}

	@Override
	public void connect(Element elem, int index){
		downstream[index] = elem;
		numberDownstream++;
		elem.setFlow(getOutFlow(index));
		elem.setUpstream(this);
	}

	@Override
	public void print(SimulationObserver observer) {
		double[] outFlows = new double[numberDownstream];
		int i = 0;

		while (i < numberDownstream)
			outFlows[i] = getOutFlow(i++);
		
		observer.notifyFlow("Split", getName(), flows[INPUT], outFlows);
	}

	/**
	 * Define the proportion of the output flows w.r.t. the input flow.
	 * 
	 * The sum of the proportions should be 1.0 and 
	 * the number of proportions should be equals to the number of outputs.
	 * Otherwise a check would detect an error.
	 * 
	 * @param proportions the proportions of flow for each output
	 */
	public void setProportions(double... proportions) {
		int i = 0;

		for (double p : proportions)
			outFlowProportions[i++] = p;
	}
	
}
