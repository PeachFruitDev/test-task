package test.interview.reporter;

import test.interview.model.Employee;
import test.interview.model.report.FullOrgStructReport;
import test.interview.model.report.ManagerSalaryReport;
import test.interview.model.report.ReportingLineReport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

public class OrgStructReporter {

    private final String salaryMinProp = "managers.salary.min.percent";
    private final String salaryMaxProp = "managers.salary.max.percent";
    private final String lineMaxProp = "managers.line.max";

    /**
     * Method to create a report on an organizational structure based on relations of employees and salary.
     *
     * @param ceo   CEO of a company (main node in a tree hierarchy);
     * @param props Properties to be applied to generate a report
     * @return FullOrgStructReport Full Report with parametrized structure analysis
     * @throws ReporterException with a clarifying message
     */
    public FullOrgStructReport report(Employee ceo, Properties props) {
        if (ceo == null) {
            throw new ReporterException("CEO can't be null");
        }

        // As this app is used for internal analysis, I assume we don't need rigorously check props, only simple check
        // is included
        try {
            var salaryMin = new BigDecimal(props.getProperty(salaryMinProp));
            var salaryMax = new BigDecimal(props.getProperty(salaryMaxProp));
            var lineMax = Integer.valueOf(props.getProperty(lineMaxProp));

            return traverseTreeReporting(ceo, salaryMin, salaryMax, lineMax, 0);
        } catch (NumberFormatException e) {
            throw new ReporterException("Properties should be numeric");
        }
    }

    private FullOrgStructReport traverseTreeReporting(Employee employee, BigDecimal salaryMin, BigDecimal salaryMax, Integer lineMax, Integer curLine) {
        if (employee == null) {
            throw new ReporterException("Employee can't be null");
        }

        var salaryLessReport = new ArrayList<ManagerSalaryReport>();
        var salaryMoreReport = new ArrayList<ManagerSalaryReport>();
        var reportingLineReport = new ArrayList<ReportingLineReport>();

        if (employee.getSubordinates() != null && !employee.getSubordinates().isEmpty()) {
            var salaries = employee.getSubordinates().stream()
                    .map(Employee::getSalary)
                    .filter(Objects::nonNull)
                    .toList();

            if (employee.getSubordinates().size() > salaries.size()) {
                throw new ReporterException("Salary can't be null; Employees' IDs: " + employee.getSubordinates().stream()
                        .filter(subordinate -> subordinate.getSalary() == null)
                        .map(Employee::getId)
                        .map(Object::toString)
                        .collect(Collectors.joining(",")));
            }

            var total = salaries.stream()
                    .reduce(BigDecimal::add)
                    .get();

            var avg = total.divide(BigDecimal.valueOf(salaries.size()), 2, RoundingMode.HALF_EVEN);
            var min = avg.multiply(salaryMin);
            var max = avg.multiply(salaryMax);

            if (employee.getSalary().compareTo(min) < 0) {
                salaryLessReport.add(new ManagerSalaryReport(employee, employee.getSalary().subtract(min)));
            } else if (employee.getSalary().compareTo(max) > 0) {
                salaryMoreReport.add(new ManagerSalaryReport(employee, employee.getSalary().subtract(max)));
            }

            employee.getSubordinates().forEach(subordinate -> {
                var report = traverseTreeReporting(subordinate, salaryMin, salaryMax, lineMax, curLine + 1);
                salaryLessReport.addAll(report.salaryLessReport());
                salaryMoreReport.addAll(report.salaryMoreReport());
                reportingLineReport.addAll(report.reportingLineReport());
            });
        }

        if (curLine > lineMax + 1) {
            reportingLineReport.add(new ReportingLineReport(employee, curLine - lineMax - 1));
        }

        return new FullOrgStructReport(salaryLessReport, salaryMoreReport, reportingLineReport);
    }
}
