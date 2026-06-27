package spp.businesslogic.interfaces;

import spp.businesslogic.dto.ReportDocumentFileDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

public interface IPartialReportDAO {

    List<ReportDocumentFileDTO> getPartialReportsByIntern(String studentNumber) throws DAOException;
}
