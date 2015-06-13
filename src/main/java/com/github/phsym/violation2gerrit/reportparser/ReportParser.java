package com.github.phsym.violation2gerrit.reportparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.github.phsym.violation2gerrit.comments.Comment;

public interface ReportParser {
	
	public List<Comment> parse(InputStream stream) throws ReportParseException;
	
	public default List<Comment> parse(File file) throws ReportParseException, FileNotFoundException {
		FileInputStream f = new FileInputStream(file);
		try {
			return parse(f);
		} finally {
			try {
				f.close();
			} catch (IOException e) {}
		}
	}
	
	public default List<Comment> parse(String file) throws ReportParseException, FileNotFoundException {
		return parse(new File(file));
	}
}
