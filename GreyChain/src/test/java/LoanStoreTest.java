import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class LoanStoreTest {

    private LoanStore loanStore;

    @Before
    public void setUp() {
        loanStore = new LoanStore();
    }

    @Test
    public void testAddValidLoan() throws InvalidLoanException, ParseException {
        Loan loan = new Loan("L1", "C1", "LEN1", 10000.0, 10000.0, new SimpleDateFormat("dd/mm/yyyy").parse("05/06/2023"),new SimpleDateFormat("dd/mm/yyyy").parse("05/07/2023"), 1, 0.01);
        assertTrue(loanStore.addLoan(loan));
        assertEquals(1, loanStore.getLoans().size());
    }

    @Test
    public void testAddInvalidLoanDueDate() throws ParseException, InvalidLoanException {
        Loan loan = new Loan("L1", "C1", "LEN1", 10000, 10000, new SimpleDateFormat("dd/mm/yyyy").parse("05/06/2023"),new SimpleDateFormat("dd/mm/yyyy").parse("05/05/2023"), 1, 0.01);
        try {
        	loanStore.addLoan(loan);
        } catch(InvalidLoanException e) {
        	 assertEquals(0, loanStore.getLoans().size());
        }
    }

    @Test
    public void testGroupLoansByInterest() throws ParseException, InvalidLoanException {
        Loan loan1 = new Loan("L1", "C1", "LEN1", 10000, 10000, new SimpleDateFormat("dd/mm/yyyy").parse("05/06/2023"), new SimpleDateFormat("dd/mm/yyyy").parse("05/07/2023"), 1, 0.01);
        Loan loan2 = new Loan("L2", "C2", "LEN2", 20000, 20000, new SimpleDateFormat("dd/mm/yyyy").parse("05/06/2023"), new SimpleDateFormat("dd/mm/yyyy").parse("05/07/2023"), 2, 0.01);
        
        loanStore.addLoan(loan1);
        loanStore.addLoan(loan2);
        
        Map<Integer, List<Loan>> loansByInterest = loanStore.groupLoansByInterest();
        
        assertEquals(2, loansByInterest.size());
        assertEquals(1, loansByInterest.get(1).size());
        assertEquals(1, loansByInterest.get(2).size());
    }

    @Test
    public void testGroupLoansByCustomer() throws ParseException, InvalidLoanException {
        Loan loan1 = new Loan("L1", "C1", "LEN1", 10000, 10000, new SimpleDateFormat("dd/mm/yyyy").parse("05/06/2023"), new SimpleDateFormat("dd/mm/yyyy").parse("05/07/2023"), 1, 0.01);
        Loan loan2 = new Loan("L2", "C1", "LEN2", 20000, 20000, new SimpleDateFormat("dd/mm/yyyy").parse("05/06/2023"), new SimpleDateFormat("dd/mm/yyyy").parse("05/07/2023"), 2, 0.01);

        loanStore.addLoan(loan1);
        loanStore.addLoan(loan2);

        Map<String, List<Loan>> loansByCustomer = loanStore.groupLoansByCustomer();

        assertEquals(1, loansByCustomer.size());
        assertEquals(2, loansByCustomer.get("C1").size());
    }

    @Test
    public void testCalculateTotalRemainingAmountForLender() throws ParseException, InvalidLoanException {
        Loan loan1 = new Loan("L1", "C1", "LEN1", 10000, 5000, new SimpleDateFormat("dd/mm/yyyy").parse("05/06/2023"), new SimpleDateFormat("dd/mm/yyyy").parse("05/07/2023"), 1, 0.01);
        Loan loan2 = new Loan("L2", "C1", "LEN1", 20000, 10000, new SimpleDateFormat("dd/mm/yyyy").parse("05/06/2023"), new SimpleDateFormat("dd/mm/yyyy").parse("05/07/2023"), 1, 0.01);

        loanStore.addLoan(loan1);
        loanStore.addLoan(loan2);

        double totalRemainingAmount = loanStore.calculateTotalRemainingAmountForLender("LEN1");

        assertEquals(15000.0, totalRemainingAmount, 0.001);
    }

    @Test
    public void testCalculateTotalInterestByInterest() throws InvalidLoanException, ParseException {
        Loan loan1 = new Loan("L1", "C1", "LEN1", 10000, 5000, new SimpleDateFormat("dd/mm/yyyy").parse("05/06/2023"), new SimpleDateFormat("dd/mm/yyyy").parse("05/07/2023"), 1, 0.01);
        Loan loan2 = new Loan("L2", "C2", "LEN1", 20000, 10000, new SimpleDateFormat("dd/mm/yyyy").parse("05/06/2023"), new SimpleDateFormat("dd/mm/yyyy").parse("05/07/2023"), 2, 0.01);

        loanStore.addLoan(loan1);
        loanStore.addLoan(loan2);

        Map<String, Double> totalInterestByInterest = loanStore.calculateTotalInterestByInterest();

        assertEquals(50.0, totalInterestByInterest.get("C1"), 0.001);
        assertEquals(200.0, totalInterestByInterest.get("C2"), 0.001);
    }

    @Test
    public void testCalculateTotalPenaltyByCustomer() throws InvalidLoanException, ParseException {
        Loan loan1 = new Loan("L1", "C1", "LEN1", 10000, 5000,new SimpleDateFormat("dd/mm/yyyy").parse("05/06/2022"), new SimpleDateFormat("dd/mm/yyyy").parse("05/07/2023"), 1, 0.01);
        Loan loan2 = new Loan("L2", "C2", "LEN1", 20000, 10000, new SimpleDateFormat("dd/mm/yyyy").parse("05/06/2022"), new SimpleDateFormat("dd/mm/yyyy").parse("05/07/2023"), 2, 0.01);
        Loan loan3 = new Loan("L3", "C2", "LEN2", 50000, 30000, new SimpleDateFormat("dd/mm/yyyy").parse("04/04/2022"), new SimpleDateFormat("dd/mm/yyyy").parse("04/05/2022"), 2, 0.02);
        Loan loan4 = new Loan("L4", "C3", "LEN2", 50000, 30000, new SimpleDateFormat("dd/mm/yyyy").parse("04/04/2022"), new SimpleDateFormat("dd/mm/yyyy").parse("04/05/2022"), 2, 0.02);

        loanStore.addLoan(loan1);
        loanStore.addLoan(loan2);
        loanStore.addLoan(loan3);
        loanStore.addLoan(loan4);

        Map<String, Double> totalPenaltyByCustomer = loanStore.calculateTotalPenaltyByCustomer();

        assertEquals(106.0, totalPenaltyByCustomer.get("C1"), 0.001);
        assertEquals(3680.0, totalPenaltyByCustomer.get("C2"), 0.001);
        assertEquals(3468.0, totalPenaltyByCustomer.get("C3"), 0.001);
    }
}
