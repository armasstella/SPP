package spp.businesslogic.interfaces;

import spp.businesslogic.dto.UserDTO;
import spp.businesslogic.exceptions.DAOException;

import java.sql.PreparedStatement;

public interface IUserDAO {
    int insertUser(UserDTO userDTO) throws DAOException;
    int getGeneratedKey(PreparedStatement preparedStatement) throws DAOException;
}
