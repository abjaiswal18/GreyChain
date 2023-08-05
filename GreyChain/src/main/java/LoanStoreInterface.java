import java.util.List;
import java.util.Map;

public interface LoanStoreInterface {
	
	 public double calculateTotalRemainingAmountForLender(String lenderId);
	 public Map<String, Double> calculateTotalInterestByInterest();
	 public Map<String, Double> calculateTotalPenaltyByCustomer();
	 public Map<Integer, List<Loan>> groupLoansByInterest();
	 public Map<String, List<Loan>> groupLoansByCustomer();
}