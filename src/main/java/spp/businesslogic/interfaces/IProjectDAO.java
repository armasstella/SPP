package spp.businesslogic.interfaces;


import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;


public interface IProjectDAO {

    boolean addProject(ProjectDTO projectDTO) throws DAOException;
    boolean deleteProject(ProjectDTO projectDTO) throws DAOException;
    boolean updateProject(ProjectDTO projectDTO) throws DAOException;
    List<ProjectDTO> obtainAllProjects() throws DAOException;
    boolean verifyMinimumProjects() throws DAOException;
    List<ProjectDTO> obtainSelectedProjectsByIntern(String studentNumber) throws DAOException;

}
