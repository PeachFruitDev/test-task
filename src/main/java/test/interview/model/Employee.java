package test.interview.model;

import java.math.BigDecimal;
import java.util.List;

public record Employee(
        Integer id,
        String firstName,
        String lastName,
        BigDecimal salary,
        List<Employee> subordinates
) {

}
