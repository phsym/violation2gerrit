package com.github.phsym.violation2gerrit.reportparser;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.github.phsym.violation2gerrit.comments.Comment;
import com.github.phsym.violation2gerrit.comments.CommentList;
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
		CommentList comments = parser.parse("src/test/resources/pylint-report.txt");
		assertEquals(comments.size(), 5);
		Comment c = comments.stream().filter((com) -> com.getFile().equals("tests/file3.py")).findFirst().orElse(null);
		assertNotNull(c);
		assertEquals(c.getLine(), 16);
		assertEquals(c.getMessage(), "R0904 : Too many public methods (102/100)");
		assertEquals(c.getSeverity(), Severity.INFO);
	}
	
	@Test
	public void test_severity() {
		Map<String, Severity> tests = new HashMap<>();
		tests.put("", Severity.UNKNOWN);
		tests.put("C123", Severity.INFO);
		tests.put("R123", Severity.INFO);
		tests.put("W123", Severity.WARNING);
		tests.put("E123", Severity.ERROR);
		tests.put("123", Severity.UNKNOWN);
		tests.put("ABC", Severity.UNKNOWN);
		
		for(String k : tests.keySet()) {
			assertEquals(tests.get(k), PylintReportParser.parseSeverity(k));
		}
	}
}
