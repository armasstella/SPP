package spp.businesslogic.interfaces;

import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

public interface IPrioritizedProjectDAO {

    boolean savePrioritizedProjects(String email, List<ProjectDTO> chosenProjects) throws DAOException;
    boolean searchPrioritizedProjectsRegister(String email) throws DAOException;

}
