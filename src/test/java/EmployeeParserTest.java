import org.junit.jupiter.api.Test;
import test.interview.model.parser.EmployeeParser;
import test.interview.model.parser.ParserException;

import java.io.File;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


public class EmployeeParserTest {

    private final EmployeeParser parser = new EmployeeParser();

    @Test
    public void parserTest_success() {
        var ceo = parser.parse(new File("src/test/resources/parserTest_success.csv"));

        assertNotNull(ceo);

        assertEquals(ceo.getId(), 1);
        assertEquals(ceo.getFirstName(), "Joe");
        assertEquals(ceo.getLastName(), "Doe");
        assertEquals(ceo.getSalary(), BigDecimal.TEN);
        assertNull(ceo.getManagerId());
        assertEquals(ceo.getSubordinates().size(), 1);

        assertEquals(ceo.getSubordinates().get(0).getId(), 2);
        assertEquals(ceo.getSubordinates().get(0).getFirstName(), "Joe2");
        assertEquals(ceo.getSubordinates().get(0).getLastName(), "Doe2");
        assertEquals(ceo.getSubordinates().get(0).getSalary(), new BigDecimal("11"));
        assertEquals(ceo.getSubordinates().get(0).getManagerId(), 1);
        assertEquals(ceo.getSubordinates().get(0).getSubordinates().size(), 0);
    }

    @Test
    public void parserTest_twoCEO_fail() {
        var e = assertThrows(ParserException.class, () -> parser.parse(new File("src/test/resources/parserTest_twoCEO_fail.csv")));

        assertEquals(e.getErrors().size(), 1);
        assertEquals(e.getErrors().get(0), "There should be only one CEO at 4");
    }

    @Test
    public void parserTest_sameID_fail() {
        var e = assertThrows(ParserException.class, () -> parser.parse(new File("src/test/resources/parserTest_sameID_fail.csv")));

        assertEquals(e.getErrors().size(), 1);
        assertEquals(e.getErrors().get(0), "ID is duplicated at 4");
    }

    @Test
    public void parserTest_selfManager_fail() {
        var e = assertThrows(ParserException.class, () -> parser.parse(new File("src/test/resources/parserTest_selfManager_fail.csv")));

        assertEquals(e.getErrors().size(), 2);
        assertTrue(e.getErrors().contains("Employees can't be managers to themselves at 3"));
        assertTrue(e.getErrors().contains("No existing manager's ID found for an employee at 3"));
    }

    @Test
    public void parserTest_nonExistentManager_fail() {
        var e = assertThrows(ParserException.class, () -> parser.parse(new File("src/test/resources/parserTest_nonExistingManager_fail.csv")));
        assertEquals(e.getErrors().size(), 1);
        assertEquals(e.getErrors().get(0), "No existing manager's ID found for an employee at 3");
    }

    @Test
    public void parserTest_corruptHeader_fail() {
        var e = assertThrows(ParserException.class, () -> parser.parse(new File("src/test/resources/parserTest_corruptHeader_fail.csv")));
        assertEquals(e.getErrors().size(), 1);
        assertEquals(e.getErrors().get(0), "CSV header is not correct");
    }

    @Test
    public void parserTest_notANumber_fail() {
        var e = assertThrows(ParserException.class, () -> parser.parse(new File("src/test/resources/parserTest_notANumber_fail.csv")));

        assertEquals(e.getErrors().size(), 3);
        assertTrue(e.getErrors().contains("Manager's ID is not a number at 3"));
        assertTrue(e.getErrors().contains("ID is not a number at 3"));
        assertTrue(e.getErrors().contains("Salary is not a number at 3"));
    }

    @Test
    public void parserTest_emptyFile_fail() {
        var e = assertThrows(ParserException.class, () -> parser.parse(new File("src/test/resources/parserTest_empty_file.csv")));

        assertEquals(e.getErrors().size(), 2);
        assertTrue(e.getErrors().contains("File is empty"));
        assertTrue(e.getErrors().contains("No CEO in file"));
    }
}
