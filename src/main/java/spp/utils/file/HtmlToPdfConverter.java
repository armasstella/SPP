package spp.utils.file;


import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public final class HtmlToPdfConverter {

    private HtmlToPdfConverter() {
    }

    public static void convertToFile(String html, File outputFile) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(outputFile)) {
            convert(html, outputStream);
        }

    }

    public static void convert(String html, OutputStream outputStream) throws IOException {
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();
        builder.withHtmlContent(html, null);
        builder.toStream(outputStream);
        builder.run();

    }

}
