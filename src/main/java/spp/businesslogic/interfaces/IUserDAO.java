package spp.businesslogic.interfaces;


import spp.businesslogic.dto.LoginResultDTO;
import spp.businesslogic.dto.UserDTO;
import spp.businesslogic.exceptions.DAOException;


public interface IUserDAO {

    int registerUser(UserDTO userDTO) throws DAOException;
    int obtainId(String email) throws DAOException;
    LoginResultDTO login(String identifier, String password) throws DAOException;
    boolean existsEmailRegister(String email) throws DAOException;

}
