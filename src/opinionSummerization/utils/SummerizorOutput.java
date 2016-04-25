package opinionSummerization.utils;
import java.util.LinkedList;
import java.util.List;

public class SummerizorOutput {
	public SummerizorOutput() {
		this.sentences = new LinkedList<Sentence>();
	}
	public List<Sentence> sentences;
	public double actual_compression_ratio;
	
	public void printBrief() {
		for (Sentence sen : sentences) {
			System.out.printf("%s\t%f\n", sen.getText(), sen.getScore());
		}
	}

	public void printResult() {
		for (Sentence sen : sentences) {
			System.out.print(sen.getText() + ",");
		}
		
		System.out.println();
	}
}
