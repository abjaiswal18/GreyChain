import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class LoanStore implements LoanStoreInterface{
    private List<Loan> loans = new ArrayList<>();
    private Map<String, List<Loan>> loansByLender = new HashMap<>();
    private Map<String, List<Loan>> loansByInterest = new HashMap<>();
    private Map<String, List<Loan>> loansByCustomer = new HashMap<>();

    public boolean addLoan(Loan loan) throws InvalidLoanException {
        if (loan.getPaymentDate().compareTo(loan.getDueDate()) > 0) {
            throw new InvalidLoanException("Payment date cannot be greater than due date.");
        }

        loans.add(loan);

        loansByLender.computeIfAbsent(loan.getLenderId(), k -> new ArrayList<Loan>()).add(loan);
        loansByInterest.computeIfAbsent(loan.getCustomerId(), k -> new ArrayList<>()).add(loan);
        loansByCustomer.computeIfAbsent(loan.getCustomerId(), k -> new ArrayList<>()).add(loan);

        return true;
    }
    
    public List<Loan> getLoans() {
        return loans;
    }

    public List<Loan> getLoansByLender(String lenderId) {
        return loansByLender.getOrDefault(lenderId, new ArrayList<>());
    }

    public Map<String, List<Loan>> getLoansByInterest() {
        return loansByInterest;
    }

    public Map<String, List<Loan>> getLoansByCustomer() {
        return loansByCustomer;
    }
    
    @Override
    public double calculateTotalRemainingAmountForLender(String lenderId) {
        List<Loan> lenderLoans = loansByLender.getOrDefault(lenderId, new ArrayList<>());
        double totalRemainingAmount = 0;

        for (Loan loan : lenderLoans) {
            totalRemainingAmount += loan.getRemainingAmount();
        }

        return totalRemainingAmount;
    }
    
    @Override
    public Map<String, Double> calculateTotalInterestByInterest() {
        Map<String, Double> totalInterestByInterest = new HashMap<>();

        for (Map.Entry<String, List<Loan>> entry : loansByInterest.entrySet()) {
            double totalInterest = 0;

            for (Loan loan : entry.getValue()) {
            	double interest = loan.getInterestPerDay();
                totalInterest += loan.getRemainingAmount() * (interest / 100);
            }

            totalInterestByInterest.put(entry.getKey(), totalInterest);
        }

        return totalInterestByInterest;
    }
    
    @Override
    public Map<String, Double> calculateTotalPenaltyByCustomer() {
        Map<String, Double> totalPenaltyByCustomer = new HashMap<>();

        for (Map.Entry<String, List<Loan>> entry : loansByCustomer.entrySet()) {
            String customerId = entry.getKey();
            double totalPenalty = 0;

            for (Loan loan : entry.getValue()) {
            	double penalty = 0;
                if (loan.hasDueDatePassed()) {
                	penalty += loan.getRemainingAmount() * (loan.getPenaltyPerDay() / 100);
                    long diff = new Date().getTime() - loan.getDueDate().getTime();
                    long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    totalPenalty += penalty*days;
                }
            }
            totalPenaltyByCustomer.put(customerId, totalPenalty);
        }

        return totalPenaltyByCustomer;
    }
    
    @Override
    public Map<Integer, List<Loan>> groupLoansByInterest() {
        return loans.stream()
                .collect(Collectors.groupingBy(Loan::getInterestPerDay));
    }
    
    @Override
    public Map<String, List<Loan>> groupLoansByCustomer() {
        return loans.stream()
                .collect(Collectors.groupingBy(Loan::getCustomerId));
    }

}
