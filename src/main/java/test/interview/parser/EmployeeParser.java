package test.interview.parser;

import test.interview.model.Employee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class EmployeeParser {

    private final List<String> header = List.of("Id", "firstName", "lastName", "salary", "managerId");

    /**
     * Parses a CSV file with list of employees. Method throws a sufficiently informative exception with CSV issues analyzed.
     *
     * @param file file to be parsed
     * @return Employee hierarchical tree
     * @throws ParserException an exception on parsing problems with a list of issues
     */
    public Employee parse(File file) {
        var employees = new HashMap<Integer, Employee>();
        var errors = new ArrayList<String>();
        Employee ceo = null;

        try (var reader = new BufferedReader(new FileReader(file))) {
            int curLine = 1;
            checkHeader(reader.readLine(), errors);
            String line;

            while ((line = reader.readLine()) != null) {
                curLine++;
                var curErrorCount = errors.size();

                var employee = parseLine(line, curLine, employees, errors);

                if (curErrorCount < errors.size()) {
                    continue;
                }

                if (ceo == null && employee.getManagerId() == null) {
                    ceo = employee;
                } else if (employee.getManagerId() != null) {
                    employees.get(employee.getManagerId()).getSubordinates().add(employee);
                }

                employees.put(employee.getId(), employee);
            }

            if (ceo == null) {
                errors.add("No CEO in file");
            }

            if (!errors.isEmpty()) {
                System.out.println(errors);
                throw new ParserException("Parsing has not succeeded", errors);
            }

            return ceo;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new ParserException("Parsing has not succeeded due to IO exception", errors, e);
        }
    }

    private Employee parseLine(String line, Integer curLine, Map<Integer, Employee> employees, ArrayList<String> errors) {
        if (line.isEmpty()) {
            errors.add("Empty line at " + curLine);
        }
        var splitLine = line.split("\\s*,\\s*");
        if (splitLine.length != 5 && splitLine.length != 4) {
            errors.add("Incorrect number of properties at " + curLine);
        }

        if (splitLine.length == 4 && !employees.isEmpty()) {
            errors.add("There should be only one CEO at " + curLine);
        }

        Integer id = null;
        try {
            id = Integer.valueOf(splitLine[0]);
            if (employees.containsKey(id)) {
                errors.add("ID is duplicated at " + curLine);
            }
        } catch (NumberFormatException e) {
            errors.add("ID is not a number at " + curLine);
        }

        String firstName = splitLine[1];
        String lastName = splitLine[2];

        BigDecimal salary = null;
        try {
            salary = new BigDecimal(splitLine[3]);
        } catch (NumberFormatException e) {
            errors.add("Salary is not a number at " + curLine);
        }

        Integer managerId = null;
        if (splitLine.length == 5) {
            try {
                managerId = Integer.valueOf(splitLine[4]);
                if (!employees.containsKey(managerId)) {
                    errors.add("No existing manager's ID found for an employee at " + curLine);
                }
            } catch (NumberFormatException e) {
                errors.add("Manager's ID is not a number at " + curLine);
            }
        }

        if (id != null && Objects.equals(managerId, id)) {
            errors.add("Employees can't be managers to themselves at " + curLine);
        }

        return new Employee(id, firstName, lastName, salary, managerId, new ArrayList<>());
    }

    private void checkHeader(String line, List<String> errors) {
        if (line == null) {
            errors.add("File is empty");
            return;
        }

        var parsedHeader = Arrays.asList(line.split("\\s*,\\s*"));
        if (!parsedHeader.equals(header)) {
            errors.add("CSV header is not correct");
        }
    }
}
