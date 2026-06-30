package spp.businesslogic.interfaces;

import spp.businesslogic.dto.PartialReportDTO;
import spp.businesslogic.dto.ReportDocumentFileDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

public interface IPartialReportDAO {

    PartialReportDTO findReportHeaderByStudentNumber(String studentNumber) throws DAOException;
    List<ReportDocumentFileDTO> getPartialReportsByIntern(String studentNumber) throws DAOException;
}
