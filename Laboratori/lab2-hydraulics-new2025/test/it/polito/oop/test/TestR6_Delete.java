package it.polito.oop.test;

import hydraulic.*;

import static org.junit.Assert.*;
import static it.polito.oop.test.OOPAssertions.*;

import org.junit.Test;


public class TestR6_Delete {

	@Test
	public void testSimpleElementRemove(){
		HSystem s = new HSystem();
		Source src = new Source("Src");		
		Tap tap = new Tap("Tap");		
		Sink sink = new Sink("Sink");		
		s.addElement(src);
		s.addElement(tap);
		s.addElement(sink);
		
		src.connect(tap);
		tap.connect(sink);
		
		boolean done = s.deleteElement("Tap");		

		assertEquals("Wrong number of elements after delete",2, s.size());
		assertTrue("Operation not performed", done);
	}
	
	@Test
	public void testSimpleElementsRelink(){
		HSystem s = new HSystem();
		Source src = new Source("Src");		
		Tap tap = new Tap("Tap");		
		Sink sink = new Sink("Sink");		
		s.addElement(src);
		s.addElement(tap);
		s.addElement(sink);
		
		src.connect(tap);
		tap.connect(sink);
		
		System.out.println(s);
		boolean done = s.deleteElement("Tap");
		System.out.println(s);

		assertTrue("Operation not performed", done);
		assertSameElement("Output not fixed after delete",sink,src.getOutput());
	}

	@Test
	public void testSinkRelink(){
		HSystem s = new HSystem();
		Source src = new Source("Src");		
		Tap tap = new Tap("Tap");		
		Sink sink = new Sink("Sink");		
		s.addElement(src);
		s.addElement(tap);
		s.addElement(sink);
		
		src.connect(tap);
		tap.connect(sink);
		
		boolean done = s.deleteElement("Sink");

		assertTrue("Operation not performed", done);
		assertSameElement("Output not fixed after delete", null, tap.getOutput());
	}

	@Test
	public void testSinkAfterSplit(){
		HSystem s = new HSystem();
		Source src = new Source("Src");
		Tap tap = new Tap("Tap");
		Split t = new Split("T");
		Element sink1 = new Sink("Sink 1");		
		Element sink2 = new Sink("Sink 2");		
		s.addElement(src);
		s.addElement(tap);
		s.addElement(t);
		s.addElement(sink1);
		s.addElement(sink2);
		
		src.connect(tap);
		tap.connect(t);
		t.connect(sink1,0);
		t.connect(sink2,1);
		
		System.out.println(s);
		boolean done = s.deleteElement("Sink 2");
		System.out.println(s);

		
		assertTrue("Operation should be performed", done);
		assertEquals("Wrong number of elements after delete",4,s.size());
		assertSameElement("Output not fixed after delete", sink1, t.getOutputs()[0]);
		assertSameElement("Output not fixed after delete", null, t.getOutputs()[1]);
	}

	@Test
	public void testWithSplit(){
		HSystem s = new HSystem();
		Source src = new Source("Src");
		Tap tap = new Tap("Tap");
		Split t = new Split("T");
		Element sink1 = new Sink("Sink 1");		
		Element sink2 = new Sink("Sink 2");		
		s.addElement(src);
		s.addElement(tap);
		s.addElement(t);
		s.addElement(sink1);
		s.addElement(sink2);
		
		src.connect(tap);
		tap.connect(t);
		t.connect(sink1,0);
		t.connect(sink2,1);
		
		boolean done = s.deleteElement("T");

		assertFalse("Operation should not be performed on a connected Split", done);
		assertEquals("Wrong number of elements after denied delete", 5, s.size());
	}
	
	@Test
	public void testWithSplitUnconnected(){
		HSystem s = new HSystem();
		Source src = new Source("Src");
		Tap tap = new Tap("Tap");
		Split t = new Split("T");
		Element sink1 = new Sink("Sink 1");		
		s.addElement(src);
		s.addElement(tap);
		s.addElement(t);
		s.addElement(sink1);
		
		src.connect(tap);
		tap.connect(t);
		t.connect(sink1,0); // only one output connected!
		
		boolean done = s.deleteElement("T");
		
		assertTrue("Operation should be permitted on a single-connected Split", done);
		assertEquals("Wrong number of elements after delete", 3, s.size());
		assertSameElement("Output not fixed after delete", sink1, tap.getOutput());

	}
	
	@Test
	public void testMultipleDeletes(){
		HSystem s = new HSystem();
		Source src = new Source("Src");
		Tap tap = new Tap("Tap");
		Split t = new Split("T");
		Element sink1 = new Sink("Sink 1");		
		Element sink2 = new Sink("Sink 2");		
		s.addElement(src);
		s.addElement(tap);
		s.addElement(t);
		s.addElement(sink1);
		s.addElement(sink2);
		
		src.connect(tap);
		tap.connect(t);
		t.connect(sink1,0);
		t.connect(sink2,1);
		
		boolean done = s.deleteElement("Sink 1");
		assertTrue("Operation should be permitted!", done);
		
		
		done = s.deleteElement("Sink 2");
		assertTrue("Operation should be permitted!", done);
		
		
		done = s.deleteElement("T");

		assertTrue("Operation should be permitted!", done);
		
		assertEquals("Wrong number of elements after multiple deletes", 2, s.size());
	}
}
