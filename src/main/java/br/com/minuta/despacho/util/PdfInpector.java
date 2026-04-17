package br.com.minuta.despacho.util;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class PdfInpector {

    private static final Logger logger = LoggerFactory.getLogger(PdfInpector.class);

    public static void main(String[] args) throws Exception {
        String caminhoPdf = "/templates/MINUTA_AZUL.pdf";

        PdfReader reader = new PdfReader(caminhoPdf);
        PdfDocument pdfDoc = new PdfDocument(reader);

        // Tenta ler campos AcroForm
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, false);

        if (form != null && !form.getFormFields().isEmpty()) {
            logger.info("=== PDF TEM CAMPOS ACROFORM ===");
            Map<String, PdfFormField> campos = form.getFormFields();
            for (Map.Entry<String, PdfFormField> entry : campos.entrySet()) {
                PdfFormField field = entry.getValue();
                Rectangle rect = field.getWidgets().get(0).getRectangle().toRectangle();
                logger.info("Campo: {:40} | X:{} Y:{} W:{} H:{}",
                        entry.getKey(), rect.getX(), rect.getY(),
                        rect.getWidth(), rect.getHeight());
            }
        } else {
            logger.info("=== PDF NÃO TEM ACROFORM — usar coordenadas manuais ===");
            logger.info("Tamanho da página: {}", pdfDoc.getFirstPage().getPageSize());
        }

        pdfDoc.close();
    }
}
