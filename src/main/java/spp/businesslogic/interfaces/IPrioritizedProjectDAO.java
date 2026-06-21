package spp.businesslogic.interfaces;

import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

public interface IPrioritizedProjectDAO {

    boolean savePrioritizedProjects(String email, List<ProjectDTO> priotitizedProjectsList) throws DAOException;
    boolean findPrioritizedProjectsByInternEmail(String email) throws DAOException;
    List<ProjectDTO> findPrioritizedProjectsIdentifiersByStudentNumber(String studentNumber) throws DAOException;

}
