package spp.businesslogic.interfaces;


import spp.businesslogic.dto.ReportDTO;
import spp.businesslogic.dto.ReportDocumentFileDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;


public interface IFinalReportDAO {

    List<ReportDocumentFileDTO> getFinalReportsByIntern(String studentNumber) throws DAOException;
    boolean hasFinalReportByInternEmail(String email) throws DAOException;

}
