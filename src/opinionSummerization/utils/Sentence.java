package opinionSummerization.utils;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.ansj.splitWord.analysis.BaseAnalysis;

public class Sentence {
	public Sentence(String text) {
		this._text = text;
		this._word_set = new HashSet<String>();
	}
	
	public Sentence(String text, int index) {
		this(text);
		this._index = index;
	}
	 
	public String text() {
		return _text;
	}
	
	public double score() {
		return _score;
	}
	
	public void set_score(double score) {
		_score = score;
	}
	
	public int index() {
		return this._index;
	}
	public Set<String> word_set() {
		return _word_set;
	}
	
	public void set_word_set(Collection<String> word_set) {
		this._word_set.clear();
		this._word_set.addAll(word_set);
	}
	
	public String toString() {
		return "Score=" + this.score() + "\t Text=" + this.text() + "\t Parse=" + BaseAnalysis.parse(this.text());
	}
	
	private String _text;
	private Set<String> _word_set;
	private double _score;
	private int _index;
}
