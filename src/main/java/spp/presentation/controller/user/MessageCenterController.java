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
import spp.businesslogic.dto.MessageDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.MessageDAO;
import spp.businesslogic.dao.UserDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.GenericNestedSelector;
import spp.utils.view.InputFilter;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
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
    @FXML private TextArea txtBody;
    @FXML private VBox vbMessageDetail;
    @FXML private Label lblDetailSender;
    @FXML private Label lblDetailDate;
    @FXML private Label lblDetailSubject;
    @FXML private TextArea txtDetailContent;
    private final MessageDAO messageDAO = new MessageDAO();
    private final UserDAO userDAO = new UserDAO();
    private ObservableList<MessageDTO> messagesObservableList;
    private String previousViewPath;
    private String previosViewTitle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        obtainMessages();
        setUpFields();
        setUpDoubleClickOnMessage();

    }

    private void setUpFields() {
        InputFilter.applyFilter(txtRecipient, InputFilter.EMAIL_CHARS_PATTERN, 20);
        InputFilter.applyFilter(txtSubject, InputFilter.NAME_PATTERN, 20);
        InputFilter.applyFilter(txtBody, InputFilter.NAME_PATTERN, 250);

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
        this.previosViewTitle = title;

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
            List<MessageDTO> messagesList = messageDAO.obtainMessagesForUser();
            messagesObservableList = FXCollections.observableArrayList(messagesList);
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

    @FXML
    private void sendMessage(ActionEvent event) {
        if(txtRecipient.getText().trim().isEmpty() ||
            txtSubject.getText().trim().isEmpty() ||
            txtBody.getText().trim().isEmpty()) {
            StatusLabel.showError(lblStatus, "Llene todo los campos.");
            return;
        }

        try {
            if (userDAO.searchEmailRegister(txtRecipient.getText().trim())) {
                try {
                    if (messageDAO.sendMessage(buildMessageDTO())) {
                        StatusLabel.showSuccess(lblStatus, "Mensaje enviado correctamente.");
                    }
                    clearNewMessageFields();
                } catch (DAOException e) {
                    AppLogger.logError(e);
                    StatusLabel.showError(lblStatus, "Error enviando mensaje");
                }
            }
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "El correo ingresado no está registrado en el sistema" +
                    "\nNo es posible enviarle mensaje.");
        }

    }

    private void displayMessageDetail(MessageDTO message) {
        vbOptionAllMessages.setVisible(false);
        vbOptionNewMessage.setVisible(false);
        vbMessageDetail.setVisible(true);

        lblDetailSender.setText(message.getEmailSender());
        lblDetailDate.setText(message.getDate());
        lblDetailSubject.setText(message.getSubject());
        txtDetailContent.setText(message.getContent());

    }

    public MessageDTO buildMessageDTO() {
        MessageDTO newMessageDTO = new MessageDTO();
        newMessageDTO.setEmailReceiver(txtRecipient.getText().trim());
        newMessageDTO.setSubject(txtSubject.getText().trim());
        newMessageDTO.setContent(txtBody.getText().trim());
        return newMessageDTO;

    }

    private void clearNewMessageFields() {
        txtRecipient.clear();
        txtSubject.clear();
        txtBody.clear();

    }

    @FXML
    public void goToMenuView(ActionEvent event) {
        if (previousViewPath != null &&
            previosViewTitle != null) {
            ViewNavigator.loadView(previousViewPath, previosViewTitle, event);
        } else {
            StatusLabel.showError(lblStatus, "Error al regresar. Reinicie el programa");
        }

    }

}
