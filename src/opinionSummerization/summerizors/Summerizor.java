package opinionSummerization.summerizors;
import opinionSummerization.utils.SummerizorOutput;
import opinionSummerization.utils.Document;

public interface Summerizor {
	public SummerizorOutput summerize(String text);
	public SummerizorOutput summerize(Document doc);
	public void setCompressionRatio(double ratio);
}
