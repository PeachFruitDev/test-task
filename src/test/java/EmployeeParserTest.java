import org.junit.jupiter.api.Test;
import test.interview.model.Employee;
import test.interview.model.parser.EmployeeParser;
import test.interview.model.parser.ParserException;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;


public class EmployeeParserTest {

    private final EmployeeParser parser = new EmployeeParser();

    @Test
    public void parseTest_success() {
        var employees = parser.parse("parseTest_success.csv");
        assertNotNull(employees);
        assertEquals(employees.size(), 2);
        Employee subordinate = new Employee(2, "Joe2", "Doe2", BigDecimal.valueOf(11), null);
        assertEquals(employees.get(1), new Employee(1, "Joe", "Doe", BigDecimal.valueOf(10), Collections.singletonList(subordinate)));
        assertEquals(employees.get(2), subordinate);
    }

    @Test
    public void parseTest_twoCEO_fail() {
        var e = assertThrows(ParserException.class, () -> parser.parse("parseTest_twoCEO_fail.csv"));
        assertEquals(e.getMessage(), "There should be only one CEO");
    }

    @Test
    public void parseTest_sameID_fail() {
        var e = assertThrows(ParserException.class, () -> parser.parse("parseTest_sameID_fail.csv"));
        assertEquals(e.getMessage(), "ID is duplicated");
    }

    @Test
    public void parseTest_selfManager_fail() {
        var e = assertThrows(ParserException.class, () -> parser.parse("parseTest_selfManager_fail.csv"));
        assertEquals(e.getMessage(), "Employees can't be managers to themselves");
    }

    @Test
    public void parseTest_nonExistentManager_fail() {
        var e = assertThrows(ParserException.class, () -> parser.parse("parseTest_nonExistentManager_fail.csv"));
        assertEquals(e.getMessage(), "No existing manager's ID found for an employee");
    }

    @Test
    public void parseTest_corruptHeader_fail() {
        var e = assertThrows(ParserException.class, () -> parser.parse("parseTest_corruptHeader_fail.csv"));
        assertEquals(e.getMessage(), "CSV header is not correct");
    }
}
