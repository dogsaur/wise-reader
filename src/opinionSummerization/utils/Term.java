package opinionSummerization.utils;

public class Term {
	public Term(String text) {
		this._text = text;
		this._senti_score = 0;
		this._count = 0;
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
	
	public void countIncrease() {
		this._count += 1;
	}
	
	private int _count;
	private String _text;
	private double _senti_score;
	private double _weight_score;

}
