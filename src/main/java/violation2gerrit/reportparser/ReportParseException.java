package violation2gerrit.reportparser;

public class ReportParseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4295080519970188919L;

	public ReportParseException() {
		super();
	}

	public ReportParseException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ReportParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReportParseException(String message) {
		super(message);
	}

	public ReportParseException(Throwable cause) {
		super(cause);
	}
}
