package test.interview.model.report;

import test.interview.model.Employee;

import java.math.BigDecimal;

public record ManagerSalaryReport(
        Employee employee,
        BigDecimal difference
) {

    @Override
    public String toString() {
        return "ID " + employee.getId() + ", " +
                employee.getFirstName() + " " +
                employee.getLastName() + ", " +
                difference;
    }
}
