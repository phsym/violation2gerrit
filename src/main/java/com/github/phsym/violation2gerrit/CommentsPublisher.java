package com.github.phsym.violation2gerrit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.phsym.violation2gerrit.comments.Comment;
import com.google.gerrit.extensions.api.GerritApi;
import com.google.gerrit.extensions.api.changes.ReviewInput;
import com.google.gerrit.extensions.api.changes.ReviewInput.CommentInput;
import com.google.gerrit.extensions.api.changes.RevisionApi;
import com.google.gerrit.extensions.common.ChangeInfo;
import com.google.gerrit.extensions.common.CommentInfo;
import com.google.gerrit.extensions.restapi.RestApiException;

public class CommentsPublisher {
	
	private GerritApi api;
	private boolean labelize;
	private String sourceRoot = "";
	
	public CommentsPublisher(GerritApi gerrit, boolean labelize, String sourceRoot) {
		this.api = gerrit;
		this.labelize = labelize;
		if(sourceRoot != null)
			this.sourceRoot = sourceRoot;
	}
	
	public CommentsPublisher(GerritApi gerrit, boolean labelize) {
		this(gerrit, labelize, "");
	}
	
	public void publishComments(int changeNum, int review, List<Comment> comments) throws RestApiException {
		SeverityCounter counter = new SeverityCounter();
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
					counter.increment(cmt.getSeverity());
					if(!setComments.stream().anyMatch((c) -> (c.line == cmt.getLine()) && (c.message.equals(cmt.getMessage())))) {
						l.add(cmt.toCommentInput());
					}
				}
			}
			commentsInput.put(f, l);
		}
		
		if(counter.getTotal() > 0) {
			ReviewInput revInput = new ReviewInput();
			revInput.comments = commentsInput;
			revInput.message = "Published violation report : " + counter;
			if(labelize) {
				switch(counter.highest()) {
				case ERROR:
					revInput.message += "\n\nCode-Review = -2";
					revInput.label("Code-Review", -2);
					break;
				case WARNING:
					revInput.message += "\n\nCode-Review = -1";
					revInput.label("Code-Review", -1);
					break;
				default:
					revInput.message += "\n\nCode-Review = 0";
					revInput.label("Code-Review", 0);
					break;
				}
			}
			rev.review(revInput);
		}
	}
}
