package violation2gerrit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gerrit.extensions.api.GerritApi;
import com.urswolfer.gerrit.client.rest.GerritAuthData;
import com.urswolfer.gerrit.client.rest.GerritRestApiFactory;

import phsym.argparse.ArgParse;
import phsym.argparse.arguments.Type;
import violation2gerrit.reportparser.CheckStyleReportParser;
import violation2gerrit.reportparser.PylintReportParser;

public class Main {
	
	private static String url;
	private static String user;
	private static String password;
	private static String rootDir;
	private static List<String> pylintFiles;
	private static List<String> checkstyleFiles;
	private static boolean debug;
	
	public static Map<String, Object> parseArgs(String[] args) {
		ArgParse argParse = new ArgParse("viloation2gerrit")
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
			.dest("change-number");
		argParse.add(Type.INT, "-r", "--review-id")
			.help("Gerrit review id (patchset) for the given change. Default will be taken from GERRIT_PATCHSET_NUMBER environment variable")
			.dest("review-id");
		argParse.add(Type.BOOL, "-d", "--debug")
			.help("Enable debugging")
			.setDefault(false)
			.consume((d) -> debug = d);
		return argParse.parse(args);
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

	public static void main(String[] args) throws Exception {
		
		Map<String, Object> arguments = parseArgs(args);
		
		List<Comment> comments = new ArrayList<>();
		GerritApi api = getGerritApi();
		CommentsPublisher pub = new CommentsPublisher(api, rootDir);
		PylintReportParser pylint = new PylintReportParser();
		CheckStyleReportParser checkstyle = new CheckStyleReportParser();
		
		Integer gerritChange = (Integer) arguments.get("change-number");
		Integer revId = (Integer) arguments.get("review-id");
		if(gerritChange == null)
			gerritChange = Integer.parseInt(getMandatoryEnv("GERRIT_CHANGE_NUMBER"));
		if(revId == null)
			revId = Integer.parseInt(getMandatoryEnv("GERRIT_PATCHSET_NUMBER"));
		
		for(Object pylintFile : pylintFiles) {
			comments.addAll(pylint.parse((String)pylintFile));
		}
		for(Object csFile : checkstyleFiles) {
			comments.addAll(checkstyle.parse((String)csFile));
		}
		if(debug)
			System.out.println(comments);
		
		pub.publishComments(gerritChange, revId, comments);
	}
}
