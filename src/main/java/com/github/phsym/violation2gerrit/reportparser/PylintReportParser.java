package com.github.phsym.violation2gerrit.reportparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.phsym.violation2gerrit.comments.Comment;
import com.github.phsym.violation2gerrit.comments.Severity;

public class PylintReportParser implements ReportParser {
	
	private static final Pattern pat = Pattern.compile("(.+):([0-9]+): \\[(.+)\\(.+\\), .*\\] (.*)");

	public PylintReportParser() {
	}
	
	public static Severity parseSeverity(String severity) {
		if(severity.length() == 0)
			return Severity.UNKNOWN;
		char c = severity.charAt(0);
		switch(c) {
		case 'R':
		case 'C':
			return Severity.LOW;
		case 'W':
			return Severity.WARNING;
		case 'E':
			return Severity.ERROR;
		default:
			return Severity.UNKNOWN;
		}
	}
	
	@Override
	public List<Comment> parse(InputStream stream) throws ReportParseException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			List<Comment> comments = new ArrayList<>();
			
			String line = null;
			while((line = reader.readLine()) != null) {
				Matcher matcher = pat.matcher(line);
				if(matcher.find()) {
					Comment c = new Comment(
							matcher.group(1),
							matcher.group(2),
							matcher.group(3) + " : " + matcher.group(4),
							parseSeverity(matcher.group(3))
							);
					comments.add(c);
				}
			}
			return comments;
		} catch(IOException e) {
			throw new ReportParseException(e);
		}
	}
}
