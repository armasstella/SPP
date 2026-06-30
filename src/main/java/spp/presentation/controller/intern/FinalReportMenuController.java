package spp.presentation.controller.intern;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import spp.businesslogic.dao.DeliverableProductDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.dto.DeliverableProductDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.ActivityDAO;
import spp.businesslogic.dao.InternDAO;
import spp.utils.view.table.GenericNestedSelector;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.window.ViewNavigator;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class FinalReportMenuController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TableView<ActivityDTO> tblActivities;
    @FXML private TableColumn<ActivityDTO, String> colTitle;
    @FXML private TableColumn<ActivityDTO, String> colDescription;
    @FXML private TableColumn<ActivityDTO, String> colStartDate;
    @FXML private TableColumn<ActivityDTO, String> colEndDate;
    @FXML private TableView<DeliverableProductDTO> tblDeliverableProducts;
    @FXML private TableColumn<DeliverableProductDTO, String> colDeliverableProductName;
    @FXML private TableColumn<DeliverableProductDTO, String> colDeliverableProductDescription;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        obtainActivities();
        obtainDeliverableProducts();

    }

    private void setUpColumns() {
        colTitle.setCellValueFactory(
                new GenericNestedSelector<>("title", "Sin título"));
        colDescription.setCellValueFactory(
                new GenericNestedSelector<>("description", "Sin descripción"));
        colStartDate.setCellValueFactory(
                new GenericNestedSelector<>("startDateText", ""));
        colEndDate.setCellValueFactory(
                new GenericNestedSelector<>("endDateText", ""));

        colDeliverableProductName.setCellValueFactory(
                new GenericNestedSelector<>("name", ""));
        colDeliverableProductDescription.setCellValueFactory(
                new GenericNestedSelector<>("description", ""));

    }

    private void obtainActivities() {
        ActivityDAO activityDAO = new ActivityDAO();
        InternDAO internDAO = new InternDAO();
        try {
            String studentNumber = internDAO.findActiveStudentNumberByEmail(ActiveSessionDTO.get().getEmail());
            List<ActivityDTO> activityList = activityDAO.findFinalActivitiesByStudentNumber(studentNumber);
            tblActivities.setItems(FXCollections.observableArrayList(activityList));
        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, e.getMessage());
        }

    }

    private void obtainDeliverableProducts() {
        DeliverableProductDAO deliverableProductDAO = new DeliverableProductDAO();
        InternDAO internDAO = new InternDAO();
        try {
            String studentNumber = internDAO.findActiveStudentNumberByEmail(ActiveSessionDTO.get().getEmail());
            List<DeliverableProductDTO> deliverableProductList = deliverableProductDAO.findDeliverableProductsByStudentNumber(studentNumber);
            tblDeliverableProducts.setItems(FXCollections.observableArrayList(deliverableProductList));
        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, e.getMessage());
        }

    }

    @FXML
    private void goBackToMenu(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/InternMenuView.fxml",
                "Menú Practicante", event);

    }

    @FXML
    private void goToActivityRegistrationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/FinalActivityRegistrationView.fxml",
                "Registro actividades", event);

    }

    @FXML
    private void goToDeliverableProductRegistrationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/DeliverableProductRegistrationView.fxml",
                "Registro productos entregables", event);

    }

    @FXML
    private void goToFinalReportGenerationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/FinalReportGenerationView.fxml",
                "Generar reporte final", event);

    }

}
