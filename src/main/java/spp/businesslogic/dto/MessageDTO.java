package spp.businesslogic.dto;

import spp.businesslogic.enums.MesaggeStatus;

public class MessageDTO {
    private String content;
    private MesaggeStatus messageStatus;

    public MessageDTO() {

    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setMessageStatus(MesaggeStatus messageStatus) {
        this.messageStatus = messageStatus;
    }

    public MesaggeStatus getMessageStatus() {
        return messageStatus;
    }
}
