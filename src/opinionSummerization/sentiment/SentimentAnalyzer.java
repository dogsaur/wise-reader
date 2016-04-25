/**
 * @file SentimentAnalyzer.java
 * @author chaoqiao
 * @date 2016年4月5日
 */
package opinionSummerization.sentiment;

import opinionSummerization.utils.Sentence;

/**
 * @description $
 * @author chaoqiao
 *
 */
public class SentimentAnalyzer {
	public double analyse(SentimentAnalyzerInput input) {

		double score = 0;
		double count = 0;
		final double EPS = 1e-7;

		for (Sentence sentence : input.sentences) {

			double sentenceScore = 0;
			for (String termText : sentence.getTermTexts()) {

				if (SentimentWords.isSentiWord(termText)) {
					sentenceScore += SentimentWords.getSentiScore(termText);
				}
			}

			if (Math.abs(sentenceScore - 0) > EPS) {
				count += 1;
				score += (1 + sentence.getScore()) * sentenceScore;
			}

			System.out.println(sentenceScore + " : " + sentence.getText());
		}

		if (count != 0) {
			return score / count;
		}

		return 0;
	}
}
