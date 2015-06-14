package com.github.phsym.violation2gerrit;

import static org.junit.Assert.*;

import org.junit.Test;

import phsym.argparse.exceptions.ArgParseException;
import phsym.argparse.exceptions.MissingArgumentException;
import static com.github.phsym.violation2gerrit.Assert.*;

public class TestMain {

	@Test
	public void test_arg_parse() {
		assertThrow(MissingArgumentException.class, () -> Main.parseArgs(new String[]{}));
		try {
			Main.parseArgs(new String[]{
					"-g", "http://127.0.0.1:1234/",
					"-u", "foo",
					"-p", "bar"
					
			});
			assertEquals(Main.url, "http://127.0.0.1:1234/");
			assertEquals(Main.user, "foo");
			assertEquals(Main.password, "bar");
		} catch (ArgParseException e) {
			e.printStackTrace();
			fail("Exception raised : " + e.getMessage());
		}
	}

}
