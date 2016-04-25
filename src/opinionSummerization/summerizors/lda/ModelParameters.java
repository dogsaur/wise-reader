/**
 * @file ModelParameters.java
 * @author chaoqiao
 * @date 2016年3月17日
 */
package opinionSummerization.summerizors.lda;

/**
 * @description parameters for LDA model 
 * @author chaoqiao
 *
 */
public class ModelParameters {
	public ModelParameters(double alpha, double beta, int maxIterTimes, int topicNum){
		this.alpha = alpha;
		this.beta = beta;
		this.maxIterTimes = maxIterTimes;
		this.topicNum = topicNum;
		this.saveStep = 100;
		this.saveBeginIteration = 1000;
	}
	
	public ModelParameters(String path) {
		// TODO
	}
	
	public double alpha;
	public double beta;
	public int maxIterTimes;
	public int topicNum;
	public int saveStep;
	public int saveBeginIteration;
}
