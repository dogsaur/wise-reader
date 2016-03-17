package opinionSummerization.utils;

import java.util.ArrayList;
import java.util.HashSet;

public class StopWords {
	public StopWords(String filename) {
		ArrayList<String> lines = new ArrayList<String>();
		FileUtil.readLines(filename, lines);
		for (String line : lines) {
			m_stop_words.add(line);
		}
	}
	
	public static boolean isStopWord(String word) {
		return m_stop_words.contains(word);
	}
	
	private static HashSet<String> m_stop_words;
}
