package spp.businesslogic.interfaces;

import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

public interface ICoordinatorDAO {
    boolean addCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException;
    boolean inactivateCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException;
    boolean activateCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException;
    boolean existCoordinator(String personalNumber) throws DAOException;
    List<CoordinatorDTO> obtainAllActiveCoordinators() throws DAOException;

}
