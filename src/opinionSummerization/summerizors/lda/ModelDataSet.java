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
					_termsMap.put(term_text, new Term(term_text));
				}
				Term term = _termsMap.get(term_text);
				term.countIncrease();
			}
			_docs.add(doc);
		}
		
		System.out.printf("read %d doc succ in %s\n", _docs.size(), path);
	}
	
	public void printBrief() {
		System.out.printf("total docs: %d\n", _docs.size());
		for (Document doc : _docs) {
			System.out.printf("%s : word size = %d\n",doc.get_name(), doc.getTermTexts().size());
			for (String term : doc.getTermTexts()) {
				System.out.println(term);
			}
		}
	}
	
	/**
	 * @return the _docs
	 */
	public ArrayList<Document> get_docs() {
		return _docs;
	}

	/**
	 * @return the _termsMap
	 */
	public HashMap<String, Term> get_termsMap() {
		return _termsMap;
	}

	private ArrayList<Document> _docs;
	private HashMap<String, Term> _termsMap;
}
