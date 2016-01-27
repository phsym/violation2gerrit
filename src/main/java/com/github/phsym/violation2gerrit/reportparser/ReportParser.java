package com.github.phsym.violation2gerrit.reportparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import com.github.phsym.violation2gerrit.comments.CommentList;

public abstract class ReportParser {
	
	public abstract void parse(InputStream stream, CommentList comments) throws ReportParseException;
	
	public final CommentList parse(InputStream stream) throws ReportParseException {
		CommentList comments = new CommentList();
		parse(stream, comments);
		return comments;
	}
	
	public final void parse(File file, CommentList comments) throws ReportParseException, FileNotFoundException {
		FileInputStream f = new FileInputStream(file);
		try {
			parse(f, comments);
		} finally {
			try {
				f.close();
			} catch (IOException e) {}
		}
	}
	
	public final CommentList parse(File file) throws ReportParseException, FileNotFoundException {
		CommentList comments = new CommentList();
		parse(file, comments);
		return comments;
	}
	
	public final void parse(String file, CommentList comments) throws ReportParseException, FileNotFoundException {
		parse(new File(file), comments);
	}
	
	public final CommentList parse(String file) throws ReportParseException, FileNotFoundException {
		return parse(new File(file));
	}
}
