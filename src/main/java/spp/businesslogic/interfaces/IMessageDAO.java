package spp.businesslogic.interfaces;

import spp.businesslogic.dto.MessageDTO;
import spp.businesslogic.exceptions.DAOException;

public interface IMessageDAO {
    void sendMessage(MessageDTO messageDTO)  throws DAOException;
}
