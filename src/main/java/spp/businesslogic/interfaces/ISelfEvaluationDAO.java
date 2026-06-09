package spp.businesslogic.interfaces;


import spp.businesslogic.dto.SelfEvaluationDTO;
import spp.businesslogic.exceptions.DAOException;


public interface ISelfEvaluationDAO {

    SelfEvaluationDTO obtainEvaluationData(String email) throws DAOException;

}
