package opinionSummerization.utils;

public class Word {
	public Word(String text) {
		this._text = text;
		this._senti_score = 0;
	}
	
	public String Text() {
		return _text;
	}
	
	public double SentiScore() {
		return _senti_score;
	}
	
	public double WeightScore() {
		return _weight_score;
	}
	
	private String _text;
	private double _senti_score;
	private double _weight_score;

}
