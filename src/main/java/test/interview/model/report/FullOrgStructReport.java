package test.interview.model.report;

public record FullOrgStructReport(
        ManagerSalaryLessReport salaryLessReport,
        ManagerSalaryMoreReport salaryMoreReport,
        ReportingLineReport reportingLineReport
) {

    @Override
    public String toString() {
        return "\nFull Structure Efficiency Report\n" +
                salaryLessReport.toString() + "\n" +
                salaryMoreReport.toString() + "\n" +
                reportingLineReport.toString() + "\n";
    }
}
