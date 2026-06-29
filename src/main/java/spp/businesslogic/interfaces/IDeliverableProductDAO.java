package spp.businesslogic.interfaces;

import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.dto.DeliverableProductDTO;
import spp.businesslogic.enums.ActivityType;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

public interface IDeliverableProductDAO {

    boolean saveDeliverableProductForIntern(String studentNumber, DeliverableProductDTO deliverableProductDTO) throws DAOException;
    List<DeliverableProductDTO> findDeliverableProductsByStudentNumber(String studentNumber) throws DAOException;
}
