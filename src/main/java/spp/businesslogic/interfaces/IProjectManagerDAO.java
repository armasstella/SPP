package spp.businesslogic.interfaces;

import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;

public interface IProjectManagerDAO {
    boolean addProjectManagerDAO(ProjectManagerDTO projectManagerDTO) throws DAOException;
    boolean updateProjectManagerDAO(ProjectManagerDTO projectManagerDTO) throws DAOException;
}
