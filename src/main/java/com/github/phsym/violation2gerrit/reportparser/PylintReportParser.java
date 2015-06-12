package com.github.phsym.violation2gerrit.reportparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.phsym.violation2gerrit.Comment;

public class PylintReportParser implements ReportParser {
	
	private static final Pattern pat = Pattern.compile("(.+):([0-9]+): \\[(.+)\\(.+\\), .*\\] (.*)");

	public PylintReportParser() {
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
							matcher.group(3) + " : " + matcher.group(4)
							);
					comments.add(c);
				}
			}
			return comments;
			
	//		return reader.lines().filter((l) -> pat.matcher(l).matches())
	//				.map(
	//					(line) -> {
	//						Matcher matcher = pat.matcher(line);
	//						matcher.find();
	//						return new Comment(
	//								matcher.group(1),
	//								matcher.group(2),
	//								matcher.group(3) + " : " + matcher.group(4)
	//								);
	//					}
	//				)
	//				.collect(
	//						() -> new ArrayList<Comment>(),
	//						(acc, c) -> acc.add(c),
	//						(l1, l2) -> l1.addAll(l2)
	//				);
		} catch(IOException e) {
			throw new ReportParseException(e);
		}
	}
}
