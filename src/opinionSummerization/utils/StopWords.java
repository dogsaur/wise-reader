package opinionSummerization.utils;

import java.util.ArrayList;
import java.util.HashSet;

public class StopWords {
	private static HashSet<String> m_stop_words;
	
	static{
		m_stop_words = new HashSet<String>();
		String stop_words_path = "./data/dicts/chinese-stop-words.dict";
		ArrayList<String> lines = new ArrayList<String>();
		FileUtil.readLines(stop_words_path, lines);
		for (String line : lines) {
			m_stop_words.add(line);
		}
	}
	
	public static boolean isStopWord(String word) {
		return m_stop_words.contains(word);
	}
}
