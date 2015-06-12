package com.github.phsym.violation2gerrit;

import com.google.gerrit.extensions.api.changes.ReviewInput.CommentInput;

public class Comment {
	private String file;
	private int line;
	private String message;
	
	public Comment(String file, int line, String message) {
		this.file = file.replaceAll("\\\\", "/");
		this.line = line;
		this.message = message;
	}
	
	public Comment(String file, String line, String message) {
		this(file, Integer.parseInt(line), message);
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
		return "Comment [file=" + file + ", line=" + line + ", message="
				+ message + "]";
	}
}