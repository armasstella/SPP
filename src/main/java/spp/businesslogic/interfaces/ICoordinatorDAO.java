package spp.businesslogic.interfaces;

import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.DAOException;

public interface ICoordinatorDAO {
    boolean addCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException;
    boolean inactivateCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException;
    boolean activateCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException;
}
