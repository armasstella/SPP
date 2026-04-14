package spp.businesslogic.interfaces;

import spp.businesslogic.dto.MessageDTO;

public interface IMessageDAO {
    void sendMessage(MessageDTO messageDTO);
}
