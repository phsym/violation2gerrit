package com.github.phsym.violation2gerrit;

import java.util.HashMap;
import java.util.Map;

import com.github.phsym.violation2gerrit.comments.Severity;

public class SeverityCounter {
	
	private Map<Severity, Integer> counter;

	public SeverityCounter() {
		Severity[] severities = Severity.values();
		counter = new HashMap<>(severities.length);
		for(Severity s : severities)
			counter.put(s, 0);
	}

	public void increment(Severity severity) {
		counter.put(severity, get(severity) + 1);
	}
	
	public int get(Severity severity) {
		return counter.get(severity);
	}
	
	public Severity highest() {
		Severity[] s = Severity.values();
		for(int i = s.length - 1; i >= 0; i--) {
			if(get(s[i]) > 0)
				return s[i];
		}
		return null;
	}
	
	public int getTotal() {
		int total = 0;
		for(int v : counter.values())
			total += v;
		return total;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("Errors: ")
			.append(get(Severity.ERROR))
			.append(", Warnings: ")
			.append(get(Severity.WARNING))
			.append(", Info: ")
			.append(get(Severity.INFO))
			.append(", Unknown: ")
			.append(get(Severity.UNKNOWN))
			.append(" (Total: ")
			.append(getTotal())
			.append(")");
		return str.toString();
	}
}
