package spp.businesslogic.interfaces;

import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.CoordinatorException;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.exceptions.DataAccessException;
import spp.businesslogic.exceptions.LogicLayerException;

import java.sql.Connection;
import java.sql.SQLException;

public interface ICoordinatorDAO {
    void addCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException;
    boolean inactivateCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException;

}
