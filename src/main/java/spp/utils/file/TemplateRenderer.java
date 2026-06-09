package spp.utils.file;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public final class TemplateRenderer {

    private TemplateRenderer() {
    }

    public static String render(String templateResourcePath, Map<String, String> values) throws IOException {
        String result = loadTemplate(templateResourcePath);
        for (Map.Entry<String, String> value : values.entrySet()) {
            result = result.replace("${" + value.getKey() + "}", value.getValue());
        }
        return result;

    }

    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");

    }

    private static String loadTemplate(String templateResourcePath) throws IOException {
        try (InputStream inputStream = TemplateRenderer.class.getResourceAsStream(templateResourcePath)) {
            if (inputStream == null) {
                throw new IOException("No se encontró la plantilla: " + templateResourcePath);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }

    }

}
