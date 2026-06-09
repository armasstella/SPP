package spp.presentation.controller.intern;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.ActivityDAO;
import spp.businesslogic.dao.InternDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.GenericNestedSelector;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class MonthlyActivityRegistersController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TableView<ActivityDTO> tblActivities;
    @FXML private TableColumn<ActivityDTO, String> colTitle;
    @FXML private TableColumn<ActivityDTO, String> colDescription;
    @FXML private TableColumn<ActivityDTO, String> colStartDate;
    @FXML private TableColumn<ActivityDTO, String> colEndDate;
    private final ActivityDAO activityDAO = new ActivityDAO();
    private final InternDAO internDAO = new InternDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        obtainActivities();

    }

    private void setUpColumns() {
        colTitle.setCellValueFactory(new GenericNestedSelector<>("title", "Sin título"));
        colDescription.setCellValueFactory(new GenericNestedSelector<>("description", "Sin descripción"));
        colStartDate.setCellValueFactory(new GenericNestedSelector<>("startDateText", ""));
        colEndDate.setCellValueFactory(new GenericNestedSelector<>("endDateText", ""));

    }

    private void obtainActivities() {
        try {
            String studentNumber = internDAO.obtainStudentNumber(ActiveSessionDTO.get().getEmail());
            List<ActivityDTO> activityList = activityDAO.obtainActivitiesByIntern(studentNumber);
            tblActivities.setItems(FXCollections.observableArrayList(activityList));
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al obtener actividades");
        }

    }

    @FXML
    private void goBackToMenu(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/InternMenuView.fxml",
                "Menú Practicante", event);

    }

    @FXML
    private void goToActivityRegistrationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/ActivityRegistrationView.fxml",
                "Registro actividades", event);

    }

    @FXML
    private void goToMonthlyReportGenerationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/MonthlyReportGenerationView.fxml",
                "Generar reporte mensual", event);

    }

}
