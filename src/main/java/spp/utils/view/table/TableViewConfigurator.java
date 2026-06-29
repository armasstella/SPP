package spp.utils.view.table;

import javafx.scene.control.TableView;

public class TableViewConfigurator {

    public static <T> void enableDoubleClickSelection(TableView<T> table, DoubleClickListener<T> listener) {
        boolean isValidConfiguration = false;

        if (table != null && listener != null) {
            isValidConfiguration = true;
        }

        if (isValidConfiguration) {
            TableDoubleClickHandler<T> doubleClickHandler = new TableDoubleClickHandler<>(table, listener);
            table.setOnMouseClicked(doubleClickHandler);
        }
    }
}