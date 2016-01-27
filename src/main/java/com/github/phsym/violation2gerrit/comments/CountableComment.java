package com.github.phsym.violation2gerrit.comments;

import com.google.gerrit.extensions.api.changes.ReviewInput.CommentInput;

public class CountableComment extends Comment {
	
	private int count = 1;
	
	public CountableComment(Comment c) {
		this(c.file, c.line, c.message, c.severity);
	}

	public CountableComment(String file, int line, String message, Severity severity) {
		super(file, line, message, severity);
	}

	public CountableComment(String file, int line, String message) {
		super(file, line, message);
	}

	public CountableComment(String file, String line, String message, Severity severity) {
		super(file, line, message, severity);
	}

	public CountableComment(String file, String line, String message) {
		super(file, line, message);
	}

	public void incrementCountBy(int by) {
		count += by;
	}
	
	public void incrementCount() {
		incrementCountBy(1);
	}
	
	public int getCount() {
		return count;
	}
	
	@Override
	public CommentInput toCommentInput() {
		CommentInput cmt = super.toCommentInput();
		if(count > 1)
			cmt.message += " (x" + count + ")";
		return cmt;
	}
}
