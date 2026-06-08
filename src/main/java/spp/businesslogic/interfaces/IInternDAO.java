package spp.businesslogic.interfaces;


import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;


public interface IInternDAO{

    boolean addIntern(InternDTO internDTO) throws DAOException;
    int obtainId(String studentNumber) throws DAOException;
    boolean searchStudentNumberRegister(String studentNumber) throws DAOException;
    boolean inactivateIntern(InternDTO internDTO) throws DAOException;
    List<InternDTO> obtainAllActiveInterns() throws DAOException;
    String obtainStudentNumber(String email) throws DAOException;

}
