package spp.utils.htmlbuilder;

import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.dto.ReportDTO;
import spp.businesslogic.exceptions.FileGenerationException;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.file.TemplateRenderer;
import spp.utils.logger.AppLogger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class FinalReportHtmlBuilder {

    private static final String TEMPLATE_PATH = "/spp/presentation/templates/final-report.html";

    private FinalReportHtmlBuilder() {
    }

    public static String buildFinalReport(ReportDTO report, List<ActivityDTO> activities) throws FileGenerationException {
        String finalRenderedReport = "";
        Map<String, String> templateValues = new HashMap<>();

        String escapedCareer = TemplateRenderer.escape(report.getCareer());
        String escapedNrc = TemplateRenderer.escape(report.getNrc());
        String escapedProfessorName = TemplateRenderer.escape(report.getProfessorName());
        String escapedSchoolPeriod = TemplateRenderer.escape(report.getSchoolPeriod());
        String escapedStudentName = TemplateRenderer.escape(report.getStudentName());
        String escapedLinkedOrganization = TemplateRenderer.escape(report.getLinkedOrganization());
        String escapedProjectName = TemplateRenderer.escape(report.getProjectName());
        String escapedTotalHours = TemplateRenderer.escape(report.getTotalHours());
        String escapedReportDate = TemplateRenderer.escape(report.getReportDate());
        String escapedReportType = TemplateRenderer.escape(report.getReportType());

        String activityRowsHtml = buildActivityRows(activities);

        templateValues.put("career", escapedCareer);
        templateValues.put("nrc", escapedNrc);
        templateValues.put("professorName", escapedProfessorName);
        templateValues.put("schoolPeriod", escapedSchoolPeriod);
        templateValues.put("studentName", escapedStudentName);
        templateValues.put("linkedOrganization", escapedLinkedOrganization);
        templateValues.put("projectName", escapedProjectName);
        templateValues.put("totalHours", escapedTotalHours);
        templateValues.put("reportDate", escapedReportDate);
        templateValues.put("reportType", escapedReportType);
        templateValues.put("activityRows", activityRowsHtml);

        try {
            finalRenderedReport = TemplateRenderer.render(TEMPLATE_PATH, templateValues);
        } catch (IOException e) {
            AppLogger.log(ExceptionLevel.ERROR, e);
            throw new FileGenerationException("Error generando archivo de reporte final", e);
        }


        return finalRenderedReport;
    }

    private static String buildActivityRows(List<ActivityDTO> activities) {
        StringBuilder rowsBuilder = new StringBuilder();

        for (ActivityDTO currentActivity : activities) {
            String escapedTitle = TemplateRenderer.escape(currentActivity.getTitle());

            String progressString = String.valueOf(currentActivity.getProgress());
            String escapedProgress = TemplateRenderer.escape(progressString);

            String escapedObservations = TemplateRenderer.escape(currentActivity.getObservations());

            rowsBuilder.append("<tr>");

            rowsBuilder.append("<td>");
            rowsBuilder.append(escapedTitle);
            rowsBuilder.append("</td>");

            rowsBuilder.append("<td>");
            rowsBuilder.append(escapedProgress);
            rowsBuilder.append("%</td>");

            rowsBuilder.append("<td>");
            rowsBuilder.append(escapedObservations);
            rowsBuilder.append("</td>");

            rowsBuilder.append("</tr>");
        }

        String finalRows = rowsBuilder.toString();
        return finalRows;
    }
}