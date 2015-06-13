package com.github.phsym.violation2gerrit;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.phsym.violation2gerrit.comments.Comment;
import com.github.phsym.violation2gerrit.comments.Severity;
import com.github.phsym.violation2gerrit.reportparser.CheckStyleReportParser;
import com.github.phsym.violation2gerrit.reportparser.ReportParseException;

import static com.github.phsym.violation2gerrit.Assert.*;

public class TestCheckStyleParser {
	
	private CheckStyleReportParser parser = null;

	@Before
	public void setUp() throws Exception {
		parser = new CheckStyleReportParser();
	}

	@Test
	public void test_not_exist() {
		assertThrow(FileNotFoundException.class, () -> parser.parse("file_not_exist"));
	}
	
	@Test
	public void test_parse() throws FileNotFoundException, ReportParseException {
		List<Comment> comments = parser.parse("src/test/resources/checkstyle-report.xml");
		assertEquals(comments.size(), 3);
		Comment com = comments.get(0);
		assertEquals(com.getSeverity(), Severity.WARNING);
		assertEquals(com.getFile(), "tools/script.sh");
		assertEquals(com.getLine(), 30);
		assertEquals(com.getMessage(), "warning : Use -print0/-0 or -exec + to allow for non-alphanumeric filenames.");
	}
}