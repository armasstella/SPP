package spp.businesslogic.dto;


import spp.businesslogic.enums.MesaggeStatus;


public class MessageDTO {
    private String subject;
    private String content;
    private MesaggeStatus messageStatus;
    private int sender;
    private int receiver;
    private String emailSender;
    private String emailReceiver;
    private String date;

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmailSender() {
        return emailSender;
    }

    public void setEmailSender(String emailSender) {
        this.emailSender = emailSender;
    }

    public String getEmailReceiver() {
        return emailReceiver;
    }

    public void setEmailReceiver(String emailReceiver) {
        this.emailReceiver = emailReceiver;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}