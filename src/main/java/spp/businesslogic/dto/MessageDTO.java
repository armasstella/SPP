package spp.businesslogic.dto;

import spp.businesslogic.enums.MesaggeStatus;
import java.util.regex.Pattern;

public class MessageDTO {

    private static final int MAX_LENGTH_SUBJECT = 100;
    private static final int MAX_LENGTH_CONTENT = 1000;
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

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
        validateNotEmpty(subject, "Asunto");
        validateStringLength(subject, MAX_LENGTH_SUBJECT, "Asunto");
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setContent(String content) {
        validateNotEmpty(content, "Contenido");
        validateStringLength(content, MAX_LENGTH_CONTENT, "Contenido");
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

    public void setEmailSender(String emailSender) {
        validateRegex(emailSender, "El correo del remitente no tiene un formato válido.");
        this.emailSender = emailSender;
    }

    public String getEmailSender() {
        return emailSender;
    }

    public void setEmailReceiver(String emailReceiver) {
        validateRegex(emailReceiver, "El correo del destinatario no tiene un formato válido.");
        this.emailReceiver = emailReceiver;
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

    protected void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " no debe estar vacío.");
        }
    }

    protected void validateStringLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new IllegalArgumentException("El campo " + fieldName + " no debe exceder " + maxLength + " caracteres.");
        }
    }

    protected void validateRegex(String value, String errorMessage) {
        if (value == null || !Pattern.matches(MessageDTO.EMAIL_REGEX, value)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}