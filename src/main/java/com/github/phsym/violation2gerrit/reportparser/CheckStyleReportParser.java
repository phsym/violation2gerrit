package com.github.phsym.violation2gerrit.reportparser;

import java.io.IOException;
import java.io.InputStream;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.github.phsym.violation2gerrit.comments.Comment;
import com.github.phsym.violation2gerrit.comments.CommentList;
import com.github.phsym.violation2gerrit.comments.Severity;

public class CheckStyleReportParser extends ReportParser {
	
	public CheckStyleReportParser() {
	}
	
	public static Severity parseSeverity(String severity) {
		switch(severity) {
		case "info": 
			return Severity.INFO;
		case "warning":
			return Severity.WARNING;
		case "error":
			return Severity.ERROR;
		default:
			return Severity.UNKNOWN;
		}
	}
	
	@Override
	public void parse(InputStream stream, CommentList comments) throws ReportParseException {
		try {
			Element root = new SAXBuilder().build(stream).getRootElement();
			for(Element file : root.getChildren("file")) {
				String fileName = file.getAttributeValue("name");
				for(Element error : file.getChildren("error")) {
					String severity = error.getAttributeValue("severity");
					String msg = severity + " : " + error.getAttributeValue("message");
					Comment com = new Comment(fileName, error.getAttributeValue("line"), msg, parseSeverity(severity));
					comments.add(com);
				}
			}
		} catch(IOException | JDOMException e) {
			throw new ReportParseException(e);
		}
	}
}
