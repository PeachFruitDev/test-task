package test.interview.model.report;

import test.interview.model.Employee;

public record ReportingLineReport(
        Employee employee,
        Integer difference
) {

    @Override
    public String toString() {
        return "ID " + employee.getId() + ", " +
                employee.getFirstName() + " " +
                employee.getLastName() + ", " +
                difference;
    }
}
