/**
 * @file SentimentAnlyzerInput.java
 * @author chaoqiao
 * @date 2016年4月7日
 */
package opinionSummerization.sentiment;

import java.util.List;
import opinionSummerization.utils.Sentence;

/**
 * @description sentiment analyzer input 
 * @author chaoqiao
 *
 */
public class SentimentAnalyzerInput {
	public SentimentAnalyzerInput(List<Sentence> sents) {
		this.sentences = sents;
	}
	public List<Sentence> sentences;
}