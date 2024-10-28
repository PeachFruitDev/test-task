package test.interview.model.report;

import java.util.List;
import java.util.stream.Collectors;

public record FullOrgStructReport(
        List<ManagerSalaryReport> salaryLessReport,
        List<ManagerSalaryReport> salaryMoreReport,
        List<ReportingLineReport> reportingLineReport
) {

    @Override
    public String toString() {
        String reportDelimiter = "\n---\n";
        var builder = new StringBuilder();
        if (!salaryLessReport.isEmpty()) {
            builder.append("Managers Earn Less Than Needed:\n");
            builder.append(salaryLessReport.stream()
                    .map(ManagerSalaryReport::toString)
                    .collect(Collectors.joining("\n")));
            builder.append(reportDelimiter);
        }
        if (!salaryMoreReport.isEmpty()) {
            builder.append("Managers Earn More Than Needed:\n");
            builder.append(salaryMoreReport.stream()
                    .map(ManagerSalaryReport::toString)
                    .collect(Collectors.joining("\n")));
            builder.append(reportDelimiter);
        }
        if (!reportingLineReport.isEmpty()) {
            builder.append("Reporters Line Than Needed:\n");
            builder.append(reportingLineReport.stream()
                    .map(ReportingLineReport::toString)
                    .collect(Collectors.joining("\n")));
            builder.append(reportDelimiter);
        }

        if (builder.isEmpty()) {
            return "Everything is alright!";
        }

        return builder.toString();
    }
}
