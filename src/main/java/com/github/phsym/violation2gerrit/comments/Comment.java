package com.github.phsym.violation2gerrit.comments;

import com.google.gerrit.extensions.api.changes.ReviewInput.CommentInput;

public class Comment {
	private Severity severity;
	private String file;
	private int line;
	private String message;
	
	public Comment(String file, int line, String message) {
		this(file, line, message, Severity.UNKNOWN);
	}
	
	public Comment(String file, int line, String message, Severity severity) {
		this.file = file.replaceAll("\\\\", "/");
		this.line = line;
		this.message = message;
		this.severity = severity;
	}
	
	public Comment(String file, String line, String message) {
		this(file, line, message, Severity.UNKNOWN);
	}
	
	public Comment(String file, String line, String message, Severity severity) {
		this(file, Integer.parseInt(line), message, severity);
	}
	
	public Severity getSeverity() {
		return severity;
	}
	
	public String getFile() {
		return file;
	}
	
	public int getLine() {
		return line;
	}
	
	public String getMessage() {
		return message;
	}
	
	public CommentInput toCommentInput() {
		CommentInput cmt = new CommentInput();
		cmt.line = line;
		cmt.message = message;
		return cmt;
	}

	@Override
	public String toString() {
		return "Comment [severity=" + severity + ", file=" + file + ", line="
				+ line + ", message=" + message + "]";
	}
}