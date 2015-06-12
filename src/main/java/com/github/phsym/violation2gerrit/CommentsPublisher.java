package com.github.phsym.violation2gerrit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.gerrit.extensions.api.GerritApi;
import com.google.gerrit.extensions.api.changes.ReviewInput;
import com.google.gerrit.extensions.api.changes.ReviewInput.CommentInput;
import com.google.gerrit.extensions.api.changes.RevisionApi;
import com.google.gerrit.extensions.common.ChangeInfo;
import com.google.gerrit.extensions.common.CommentInfo;
import com.google.gerrit.extensions.restapi.RestApiException;

public class CommentsPublisher {
	
	private GerritApi api;
	private String sourceRoot = "";
	
	public CommentsPublisher(GerritApi gerrit, String sourceRoot) {
		this.api = gerrit;
		this.sourceRoot = Objects.requireNonNull(sourceRoot, "sourceRoot must be non null");
	}
	
	public CommentsPublisher(GerritApi gerrit) {
		this(gerrit, "");
	}
	
	public void publishComments(int changeNum, int review, List<Comment> comments) throws RestApiException {
		int counter = 0;
		ChangeInfo change = api.changes().query(Integer.toString(changeNum)).get().get(0);
		RevisionApi rev = api.changes().id(change.changeId).revision(review);
		Set<String> revFiles = rev.files().keySet();

		Map<String, List<CommentInfo>> alreadySetComments = rev.comments();
		Map<String, List<CommentInput>> commentsInput = new HashMap<>();
		for (String f : revFiles) {
			List<CommentInfo> setComments = alreadySetComments.getOrDefault(f, Collections.emptyList());
			List<CommentInput> l = new ArrayList<>();
			for(Comment cmt : comments) {
				if(cmt.getFile().equals(f) || cmt.getFile().equals(sourceRoot + "/" + f)) {
					if(!setComments.stream().anyMatch((c) -> (c.line == cmt.getLine()) && (c.message.equals(cmt.getMessage())))) {
						l.add(cmt.toCommentInput());
						counter += 1;
					}
				}
			}
//			
//			List<CommentInput> l = comments.stream()
//					.filter((cm) -> cm.getFile().equals(f))
//					.collect(
//						() -> new ArrayList<CommentInput>(),
//						(acc, cmt) -> acc.add(cmt.toCommentInput()),
//						(l1, l2) -> l1.addAll(l2)
//					);
			commentsInput.put(f, l);
		}
		
		if(counter > 0) {
			ReviewInput revInput = new ReviewInput();
			revInput.comments = commentsInput;
			revInput.message = "Published violation report (found " + counter + ")";
			rev.review(revInput);
		}
	}
}
