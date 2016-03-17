package opinionSummerization.utils;

import java.util.ArrayList;
import java.util.HashSet;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;

public class Document {
	
	public Document(String filename) {
		ArrayList<String> lines = new ArrayList<String>();
		FileUtil.readLines(filename, lines);
		for (String line : lines) {
			for (Term term:BaseAnalysis.parse(line)) {
				if (!StopWords.isStopWord(term.getName())) {
					m_words.add(term.getName());
				}
			}
		}
	}
	
	private HashSet<String> m_words;
}
