package spp.utils.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import java.lang.reflect.Method;

public class NestedPropertyValueFactory<S, T> implements Callback<CellDataFeatures<S, T>, ObservableValue<T>> {

    private final String fieldsPath;
    private final String defaultValue;

    public NestedPropertyValueFactory(String fieldsPath, String defaultValue) {
        this.fieldsPath = fieldsPath;
        this.defaultValue = defaultValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ObservableValue<T> call(CellDataFeatures<S, T> cellData) {
        if (cellData == null || cellData.getValue() == null) {
            return (ObservableValue<T>) new SimpleStringProperty(defaultValue);
        }

        try {
            Object currentObject = cellData.getValue();
            String[] fields = fieldsPath.split("\\.");

            for (String field : fields) {
                if (currentObject == null) {
                    break;
                }

                String getterName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
                Method method = currentObject.getClass().getMethod(getterName);
                currentObject = method.invoke(currentObject);
            }

            String result = (currentObject != null) ? currentObject.toString() : defaultValue;
            return (ObservableValue<T>) new SimpleStringProperty(result);

        } catch (Exception e) {
            // Si ocurre un error de reflexión (ej. el método no existe), muestra el valor por defecto
            return (ObservableValue<T>) new SimpleStringProperty(defaultValue);
        }
    }
}