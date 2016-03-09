package opinionSummerization.summerizors;
import java.util.List;
import java.util.Set;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;

import opinionSummerization.utils.Sentence;
import opinionSummerization.utils.SummerizorOutput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class NaiveSummerizor implements Summerizor{
	public SummerizorOutput Summerize(String text) {
		System.out.println(text);
		String[] strs = text.split("，|。");
		System.out.println(strs.length);
		List<Sentence> sentences = new ArrayList<Sentence>();

		for (String str:strs) {
			Sentence sentence = new Sentence(str);
			List<String> words = new ArrayList<String>();
			for (Term term:BaseAnalysis.parse(str)) {
				words.add(term.getName());
			}
			sentence.set_word_set(words);
			sentences.add(sentence);
			System.out.println(str);
		}
		
		for ( int i = 0; i < sentences.size(); i++) {
			for (int j = 0; j < sentences.size(); j++) {
				if (i == j) {
					continue;
				}
				sentences.get(i).set_score(sentences.get(i).score() + calc_score(sentences.get(i), sentences.get(j)));
			}
			//System.out.println("Sentence #" + i + " Score=" + sentences.get(i).score());
		}
		
		for (int i = 0; i < sentences.size(); i++) {
			System.out.println("Sentence #" + i + ":" + "\t"+sentences.get(i).toString());
		}
		// 根据score排序
		Collections.sort(sentences, 
						 (lhs, rhs) -> lhs.score() < rhs.score() ? 1 : -1
						);
		
		// 根据compression_ratio 截断前若干个sentence
		int count_of_retain = (int)(sentences.size() * this._compression_ratio);
		List<Sentence> retained_sentences = sentences.subList(0, count_of_retain);
		
		// 根据sentence在text中的index排序
		Collections.sort(retained_sentences,
						 (lhs, rhs) -> lhs.index() < rhs.index() ? 1 : -1
						);
		
		SummerizorOutput output = new SummerizorOutput();
		output.sentences = retained_sentences;
		return output;
	}
	
	public void set_compression_ratio(double ratio) {
		this._compression_ratio = ratio;
	}
	
	private double calc_score(Sentence first_sentence, Sentence second_sentence) {
		Set<String> result = new HashSet<String>();
		result.clear();
		result.addAll(first_sentence.word_set());
		result.retainAll(second_sentence.word_set());
		return result.size();
	}
	
	private double _compression_ratio;
}