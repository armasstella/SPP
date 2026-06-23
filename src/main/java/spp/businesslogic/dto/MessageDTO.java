package spp.businesslogic.dto;

import spp.businesslogic.enums.MesaggeStatus;
import spp.utils.validation.Validation;


public class MessageDTO extends BaseDTO {

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

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
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

    public boolean setEmailSender(String emailSender) {
        boolean isValid;
        Validation validator = new Validation();

        if (validator.validateEmail(emailSender)) {
            this.emailSender = emailSender.trim();
            isValid = true;
        } else {
            addErrors(validator.getErrors());
            isValid = false;
        }

        return isValid;
    }

    public String getEmailSender() {
        return emailSender;
    }

    public boolean setEmailReceiver(String emailReceiver) {
        boolean isValid;
        Validation validator = new Validation();

        if (validator.validateEmail(emailReceiver)) {
            this.emailReceiver = emailReceiver.trim();
            isValid = true;
        } else {
            addErrors(validator.getErrors());
            isValid = false;
        }

        return isValid;
    }

    public String getEmailReceiver() {
        return emailReceiver;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}
