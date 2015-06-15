package com.github.phsym.violation2gerrit;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.phsym.violation2gerrit.comments.Severity;

public class TestSeverityCounter {

	@Test
	public void test_init() {
		SeverityCounter c = new SeverityCounter();
		Severity[] sev = Severity.values();
		for(Severity s : sev)
			assertEquals(0, c.get(s));
	}

	@Test
	public void test_increment() {
		SeverityCounter c = new SeverityCounter();
		for(int i = 0; i < 1000; i++) {
			assertEquals(c.get(Severity.ERROR), i);
			c.increment(Severity.ERROR);
		}
	}
	
	@Test
	public void test_highest() {
		SeverityCounter c = new SeverityCounter();
		assertNull(c.highest());
		c.increment(Severity.UNKNOWN);
		assertEquals(c.highest(), Severity.UNKNOWN);
		c.increment(Severity.INFO);
		assertEquals(c.highest(), Severity.INFO);
		c.increment(Severity.WARNING);
		assertEquals(c.highest(), Severity.WARNING);
		c.increment(Severity.ERROR);
		assertEquals(c.highest(), Severity.ERROR);
		c.increment(Severity.INFO);
		assertEquals(c.highest(), Severity.ERROR);
	}
	
	@Test
	public void test_get_total() {
		SeverityCounter c = new SeverityCounter();
		assertEquals(0, c.getTotal());
		c.increment(Severity.UNKNOWN);
		c.increment(Severity.INFO);
		c.increment(Severity.WARNING);
		c.increment(Severity.ERROR);
		assertEquals(4, c.getTotal());
		c.increment(Severity.INFO);
		c.increment(Severity.WARNING);
		assertEquals(6, c.getTotal());
	}
	
	@Test
	public void test_to_string() {
		SeverityCounter c = new SeverityCounter();
		assertEquals(c.toString(), "Errors: 0, Warnings: 0, Info: 0, Unknown: 0 (Total: 0)");
		
		c.increment(Severity.UNKNOWN);
		c.increment(Severity.INFO);
		c.increment(Severity.WARNING);
		c.increment(Severity.ERROR);
		c.increment(Severity.INFO);
		c.increment(Severity.WARNING);
		assertEquals(c.toString(), "Errors: 1, Warnings: 2, Info: 2, Unknown: 1 (Total: 6)");
	}
}
