package spp.utils.view.table;

import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class TableDoubleClickHandler<T> implements EventHandler<MouseEvent> {
    private final TableView<T> targetTable;
    private final DoubleClickListener<T> actionListener;
    private static final int REQUIRED_CLICKS = 2;

    public TableDoubleClickHandler(TableView<T> targetTable, DoubleClickListener<T> actionListener) {
        this.targetTable = targetTable;
        this.actionListener = actionListener;
    }

    @Override
    public void handle(MouseEvent event) {
        boolean isDoubleClick = false;
        boolean hasSelection = false;

        int currentClicks = event.getClickCount();
        if (currentClicks == REQUIRED_CLICKS) {
            isDoubleClick = true;
        }

        T selectedItem = targetTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            hasSelection = true;
        }

        if (isDoubleClick && hasSelection) {
            actionListener.onItemSelected(selectedItem);
        }
    }

}
