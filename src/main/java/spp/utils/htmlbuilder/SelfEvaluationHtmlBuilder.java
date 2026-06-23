package spp.utils.htmlbuilder;

import spp.businesslogic.dto.SelfEvaluationDTO;
import spp.utils.file.TemplateRenderer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class SelfEvaluationHtmlBuilder {

    private static final String TEMPLATE = "/spp/presentation/templates/self-evaluation.html";

    private SelfEvaluationHtmlBuilder() {

    }

    public static String buildSelfEvaluation(SelfEvaluationDTO evaluation) throws IOException {
        Map<String, String> values = new HashMap<>();
        values.put("studentName", TemplateRenderer.escape(evaluation.getStudentName()));
        values.put("studentNumber", TemplateRenderer.escape(evaluation.getStudentNumber()));
        values.put("linkedOrganization", TemplateRenderer.escape(evaluation.getLinkedOrganization()));
        values.put("department", TemplateRenderer.escape(evaluation.getDepartment()));
        values.put("projectManager", TemplateRenderer.escape(evaluation.getProjectManager()));
        values.put("projectName", TemplateRenderer.escape(evaluation.getProjectName()));
        values.put("finalScore", String.valueOf(evaluation.getFinalScore()));

        values.put("evaluationRows", buildEvaluationRows(evaluation.getScores()));

        return TemplateRenderer.render(TEMPLATE, values);
    }

    private static String buildEvaluationRows(int[] scores) {
        String[] statements = {
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

        StringBuilder rows = new StringBuilder();
        for (int i = 0; i < statements.length; i++) {
            rows.append("<tr>")
                    .append("<td class='statement'>").append(TemplateRenderer.escape(statements[i])).append("</td>");

            for (int value = 1; value <= 5; value++) {
                if (scores[i] == value) {
                    rows.append("<td class='center'>X</td>");
                } else {
                    rows.append("<td></td>");
                }
            }
            rows.append("</tr>");
        }
        return rows.toString();

    }

}
