package spp.utils.view.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.logger.AppLogger;

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
        String finalExtractedValue = defaultValue;

        boolean hasValidCellData = cellData != null;
        if (hasValidCellData) {
            boolean hasValidObject = cellData.getValue() != null;

            if (hasValidObject) {
                finalExtractedValue = extractValueFromPath(cellData.getValue());
            }
        }

        return new SimpleStringProperty(finalExtractedValue);
    }

    private String extractValueFromPath(Object rootObject) {
        String extractedString = defaultValue;
        Object currentIterationObject = rootObject;
        String[] propertiesToNavigate = fieldsPath.split("\\.");
        boolean brokenPath = false;

        try {
            for (String propertyName : propertiesToNavigate) {
                if (currentIterationObject == null) {
                    brokenPath = true;
                    break;
                }

                String firstLetter = propertyName.substring(0, 1);
                String firstLetterCapitalized = firstLetter.toUpperCase();
                String restOfWord = propertyName.substring(1);
                String capitalizedPropertyName = firstLetterCapitalized + restOfWord;

                Method propertyMethod = findGetterMethod(currentIterationObject, capitalizedPropertyName);
                currentIterationObject = propertyMethod.invoke(currentIterationObject);
            }

            if (!brokenPath && currentIterationObject != null) {
                extractedString = String.valueOf(currentIterationObject);
            }

        } catch (Exception reflectionException) {
            AppLogger.log(ExceptionLevel.ERROR, reflectionException);
        }

        return extractedString;
    }

    private Method findGetterMethod(Object targetObject, String capitalizedPropertyName) throws NoSuchMethodException {
        Method foundMethod = null;
        String standardGetterName = "get" + capitalizedPropertyName;

        try {
            foundMethod = targetObject.getClass().getMethod(standardGetterName);
        } catch (NoSuchMethodException exception) {
            String booleanGetterName = "is" + capitalizedPropertyName;
            foundMethod = targetObject.getClass().getMethod(booleanGetterName);
        }

        return foundMethod;
    }
}