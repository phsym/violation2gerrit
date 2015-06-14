package com.github.phsym.violation2gerrit.reportparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.github.phsym.violation2gerrit.comments.Comment;

public abstract class ReportParser {
	
	public abstract void parse(InputStream stream, List<Comment> comments) throws ReportParseException;
	
	public List<Comment> parse(InputStream stream) throws ReportParseException {
		List<Comment> comments = new ArrayList<>();
		parse(stream, comments);
		return comments;
	}
	
	public void parse(File file, List<Comment> comments) throws ReportParseException, FileNotFoundException {
		FileInputStream f = new FileInputStream(file);
		try {
			parse(f, comments);
		} finally {
			try {
				f.close();
			} catch (IOException e) {}
		}
	}
	
	public List<Comment> parse(File file) throws ReportParseException, FileNotFoundException {
		List<Comment> comments = new ArrayList<>();
		parse(file, comments);
		return comments;
	}
	
	public void parse(String file, List<Comment> comments) throws ReportParseException, FileNotFoundException {
		parse(new File(file), comments);
	}
	
	public List<Comment> parse(String file) throws ReportParseException, FileNotFoundException {
		return parse(new File(file));
	}
}
