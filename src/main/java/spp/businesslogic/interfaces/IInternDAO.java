package spp.businesslogic.interfaces;

import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.exceptions.InternException;
import spp.businesslogic.exceptions.LogicLayerException;

import java.sql.SQLException;

public interface IInternDAO{
    void addIntern(InternDTO internDTO) throws InternException;
    void insertIntern(InternDTO internDTO, int userId) throws LogicLayerException;
}
