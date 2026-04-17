package spp.businesslogic.interfaces;

import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;

public interface IProjectDAO {
    void addProject(ProjectDTO projectDTO) throws DAOException;
}
