package spp.businesslogic.dao;


import spp.businesslogic.dto.PresentationTemplateDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IPresentationTemplateDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PresentationTemplateDAO implements IPresentationTemplateDAO {

    private static final int NO_ROWS_AFFECTED = 0;

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
                isSaveSuccessful = preparedStatement.executeUpdate() != NO_ROWS_AFFECTED;
            }

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al guardar documento", e);
        }

        return isSaveSuccessful;

    }

}
