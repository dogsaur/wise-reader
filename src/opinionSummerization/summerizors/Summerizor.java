package opinionSummerization.summerizors;
import opinionSummerization.utils.SummerizorOutput;

public interface Summerizor {
	public SummerizorOutput Summerize(String text);
	public void set_compression_ratio(double ratio);
}
