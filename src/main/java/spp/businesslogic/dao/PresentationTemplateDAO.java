package spp.businesslogic.dao;


import spp.businesslogic.dto.PresentationTemplateDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IPresentationTemplateDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.exceptionmanager.SQLStateConstant;
import spp.utils.logger.AppLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLTimeoutException;
import java.sql.Timestamp;

public class PresentationTemplateDAO implements IPresentationTemplateDAO {

    public PresentationTemplateDAO() {
    }

    @Override
    public boolean saveDocument(String personalNumber, PresentationTemplateDTO presentationTemplateDTO) throws DAOException {
        final String INSERT_DOCUMENT = " INSERT INTO plantillas_presentaciones (nombre_original, " +
                "nombre_almacenado, ruta_archivo, tamaño_mb, extension, fecha_subida," +
                "id_usuario_profesor, num_personal) SELECT ?, ?, ?, ?, ?, ?, p.id_usuario, p.num_personal " +
                "FROM profesores p WHERE p.num_personal = ?";
        boolean isSaveSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DOCUMENT)) {
                preparedStatement.setString(1, presentationTemplateDTO.getOriginalName());
                preparedStatement.setString(2, presentationTemplateDTO.getSavedName());
                preparedStatement.setString(3, presentationTemplateDTO.getFilePath());
                preparedStatement.setDouble(4, presentationTemplateDTO.getSizeMb());
                preparedStatement.setString(5, presentationTemplateDTO.getExtension());
                preparedStatement.setTimestamp(6, Timestamp.valueOf(presentationTemplateDTO.getUploadDate()));
                preparedStatement.setString(7, personalNumber);
                isSaveSuccessful = preparedStatement.executeUpdate() != DAOResultConstant.NO_ROWS_AFFECTED;
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.log(ExceptionLevel.WARN, e);
            throw new DAOException("No se pudo guardar el documento. Es posible que el archivo ya exista o el número de personal no sea válido.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al guardar el documento.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al guardar el documento.");
            } else if (SQLStateConstant.TRIGGER_EXCEPTION_CODE.equals(e.getSQLState())) {
                throw new DAOException(e.getMessage());
            } else {
                throw new DAOException("Ocurrió un error al intentar guardar el documento.");
            }
        }

        return isSaveSuccessful;
    }
}