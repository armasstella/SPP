package spp.utils.view;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import java.lang.reflect.Method;


public class GenericNestedSelector<S> implements Callback<CellDataFeatures<S, String>, ObservableValue<String>> {

    private final String fieldsPath;
    private final String defaultValue;

    public GenericNestedSelector(String fieldsPath, String defaultValue) {
        this.fieldsPath = fieldsPath;
        this.defaultValue = defaultValue;
    }

    @Override
    public ObservableValue<String> call(CellDataFeatures<S, String> cellData) {
        if (cellData == null || cellData.getValue() == null) {
            return new SimpleStringProperty(defaultValue);
        }

        try {
            Object currentObject = cellData.getValue();
            String[] fields = fieldsPath.split("\\.");

            for (String field : fields) {
                if (currentObject == null) {
                    break;
                }

                String getterName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);

                Method method;
                try {
                    method = currentObject.getClass().getMethod(getterName);
                } catch (NoSuchMethodException e) {
                    String isGetterName = "is" + field.substring(0, 1).toUpperCase() + field.substring(1);
                    method = currentObject.getClass().getMethod(isGetterName);
                }

                currentObject = method.invoke(currentObject);
            }

            if (currentObject == null) {
                return new SimpleStringProperty(defaultValue);
            }
            
            return new SimpleStringProperty(String.valueOf(currentObject));

        } catch (Exception e) {
            return new SimpleStringProperty(defaultValue);
        }
    }

}