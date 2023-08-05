
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Loan {
	
	private static final Logger logger = Logger.getLogger(Loan.class.getName());
	
    private String loanId;
    private String customerId;
    private String lenderId;
    public Loan(String loanId, String customerId, String lenderId, double amount, double remainingAmount,
			Date paymentDate, Date dueDate, int interestPerDay, double penaltyPerDay) {
		super();
		this.loanId = loanId;
		this.customerId = customerId;
		this.lenderId = lenderId;
		this.amount = amount;
		this.remainingAmount = remainingAmount;
		this.paymentDate = paymentDate;
		this.dueDate = dueDate;
		this.interestPerDay = interestPerDay;
		this.penaltyPerDay = penaltyPerDay;
	}

	private double amount;
    private double remainingAmount;
    private Date paymentDate;
    private Date dueDate;
    private int interestPerDay;
    private double penaltyPerDay;
    
    
	
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public String getLoanId() {
		return loanId;
	}
	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getLenderId() {
		return lenderId;
	}
	public void setLenderId(String lenderId) {
		this.lenderId = lenderId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getRemainingAmount() {
		return remainingAmount;
	}
	public void setRemainingAmount(double remainingAmount) {
		this.remainingAmount = remainingAmount;
	}
	
	public int getInterestPerDay() {
		return interestPerDay;
	}
	public void setInterestPerDay(int interestPerDay) {
		this.interestPerDay = interestPerDay;
	}
	public double getPenaltyPerDay() {
		return penaltyPerDay;
	}
	public void setPenaltyPerDay(double penaltyPerDay) {
		this.penaltyPerDay = penaltyPerDay;
	}

    public boolean hasDueDatePassed() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        Date currentDate = new Date();
        
        if (currentDate.after(dueDate)) {
            logger.log(Level.WARNING, "Alert: Loan {0} has crossed the due date.", loanId);
            return true;
        }

        return false;
    }
}
