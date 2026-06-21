package spp.businesslogic.interfaces;


import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;


public interface IInternDAO{

    boolean registerIntern(InternDTO internDTO) throws DAOException;
    boolean existsStudentByStudentNumber(String studentNumber) throws DAOException;
    boolean deactivateIntern(InternDTO internDTO) throws DAOException;
    List<InternDTO> getActiveInterns() throws DAOException;
    String findActiveStudentNumberByEmail(String email) throws DAOException;
    List<InternDTO> findUnassignedInternsIdentifiers() throws DAOException;

}
