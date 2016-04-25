package opinionSummerization.utils;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;

public class Sentence {
	public Sentence(String text) {
		this._text = text;
		this._termTexts = new HashSet<String>();
		for (Term term:BaseAnalysis.parse(text)) {
			// 去除停用词及噪声
			if (!StopWords.isStopWord(term.getName()) 
					&& !StopWords.isNoiseWord(term.getName())) {
				_termTexts.add(term.getName());
				System.out.println(term.getName());
			}
		}
	}
	
	public Sentence(String text, int index) {
		this(text);
		this._index = index;
	}
	
	public Sentence(String text, int index, char token) {
		this(text, index);
		this._token = token;
	}
	 
	public String getText() {
		return _text;
	}
	
	public double getScore() {
		return _score;
	}
	
	public void setScore(double score) {
		_score = score;
	}
	
	public int getIndex() {
		return this._index;
	}
	public Set<String> getTermTexts() {
		return _termTexts;
	}
	
	public void setTermTexts(Collection<String> word_set) {
		this._termTexts.clear();
		this._termTexts.addAll(word_set);
	}
	
	public String toString() {
		return "Score=" + this.getScore() + "\t Text=" + this.getText() + "\t Parse=" + BaseAnalysis.parse(this.getText());
	}
	
	public char getToken() {
		return this._token;
	}
	
	private String _text;
	private Set<String> _termTexts;
	private double _score;
	private int _index;
	private char _token;
}
