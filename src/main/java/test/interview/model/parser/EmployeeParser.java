package test.interview.model.parser;

import test.interview.model.Employee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class EmployeeParser {

    public Map<Integer, Employee> parse(String fileName) {
        try (var reader = new BufferedReader(new FileReader(fileName))) {
            return Collections.emptyMap();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new ParserException("Parsing has not succeeded");
        }
    }
}
