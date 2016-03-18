package opinionSummerization.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;

public class Document {
	
	private String _name;
	
	private HashSet<String> _terms;
	
	public Document(String filename) {
		this._name = filename;
		this._terms = new HashSet<String>();
		
		ArrayList<String> lines = new ArrayList<String>();
		FileUtil.readLines(filename, lines);
		for (String line : lines) {
			for (Term term:BaseAnalysis.parse(line)) {
				if (!StopWords.isStopWord(term.getName())) {
					_terms.add(term.getName());
				}
			}
		}
	}
	/**
	 * @return the _name
	 */
	public String get_name() {
		return _name;
	}
	public Set<String> getTermTexts() {
		return _terms;
	}
}
