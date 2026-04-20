package spp.businesslogic.interfaces;

import spp.businesslogic.dto.MessageDTO;
import spp.businesslogic.exceptions.DAOException;

public interface IMessageDAO {
    boolean sendMessage(MessageDTO messageDTO)  throws DAOException;
}