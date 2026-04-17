package spp.businesslogic.interfaces;

import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.DAOException;

public interface ICoordinatorDAO {
    void addCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException;

}
