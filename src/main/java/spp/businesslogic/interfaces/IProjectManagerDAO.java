package spp.businesslogic.interfaces;


import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;


public interface IProjectManagerDAO {

    boolean addProjectManagerDAO(ProjectManagerDTO projectManagerDTO) throws DAOException;
    boolean updateProjectManagerDAO(ProjectManagerDTO projectManagerDTO) throws DAOException;
    List<ProjectManagerDTO> obtainActiveProjectManagers() throws DAOException;
    boolean searchProjectManagerRegisters() throws DAOException;

}
