package spp.businesslogic.dao;


import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.ITermDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TermDAO implements ITermDAO {


    @Override
    public List<String> findTermNames() throws DAOException {
        List<String> periods = new ArrayList<>();
        final String SELECT_ALL_PERIOD_NAMES = "SELECT nombre_periodo FROM periodos ORDER BY id_periodo DESC";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PERIOD_NAMES);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    periods.add(resultSet.getString("nombre_periodo"));
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al obtener los nombres de los periodos escolares", e);
        }

        return periods;
    }
}
