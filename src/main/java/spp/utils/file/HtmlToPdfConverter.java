package spp.utils.file;


import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import spp.businesslogic.exceptions.FileGenerationException;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.logger.AppLogger;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public final class HtmlToPdfConverter {

    private HtmlToPdfConverter() {
    }

    public static void convertToFile(String html, File outputFile) throws FileGenerationException {
        try (OutputStream outputStream = new FileOutputStream(outputFile)) {
            convert(html, outputStream);
        } catch (IOException e) {
            AppLogger.log(ExceptionLevel.ERROR, e);
            throw new FileGenerationException("Error generando archivo");
        }

    }

    public static void convert(String html, OutputStream outputStream) throws FileGenerationException {
        try {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();
        } catch (IOException e) {
            AppLogger.log(ExceptionLevel.ERROR, e);
            throw new FileGenerationException("Error generando archivo");
        }


    }

}
