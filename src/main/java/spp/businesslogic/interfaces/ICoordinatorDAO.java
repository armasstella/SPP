package spp.businesslogic.interfaces;


import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;


public interface ICoordinatorDAO {

    boolean registerCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException;
    boolean deactivateCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException;
    boolean existsActiveCoordinatorByPersonalNumber(String personalNumber) throws DAOException;
    List<CoordinatorDTO> getActiveCoordinators() throws DAOException;

}
