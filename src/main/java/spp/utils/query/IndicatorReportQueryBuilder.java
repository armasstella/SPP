package spp.utils.query;

import spp.businesslogic.dto.IndicatorFilterDTO;
import spp.businesslogic.enums.GenderFilter;
import spp.businesslogic.enums.YesNoAllFilter;

import java.util.List;

public class IndicatorReportQueryBuilder {

    public static String buildDynamicQuery(IndicatorFilterDTO filters, List<Object> parameters) {
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT ");
        queryBuilder.append("COUNT(DISTINCT p.matricula) as total_practicantes, ");
        queryBuilder.append("SUM(CASE WHEN p.sexo IN ('M', 'Masculino') THEN 1 ELSE 0 END) as total_masculino, ");
        queryBuilder.append("SUM(CASE WHEN p.sexo IN ('F', 'Femenino') THEN 1 ELSE 0 END) as total_femenino, ");
        queryBuilder.append("SUM(CASE WHEN p.habla_lengua_indigena LIKE '1%' OR p.habla_lengua_indigena LIKE 'true%' THEN 1 ELSE 0 END) as total_lengua_indigena, ");
        queryBuilder.append("SUM(CASE WHEN p.habla_lengua_indigena LIKE '0%' OR p.habla_lengua_indigena LIKE 'false%' THEN 1 ELSE 0 END) as total_no_lengua_indigena ");
        queryBuilder.append("FROM practicantes p ");
        queryBuilder.append("LEFT JOIN inscripciones_practicas_profesionales i ON p.matricula = i.matricula ");
        queryBuilder.append("LEFT JOIN experiencias_educativas ee ON i.id_experiencia_educativa = ee.id_experiencia_educativa ");
        queryBuilder.append("WHERE 1=1 ");

        if (filters.getGender() != null && filters.getGender() != GenderFilter.TODOS) {
            if (filters.getGender() == GenderFilter.MASCULINO) {
                queryBuilder.append("AND p.sexo IN ('M', 'Masculino') ");
            } else if (filters.getGender() == GenderFilter.FEMENINO) {
                queryBuilder.append("AND p.sexo IN ('F', 'Femenino') ");
            }
        }

        if (filters.getMinAge() != null) {
            queryBuilder.append("AND TIMESTAMPDIFF(YEAR, p.fecha_nacimiento, CURDATE()) >= ? ");
            parameters.add(filters.getMinAge());
        }

        if (filters.getMaxAge() != null) {
            queryBuilder.append("AND TIMESTAMPDIFF(YEAR, p.fecha_nacimiento, CURDATE()) <= ? ");
            parameters.add(filters.getMaxAge());
        }

        if (filters.getPeriod() != null && !filters.getPeriod().equals("Todos")) {
            queryBuilder.append("AND ee.periodo = ? ");
            parameters.add(filters.getPeriod());
        }

        if (filters.getIndigenousLanguage() != null && filters.getIndigenousLanguage() != YesNoAllFilter.TODOS) {
            if (filters.getIndigenousLanguage() == YesNoAllFilter.SI) {
                queryBuilder.append("AND (p.habla_lengua_indigena LIKE '1%' OR p.habla_lengua_indigena LIKE 'true%') ");
            } else if (filters.getIndigenousLanguage() == YesNoAllFilter.NO) {
                queryBuilder.append("AND (p.habla_lengua_indigena LIKE '0%' OR p.habla_lengua_indigena LIKE 'false%') ");
            }
        }

        return queryBuilder.toString();
    }

}
