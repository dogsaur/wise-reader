/**
 * @file SentimentWords.java
 * @author chaoqiao
 * @date 2016年4月5日
 */
package opinionSummerization.sentiment;

import java.util.ArrayList;
import java.util.HashMap;
import opinionSummerization.utils.FileUtil;

/**
 * @description sentiment dictionary by HowNet 
 * @author chaoqiao
 *
 */

public class SentimentWords {
	static {
		loadSentiDicts();
		loadUserWords();
	}

	public static void loadSentiDicts() {
		_sentiDict = new HashMap<String, Double>();
		ArrayList<String> lines = new ArrayList<String>();

		FileUtil.readLines("./data/dicts/负面评价词语（中文）.txt", lines);
		for (String word : lines) {
			if (!word.isEmpty()) {
				_sentiDict.put(word, -1.0);
			}
		}
		
		FileUtil.readLines("./data/dicts/正面评价词语（中文）.txt", lines);
		for (String word: lines) {
			if (!word.isEmpty()) {
				_sentiDict.put(word, 1.0);
			}
		}
	}
	
	public static void loadUserWords() {
		String[] positiveWords = {
				 "涨",
				 "上涨",
				 "盈利",
				 "反弹"};
		for (String word : positiveWords) {
			_sentiDict.put(word, 1.0);		
		}
		
		String[] negativeWords = {
				"跌",
				"震荡"};
		for (String word : negativeWords) {
			_sentiDict.put(word, -1.0);		
		}
		
	}
	public static boolean isSentiWord(String word) {
		return _sentiDict.containsKey(word);
	}
	
	public static double getSentiScore(String word) {
		System.out.println("[sentiword: "+ word + " score: " + _sentiDict.get(word) + "]" );
		return _sentiDict.get(word);
	}

	private static HashMap<String, Double> _sentiDict;
	
}
