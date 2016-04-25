package opinionSummerization.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Document {
	
	private String _name;
	
	private HashSet<String> _terms;
	private ArrayList<Sentence> _sentences;
	
	public Document(String filename, String delimit) {
		this._name = filename;
		this._terms = new HashSet<String>();
		this._sentences = new ArrayList<Sentence>();
		
		ArrayList<String> lines = new ArrayList<String>();
		FileUtil.readLines(filename, lines);
		
		int lineIndex = 0;
		for (String line : lines) {
			String[] senTexts = line.split(delimit);
			for (String senText : senTexts) {
				//System.out.println(senText);
				Sentence sentence = new Sentence(senText, lineIndex++);
				this._sentences.add(sentence);
				this._terms.addAll(sentence.getTermTexts());
			}
		}
	}
	
	public Document(String filename) {
		this(filename, "(?<=。)|(?<=，)|(?<=；)|(?<= )");
	}

	public String getName() {
		return _name;
	}
	
	public ArrayList<Sentence> getSentences() {
		return this._sentences;
	}
	public Set<String> getTermTexts() {
		return _terms;
	}
}
