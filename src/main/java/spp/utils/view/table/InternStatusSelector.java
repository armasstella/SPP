package spp.utils.view.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.dto.InternDocumentReviewDTO;
import spp.businesslogic.enums.DocumentType;
import java.util.List;

public class InternStatusSelector implements Callback<TableColumn.CellDataFeatures<InternDTO, String>, ObservableValue<String>> {

    @Override
    public ObservableValue<String> call(TableColumn.CellDataFeatures<InternDTO, String> cellData) {
        ObservableValue<String> finalStatusResult = null;

        InternDTO intern = cellData.getValue();
        List<InternDocumentReviewDTO> documents = intern.getDocuments();

        boolean hasNoDocuments = documents == null || documents.isEmpty();

        if (hasNoDocuments) {
            finalStatusResult = new SimpleStringProperty("Sin entregas");
        } else {
            int index = 0;
            int totalDocuments = documents.size();
            boolean hasPending = false;
            String statusMessage = "Todo evaluado";

            while (index < totalDocuments && !hasPending) {
                InternDocumentReviewDTO currentDocument = documents.get(index);
                boolean isGraded = currentDocument.isGraded();

                if (!isGraded) {
                    hasPending = true;
                    DocumentType docType = currentDocument.getDocumentType();

                    String docName = currentDocument.getOriginalName();
                    boolean hasValidType = docType != null;

                    if (hasValidType) {
                        docName = docType.name();
                    }

                    statusMessage = docName + " (Pendiente)";
                }

                index++;
            }

            finalStatusResult = new SimpleStringProperty(statusMessage);
        }

        return finalStatusResult;
    }
}