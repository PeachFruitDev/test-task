import org.junit.jupiter.api.Test;
import test.interview.model.Employee;
import test.interview.reporter.OrgStructReporter;
import test.interview.reporter.ReporterException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;

public class OrgStructReporterTest {

    private final OrgStructReporter reporter = new OrgStructReporter();
    private final Properties props = new Properties() {{
        setProperty("managers.salary.min.percent", "1.2");
        setProperty("managers.salary.max.percent", "1.5");
        setProperty("managers.line.max", "1");
    }};

    @Test
    public void reporterTest_allGood_success() {
        var subordinate1 = new Employee(2,
                "Joe2",
                "Doe2",
                BigDecimal.TEN,
                1,
                emptyList());

        var subordinate2 = new Employee(3,
                "Joe3",
                "Doe3",
                BigDecimal.ONE,
                1,
                emptyList());

        var ceo =
                new Employee(1,
                        "Joe",
                        "Doe",
                        new BigDecimal("7.15"),
                        null,
                        List.of(subordinate1, subordinate2));

        var report = reporter.report(ceo, props);

        assertEquals(report.salaryLessReport().size(), 0);
        assertEquals(report.salaryMoreReport().size(), 0);
        assertEquals(report.reportingLineReport().size(), 0);

        assertEquals(report.toString(), "Everything is alright!");
    }

    @Test
    public void reporterTest_onlyCeo_success() {
        var ceo =
                new Employee(1,
                        "Joe",
                        "Doe",
                        new BigDecimal("7.15"),
                        null,
                        emptyList());

        var report = reporter.report(ceo, props);

        assertEquals(report.salaryLessReport().size(), 0);
        assertEquals(report.salaryMoreReport().size(), 0);
        assertEquals(report.reportingLineReport().size(), 0);

        assertEquals(report.toString(), "Everything is alright!");

    }

    @Test
    public void reporterTest_lessSalary_success() {
        var subordinate1 = new Employee(2,
                "Joe2",
                "Doe2",
                BigDecimal.TEN,
                1,
                emptyList());

        var subordinate2 = new Employee(3,
                "Joe3",
                "Doe3",
                BigDecimal.ONE,
                1,
                emptyList());

        var ceo = new Employee(1,
                        "Joe",
                        "Doe",
                        new BigDecimal("1.15"),
                        null,
                        List.of(subordinate1, subordinate2));

        var report = reporter.report(ceo, props);

        assertEquals(report.salaryLessReport().size(), 1);
        assertEquals(report.salaryMoreReport().size(), 0);
        assertEquals(report.reportingLineReport().size(), 0);

        assertEquals(report.salaryLessReport().get(0).employee(), ceo);
        assertTrue(report.salaryLessReport().get(0).difference().compareTo(new BigDecimal("-5.45")) == 0);
    }

    @Test
    public void reporterTest_moreSalary_success() {
        var subordinate1 = new Employee(2,
                "Joe2",
                "Doe2",
                BigDecimal.TEN,
                1,
                emptyList());

        var subordinate2 = new Employee(3,
                "Joe3",
                "Doe3",
                BigDecimal.ONE,
                1,
                emptyList());

        var ceo = new Employee(1,
                        "Joe",
                        "Doe",
                        new BigDecimal("8.8"),
                        null,
                        List.of(subordinate1, subordinate2));

        var report = reporter.report(ceo, props);

        assertEquals(report.salaryLessReport().size(), 0);
        assertEquals(report.salaryMoreReport().size(), 1);
        assertEquals(report.reportingLineReport().size(), 0);

        assertEquals(report.salaryMoreReport().get(0).employee(), ceo);
        assertTrue(report.salaryMoreReport().get(0).difference().compareTo(new BigDecimal("0.55")) == 0);
    }

    @Test
    public void reporterTest_reporterLine_success() {

        var subordinate3 = new Employee(4,
                "Joe3",
                "Doe3", new BigDecimal("6"),
                3,
                emptyList());

        var subordinate2 = new Employee(3,
                "Joe3",
                "Doe3", new BigDecimal("8"),
                2,
                List.of(subordinate3));

        var subordinate1 = new Employee(2,
                "Joe2",
                "Doe2",
                BigDecimal.TEN,
                1,
                List.of(subordinate2));

        var ceo = new Employee(1,
                        "Joe",
                        "Doe",
                        new BigDecimal("13"),
                        null,
                        List.of(subordinate1));

        var report = reporter.report(ceo, props);

        assertEquals(report.salaryLessReport().size(), 0);
        assertEquals(report.salaryMoreReport().size(), 0);
        assertEquals(report.reportingLineReport().size(), 1);

        assertEquals(report.reportingLineReport().get(0).employee(), subordinate3);
        assertEquals(report.reportingLineReport().get(0).difference(), 1);
    }

    @Test
    public void reporterTest_null_success() {
        Employee ceo = null;

        var e = assertThrows(ReporterException.class, () -> reporter.report(ceo, props));
        assertEquals(e.getMessage(), "CEO can't be null");
    }

    @Test
    public void reporterTest_nullSalary_success() {
        var subordinate1 = new Employee(2, "Joe2", "Doe2", null, 1, emptyList());

        var subordinate2 = new Employee(3, "Joe3", "Doe3", null, 1, emptyList());

        var ceo = new Employee(1,
                "Joe",
                "Doe",
                new BigDecimal("7.15"),
                null,
                List.of(subordinate1, subordinate2));

        var e = assertThrows(ReporterException.class, () -> reporter.report(ceo, props));
        assertEquals(e.getMessage(), "Salary can't be null; Employees' IDs: 2,3");
    }
}
