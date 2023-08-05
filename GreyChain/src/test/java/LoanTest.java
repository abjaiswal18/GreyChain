import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

public class LoanTest {

    @Test
    public void testHasDueDatePassed() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        // Create a past due date
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date pastDueDate = calendar.getTime();

        // Create a future due date
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        Date futureDueDate = calendar.getTime();

        Loan loanWithPastDueDate = new Loan("L1", "C1", "LEN1", 10000, 5000, futureDueDate, pastDueDate, 1, 0.01);
        assertTrue(loanWithPastDueDate.hasDueDatePassed());

        Loan loanWithFutureDueDate = new Loan("L2", "C2", "LEN1", 20000, 10000, futureDueDate, futureDueDate, 1, 0.01);
        assertFalse(loanWithFutureDueDate.hasDueDatePassed());
    }
}
