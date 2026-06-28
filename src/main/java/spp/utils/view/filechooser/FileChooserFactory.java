package spp.utils.view.filechooser;

import javafx.stage.FileChooser;
import java.util.ArrayList;
import java.util.List;

public class FileChooserFactory {

    private final int ZERO_LENGTH = 0;

    public FileChooser createOpenDialog(String title, AllowedExtension... allowedExtensions) {
        FileChooser configuredFileChooser = new FileChooser();
        configuredFileChooser.setTitle(title);

        if (allowedExtensions != null && allowedExtensions.length > ZERO_LENGTH) {
            FileChooser.ExtensionFilter combinedFilter = buildCombinedFilter(allowedExtensions);
            configuredFileChooser.getExtensionFilters().add(combinedFilter);
        }

        return configuredFileChooser;
    }

    public FileChooser createSaveDialog(String title, String initialFileName, AllowedExtension allowedExtension) {
        FileChooser configuredFileChooser = new FileChooser();
        configuredFileChooser.setTitle(title);
        configuredFileChooser.setInitialFileName(initialFileName);

        if (allowedExtension != null) {
            configuredFileChooser.getExtensionFilters().add(allowedExtension.toJavaFXFilter());
        }

        return configuredFileChooser;
    }

    private FileChooser.ExtensionFilter buildCombinedFilter(AllowedExtension[] extensions) {
        List<String> rawPatternsList = new ArrayList<>();
        StringBuilder descriptionBuilder = new StringBuilder("Documentos permitidos (");

        for (int index = 0; index < extensions.length; index++) {
            AllowedExtension currentExtension = extensions[index];
            rawPatternsList.add(currentExtension.getExtensionPattern());
            descriptionBuilder.append(currentExtension.getExtensionPattern());

            if (index < extensions.length - 1) {
                descriptionBuilder.append(", ");
            }
        }
        descriptionBuilder.append(")");

        return new FileChooser.ExtensionFilter(
                descriptionBuilder.toString(),
                rawPatternsList
        );
    }
}