package com.github.phsym.violation2gerrit.reportparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.phsym.violation2gerrit.comments.Comment;
import com.github.phsym.violation2gerrit.comments.CommentList;
import com.github.phsym.violation2gerrit.comments.Severity;

public class PylintReportParser extends ReportParser {
	
	private static final Pattern pat = Pattern.compile("^(.+):([0-9]+): \\[(.+)\\(.+\\), .*\\] (.*)$");

	public PylintReportParser() {
	}
	
	public static Severity parseSeverity(String severity) {
		if(severity.length() == 0)
			return Severity.UNKNOWN;
		char c = severity.charAt(0);
		switch(c) {
		case 'R':
		case 'C':
			return Severity.INFO;
		case 'W':
			return Severity.WARNING;
		case 'F':
		case 'E':
			return Severity.ERROR;
		default:
			return Severity.UNKNOWN;
		}
	}
	
	@Override
	public void parse(InputStream stream, CommentList comments) throws ReportParseException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			
			String line = null;
			while((line = reader.readLine()) != null) {
				Matcher m = pat.matcher(line);
				if(m.find()) {
					Comment c = new Comment(
							m.group(1),
							m.group(2),
							m.group(3) + " : " + m.group(4),
							parseSeverity(m.group(3))
							);
					comments.add(c);
				}
			}
		} catch(IOException e) {
			throw new ReportParseException(e);
		}
	}
}
