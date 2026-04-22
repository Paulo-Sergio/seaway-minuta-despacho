package br.com.minuta.despacho.printer;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.print.PrinterJob;
import java.io.File;

public class PrinterService {

    private static final Logger logger = LoggerFactory.getLogger(PrinterService.class);
    private final String nomeDaImpressora;

    public PrinterService(String nomeDaImpressora) {
        this.nomeDaImpressora = nomeDaImpressora;
    }

    public static void listarImpressoras() {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        logger.info("=== Impressoras disponíveis ===");
        for (PrintService ps : services) {
            logger.info("  -> {}", ps.getName());
        }
        logger.info("===============================");
    }

    public void imprimir(File pdfFile) throws Exception {
        PrintService impressora = encontrarImpressora();

        if (impressora == null) {
            throw new RuntimeException(
                    "Impressora não encontrada: [" + nomeDaImpressora + "]. " +
                            "Execute PrinterService.listarImpressoras() para ver os nomes disponíveis."
            );
        }

        // Usando PDFBox para imprimir de forma confiável (renderiza antes de enviar)
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintService(impressora);
            job.setPageable(new PDFPageable(document));
            job.print();
        }

        logger.info("[IMPRESSO] {} -> {}", pdfFile.getName(), impressora.getName());
    }

    public boolean existeImpressora() {
        return encontrarImpressora() != null;
    }

    private PrintService encontrarImpressora() {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService ps : services) {
            if (ps.getName().equalsIgnoreCase(nomeDaImpressora)) {
                return ps;
            }
        }
        return null;
    }
}
