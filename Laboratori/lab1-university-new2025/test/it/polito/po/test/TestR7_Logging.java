package it.polito.po.test;

import static org.junit.Assert.*;

import java.util.logging.Level;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import university.University;

import static it.polito.po.test.TestR5_Exams.*;

public class TestR7_Logging {
	private University poli;
	
	private String logLastMessage;
	private int logCount;

	@Before
	public void setUp() {
		poli = new University("Politecnico di Torino");
		poli.setRector("Guido", "Saracco");
		
		University.logger.setFilter( rec -> {
			logLastMessage = rec.getMessage();
			logCount++;
			return true;
		});
		logLastMessage = "";
		logCount = 0;
		University.logger.setLevel(Level.ALL);
	}
	
	@After
	public void tearDown() {
		University.logger.setFilter(null);
	}
	
	@Test
	public void testLogging() {		
		poli.enroll("Mario","Rossi");	
		assertEquals("No log record for enroll",1,logCount);
		assertContained("Wrong log message","Rossi",logLastMessage);
		
		poli.activate("Object Oriented Programming", "James Gosling");
		assertEquals("No log record for activate",2,logCount);
		assertContained("Wrong log message","Object Oriented",logLastMessage);
		
		poli.register(10000,10);
		assertEquals("No log record for register",3,logCount);
		assertContained("Wrong log message","10000",logLastMessage);
	}
}
