/**
 * @file Model.java
 * @author chaoqiao
 * @date 2016年3月17日
 */
package opinionSummerization.summerizors.lda;

import java.util.ArrayList;
import java.util.HashMap;

import opinionSummerization.utils.Document;
import opinionSummerization.utils.FileUtil;
import opinionSummerization.utils.Sentence;

/**
 * @description LDA Model
 * @author chaoqiao
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class Model {

	File resultFolder;
	int[][] doc; // word index array
	int V, K, M; // vocabulary size, topic number, document number
	int[][] z; // topic label array
	double alpha; // doc-topic dirichlet prior parameter
	double beta; // topic-word dirichlet prior parameter
	int[][] nmk; // given document m, count times of topic k. M*K
	int[][] nkt; // given topic k, count times of term t. K*V
	int[] nmkSum; // Sum for each row in nmk
	int[] nktSum; // Sum for each row in nkt
	double[][] phi; // Parameters for topic-word distribution K*V
	double[][] theta; // Parameters for doc-topic distribution M*K
	int iterations; // Times of iterations
	int saveStep; // The number of iterations between two saving
	int beginSaveIters; // Begin save model at this iteration

	String[] termList;

	public static Model loadModel(String dumpFilename) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FileReader fr;
		try {
			fr = new FileReader(dumpFilename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		Model model = gson.fromJson(new JsonReader(fr), Model.class);
		return model;
	}
	public Model() {

	}

	public Model(ModelParameters params) {
		alpha = params.alpha;
		beta = params.beta;
		iterations = params.maxIterTimes;
		K = params.topicNum;
		saveStep = params.saveStep;
		beginSaveIters = params.saveBeginIteration;
	}

	public void initializeModel(ModelDataSet docSet) {
		M = docSet.getDocs().size();
		System.out.println(M);
		V = docSet.getTermsMap().size();
		nmk = new int[M][K];
		nkt = new int[K][V];
		nmkSum = new int[M];
		nktSum = new int[K];
		phi = new double[K][V];
		theta = new double[M][K];

		// initialize documents index array
		doc = new int[M][];
		for (int m = 0; m < M; m++) {

			Set<String> doc_words_set = docSet.getDocs().get(m).getTermTexts();
			int N = doc_words_set.size();
			doc[m] = new int[N];

			int n = 0;
			for (String word : doc_words_set) {
				doc[m][n] = docSet.getIndexOfTerm(word);
				n++;
			}
		}

		// initialize topic lable z for each word
		z = new int[M][];
		for (int m = 0; m < M; m++) {
			int N = docSet.getDocs().get(m).getTermTexts().size();
			z[m] = new int[N];
			for (int n = 0; n < N; n++) {
				int initTopic = (int) (Math.random() * K);// From 0 to K - 1
				z[m][n] = initTopic;
				// number of words in doc m assigned to topic initTopic add 1
				nmk[m][initTopic]++;
				// number of terms doc[m][n] assigned to topic initTopic add 1
				nkt[initTopic][doc[m][n]]++;
				// total number of words assigned to topic initTopic add 1
				nktSum[initTopic]++;
			}
			// total number of words in document m is N
			nmkSum[m] = N;
		}

		termList = new String[V];
		for (int i = 0; i < V; i++) {
			termList[i] = docSet.getTermOfIndex(i).Text();
		}
	}

	public void inferenceModel(ModelDataSet docSet) throws IOException {
		// make sure total iterations greater than beginSaveIters
		if (iterations < beginSaveIters) {
			System.err.println("Error: the number of iterations should be larger than " + (beginSaveIters));
			System.exit(0);
		}

		// make result folder
		SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd_HH：mm");// 设置日期格式
		String resPath = "./data/train-results/" + df.format(new Date()) + "/";
		this.resultFolder = new File(resPath);
		if (!resultFolder.isDirectory() && !resultFolder.mkdir()) {
			System.out.println("can not make result folder : " + resultFolder.getName());
			return;
		}

		for (int i = 0; i < iterations; i++) {
			System.out.println("Iteration " + i);
			if ((i >= beginSaveIters) && (((i - beginSaveIters) % saveStep) == 0)) {
				// Saving the model
				System.out.println("dump model at iteration " + i + " ... ");

				// Firstly update parameters
				updateEstimatedParameters();

				// Secondly print model variables
				// saveIteratedModel(i, docSet);
				this.dump("iter-" + i + ".json");
			}

			// Use Gibbs Sampling to update z[][]
			for (int m = 0; m < M; m++) {
				int N = docSet.getDocs().get(m).getTermTexts().size();
				for (int n = 0; n < N; n++) {
					// Sample from p(z_i|z_-i, w)
					int newTopic = sampleTopicZ(m, n);
					z[m][n] = newTopic;
				}
			}
		}
	}

	private void updateEstimatedParameters() {
		// TODO Auto-generated method stub
		for (int k = 0; k < K; k++) {
			for (int t = 0; t < V; t++) {
				phi[k][t] = (nkt[k][t] + beta) / (nktSum[k] + V * beta);
			}
		}

		for (int m = 0; m < M; m++) {
			for (int k = 0; k < K; k++) {
				theta[m][k] = (nmk[m][k] + alpha) / (nmkSum[m] + K * alpha);
			}
		}
	}

	private int sampleTopicZ(int m, int n) {
		// TODO Auto-generated method stub
		// Sample from p(z_i|z_-i, w) using Gibbs upde rule

		// Remove topic label for w_{m,n}
		int oldTopic = z[m][n];
		nmk[m][oldTopic]--;
		nkt[oldTopic][doc[m][n]]--;
		nmkSum[m]--;
		nktSum[oldTopic]--;

		// Compute p(z_i = k|z_-i, w)
		double[] p = new double[K];
		for (int k = 0; k < K; k++) {
			p[k] = (nkt[k][doc[m][n]] + beta) / (nktSum[k] + V * beta) * (nmk[m][k] + alpha) / (nmkSum[m] + K * alpha);
		}

		// Sample a new topic label for w_{m, n} like roulette
		// Compute cumulated probability for p
		for (int k = 1; k < K; k++) {
			p[k] += p[k - 1];
		}
		double u = Math.random() * p[K - 1]; // p[] is unnormalised
		int newTopic;
		for (newTopic = 0; newTopic < K; newTopic++) {
			if (u < p[newTopic]) {
				break;
			}
		}

		// Add new topic label for w_{m, n}
		nmk[m][newTopic]++;
		nkt[newTopic][doc[m][n]]++;
		nmkSum[m]++;
		nktSum[newTopic]++;
		return newTopic;
	}
	
	List<ArrayList<Integer>> sortTopicTerm() {
		List<ArrayList<Integer>> sortedIndex = new ArrayList<ArrayList<Integer>>();
		for (int k = 0; k < K; k++) {
			ArrayList<Integer> arrayIndex = new ArrayList<Integer>();
			for (int i = 0; i < V; i++) {
				arrayIndex.add(new Integer(i));
			}
			
			final int kk = k;
			Comparator<Integer> comp = 
					(Integer lhs, Integer rhs) -> {
						if (phi[kk][lhs] == phi[kk][rhs]) 
							return 0;
						else 
							return phi[kk][lhs] > phi[kk][rhs] ? -1 : 1;
					};
			System.out.println(V);
			Collections.sort(arrayIndex, comp);
			for (int i = 0; i < 20; i++) {
				System.out.print(arrayIndex.get(i) + " ");
			}
			System.out.println();
			sortedIndex.add(arrayIndex);
		}
		
		return sortedIndex;
	}
	
	public String[][] getTopicTopNTerm(int n) {
		List<ArrayList<Integer>> sortedIndex = this.sortTopicTerm();
		String[][] result = new String[K][n];
		for (int k = 0; k < K; k++) {
			for (int i = 0; i < n; i++) {
				int index = sortedIndex.get(k).get(i);
				result[k][i] = this.termList[index];
			}
		}
		
		return result;
	}

	public void dump(String filename) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(this);
		FileUtil.writeString(resultFolder.getAbsolutePath() + "/" + filename, json);
		
		String[][] topicTerm = this.getTopicTopNTerm(20);
		json = gson.toJson(topicTerm);
		FileUtil.writeString(resultFolder.getAbsolutePath() + "/terms-" + filename, json);
	}

	/*--------------part II : analyze new document ---------*/

	private HashMap<String, Integer> termIndex;

	public void recoverTermIndex() {
		if (termIndex != null) {
			return;
		}
		termIndex = new HashMap<String, Integer>();
		for (int i = 0; i < termList.length; i++) {
			termIndex.put(termList[i], i);
		}
	}

	public int sampleZ(int t, int[][] cnt_kt, int[] cnt_mk, int old_topic) {

		cnt_kt[old_topic][t]--;
		cnt_mk[old_topic]--;

		// Compute p(z_i = k|z_-i, w)
		double[] p = new double[K];
		for (int k = 0; k < K; k++) {
			p[k] = (nkt[k][t] + beta) / (nktSum[k] + V * beta) * (cnt_mk[k] + alpha) / (K + K * alpha);
		}

		// Sample a new topic label for w_{m, n} like roulette
		// Compute cumulated probability for p
		for (int k = 1; k < K; k++) {
			p[k] += p[k - 1];
		}
		double u = Math.random() * p[K - 1]; // p[] is unnormalised
		int newTopic;
		for (newTopic = 0; newTopic < K; newTopic++) {
			if (u < p[newTopic]) {
				break;
			}
		}

		// Add new topic label for w_{m, n}
		cnt_mk[newTopic]++;
		cnt_kt[newTopic][t]++;

		return newTopic;
	}

	public double[] analyze(Document doc) {
		this.recoverTermIndex();

		int[][] cnt_kt = new int[K][V];
		int[] cnt_mk = new int[K];
		int[] new_z = new int[V];

		// assign random topic for every term in doc
		for (String term_text : doc.getTermTexts()) {
			if (termIndex.containsKey(term_text)) {
				int t = termIndex.get(term_text);
				new_z[t] = (int) (Math.random() * K);
				cnt_mk[new_z[t]] += 1;
				cnt_kt[new_z[t]][t] += 1;
			}
		}

		// sample topic for every term in doc
		for (int iter = 0; iter < 10000; iter++) {
			for (String term_text : doc.getTermTexts()) {
				if (termIndex.containsKey(term_text)) {
					int t = termIndex.get(term_text);
					int new_topic = sampleZ(t, cnt_kt, cnt_mk, new_z[t]);

					new_z[t] = new_topic;

				}
			}
		}

		// calc theta for doc
		double[] new_theta = new double[K];
		for (int k = 0; k < K; k++) {
			new_theta[k] = (cnt_mk[k] + alpha) / (doc.getTermTexts().size() + K * alpha);
		}
		// theta[m][k] = (nmk[m][k] + alpha) / (nmkSum[m] + K * alpha);

		// probability of every topic for doc
		double[] p = new double[K];

		for (int k = 0; k < K; k++) {
			p[k] = new_theta[k];
		}
		return p;
	}

	public double[] analyze(Sentence sentence, double[] p_doc) {
		this.recoverTermIndex();
		if (p_doc.length != K) {
			System.out.printf("p_doc length [%d] not equal to K [%d]\n", doc.length, K);
		}

		// probability of every topic for sentence
		double[] p = new double[K];
		
		for (String term_text : sentence.getTermTexts()) {
			if (termIndex.containsKey(term_text)) {
				int t = termIndex.get(term_text);
				for (int k = 0; k < K; k++) {
					p[k] += phi[k][t] * p_doc[k];
				}
			}
		}
		double p_sum = 0;
		for (int k = 0; k < K; k++) {
			p_sum += p[k];
		}
		
		for (int k = 0; k < K; k++) {
			p[k] = p[k] / p_sum;
		}

		return p;
	}
}
