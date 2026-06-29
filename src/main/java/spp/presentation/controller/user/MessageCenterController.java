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
import spp.utils.view.table.GenericNestedSelector;
import spp.utils.view.inputdata.InputFilter;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.ViewConstant;
import spp.utils.view.window.ViewNavigator;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class MessageCenterController implements Initializable {

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
        setUpDoubleClickOnMessage();

    }

    private void setUpFields() {
        InputFilter.applyFormatFilter(txtRecipient,
                ViewConstant.PATTERN_EMAIL_CHARS, ViewConstant.MAX_LENGTH_EMAIL);
        InputFilter.applyFormatFilter(txtSubject,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_TITLE);
        InputFilter.applyFormatFilter(taBody,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_DESCRIPTION);

    }

    private void setUpDoubleClickOnMessage() {
        tblMessages.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblMessages.getSelectionModel().getSelectedItem() != null) {
                MessageDTO selectedMessage = tblMessages.getSelectionModel().getSelectedItem();
                displayMessageDetail(selectedMessage);
            }
        });

    }

    public void setPreviousView(String path, String title) {
        this.previousViewPath = path;
        this.previousViewTitle = title;

    }

    private void setUpColumns() {
        colSender.setCellValueFactory(
                new GenericNestedSelector<>("emailSender", "Sin remitente"));
        colSubject.setCellValueFactory(
                new GenericNestedSelector<>("subject", "Sin asunto"));
        colDate.setCellValueFactory(
                new GenericNestedSelector<>("date", "Sin fecha"));

    }

    @FXML
    private void obtainMessages() {
        try {
            List<MessageDTO> messagesList = messageDAO.findMessagesByReceiverEmail(ActiveSessionDTO.get().getEmail());
            ObservableList<MessageDTO> messagesObservableList = FXCollections.observableArrayList(messagesList);
            tblMessages.setItems(messagesObservableList);
        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, "Error al obtener mensajes");
        }

    }

    @FXML
    private void showNewMessageForm(ActionEvent event) {
        vbOptionAllMessages.setVisible(false);
        vbMessageDetail.setVisible(false);
        vbOptionNewMessage.setVisible(true);
        clearNewMessageFields();
        lblStatus.setText("");

    }

    @FXML
    private void showAllMessages(ActionEvent event) {
        vbOptionNewMessage.setVisible(false);
        vbMessageDetail.setVisible(false);
        vbOptionAllMessages.setVisible(true);
        obtainMessages();
        lblStatus.setText("");

    }

    private boolean hasEmptyFields() {
        boolean emptyFields = false;

        if (txtRecipient.getText().trim().isEmpty() ||
                txtSubject.getText().trim().isEmpty() ||
                taBody.getText().trim().isEmpty()) {
            emptyFields = true;
        }

        return emptyFields;

    }

    @FXML
    private void sendMessage(ActionEvent event) {
        if(hasEmptyFields()) {
            StatusLabel.showError(lblStatus, "Completa todos los campos obligatorios.");
        } else {
            MessageDTO messageDTO = new MessageDTO();
            setAllMessage(messageDTO);

            if (messageDTO.isValid()) {
                try {
                    if (userDAO.existsEmailRegister(txtRecipient.getText().trim())) {
                        try {
                            if (messageDAO.sendMessage(messageDTO)) {
                                StatusLabel.showSuccess(lblStatus, "Mensaje enviado correctamente.");
                            }
                            clearNewMessageFields();
                        } catch (DAOException e) {
                            StatusLabel.showError(lblStatus, e.getMessage());
                        }
                    } else {
                        StatusLabel.showError(lblStatus, "El email no está registrado en el sistema");
                    }
                } catch (DAOException e) {
                    StatusLabel.showError(lblStatus, e.getMessage());
                }
            } else {
                String errorMessages = String.join(" - ", messageDTO.getErrors());
                StatusLabel.showError(lblStatus, errorMessages);
            }
        }
    }

    private void displayMessageDetail(MessageDTO message) {
        vbOptionAllMessages.setVisible(false);
        vbOptionNewMessage.setVisible(false);
        vbMessageDetail.setVisible(true);

        lblDetailSender.setText(message.getEmailSender());
        lblDetailDate.setText(message.getDate());
        lblDetailSubject.setText(message.getSubject());
        taDetailContent.setText(message.getContent());

    }

    public void setAllMessage(MessageDTO newMessageDTO) {
        newMessageDTO.setEmailSender(ActiveSessionDTO.get().getEmail());
        newMessageDTO.setEmailReceiver(txtRecipient.getText().trim());
        newMessageDTO.setSubject(txtSubject.getText().trim());
        newMessageDTO.setContent(taBody.getText().trim());

    }

    private void clearNewMessageFields() {
        txtRecipient.clear();
        txtSubject.clear();
        taBody.clear();

    }

    @FXML
    public void goToMenuView(ActionEvent event) {
        if (previousViewPath != null &&
            previousViewTitle != null) {
            ViewNavigator.loadView(previousViewPath, previousViewTitle, event);
        } else {
            StatusLabel.showError(lblStatus, "Error al regresar. Reinicie el programa");
        }

    }

}
