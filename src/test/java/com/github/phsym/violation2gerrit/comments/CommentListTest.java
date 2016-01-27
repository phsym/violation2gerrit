package com.github.phsym.violation2gerrit.comments;

import static org.junit.Assert.*;

import org.junit.Test;

public class CommentListTest {

	@Test
	public void testMerge() {
		CommentList list = new CommentList();
		assertEquals(list.size(), 0);
		Comment c1 = new Comment("f1", "1", "m1");
		list.add(c1);
		list.add(c1);
		list.add(new Comment("f1", "1", "m1"));
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).getCount(), 3);
		
		Comment c2 = new Comment("f2", 2, "m2");
		list.add(c2);
		list.add(c2);
		assertEquals(list.size(), 2);
		assertEquals(list.get(1).getCount(), 2);
		
		list.add(new Comment("f3", 3, "m3"));
		
		assertEquals(list.get(0).toCommentInput().message, "m1 (x3)");
		assertEquals(list.get(1).toCommentInput().message, "m2 (x2)");
		assertEquals(list.get(2).toCommentInput().message, "m3");
	}

}
