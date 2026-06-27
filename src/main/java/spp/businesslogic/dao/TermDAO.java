package spp.businesslogic.dao;

import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.ITermDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.logger.AppLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.sql.SQLDataException;
import java.sql.SQLTimeoutException;
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
        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de autenticación al intentar obtener la lista de periodos.");

        } catch (SQLTimeoutException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al consultar los periodos escolares.");

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de red al intentar cargar los periodos escolares.");
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
        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de autenticación al verificar el periodo activo.");

        } catch (SQLTimeoutException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al consultar el periodo activo.");

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al obtener el periodo escolar actual.");
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
        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de autenticación al verificar el identificador del periodo.");

        } catch (SQLTimeoutException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al consultar el identificador del periodo.");

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al obtener el identificador del periodo escolar.");
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
        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de autenticación al intentar desactivar el periodo escolar.");

        } catch (SQLTimeoutException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al procesar la desactivación del periodo.");

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión con el servidor al desactivar el periodo escolar.");
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
        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.logError(ExceptionLevel.WARN, e);
            throw new DAOException("El periodo escolar que intenta registrar ya existe en el sistema.");

        } catch (SQLDataException e) {
            AppLogger.logError(ExceptionLevel.WARN, e);
            throw new DAOException("El formato o la longitud del nombre del periodo no es compatible.");

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de autenticación al registrar un nuevo periodo escolar.");

        } catch (SQLTimeoutException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al intentar registrar el periodo.");

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión con el servidor al registrar el nuevo periodo escolar.");
        }

        return isInsertSuccessful;
    }
}