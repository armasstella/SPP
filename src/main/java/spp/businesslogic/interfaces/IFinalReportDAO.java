package spp.businesslogic.interfaces;


import spp.businesslogic.dto.FinalReportDTO;
import spp.businesslogic.exceptions.DAOException;


public interface IFinalReportDAO {

    FinalReportDTO obtainReportData(String studentNumber) throws DAOException;

}
