package com.github.phsym.violation2gerrit;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.phsym.violation2gerrit.comments.Comment;
import com.github.phsym.violation2gerrit.comments.Severity;
import com.github.phsym.violation2gerrit.reportparser.PylintReportParser;
import com.github.phsym.violation2gerrit.reportparser.ReportParseException;

import static com.github.phsym.violation2gerrit.Assert.*;

public class TestPylintParser {

	private PylintReportParser parser = null;
	
	@Before
	public void setUp() {
		parser = new PylintReportParser();
	}
	
	@Test
	public void test_not_exist() {
		assertThrow(FileNotFoundException.class, () -> parser.parse("file_not_exist"));
	}
	
	@Test
	public void test_parse() throws FileNotFoundException, ReportParseException {
		List<Comment> comments = parser.parse("src/test/resources/pylint-report.txt");
		assertEquals(comments.size(), 5);
		Comment c = comments.stream().filter((com) -> com.getFile().equals("tests/file3.py")).findFirst().orElse(null);
		assertNotNull(c);
		assertEquals(c.getLine(), 16);
		assertEquals(c.getMessage(), "R0904 : Too many public methods (102/100)");
		assertEquals(c.getSeverity(), Severity.LOW);
	}
}
