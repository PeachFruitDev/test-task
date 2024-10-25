package test.interview.reporter;

import test.interview.model.Employee;
import test.interview.model.report.FullOrgStructReport;

import java.util.Properties;

public class OrgStructReporter {

    public FullOrgStructReport report(Employee ceo, Properties props) {
        if (ceo == null) {
            throw new ReporterException("CEO can't be null");
        }
        return new FullOrgStructReport(null, null, null);
    }
}
