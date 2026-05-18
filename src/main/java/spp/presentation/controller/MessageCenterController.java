package spp.presentation.controller;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import spp.businesslogic.dto.MessageDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.dataaccess.dao.MessageDAO;
import spp.dataaccess.dao.UserDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.ViewNavigator;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MessageCenterController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private VBox vbOptionAllMessages;
    @FXML private TableView<MessageDTO> tblMessages;
    @FXML private TableColumn<MessageDTO, String> clmnSender;
    @FXML private TableColumn<MessageDTO, String> clmnSubject;
    @FXML private TableColumn<MessageDTO, String> clmnDate;
    @FXML private VBox vbOptionNewMessage;
    @FXML private TextField txtRecipient;
    @FXML private TextField txtSubject;
    @FXML private TextArea txtBody;

    private final MessageDAO messageDAO = new MessageDAO();
    private final UserDAO userDAO = new UserDAO();
    private ObservableList<MessageDTO> messagesObservableList;

    private String previousViewPath;
    private String previosViewTitle;

    public void setPreviousView(String path, String title) {
        this.previousViewPath = path;
        this.previosViewTitle = title;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        obtainMessages();
    }

    private void setUpColumns() {
        clmnSender.setCellValueFactory(
                new PropertyValueFactory<>("emailSender"));
        clmnSubject.setCellValueFactory(
                new PropertyValueFactory<>("subject"));
        clmnDate.setCellValueFactory(
                new PropertyValueFactory<>("date"));
    }

    @FXML
    private void obtainMessages() {
        try {
            List<MessageDTO> messagesList = messageDAO.obtainMessagesForUser();
            messagesObservableList = FXCollections.observableArrayList(messagesList);
            tblMessages.setItems(messagesObservableList);
        } catch (DAOException e) {
            showError("Error al obtener mensajes");
        }
    }

    @FXML
    public void showNewMessageForm(ActionEvent event) {
        vbOptionAllMessages.setVisible(false);
        vbOptionNewMessage.setVisible(true);
        clearNewMessageFields();
        lblStatus.setText("");
    }

    @FXML
    public void showAllMessages(ActionEvent event) {
        vbOptionNewMessage.setVisible(false);
        vbOptionAllMessages.setVisible(true);
        obtainMessages();
        lblStatus.setText("");
    }

    @FXML
    public void sendMessage(ActionEvent event) {
        if(txtRecipient.getText().trim().isEmpty() ||
            txtSubject.getText().trim().isEmpty() ||
            txtBody.getText().trim().isEmpty()) {
            showError("Llene todo los campos.");
            return;
        }

        try {
            if (userDAO.searchEmailRegister(txtRecipient.getText().trim())) {
                try {
                    if (messageDAO.sendMessage(buildMessageDTO())) {
                        showSuccess("Mensaje enviado correctamente.");
                    }
                    clearNewMessageFields();
                } catch (DAOException e) {
                    AppLogger.logError(e);
                    showError("Error enviando mensaje");
                }
            }
        } catch (DAOException e) {
            AppLogger.logError(e);
            showError("El correo ingresado no está registrado en el sistema\nNo es posible enviarle mensaje.");
        }



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
            showError("Error al regresar. Reinicie el programa");
        }
    }

    private void showSuccess(String message) {
        lblStatus.setText(message);
        lblStatus.getStyleClass().removeAll("error", "success");
        lblStatus.getStyleClass().add("success");
    }

    private void showError(String message) {
        lblStatus.setText(message);
        lblStatus.getStyleClass().removeAll("error", "success");
        lblStatus.getStyleClass().add("error");
    }






}
