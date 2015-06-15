package com.github.phsym.violation2gerrit.comments;

import static org.junit.Assert.*;
import static com.github.phsym.violation2gerrit.Assert.*;

import org.junit.Test;

import com.google.gerrit.extensions.api.changes.ReviewInput.CommentInput;

public class TestComent {

	@Test
	public void test_new() {
		assertThrow(NumberFormatException.class, () -> new Comment("/root/file", "ab", "message test"));
		Comment com = new Comment("/root/file", "12", "message test");
		assertEquals(com.getLine(), 12);
		assertEquals(com.getFile(), "/root/file");
		assertEquals(com.getMessage(), "message test");
		assertEquals(com.getSeverity(), Severity.UNKNOWN);
		
		com = new Comment("/root/file", "12", "message test", Severity.ERROR);
		assertEquals(com.getSeverity(), Severity.ERROR);
	}

	@Test
	public void test_to_comment_input() {
		Comment com = new Comment("/root/file", "12", "message test", Severity.ERROR);
		CommentInput ci = com.toCommentInput();
		assertEquals(ci.line, new Integer(com.getLine()));
		assertEquals(ci.message, com.getMessage());
	}
}
