package it.polito.oop.test;

import hydraulic.*;
import static org.junit.Assert.*;
import org.junit.Test;


public class TestR7_MaxFlow {

	@Test
	public void testSimpleElements(){
		HSystem s = new HSystem();
		Source src = new Source("Src");		
		Tap tap = new Tap("Tap");		
		Sink sink = new Sink("Sink");		
		s.addElement(src);
		s.addElement(tap);
		s.addElement(sink);
		
		src.connect(tap);
		tap.connect(sink);
		
		double flow = 100.0;
		src.setFlow(flow);
		tap.setOpen(true);
		
		tap.setMaxFlow(90.0);
		sink.setMaxFlow(90.0);

		StoreObserver obs = new StoreObserver();

		s.simulate(obs,true);
		
		assertEquals("Wrong number of notifications",2,obs.getErrorCount());
		assertTrue("Missing simulation trace for element Tap",obs.containsError("Tap"));

		assertEquals("Wrong max flow of 'Tap'", 90.0, obs.maxFlowOf("Tap"), 0.01);
		assertEquals("Wrong max flow of 'Sink'", 90.0, obs.maxFlowOf("Sink"), 0.01);
	}

	@Test
	public void testSimpleElementsClosed(){
		HSystem s = new HSystem();
		Source src = new Source("Src");		
		Tap tap = new Tap("Tap");		
		Sink sink = new Sink("Sink");		
		s.addElement(src);
		s.addElement(tap);
		s.addElement(sink);
		
		src.connect(tap);
		tap.connect(sink);
		
		double flow = 100.0;
		src.setFlow(flow);
		tap.setOpen(false);

		tap.setMaxFlow(120.0);
		sink.setMaxFlow(90.0);
		
		StoreObserver obs = new StoreObserver();

		s.simulate(obs,true);
		
		assertEquals("Expected no error notifications", 0, obs.getErrorCount());
	}

	@Test
	public void testSplit(){
		HSystem s = new HSystem();
		Source src = new Source("Src");
		Split split = new Split("Split");
		Sink sink1 = new Sink("Sink1");
		Sink sink2 = new Sink("Sink2");
		s.addElement(src);
		s.addElement(split);
		s.addElement(sink1);
		s.addElement(sink2);
		
		src.connect(split);
		split.connect(sink1, 0);
		split.connect(sink2, 1);
		
		double flow = 100.0;
		src.setFlow(flow);
		split.setMaxFlow(90.0);
		sink1.setMaxFlow(90.0);
		sink2.setMaxFlow(90.0);
		
		StoreObserver obs = new StoreObserver();

		s.simulate(obs,true);
		
		assertEquals("Expected one error notifications", 1, obs.getErrorCount());

		assertTrue("There was no error notification for element Split",obs.containsError("Split"));

		assertEquals("Wrong max flow of 'Tap'", 90.0, obs.maxFlowOf("Split"), 0.01);
	}

	@Test
	public void testSplitOk(){
		HSystem s = new HSystem();
		Source src = new Source("Src");
		Split split = new Split("Split");
		Sink sink1 = new Sink("Sink1");
		Sink sink2 = new Sink("Sink2");
		s.addElement(src);
		s.addElement(split);
		s.addElement(sink1);
		s.addElement(sink2);
		
		src.connect(split);
		split.connect(sink1, 0);
		split.connect(sink2, 1);
		
		double flow = 50.0;
		src.setFlow(flow);
		split.setMaxFlow(90.0);
		sink1.setMaxFlow(90.0);
		sink2.setMaxFlow(90.0);
		
		StoreObserver obs = new StoreObserver();

		s.simulate(obs,true);
		
		assertEquals("Expected no error notifications", 0, obs.getErrorCount());
	}

	@Test
	public void testMultiSplit(){
		HSystem s = new HSystem();
		Source src = new Source("Src");
		Multisplit split = new Multisplit("Split",3);
		Sink sink1 = new Sink("Sink1");
		Sink sink2 = new Sink("Sink2");
		Sink sink3 = new Sink("Sink3");
		s.addElement(src);
		s.addElement(split);
		s.addElement(sink1);
		s.addElement(sink2);
		s.addElement(sink3);
		
		src.connect(split);
		split.connect(sink1, 0);
		split.connect(sink2, 1);
		split.connect(sink3, 2);
		
		double flow = 100.0;
		src.setFlow(flow);
		split.setProportions(0.5, 0.3, 0.2);
		split.setMaxFlow(90.0);
		sink1.setMaxFlow(40.0);
		sink2.setMaxFlow(40.0);
		sink3.setMaxFlow(40.0);
		
		StoreObserver obs = new StoreObserver();

		s.simulate(obs,true);
		
		assertEquals("Expected one error notifications" + obs.toString(), 2, obs.getErrorCount());

		assertTrue("There was no error notification for element Split",obs.containsError("Split"));
		assertTrue("There was no error notification for element Sink",obs.containsError("Sink1"));

		assertEquals("Wrong max flow of 'Split'", 90.0, obs.maxFlowOf("Split"), 0.01);
	}
}
