package spp.businesslogic.interfaces;


import spp.businesslogic.dto.SelfEvaluationDTO;
import spp.businesslogic.exceptions.DAOException;


public interface ISelfEvaluationDAO {

    SelfEvaluationDTO findEvaluationHeaderByStudentNumber(String studentNumber) throws DAOException;

}
