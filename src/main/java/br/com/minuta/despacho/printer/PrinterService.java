package br.com.minuta.despacho.printer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import java.io.File;
import java.nio.file.Files;

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

        byte[] dados = Files.readAllBytes(pdfFile.toPath());

        DocFlavor flavor = DocFlavor.BYTE_ARRAY.PDF;
        if (!impressora.isDocFlavorSupported(flavor)) {
            flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        }

        Doc doc = new SimpleDoc(dados, flavor, null);

        PrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();
        attrs.add(new Copies(1));
        attrs.add(Sides.ONE_SIDED);
        attrs.add(OrientationRequested.PORTRAIT);
        attrs.add(MediaSizeName.ISO_A4);

        DocPrintJob job = impressora.createPrintJob();
        job.print(doc, attrs);

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
