package com.github.phsym.violation2gerrit;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.phsym.violation2gerrit.comments.Comment;
import com.github.phsym.violation2gerrit.reportparser.CheckStyleReportParser;
import com.github.phsym.violation2gerrit.reportparser.PylintReportParser;
import com.github.phsym.violation2gerrit.reportparser.ReportParseException;
import com.google.gerrit.extensions.api.GerritApi;
import com.google.gerrit.extensions.restapi.RestApiException;
import com.urswolfer.gerrit.client.rest.GerritAuthData;
import com.urswolfer.gerrit.client.rest.GerritRestApiFactory;

import phsym.argparse.ArgParse;
import phsym.argparse.arguments.Type;
import phsym.argparse.exceptions.ArgParseException;

public class Main {
	
	protected static String url;
	protected static String user;
	protected static String password;
	protected static String rootDir;
	protected static List<String> pylintFiles;
	protected static List<String> checkstyleFiles;
	
	protected static Integer gerritChange = null;
	protected static Integer revId = null;
	
	protected static boolean debug;
	
	public static Map<String, Object> parseArgs(String[] args) throws ArgParseException {
		ArgParse argParse = new ArgParse("violation2gerrit")
			.description("Report code violation as Gerrit review comments")
			.defaultHelp()
			.version("0.1.0")
			.addDefaultErrorHandler();
		
		argParse.label("Options :");
		
		argParse.add(Type.STRING, "-g", "--gerrit")
			.pattern("https?://.+(:[0-9]+)?(/.+)*/?")
			.help("URL to gerrit rest endpoint")
			.required(true)
			.consume((g) -> url = g);
		argParse.add(Type.STRING, "-u", "--user")
			.help("Gerrit user to log with")
			.required(true)
			.consume((u) -> user = u);
		argParse.add(Type.STRING, "-p", "--password")
			.help("Password for gerrit user")
			.required(true)
			.consume((p) -> password = p);
		argParse.add(Type.STRING, "-s", "--src")
			.help("Path to project root dir")
			.setDefault("")
			.consume((s) -> rootDir = s);
		argParse.add(Type.STRING_ARRAY, "--pylint")
			.help("List of pylint report files")
			.consume((f) -> pylintFiles = f)
			.setDefault(new ArrayList<>());
		argParse.add(Type.STRING_ARRAY, "--checkstyle")
			.help("List of checkstyle report files")
			.consume((f) -> checkstyleFiles = f)
			.setDefault(new ArrayList<>());
		argParse.add(Type.INT, "-c", "--change-number")
			.help("Gerrit change number to publish comments to. Default will be taken from GERRIT_CHANGE_NUMBER environment variable")
			.consume((c) -> gerritChange = c);
		argParse.add(Type.INT, "-r", "--review-id")
			.help("Gerrit review id (patchset) for the given change. Default will be taken from GERRIT_PATCHSET_NUMBER environment variable")
			.consume((r) -> revId = r);
		argParse.add(Type.BOOL, "-d", "--debug")
			.help("Enable debugging")
			.setDefault(false)
			.consume((d) -> debug = d);
		return argParse.parseThrow(args);
	}
	
	public static String getMandatoryEnv(String key) {
		String value = System.getenv(key);
		if(value == null) {
			System.err.println("Missing environment variable " + key);
			System.exit(1);
		}
		return value;
	}
	
	public static GerritApi getGerritApi() {
		GerritRestApiFactory fact = new GerritRestApiFactory();
		GerritAuthData.Basic auth = new GerritAuthData.Basic(url, user, password);
		return fact.create(auth);
	}

	public static void main(String[] args) {
		try{ 
			parseArgs(args);
			
			List<Comment> comments = new ArrayList<>();
			GerritApi api = getGerritApi();
			CommentsPublisher pub = new CommentsPublisher(api, rootDir);
			PylintReportParser pylint = new PylintReportParser();
			CheckStyleReportParser checkstyle = new CheckStyleReportParser();
			
			if(gerritChange == null)
				gerritChange = Integer.parseInt(getMandatoryEnv("GERRIT_CHANGE_NUMBER"));
			if(revId == null)
				revId = Integer.parseInt(getMandatoryEnv("GERRIT_PATCHSET_NUMBER"));
			
			for(String pylintFile : pylintFiles) {
				pylint.parse(pylintFile, comments);
			}
			for(String csFile : checkstyleFiles) {
				checkstyle.parse(csFile, comments);
			}
			if(debug)
				System.out.println(comments);
			
			pub.publishComments(gerritChange, revId, comments);
		} catch(ArgParseException | RestApiException | FileNotFoundException | ReportParseException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
}
