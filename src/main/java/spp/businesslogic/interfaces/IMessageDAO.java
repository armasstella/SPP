package spp.businesslogic.interfaces;


import spp.businesslogic.dto.MessageDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;


public interface IMessageDAO {

    boolean sendMessage(MessageDTO messageDTO) throws DAOException;
    List<MessageDTO> findMessagesByReceiverEmail(String email) throws DAOException;

}