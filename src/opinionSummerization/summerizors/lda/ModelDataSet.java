/**
 * @file ModelDataSet.java
 * @author chaoqiao
 * @date 2016年3月17日
 */
package opinionSummerization.summerizors.lda;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import opinionSummerization.utils.Document;
import opinionSummerization.utils.Term;

/**
 * @description data set for LDA model 
 * @author chaoqiao
 *
 */

public class ModelDataSet {
	public ModelDataSet() {
		this._docs = new ArrayList<Document>();
		this._termsMap = new HashMap<String, Term>();
		this._termIndexMap = new HashMap<String, Integer>();
		this._indexToTermList = new ArrayList<Term>();
	}
	
	/**
	 * @description 
	 * read all doc files from a folder 
	 * @param path
	 * doc folder path	
	 * @return void
	 */
	public void readDocs(String path) {
		File folder = new File(path);
		if (!folder.isDirectory()) {
			return;
		}
		
		for(File docFile : folder.listFiles()){
			Document doc = new Document(docFile.getAbsolutePath());
			for (String term_text : doc.getTermTexts()) {
				if (!_termsMap.containsKey(term_text)){
					Term term = new Term(term_text);
					_termsMap.put(term_text, term);
					this._indexToTermList.add(term);
					_termIndexMap.put(term_text, this._termIndexMap.size());
				}
				Term term = _termsMap.get(term_text);
				term.countIncrease();
			}
			_docs.add(doc);
		}
		
		System.out.printf("read %d doc succ in %s\n", _docs.size(), path);
	}
	
	public void printBrief() {
		System.out.printf("total docs : %d\n", _docs.size());
		System.out.printf("total terms : %d\n", this._termIndexMap.size());
		for (Document doc : _docs) {
			System.out.printf("%s : word size = %d\n",doc.getName(), doc.getTermTexts().size());
			for (String term : doc.getTermTexts()) {
				System.out.printf("%s\t", term);
			}
			System.out.println();
		}
	}
	
	/**
	 * @return the _docs
	 */
	public ArrayList<Document> getDocs() {
		return _docs;
	}

	/**
	 * @return the _termsMap
	 */
	public HashMap<String, Term> getTermsMap() {
		return _termsMap;
	}
	
	/**
	 * @description return the index of term 
	 * @param termText
	 * @return int
	 */
	public int getIndexOfTerm(String termText) {
		
		return this._termIndexMap.get(termText);
	}
		
	/**
	 * @description return the term of specific index
	 * @param index
	 * @return Term
	 */
	public Term getTermOfIndex(int index) {
		return this._indexToTermList.get(index);
	}

	private ArrayList<Document> _docs;
	private HashMap<String, Term> _termsMap;
	private HashMap<String, Integer> _termIndexMap;
	private ArrayList<Term> _indexToTermList;
}
