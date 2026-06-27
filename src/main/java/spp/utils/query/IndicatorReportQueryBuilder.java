package spp.utils.query;

import spp.businesslogic.dto.IndicatorFilterDTO;
import spp.businesslogic.enums.GenderFilter;
import spp.businesslogic.enums.YesNoAllFilter;

import java.util.List;

public class IndicatorReportQueryBuilder {

    public static String buildDynamicQuery(IndicatorFilterDTO filters, List<Object> filterValues) {
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT ");
        queryBuilder.append("COUNT(DISTINCT p.matricula) as total_practicantes, ");
        queryBuilder.append("SUM(CASE WHEN p.sexo IN ('M', 'Masculino') THEN 1 ELSE 0 END) as total_masculino, ");
        queryBuilder.append("SUM(CASE WHEN p.sexo IN ('F', 'Femenino') THEN 1 ELSE 0 END) as total_femenino, ");
        queryBuilder.append("SUM(CASE WHEN p.habla_lengua_indigena = 1 THEN 1 ELSE 0 END) as total_lengua_indigena, ");
        queryBuilder.append("SUM(CASE WHEN p.habla_lengua_indigena = 0 THEN 1 ELSE 0 END) as total_no_lengua_indigena ");
        queryBuilder.append("FROM practicantes p ");
        queryBuilder.append("LEFT JOIN inscripciones_practicas_profesionales i ON p.matricula = i.matricula ");
        queryBuilder.append("LEFT JOIN experiencias_educativas ee ON i.id_experiencia_educativa = ee.id_experiencia_educativa ");
        queryBuilder.append("LEFT JOIN periodos per ON ee.id_periodo = per.id_periodo ");
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
            filterValues.add(filters.getMinAge());
        }

        if (filters.getMaxAge() != null) {
            queryBuilder.append("AND TIMESTAMPDIFF(YEAR, p.fecha_nacimiento, CURDATE()) <= ? ");
            filterValues.add(filters.getMaxAge());
        }

        if (filters.getPeriod() != null && !filters.getPeriod().equals("Todos")) {
            queryBuilder.append("AND per.nombre_periodo = ? ");
            filterValues.add(filters.getPeriod());
        }

        if (filters.getIndigenousLanguage() != null && filters.getIndigenousLanguage() != YesNoAllFilter.TODOS) {
            queryBuilder.append("AND p.habla_lengua_indigena = ? ");
            filterValues.add(filters.getIndigenousLanguage() == YesNoAllFilter.SI ? 1 : 0);
        }

        return queryBuilder.toString();
    }

}
