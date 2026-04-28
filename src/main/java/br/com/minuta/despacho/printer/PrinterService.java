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
    private final int copias;

    public PrinterService(String nomeDaImpressora, int copias) {
        this.nomeDaImpressora = nomeDaImpressora;
        this.copias = copias > 0 ? copias : 1;
    }

    public static void listarImpressoras() {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();

        logger.info("=== Impressoras disponíveis ===");
        for (PrintService ps : services) {
            String isDefault = (defaultService != null && ps.getName().equals(defaultService.getName())) ? " [DEFAULT]" : "";
            logger.info("  -> {}{}", ps.getName(), isDefault);
        }
        logger.info("===============================");
    }

    public void imprimir(File pdfFile) throws Exception {
        PrintService impressora = encontrarImpressora();

        if (impressora == null) {
            throw new RuntimeException(
                    "Nenhuma impressora encontrada. Configure 'impressora.nome' no config.properties " +
                            "ou defina uma impressora padrão no Windows."
            );
        }

        // Usando PDFBox para imprimir de forma confiável (renderiza antes de enviar)
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintService(impressora);
            job.setPageable(new PDFPageable(document));
            job.setCopies(copias);
            job.print();
        }

        logger.info("[IMPRESSO] {} ({} vias) -> {}", pdfFile.getName(), copias, impressora.getName());
    }

    public boolean existeImpressora() {
        return encontrarImpressora() != null;
    }

    public String getNomeImpressoraUtilizada() {
        PrintService ps = encontrarImpressora();
        return (ps != null) ? ps.getName() : "Nenhuma encontrada";
    }

    private PrintService encontrarImpressora() {
        // 1. Se não houver nome configurado, busca a padrão
        if (nomeDaImpressora == null || nomeDaImpressora.trim().isEmpty() || nomeDaImpressora.equals("NOME_DA_IMPRESSORA_AQUI")) {
            return PrintServiceLookup.lookupDefaultPrintService();
        }

        // 2. Tenta encontrar pelo nome exato
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService ps : services) {
            if (ps.getName().equalsIgnoreCase(nomeDaImpressora)) {
                return ps;
            }
        }

        // 3. Se não encontrou pelo nome, tenta a padrão como fallback
        logger.warn("Impressora '{}' não encontrada. Tentando utilizar a impressora padrão do sistema.", nomeDaImpressora);
        return PrintServiceLookup.lookupDefaultPrintService();
    }
}
