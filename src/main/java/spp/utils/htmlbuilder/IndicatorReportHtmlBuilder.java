package spp.utils.htmlbuilder;

import spp.businesslogic.dto.IndicatorReportDTO;
import spp.businesslogic.exceptions.FileGenerationException;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.file.TemplateRenderer;
import spp.utils.logger.AppLogger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class IndicatorReportHtmlBuilder {

    private static final String TEMPLATE_PATH = "/spp/presentation/templates/indicator-report.html";

    private IndicatorReportHtmlBuilder() {
    }

    public static String buildIndicatorReport(IndicatorReportDTO indicatorReportDTO) throws FileGenerationException {
        String finalRenderedReport = "";
        Map<String, String> templateValues = new HashMap<>();

        String escapedGenerationDate = TemplateRenderer.escape(indicatorReportDTO.getGenerationDate());
        String totalStudentsString = String.valueOf(indicatorReportDTO.getTotalStudents());
        String totalMaleString = String.valueOf(indicatorReportDTO.getTotalMale());
        String totalFemaleString = String.valueOf(indicatorReportDTO.getTotalFemale());
        String totalIndigenousString = String.valueOf(indicatorReportDTO.getTotalIndigenous());
        String totalNonIndigenousString = String.valueOf(indicatorReportDTO.getTotalNonIndigenous());

        templateValues.put("generationDate", escapedGenerationDate);
        templateValues.put("totalStudents", totalStudentsString);
        templateValues.put("totalMale", totalMaleString);
        templateValues.put("totalFemale", totalFemaleString);
        templateValues.put("totalIndigenous", totalIndigenousString);
        templateValues.put("totalNonIndigenous", totalNonIndigenousString);

        String genderFilter = extractGenderFilter(indicatorReportDTO);
        String periodFilter = extractPeriodFilter(indicatorReportDTO);
        String languageFilter = extractLanguageFilter(indicatorReportDTO);
        String ageFilter = extractAgeFilter(indicatorReportDTO);

        templateValues.put("filterGender", TemplateRenderer.escape(genderFilter));
        templateValues.put("filterAge", TemplateRenderer.escape(ageFilter));
        templateValues.put("filterPeriod", TemplateRenderer.escape(periodFilter));
        templateValues.put("filterLanguage", TemplateRenderer.escape(languageFilter));

        try {
            finalRenderedReport = TemplateRenderer.render(TEMPLATE_PATH, templateValues);
        } catch (IOException e) {
            AppLogger.log(ExceptionLevel.ERROR, e);
            throw new FileGenerationException("Error generando archivo de reporte de indicadores", e);
        }

        return finalRenderedReport;
    }

    private static String extractGenderFilter(IndicatorReportDTO reportDTO) {
        String filterResult = "Todos";
        Object rawGender = reportDTO.getAppliedFilters().getGender();

        if (rawGender != null) {
            filterResult = String.valueOf(rawGender);
        }
        return filterResult;
    }

    private static String extractPeriodFilter(IndicatorReportDTO reportDTO) {
        String filterResult = "Todos";
        String rawPeriod = reportDTO.getAppliedFilters().getPeriod();

        if (rawPeriod != null) {
            filterResult = rawPeriod;
        }
        return filterResult;
    }

    private static String extractLanguageFilter(IndicatorReportDTO reportDTO) {
        String filterResult = "Todos";
        Object rawLanguage = reportDTO.getAppliedFilters().getIndigenousLanguage();

        if (rawLanguage != null) {
            filterResult = String.valueOf(rawLanguage);
        }
        return filterResult;
    }

    private static String extractAgeFilter(IndicatorReportDTO reportDTO) {
        String filterResult = "Todas";
        Integer minAge = reportDTO.getAppliedFilters().getMinAge();
        Integer maxAge = reportDTO.getAppliedFilters().getMaxAge();

        if (minAge != null && maxAge != null) {
            filterResult = minAge + " a " + maxAge + " años";
        } else if (minAge != null) {
            filterResult = "Mayor a " + minAge + " años";
        } else if (maxAge != null) {
            filterResult = "Menor a " + maxAge + " años";
        }

        return filterResult;
    }
}