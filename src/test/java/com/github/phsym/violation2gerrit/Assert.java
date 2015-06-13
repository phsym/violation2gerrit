package com.github.phsym.violation2gerrit;

import static org.junit.Assert.fail;

public class Assert {

	@FunctionalInterface
	public interface ExRunnable {
		public void run() throws Throwable;
	}
	
	public static <T extends Throwable> void assertThrow(Class<T> ex, ExRunnable r) {
		try {
			r.run();
			fail("Exception " + ex.getName() + " not thrown");
		} catch (Throwable e){
			if(!ex.isInstance(e))
				fail(e.getClass().getName() + " is different from " + ex.getName());
		}
	}

}
