package test.interview.model;

import java.math.BigDecimal;
import java.util.List;

// The usage of the record is rather a bad practice as subordinates list is not immutable and will be changed during
// parsing.

public class Employee {
    private final Integer id;
    private final String firstName;
    private final String lastName;
    private final BigDecimal salary;
    private final Integer managerId;
    private final List<Employee> subordinates;

    public Employee(Integer id, String firstName, String lastName, BigDecimal salary, Integer managerId, List<Employee> subordinates) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
        this.subordinates = subordinates;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public List<Employee> getSubordinates() {
        return subordinates;
    }
}
