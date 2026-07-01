package spp.presentation.controller.intern.listener;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import spp.businesslogic.dto.InternDTO;
import spp.presentation.controller.instructor.InternSelectorController;
import spp.presentation.controller.instructor.listener.InternSelectionChangeListener;
import spp.presentation.controller.instructor.listener.InternSelectionListener;
import spp.utils.view.table.GenericNestedSelector;
import spp.utils.view.table.InternStatusSelector;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InternProgressController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TableView<InternDTO> tblInterns;
    @FXML private TableColumn<InternDTO, String> colDocumentType;
    @FXML private TableColumn<InternDTO, String> colGrade;
    private InternSelectionListener selectionListener;
    private ObservableList<InternDTO> internsDocumentsUploadObservableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        InternSelectorController InternSelectorController = new InternSelectorController();
        InternSelectionChangeListener changeListener = new InternSelectionChangeListener(InternSelectorController);
        tblInterns.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }

    public void setStatusLabel(Label sharedStatusLabel) {
        this.lblStatus = sharedStatusLabel;
    }

    private void setUpColumns() {
        GenericNestedSelector<InternDTO> nameSelector =
                new GenericNestedSelector<>("fullName", "Sin nombre");

        InternStatusSelector statusSelector = new InternStatusSelector();

        colDocumentType.setCellValueFactory(nameSelector);
        colGrade.setCellValueFactory(statusSelector);
    }

    public void setInternSelectionListener(InternSelectionListener listener) {
        this.selectionListener = listener;
    }

    public void displayInterns(List<InternDTO> sortedInterns) {
        internsDocumentsUploadObservableList = FXCollections.observableArrayList(sortedInterns);
        tblInterns.setItems(internsDocumentsUploadObservableList);
    }

    public void handleInternSelection(InternDTO intern) {
        if (selectionListener != null) {
            selectionListener.onInternSelected(intern);
        }
    }
}