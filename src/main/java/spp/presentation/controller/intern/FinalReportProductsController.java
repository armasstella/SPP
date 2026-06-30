package spp.presentation.controller.intern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import spp.businesslogic.dao.DeliverableProductDAO;
import spp.businesslogic.dao.InternDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.DeliverableProductDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.presentation.controller.intern.listener.DeliverableProductInclusionListener;
import spp.presentation.controller.intern.listener.IncludeDeliverableProductModificationListener;
import spp.utils.view.alert.AlertHelper;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.table.GenericNestedSelector;
import spp.utils.view.table.TableViewConfigurator;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FinalReportProductsController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private Label lblProductsCounter;
    @FXML private TableView<DeliverableProductDTO> tblDeliverableProducts;
    @FXML private TableColumn<DeliverableProductDTO, String> colDeliverableProductName;
    @FXML private TableColumn<DeliverableProductDTO, String> colDeliverableProductDescription;
    @FXML private TableColumn<DeliverableProductDTO, String> colDeliverableProductProgress;
    @FXML private TableColumn<DeliverableProductDTO, String> colDeliverableProductObservations;
    @FXML private TableView<DeliverableProductDTO> tblIncludedDeliverableProducts;
    @FXML private TableColumn<DeliverableProductDTO, String> colChosenDeliverableProductName;
    @FXML private TableColumn<DeliverableProductDTO, String> colChosenDeliverableProductDescription;
    @FXML private TableColumn<DeliverableProductDTO, String> colChosenDeliverableProductProgress;
    @FXML private TableColumn<DeliverableProductDTO, String> colChosenDeliverableProductObservations;
    private final DeliverableProductDAO deliverableProductDAO = new DeliverableProductDAO();
    private final InternDAO internDAO = new InternDAO();
    private ObservableList<DeliverableProductDTO> availableDeliverableProductObservableList;
    private ObservableList<DeliverableProductDTO> includedDeliverableProductObservableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpDeliverableProductTableColumns();
        setUpIncludedDeliverableProducts();
        obtainDeliverableProducts();
        setUpDeliverableProductsTablesClicks();
        updateDeliverableProductCounter();
    }

    public List<DeliverableProductDTO> getIncludedProducts() {
        return new ArrayList<>(includedDeliverableProductObservableList);
    }

    public void clearIncludedProducts() throws DAOException {
        for (DeliverableProductDTO deliverableProduct : includedDeliverableProductObservableList) {
            int deliverableProductId = deliverableProduct.getId();
            deliverableProductDAO.deleteDeliverableProduct(deliverableProductId);
        }
        includedDeliverableProductObservableList.clear();
        updateDeliverableProductCounter();
    }

    public void setStatusLabel(Label sharedStatusLabel) {
        this.lblStatus = sharedStatusLabel;
    }

    private void setUpDeliverableProductTableColumns() {
        GenericNestedSelector<DeliverableProductDTO> nameSelector =
                new GenericNestedSelector<>("name", "Sin nombre");
        GenericNestedSelector<DeliverableProductDTO> descriptionSelector =
                new GenericNestedSelector<>("description", "Sin descripcion");
        GenericNestedSelector<DeliverableProductDTO> progressSelector =
                new GenericNestedSelector<>("progress", "Sin avance");
        GenericNestedSelector<DeliverableProductDTO> observationsSelector =
                new GenericNestedSelector<>("observations", "Sin observaciones");

        colDeliverableProductName.setCellValueFactory(nameSelector);
        colDeliverableProductDescription.setCellValueFactory(descriptionSelector);
        colDeliverableProductProgress.setCellValueFactory(progressSelector);
        colDeliverableProductObservations.setCellValueFactory(observationsSelector);

        colChosenDeliverableProductName.setCellValueFactory(nameSelector);
        colChosenDeliverableProductDescription.setCellValueFactory(descriptionSelector);
        colChosenDeliverableProductProgress.setCellValueFactory(progressSelector);
        colChosenDeliverableProductObservations.setCellValueFactory(observationsSelector);
    }

    private void setUpIncludedDeliverableProducts() {
        includedDeliverableProductObservableList = FXCollections.observableArrayList();
        tblIncludedDeliverableProducts.setItems(includedDeliverableProductObservableList);
    }

    private void obtainDeliverableProducts() {
        try {
            String userEmail = ActiveSessionDTO.get().getEmail();
            String studentNumber = internDAO.findActiveStudentNumberByEmail(userEmail);

            List<DeliverableProductDTO> deliverableProductList =
                    deliverableProductDAO.findDeliverableProductsByStudentNumber(studentNumber);
            availableDeliverableProductObservableList = FXCollections.observableArrayList(deliverableProductList);
            tblDeliverableProducts.setItems(availableDeliverableProductObservableList);

        } catch (DAOException exception) {
            StatusLabel.showError(lblStatus, exception.getMessage());
        }
    }

    private void setUpDeliverableProductsTablesClicks() {
        DeliverableProductInclusionListener deliverableProductInclusionListener =
                new DeliverableProductInclusionListener(this);
        IncludeDeliverableProductModificationListener includeDeliverableProductModificationListener =
                new IncludeDeliverableProductModificationListener(this);

        TableViewConfigurator.enableDoubleClickSelection(
                tblDeliverableProducts, deliverableProductInclusionListener);
        TableViewConfigurator.enableDoubleClickSelection(
                tblIncludedDeliverableProducts, includeDeliverableProductModificationListener);
    }

    public void includeDeliverableProduct(DeliverableProductDTO deliverableProduct) {
        availableDeliverableProductObservableList.remove(deliverableProduct);
        includedDeliverableProductObservableList.add(deliverableProduct);
        updateDeliverableProductCounter();
    }

    public void processProductModificationAction(DeliverableProductDTO deliverableProduct) {
        boolean choice = AlertHelper.showConfirmation(
                "Producto elegido", "¿Desea sacar este producto entregable?");

        if (choice) {
            excludeDeliverableProduct(deliverableProduct);
        }
    }

    private void excludeDeliverableProduct(DeliverableProductDTO deliverableProduct) {
        includedDeliverableProductObservableList.remove(deliverableProduct);
        availableDeliverableProductObservableList.add(deliverableProduct);
        updateDeliverableProductCounter();
    }

    private void updateDeliverableProductCounter() {
        int deliverableProductsCount = includedDeliverableProductObservableList.size();
        String counterText = deliverableProductsCount + " productos entregables incluidos.";
        lblProductsCounter.setText(counterText);
    }

}