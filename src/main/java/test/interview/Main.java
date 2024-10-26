package test.interview;

import test.interview.model.parser.EmployeeParser;
import test.interview.reporter.OrgStructReporter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException {
        var parser = new EmployeeParser();
        var ceo = parser.parse(new File("src/main/resources/example.csv"));

        var reporter = new OrgStructReporter();
        var props = new Properties();

        props.load(new FileReader("src/main/resources/report.properties"));
        System.out.println(reporter.report(ceo, props));
    }
}