package spp.businesslogic.dao;


import spp.businesslogic.dto.IndicatorFilterDTO;
import spp.businesslogic.dto.IndicatorReportDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IIndicatorReportDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.logger.AppLogger;
import spp.utils.query.IndicatorReportQueryBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class IndicatorReportDAO implements IIndicatorReportDAO {

    @Override
    public IndicatorReportDTO getStaticsByIndicators(IndicatorFilterDTO filters) throws DAOException {
        IndicatorReportDTO indicatorReportDTO = new IndicatorReportDTO();
        indicatorReportDTO.setAppliedFilters(filters);
        List<Object> filterValues = new ArrayList<>();

        String dynamicQuery = IndicatorReportQueryBuilder.buildDynamicQuery(filters, filterValues);

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(dynamicQuery)) {
                for (int i = 0; i < filterValues.size(); i++) {
                    preparedStatement.setObject(i + 1, filterValues.get(i));
                }

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        indicatorReportDTO.setTotalStudents(resultSet.getInt("total_practicantes"));
                        indicatorReportDTO.setTotalMale(resultSet.getInt("total_masculino"));
                        indicatorReportDTO.setTotalFemale(resultSet.getInt("total_femenino"));
                        indicatorReportDTO.setTotalIndigenous(resultSet.getInt("total_lengua_indigena"));
                        indicatorReportDTO.setTotalNonIndigenous(resultSet.getInt("total_no_lengua_indigena"));
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al calcular las métricas de los indicadores", e);
        }

        return indicatorReportDTO;
    }

}