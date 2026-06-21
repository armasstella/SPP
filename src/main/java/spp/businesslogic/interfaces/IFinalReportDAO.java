package spp.businesslogic.interfaces;


import spp.businesslogic.dto.FinalReportDTO;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.dto.ReportDocumentDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;


public interface IFinalReportDAO {

    FinalReportDTO getFinalReportDetailByStudentNumber(String studentNumber) throws DAOException;
    List<InternDTO> getAssignedInternsByProfessorEmail(String email) throws DAOException;
    List<ReportDocumentDTO> getFinalReportsByIntern(String studentNumber) throws DAOException;
    boolean assignGrade(int documentId, String email, int grade) throws DAOException;
    boolean updateGrade(int documentId, int grade) throws DAOException;
    boolean hasFinalReportByInternEmail(String email) throws DAOException;

}
