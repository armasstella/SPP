package spp.utils.htmlbuilder;

import spp.businesslogic.dto.PartialReportActivityDTO;
import spp.businesslogic.dto.PartialReportDTO;
import spp.businesslogic.exceptions.FileGenerationException;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.file.TemplateRenderer;
import spp.utils.logger.AppLogger;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PartialReportHtmlBuilder {

    private static final String TEMPLATE_PATH = "/spp/presentation/templates/partial-report.html";
    private static final int TOTAL_WEEKS = 8;

    private PartialReportHtmlBuilder() {
    }

    public static String buildPartialReport(PartialReportDTO partialReport) throws FileGenerationException {
        String renderedTemplate = "";
        Map<String, String> templateValues = new HashMap<String, String>();

        templateValues.put("career", TemplateRenderer.escape(partialReport.getCareer()));
        templateValues.put("nrc", TemplateRenderer.escape(partialReport.getNrc()));
        templateValues.put("professorName", TemplateRenderer.escape(partialReport.getProfessorName()));
        templateValues.put("schoolPeriod", TemplateRenderer.escape(partialReport.getSchoolPeriod()));
        templateValues.put("studentName", TemplateRenderer.escape(partialReport.getStudentName()));
        templateValues.put("linkedOrganization", TemplateRenderer.escape(partialReport.getLinkedOrganization()));
        templateValues.put("projectName", TemplateRenderer.escape(partialReport.getProjectName()));
        templateValues.put("reportPeriodAndHours", buildReportPeriodAndHours(partialReport));
        templateValues.put("reportDate", TemplateRenderer.escape(partialReport.getReportDate()));
        templateValues.put("reportNumber", TemplateRenderer.escape(partialReport.getReportNumber()));
        templateValues.put("objective", TemplateRenderer.escape(partialReport.getObjective()));
        templateValues.put("methodology", TemplateRenderer.escape(partialReport.getMethodology()));
        templateValues.put("results", TemplateRenderer.escape(partialReport.getResults()));
        templateValues.put("observations", TemplateRenderer.escape(partialReport.getObservations()));
        templateValues.put("activityRows", buildActivityRows(partialReport.getActivities()));

        try {
            renderedTemplate = TemplateRenderer.render(TEMPLATE_PATH, templateValues);
        } catch (IOException ioException) {
            AppLogger.log(ExceptionLevel.ERROR, ioException);
            throw new FileGenerationException("Error generando archivo del informe parcial", ioException);
        }

        return renderedTemplate;
    }

    private static String buildReportPeriodAndHours(PartialReportDTO partialReport) {
        String escapedPeriod = TemplateRenderer.escape(partialReport.getReportPeriod());
        String escapedHours = TemplateRenderer.escape(partialReport.getCoveredHours());
        return escapedPeriod + " - " + escapedHours + " horas cubiertas";
    }

    private static String buildActivityRows(List<PartialReportActivityDTO> activities) {
        StringBuilder rowsBuilder = new StringBuilder();

        for (PartialReportActivityDTO activity : activities) {
            String escapedName = TemplateRenderer.escape(activity.getName());
            String escapedDescription = TemplateRenderer.escape(activity.getDescription());
            String escapedPlannedTime = TemplateRenderer.escape(activity.getPlannedTime());
            String escapedRealTime = TemplateRenderer.escape(activity.getRealTime());
            int activityWeek = activity.getWeekNumber();

            rowsBuilder.append("<tr>");
            rowsBuilder.append("<td rowspan='2' class='activity-cell'><b>");
            rowsBuilder.append(escapedName);
            rowsBuilder.append("</b><br/>");
            rowsBuilder.append(escapedDescription);
            rowsBuilder.append("</td>");
            rowsBuilder.append("<td class='center'>Plan</td>");
            rowsBuilder.append(buildWeekCells(activityWeek, escapedPlannedTime));
            rowsBuilder.append("</tr>");

            rowsBuilder.append("<tr>");
            rowsBuilder.append("<td class='center'>Real</td>");
            rowsBuilder.append(buildWeekCells(activityWeek, escapedRealTime));
            rowsBuilder.append("</tr>");
        }

        return rowsBuilder.toString();
    }

    private static String buildWeekCells(int activityWeek, String value) {
        StringBuilder cellsBuilder = new StringBuilder();

        for (int weekColumn = 1; weekColumn <= TOTAL_WEEKS; weekColumn++) {
            if (weekColumn == activityWeek) {
                cellsBuilder.append("<td class='center'>");
                cellsBuilder.append(value);
                cellsBuilder.append("</td>");
            } else {
                cellsBuilder.append("<td></td>");
            }
        }

        return cellsBuilder.toString();
    }
}
