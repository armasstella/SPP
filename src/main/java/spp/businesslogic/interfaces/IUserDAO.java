package spp.businesslogic.interfaces;

import spp.businesslogic.dto.UserDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.exceptions.LogicLayerException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface IUserDAO {
    int insertUser(UserDTO userDTO) throws LogicLayerException;
    int getGeneratedKey(PreparedStatement preparedStatement) throws LogicLayerException;
}
