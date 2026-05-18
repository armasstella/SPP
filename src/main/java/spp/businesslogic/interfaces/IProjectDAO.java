package spp.businesslogic.interfaces;

import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;

public interface IProjectDAO {
    boolean addProject(ProjectDTO projectDTO) throws DAOException;
    boolean deleteProject(ProjectDTO projectDTO) throws DAOException;
    boolean updateProject(ProjectDTO projectDTO) throws DAOException;
}
