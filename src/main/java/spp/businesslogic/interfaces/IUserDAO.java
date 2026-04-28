package spp.businesslogic.interfaces;

import spp.businesslogic.dto.UserDTO;
import spp.businesslogic.exceptions.DAOException;

import java.sql.PreparedStatement;

public interface IUserDAO {
    int addUser(UserDTO userDTO) throws DAOException;
    int getGeneratedKey(PreparedStatement preparedStatement) throws DAOException;
    int obtainId(String email) throws DAOException;
    boolean login(String identifier, String password) throws DAOException;
}
