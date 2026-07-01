package spp.presentation.controller.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.MessageDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.MessageDAO;
import spp.businesslogic.dao.UserDAO;
import spp.utils.view.table.DoubleClickListener;
import spp.utils.view.table.GenericNestedSelector;
import spp.utils.view.table.TableViewConfigurator;
import spp.utils.view.inputdata.InputFilter;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.ViewConstant;
import spp.utils.view.window.ViewNavigator;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MessageCenterController implements Initializable, DoubleClickListener<MessageDTO> {

    @FXML private Label lblStatus;
    @FXML private VBox vbOptionAllMessages;
    @FXML private TableView<MessageDTO> tblMessages;
    @FXML private TableColumn<MessageDTO, String> colSender;
    @FXML private TableColumn<MessageDTO, String> colSubject;
    @FXML private TableColumn<MessageDTO, String> colDate;
    @FXML private VBox vbOptionNewMessage;
    @FXML private TextField txtRecipient;
    @FXML private TextField txtSubject;
    @FXML private TextArea taBody;
    @FXML private VBox vbMessageDetail;
    @FXML private Label lblDetailSender;
    @FXML private Label lblDetailDate;
    @FXML private Label lblDetailSubject;
    @FXML private TextArea taDetailContent;
    private final MessageDAO messageDAO = new MessageDAO();
    private final UserDAO userDAO = new UserDAO();
    private String previousViewPath;
    private String previousViewTitle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        obtainMessages();
        setUpFields();
        TableViewConfigurator.enableDoubleClickSelection(tblMessages, this);
        showSinglePanel(vbOptionAllMessages);
    }

    @Override
    public void onItemSelected(MessageDTO selectedItem) {
        if (selectedItem != null) {
            displayMessageDetail(selectedItem);
        }
    }

    private void setUpFields() {
        InputFilter.applyFormatFilter(txtRecipient, ViewConstant.PATTERN_EMAIL_CHARS, ViewConstant.MAX_LENGTH_EMAIL);
        InputFilter.applyFormatFilter(txtSubject, ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_TITLE);
        InputFilter.applyFormatFilter(taBody, ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_DESCRIPTION);
    }

    public void setPreviousView(String path, String title) {
        this.previousViewPath = path;
        this.previousViewTitle = title;
    }

    private void setUpColumns() {
        GenericNestedSelector<MessageDTO> senderSelector =
                new GenericNestedSelector<>("emailSender", "Sin remitente");
        GenericNestedSelector<MessageDTO> subjectSelector =
                new GenericNestedSelector<>("subject", "Sin asunto");
        GenericNestedSelector<MessageDTO> dateSelector =
                new GenericNestedSelector<>("date", "Sin fecha");

        colSender.setCellValueFactory(senderSelector);
        colSubject.setCellValueFactory(subjectSelector);
        colDate.setCellValueFactory(dateSelector);
    }

    @FXML
    private void obtainMessages() {
        try {
            String receiverEmail = ActiveSessionDTO.get().getEmail();

            List<MessageDTO> messagesList = messageDAO.findMessagesByReceiverEmail(receiverEmail);
            ObservableList<MessageDTO> messagesObservableList = FXCollections.observableArrayList(messagesList);
            tblMessages.setItems(messagesObservableList);

        } catch (DAOException exception) {
            StatusLabel.showError(lblStatus, exception.getMessage());
        }
    }

    @FXML
    private void showNewMessageForm(ActionEvent event) {
        showSinglePanel(vbOptionNewMessage);
        clearNewMessageFields();
        lblStatus.setText("");
    }

    @FXML
    private void showAllMessages(ActionEvent event) {
        showSinglePanel(vbOptionAllMessages);
        obtainMessages();
        lblStatus.setText("");
    }

    public void displayMessageDetail(MessageDTO message) {
        showSinglePanel(vbMessageDetail);
        String sender = message.getEmailSender();
        String date = message.getDate();
        String subject = message.getSubject();
        String content = message.getContent();
        lblDetailSender.setText(sender);
        lblDetailDate.setText(date);
        lblDetailSubject.setText(subject);
        taDetailContent.setText(content);
    }

    private boolean hasEmptyRequiredFields() {
        boolean emptyFields = false;

        String rawRecipient = txtRecipient.getText();
        String rawSubject = txtSubject.getText();
        String rawBody = taBody.getText();

        boolean isRecipientEmpty = rawRecipient.trim().isEmpty();
        boolean isSubjectEmpty = rawSubject.trim().isEmpty();
        boolean isBodyEmpty = rawBody.trim().isEmpty();

        if (isRecipientEmpty || isSubjectEmpty || isBodyEmpty) {
            emptyFields = true;
        }

        return emptyFields;
    }

    @FXML
    private void sendMessage(ActionEvent event) {
        boolean fieldsAreEmpty = hasEmptyRequiredFields();

        if (fieldsAreEmpty) {
            StatusLabel.showError(lblStatus, "Completa todos los campos obligatorios.");
        } else {
            MessageDTO messageDTO = new MessageDTO();
            setAllMessage(messageDTO);

            boolean isValidMessage = messageDTO.isValid();

            if (!isValidMessage) {
                List<String> errorsList = messageDTO.getErrors();
                String errorMessages = String.join(" - ", errorsList);
                StatusLabel.showError(lblStatus, errorMessages);
            } else {
                executeMessageSending(messageDTO);
            }
        }
    }

    private void executeMessageSending(MessageDTO messageDTO) {
        try {
            String targetEmail = messageDTO.getEmailReceiver();
            boolean emailExists = userDAO.existsEmailRegister(targetEmail);

            if (!emailExists) {
                StatusLabel.showError(lblStatus, "El email no está registrado en el sistema");
            } else {
                boolean isMessageSent = messageDAO.sendMessage(messageDTO);

                if (isMessageSent) {
                    StatusLabel.showSuccess(lblStatus, "Mensaje enviado correctamente.");
                    clearNewMessageFields();
                }
            }
        } catch (DAOException exception) {
            String errorMessage = exception.getMessage();
            StatusLabel.showError(lblStatus, errorMessage);
        }
    }

    public void setAllMessage(MessageDTO newMessageDTO) {
        String senderEmail = ActiveSessionDTO.get().getEmail();
        String recipient = txtRecipient.getText().trim();
        String subject = txtSubject.getText().trim();
        String body = taBody.getText().trim();

        newMessageDTO.setEmailSender(senderEmail);
        newMessageDTO.setEmailReceiver(recipient);
        newMessageDTO.setSubject(subject);
        newMessageDTO.setContent(body);
    }

    private void clearNewMessageFields() {
        txtRecipient.clear();
        txtSubject.clear();
        taBody.clear();
    }

    @FXML
    public void goToMenuView(ActionEvent event) {
        boolean hasValidPreviousView = previousViewPath != null && previousViewTitle != null;

        if (hasValidPreviousView) {
            ViewNavigator.loadView(previousViewPath, previousViewTitle, event);
        } else {
            StatusLabel.showError(lblStatus, "Error al regresar. Reinicie el programa");
        }
    }

    private void showSinglePanel(VBox panelToShow) {
        VBox[] allPanels = {vbOptionAllMessages, vbOptionNewMessage, vbMessageDetail};

        for (VBox currentPanel : allPanels) {
            boolean isTargetPanel = currentPanel == panelToShow;
            currentPanel.setVisible(isTargetPanel);
            currentPanel.setManaged(isTargetPanel);
        }
    }

}