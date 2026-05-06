package spp.businesslogic.interfaces;

import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.exceptions.DAOException;

public interface IInternDAO{
    boolean addIntern(InternDTO internDTO) throws DAOException;
}
