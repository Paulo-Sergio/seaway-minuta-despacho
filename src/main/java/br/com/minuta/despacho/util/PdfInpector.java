package br.com.minuta.despacho.util;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;

import java.util.Map;

public class PdfInpector {

    public static void main(String[] args) throws Exception {
        String caminhoPdf = "/templates/MINUTA_AZUL.pdf";

        PdfReader reader = new PdfReader(caminhoPdf);
        PdfDocument pdfDoc = new PdfDocument(reader);

        // Tenta ler campos AcroForm
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, false);

        if (form != null && !form.getFormFields().isEmpty()) {
            System.out.println("=== PDF TEM CAMPOS ACROFORM ===");
            Map<String, PdfFormField> campos = form.getFormFields();
            for (Map.Entry<String, PdfFormField> entry : campos.entrySet()) {
                PdfFormField field = entry.getValue();
                Rectangle rect = field.getWidgets().get(0).getRectangle().toRectangle();
                System.out.printf("Campo: %-40s | X:%.0f Y:%.0f W:%.0f H:%.0f%n",
                        entry.getKey(), rect.getX(), rect.getY(),
                        rect.getWidth(), rect.getHeight());
            }
        } else {
            System.out.println("=== PDF NÃO TEM ACROFORM — usar coordenadas manuais ===");
            System.out.println("Tamanho da página: " +
                    pdfDoc.getFirstPage().getPageSize());
        }

        pdfDoc.close();
    }
}
