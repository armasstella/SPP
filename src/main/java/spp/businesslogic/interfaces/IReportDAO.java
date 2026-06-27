package spp.businesslogic.interfaces;


import spp.businesslogic.dto.ReportDTO;
import spp.businesslogic.exceptions.DAOException;


public interface IReportDAO {

    ReportDTO getReportDetailByStudentNumber(String studentNumber) throws DAOException;
    boolean assignGrade(int documentId, String email, int grade) throws DAOException;
    boolean updateGrade(int documentId, int grade) throws DAOException;

}
