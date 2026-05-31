package spp.businesslogic.interfaces;


import spp.businesslogic.dto.SessionDTO;
import spp.businesslogic.exceptions.DAOException;


public interface ISessionDAO {

    String createSession(int idUser) throws DAOException;
    SessionDTO searchSession(String token) throws DAOException;
    void deleteSession(String token) throws DAOException;

}
