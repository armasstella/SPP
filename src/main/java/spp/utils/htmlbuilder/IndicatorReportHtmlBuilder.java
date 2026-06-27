package spp.utils.htmlbuilder;


import spp.businesslogic.dto.IndicatorReportDTO;
import spp.utils.file.TemplateRenderer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public final class IndicatorReportHtmlBuilder {

    private static final String TEMPLATE = "/spp/presentation/templates/indicator-report.html";

    private IndicatorReportHtmlBuilder() {
    }

    public static String buildIndicatorReport(IndicatorReportDTO indicatorReportDTO) throws IOException {
        Map<String, String> values = new HashMap<>();

        values.put("generationDate", TemplateRenderer.escape(indicatorReportDTO.getGenerationDate()));
        values.put("totalStudents", String.valueOf(indicatorReportDTO.getTotalStudents()));
        values.put("totalMale", String.valueOf(indicatorReportDTO.getTotalMale()));
        values.put("totalFemale", String.valueOf(indicatorReportDTO.getTotalFemale()));
        values.put("totalIndigenous", String.valueOf(indicatorReportDTO.getTotalIndigenous()));
        values.put("totalNonIndigenous", String.valueOf(indicatorReportDTO.getTotalNonIndigenous()));

        String genderFilter =
                indicatorReportDTO.getAppliedFilters().getGender() != null ? String.valueOf(indicatorReportDTO.getAppliedFilters().getGender()) : "Todos";
        String periodFilter =
                indicatorReportDTO.getAppliedFilters().getPeriod() != null ? indicatorReportDTO.getAppliedFilters().getPeriod() : "Todos";
        String langFilter =
                indicatorReportDTO.getAppliedFilters().getIndigenousLanguage() != null ? String.valueOf(indicatorReportDTO.getAppliedFilters().getIndigenousLanguage()) : "Todos";

        String ageFilter = "Todas";
        if (indicatorReportDTO.getAppliedFilters().getMinAge() != null && indicatorReportDTO.getAppliedFilters().getMaxAge() != null) {
            ageFilter = indicatorReportDTO.getAppliedFilters().getMinAge() + " a " + indicatorReportDTO.getAppliedFilters().getMaxAge() + " años";
        } else if (indicatorReportDTO.getAppliedFilters().getMinAge() != null) {
            ageFilter = "Mayor a " + indicatorReportDTO.getAppliedFilters().getMinAge() + " años";
        } else if (indicatorReportDTO.getAppliedFilters().getMaxAge() != null) {
            ageFilter = "Menor a " + indicatorReportDTO.getAppliedFilters().getMaxAge() + " años";
        }

        values.put("filterGender", TemplateRenderer.escape(genderFilter));
        values.put("filterAge", TemplateRenderer.escape(ageFilter));
        values.put("filterPeriod", TemplateRenderer.escape(periodFilter));
        values.put("filterLanguage", TemplateRenderer.escape(langFilter));

        return TemplateRenderer.render(TEMPLATE, values);

    }

}
