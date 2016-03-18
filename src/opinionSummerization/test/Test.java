package opinionSummerization.test;

import opinionSummerization.summerizors.NaiveSummerizor;
import opinionSummerization.summerizors.Summerizor;
import opinionSummerization.utils.Sentence;
import opinionSummerization.utils.SummerizorOutput;
import opinionSummerization.summerizors.lda.*;


/**
 * @description test class
 * @author chaoqiao
 *
 */

public class Test {
	public static String test_text1 = "自从2015年年中以来，中国股民经历了过山车，而且不是一次两次，猴年未到看猴市先行。就在这一轮一轮暴涨暴跌之中，关于证监会主席的去留传言始终不断。结果，谣言又成了遥遥领先的预言，证监会的肖刚时代在一片质疑声中黯然落幕。";
	
	public static String test_text2 = "一直以来，地方政府及企业债务的高杠杆化被认为是在于当前金融市场的结构不合理，在于中国资本市场发展程度过低，从而使得早几年中国企业及地方政府的融资只能通过间接的融资工具银行进行，而不是通过股权融资方式到证券市场进行。比如说，美国企业股权融资所占的比重达80%以上，而债务融资的比重则不到20%，中国的情况正好相反。因此，金融市场的融资结构不合理，被直接指向中国资本市场不发达、股票市场不发达，因此，大力推进股市繁荣，既是化解和转移十几年所积累的巨大金融风险的有效途径，也是保证经济稳定增长的重要的方式。";

	public static String test_text3 = "经济发展趋势，接受记者采访的经济学家普遍认为，2016年仍将继续探底，稳增长面临较大压力，经济走势或呈现前低后高的局面。央行货币政策委员会委员樊纲、中信证券首席经济学家诸建芳表示，我国经济还在这一轮周期的低谷当中运行，还没有完全到达底部，2016年经济还在继续下行。";

	public static void main(String[] args) {
		// testNaiveSummerizor();
		
		System.out.println("start test LDA model");
		ModelParameters param = new ModelParameters(1, 1, 1000, 10);
		ModelDataSet dataset = new ModelDataSet();
		dataset.readDocs("./data/original-docs/");
		dataset.printBrief();
		//Model ldaModel = new Model(param);
		
	}
	
	private static void testNaiveSummerizor() {
		Summerizor naiveSummerizor = new NaiveSummerizor();
		naiveSummerizor.set_compression_ratio(0.3);
		SummerizorOutput out = naiveSummerizor.Summerize(test_text1);
		for (Sentence sentence : out.sentences) {
			System.out.println(sentence.text());
		}
	}
}
