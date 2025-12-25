package org.example.codebase.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

@Component
@Slf4j
public class MergeFileService {

    public static byte[] mergePdfFiles(List<InputStream> inputPdfList) {
        if (inputPdfList.isEmpty()) {
            return new byte[0];
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (var outputDocument = new PdfDocument(new PdfWriter(out))) {
            PdfMerger mergedFile = new PdfMerger(outputDocument);
            for (InputStream inputStream : inputPdfList) {
                var pdfDocument = new PdfDocument(new PdfReader(inputStream));
                mergedFile.merge(pdfDocument, 1, pdfDocument.getNumberOfPages());
            }
        } catch (Exception e) {
            return new byte[0];
        }
        log.info("Merged {} files to PDF, the number of valid bytes: {} ", inputPdfList.size(), out.size());
        return out.toByteArray();
    }

    public static InputStream convertImageToPdf(byte[] input) {
        ImageData imageData = ImageDataFactory.create(input);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PdfDocument pdfDocument = new PdfDocument(new PdfWriter(out));
             Document document = new Document(pdfDocument, PageSize.A4)) {
            document.setMargins(50, 30, 50, 30);

            Image image = new Image(imageData);
            float imageHeight = image.getImageHeight();
            float imageWidth = image.getImageWidth();
            float pageWidth = pdfDocument.getDefaultPageSize().getWidth() - document.getRightMargin()
                    - document.getLeftMargin();
            float pageHeight = pdfDocument.getDefaultPageSize().getHeight() - document.getTopMargin()
                    - document.getBottomMargin();
            float scalePercent;
            if (imageWidth >= imageHeight) {
                if (imageWidth > pageWidth) {
                    scalePercent = pageWidth / imageWidth;
                    image.scaleAbsolute(imageWidth * scalePercent, imageHeight * scalePercent);
                }
            } else {
                if (imageHeight > pageHeight) {
                    scalePercent = pageHeight / imageHeight;
                    image.scaleAbsolute(imageWidth * scalePercent, imageHeight * scalePercent);
                }
            }

            image.setHorizontalAlignment(HorizontalAlignment.CENTER);
            Color borderColor = new DeviceRgb(192, 192, 192);
            image.setBorder(new SolidBorder(borderColor, Border.DASHED));

            // Vertical Alignment
            Table table = new Table(UnitValue.createPercentArray(new float[]{1})).useAllAvailableWidth();
            Cell cell = new Cell();
            cell.setBorder(Border.NO_BORDER);
            cell.setMargin(0);
            cell.setPadding(0);
            cell.setMinHeight(document.getPdfDocument().getDefaultPageSize().getHeight()
                    - document.getTopMargin() - document.getBottomMargin());
            cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
            cell.add(image);
            table.addCell(cell);
            document.add(table);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    public static InputStream resize(byte[] source) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PdfDocument pdfOut = new PdfDocument(new PdfWriter(out));
             PdfDocument pdfIn = new PdfDocument(new PdfReader(new ByteArrayInputStream(source)))) {
            pdfOut.setDefaultPageSize(PageSize.A4);
            for (int i = 1; i <= pdfIn.getNumberOfPages(); i++) {
                PdfPage page = pdfIn.getPage(i);
                Rectangle currentPageSize = page.getPageSize();
                float scaleWidth = PageSize.A4.getWidth() / currentPageSize.getWidth();

                PdfFormXObject formXObject = page.copyAsFormXObject(pdfOut);
                PdfCanvas pdfCanvas = new PdfCanvas(pdfOut.addNewPage());
                if (scaleWidth < 1) {
                    float scaleHeight = PageSize.A4.getHeight() / currentPageSize.getHeight();
                    pdfCanvas.addXObjectWithTransformationMatrix(formXObject, scaleWidth, 0, 0, scaleHeight, 0, 0);
                } else {
                    pdfCanvas.addXObject(formXObject);
                }
            }
        } catch (Exception ex) {
            log.error("resize pdf exception - {}", ex.getMessage());
            return null;
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
