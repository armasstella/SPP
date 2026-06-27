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

    private static final int NO_ROWS_AFFECTED = 0;

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

    @Override
    public String findActiveTermName() throws DAOException {
        final String SELECT_ACTIVE_TERM = "SELECT nombre_periodo FROM periodos WHERE periodoActual = 1 LIMIT 1";
        String activeTerm = null;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ACTIVE_TERM);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    activeTerm = resultSet.getString("nombre_periodo");
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al obtener el periodo escolar activo", e);
        }

        return activeTerm;
    }

    @Override
    public int findActiveTermId() throws DAOException {
        final String SELECT_ACTIVE_TERM = "SELECT id_periodo FROM periodos WHERE periodoActual = 1 LIMIT 1";
        int activeTermId = -1;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ACTIVE_TERM);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    activeTermId = resultSet.getInt("id_periodo");
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al obtener el id de periodo escolar activo", e);
        }

        return activeTermId;
    }

    @Override
    public boolean deactivateCurrentTerm() throws DAOException {
        final String UPDATE_DEACTIVATE_TERM = "UPDATE periodos SET periodoActual = 0 WHERE periodoActual = 1";
        boolean deactivationSuccesful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DEACTIVATE_TERM)) {

                int affectedRows = preparedStatement.executeUpdate();
                deactivationSuccesful = affectedRows != NO_ROWS_AFFECTED;

            }
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al desactivar el periodo escolar", e);
        }

        return deactivationSuccesful;
    }

    @Override
    public boolean insertTerm(String termName) throws DAOException {
        final String INSERT_TERM = "INSERT INTO periodos (nombre_periodo, periodoActual) VALUES (?, 1)";
        boolean isInsertSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TERM)) {

                preparedStatement.setString(1, termName);
                int affectedRows = preparedStatement.executeUpdate();
                isInsertSuccessful = affectedRows != NO_ROWS_AFFECTED;

            }
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al registrar un nuevo periodo escolar", e);
        }

        return isInsertSuccessful;
    }
}