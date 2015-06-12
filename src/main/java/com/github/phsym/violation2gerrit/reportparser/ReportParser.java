package com.github.phsym.violation2gerrit.reportparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import com.github.phsym.violation2gerrit.Comment;

public interface ReportParser {
	public List<Comment> parse(InputStream stream) throws ReportParseException;
	
	public default List<Comment> parse(File file) throws ReportParseException {
		try {
			return parse(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new ReportParseException(e);
		}
	}
	public default List<Comment> parse(String file) throws ReportParseException {
		return parse(new File(file));
	}
}
