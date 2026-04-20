package spp.dataaccess.dao;

import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IProjectManagerDAO;

public class ProjectManagerDAO implements IProjectManagerDAO {

    public ProjectManagerDAO() {

    }



    @Override
    public boolean addProjectManagerDAO(ProjectManagerDTO projectManagerDTO) throws DAOException {
        return true;
    }

    @Override
    public boolean updateProjectManagerDAO(ProjectManagerDTO projectManagerDTO) throws DAOException {
        return true;
    }
}
