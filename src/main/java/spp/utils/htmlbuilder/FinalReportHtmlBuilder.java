package spp.utils.htmlbuilder;


import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.dto.FinalReportDTO;
import spp.utils.file.TemplateRenderer;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class FinalReportHtmlBuilder {

    private static final String TEMPLATE = "/spp/presentation/templates/final-report.html";

    private FinalReportHtmlBuilder() {
    }

    public static String build(FinalReportDTO report, List<ActivityDTO> activities) throws IOException {
        Map<String, String> values = new HashMap<>();
        values.put("career", TemplateRenderer.escape(report.getCareer()));
        values.put("nrc", TemplateRenderer.escape(report.getNrc()));
        values.put("professorName", TemplateRenderer.escape(report.getProfessorName()));
        values.put("schoolPeriod", TemplateRenderer.escape(report.getSchoolPeriod()));
        values.put("studentName", TemplateRenderer.escape(report.getStudentName()));
        values.put("linkedOrganization", TemplateRenderer.escape(report.getLinkedOrganization()));
        values.put("projectName", TemplateRenderer.escape(report.getProjectName()));
        values.put("totalHours", TemplateRenderer.escape(report.getTotalHours()));
        values.put("reportDate", TemplateRenderer.escape(report.getReportDate()));
        values.put("reportType", TemplateRenderer.escape(report.getReportType()));
        values.put("activityRows", buildActivityRows(activities));

        return TemplateRenderer.render(TEMPLATE, values);

    }

    private static String buildActivityRows(List<ActivityDTO> activities) {
        StringBuilder rows = new StringBuilder();
        for (ActivityDTO activity : activities) {
            rows.append("<tr>")
                    .append("<td>").append(TemplateRenderer.escape(activity.getTitle())).append("</td>")
                    .append("<td>").append(TemplateRenderer.escape(String.valueOf(activity.getProgress()))).append("%</td>")
                    .append("<td>").append(TemplateRenderer.escape(activity.getObservations())).append("</td>")
                    .append("</tr>");
        }
        return rows.toString();

    }

}