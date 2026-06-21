package spp.businesslogic.interfaces;


import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;


public interface IProjectManagerDAO {

    boolean registerProjectManager(ProjectManagerDTO projectManagerDTO) throws DAOException;
    List<ProjectManagerDTO> getActiveProjectManagers() throws DAOException;
    boolean existsProjectManagers() throws DAOException;

}
