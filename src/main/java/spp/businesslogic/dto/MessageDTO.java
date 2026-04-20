package spp.businesslogic.dto;

import spp.businesslogic.enums.MesaggeStatus;

public class MessageDTO {
    private String content;
    private MesaggeStatus messageStatus;
    private int sender;
    private int receiver;

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

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getSender() {
        return sender;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public int getReceiver() {
        return receiver;
    }
}