package spp.utils.htmlbuilder;

import spp.businesslogic.dto.SelfEvaluationDTO;
import spp.businesslogic.exceptions.FileGenerationException;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.file.TemplateRenderer;
import spp.utils.logger.AppLogger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class SelfEvaluationHtmlBuilder {

    private static final String TEMPLATE_PATH = "/spp/presentation/templates/self-evaluation.html";

    private SelfEvaluationHtmlBuilder() {
    }

    public static String buildSelfEvaluation(SelfEvaluationDTO evaluation) throws FileGenerationException {
        String templateRenderized = "";
        Map<String, String> templateValues = new HashMap<>();

        String escapedStudentName = TemplateRenderer.escape(evaluation.getStudentName());
        String escapedStudentNumber = TemplateRenderer.escape(evaluation.getStudentNumber());
        String escapedLinkedOrganization = TemplateRenderer.escape(evaluation.getLinkedOrganization());
        String escapedDepartment = TemplateRenderer.escape(evaluation.getDepartment());
        String escapedProjectManager = TemplateRenderer.escape(evaluation.getProjectManager());
        String escapedProjectName = TemplateRenderer.escape(evaluation.getProjectName());
        String finalScoreString = String.valueOf(evaluation.getFinalScore());
        String evaluationRowsHtml = buildEvaluationRows(evaluation.getScores());

        templateValues.put("studentName", escapedStudentName);
        templateValues.put("studentNumber", escapedStudentNumber);
        templateValues.put("linkedOrganization", escapedLinkedOrganization);
        templateValues.put("department", escapedDepartment);
        templateValues.put("projectManager", escapedProjectManager);
        templateValues.put("projectName", escapedProjectName);
        templateValues.put("finalScore", finalScoreString);
        templateValues.put("evaluationRows", evaluationRowsHtml);

        try {
            templateRenderized = TemplateRenderer.render(TEMPLATE_PATH, templateValues);
        } catch (IOException e) {
            AppLogger.log(ExceptionLevel.ERROR, e);
            throw new FileGenerationException("Error generando archivo de autoevaluación", e);
        }

        return templateRenderized;
    }

    private static String buildEvaluationRows(int[] scores) {
        String[] evaluationStatements = {
                "1. Mi participación en la Organización Vinculada fue productiva.",
                "2. Logré la aplicación de los conocimientos teórico-prácticos adquiridos.",
                "3. Me sentí seguro al realizar las actividades encomendadas.",
                "4. Las actividades encomendadas despertaron mi interés.",
                "5. La Organización Vinculada me proporcionó la información y facilidades adecuados.",
                "6. La Organización Vinculada me dio a conocer las reglas internas que debía seguir.",
                "7. El Responsable del Proyecto me orientó correctamente.",
                "8. El Responsable del Proyecto realizó un seguimiento efectivo de mis actividades.",
                "9. El proyecto es congruente con la formación de mi carrera.",
                "10. Considero que las prácticas son importantes para mi formación profesional."
        };

        StringBuilder rowsBuilder = new StringBuilder();

        for (int index = 0; index < evaluationStatements.length; index++) {
            String currentStatement = evaluationStatements[index];
            String escapedStatement = TemplateRenderer.escape(currentStatement);

            rowsBuilder.append("<tr>");
            rowsBuilder.append("<td class='statement'>");
            rowsBuilder.append(escapedStatement);
            rowsBuilder.append("</td>");

            for (int possibleValue = 1; possibleValue <= 5; possibleValue++) {
                if (scores[index] == possibleValue) {
                    rowsBuilder.append("<td class='center'>X</td>");
                } else {
                    rowsBuilder.append("<td></td>");
                }
            }

            rowsBuilder.append("</tr>");
        }

        String finalRows = rowsBuilder.toString();
        return finalRows;
    }
}