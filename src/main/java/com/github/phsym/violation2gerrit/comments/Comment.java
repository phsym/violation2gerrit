package com.github.phsym.violation2gerrit.comments;

import com.google.gerrit.extensions.api.changes.ReviewInput.CommentInput;

public class Comment {
	protected Severity severity;
	protected String file;
	protected int line;
	protected String message;
	
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

	public boolean isSimilar(Comment other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		if (line != other.line)
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (severity != other.severity)
			return false;
		return true;
	}
}