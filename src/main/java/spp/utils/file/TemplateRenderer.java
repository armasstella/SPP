package spp.utils.file;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class TemplateRenderer {

    private TemplateRenderer() {
    }

    public static String render(String templateResourcePath, Map<String, String> values) throws IOException {
        String renderedResult = loadTemplate(templateResourcePath);

        for (Map.Entry<String, String> entryValue : values.entrySet()) {
            String templateKey = "${" + entryValue.getKey() + "}";
            String replacementValue = entryValue.getValue();
            renderedResult = renderedResult.replace(templateKey, replacementValue);
        }

        return renderedResult;
    }

    public static String escape(String value) {
        String escapedValue = "";

        if (value != null) {
            escapedValue = value;
            escapedValue = escapedValue.replace("&", "&amp;");
            escapedValue = escapedValue.replace("<", "&lt;");
            escapedValue = escapedValue.replace(">", "&gt;");
        }

        return escapedValue;
    }

    private static String loadTemplate(String templateResourcePath) throws IOException {
        String loadedContent = "";

        try (InputStream inputStream = TemplateRenderer.class.getResourceAsStream(templateResourcePath)) {
            if (inputStream == null) {
                throw new IOException("No se encontró la plantilla: " + templateResourcePath);
            }

            byte[] fileBytes = inputStream.readAllBytes();
            loadedContent = new String(fileBytes, StandardCharsets.UTF_8);
        }

        return loadedContent;
    }
}