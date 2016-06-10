/**
 * @file LdaSummerizor.java
 * @author chaoqiao
 * @date 2016年3月23日
 */
package opinionSummerization.summerizors;

import opinionSummerization.utils.SummerizorOutput;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import opinionSummerization.summerizors.lda.Model;
import opinionSummerization.utils.Document;
import opinionSummerization.utils.Sentence;

/**
 * @description summerizor build with lda model
 * @author chaoqiao
 *
 */
public class LdaSummerizor implements Summerizor {

	private double compressionRatio;
	/**
	 * 
	 */
	public LdaSummerizor(String ldaFile) {
		this.ldaModel = Model.loadModel(ldaFile);
		this.compressionRatio = 0.4;
	}
	
	public LdaSummerizor(Model ldaModel) {
		this.ldaModel = ldaModel;
		this.compressionRatio = 0.4;
	}
	
	public void setCompressionRatio(double ratio) {
		this.compressionRatio = ratio;
	}
	
	public SummerizorOutput summerize(String text) {
		return null;
	}
	
	public SummerizorOutput summerize(Document doc) {

		SummerizorOutput result = new SummerizorOutput();
		if (ldaModel == null) {
			System.out.println("请先指定lda模型!");
			return result;
		}		
		double [] p_doc = ldaModel.analyze(doc);
		
		double p_sum = 0;
		for (int i =  0; i < p_doc.length; i++) {
			System.out.printf("%f ", p_doc[i]);
			p_sum += p_doc[i];
		}
		System.out.println("p_doc : " + p_sum);
		
		
		for (Sentence sentence : doc.getSentences()) {
			double [] p_sen = ldaModel.analyze(sentence, p_doc);
			p_sum = 0;
			for (int i = 0; i < p_sen.length; i++) {
				System.out.printf("%f ", p_sen[i]);
				p_sum += p_sen[i];
			}
			
			// calc score by KL
			double KLsd = 0;
			for (int k = 0; k < p_doc.length; k++) {
				KLsd += p_sen[k] * Math.log(p_sen[k] / p_doc[k]);
			}
			
			double KLds = 0;
			for (int k = 0; k < p_doc.length; k++) {
				KLds += p_doc[k] * Math.log(p_doc[k] / p_sen[k]);
			}
	
			double score = -(KLsd + KLds);
			Double s = new Double(score);
			if (s.isNaN()){
				score = -99999;
			}
			sentence.setScore(score);
			System.out.println("score :" + score + "\t\t"  + sentence.getText());
		}
		
		List<Sentence> sentences = new LinkedList<Sentence>();
		sentences.addAll(doc.getSentences());
		
		// 根据compression_ratio 截断前若干个sentence
		int countOfRetain = (int)(sentences.size() * this.compressionRatio);
		result.sentences.addAll(
				this.filterTopN(sentences, countOfRetain));
		return result;
	}
	
	private List<Sentence> filterTopN(List<Sentence> sentences, int n) {
		// sort by score
		Collections.sort(sentences, 
						 (lhs, rhs) -> lhs.getScore() < rhs.getScore() ? 1 : -1
						);
		
		List<Sentence> result = sentences.subList(0, n);
		
		// sort by index
		Collections.sort(result,
						 (lhs, rhs) -> lhs.getIndex() < rhs.getIndex() ? 1 : -1
						);
		
		return result;
	}
	
	private Model ldaModel;

}
