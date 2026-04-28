package br.com.minuta.despacho;

import br.com.minuta.despacho.csv.CsvReader;
import br.com.minuta.despacho.model.GolDespachoData;
import br.com.minuta.despacho.pdf.PdfDispatcher;
import br.com.minuta.despacho.printer.PrinterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutomacaoScheduler {

    private static final Logger logger = LoggerFactory.getLogger(AutomacaoScheduler.class);
    private final List<String> pastasEntrada;
    private final String pastaProcessados;
    private final PdfDispatcher pdfDispatcher;
    private final PrinterService printerService;

    public AutomacaoScheduler(List<String> pastasEntrada,
                              String pastaProcessados,
                              String pastaTemp,
                              String nomeDaImpressora,
                              int copias) {
        this.pastasEntrada = pastasEntrada;
        this.pastaProcessados = pastaProcessados;
        this.pdfDispatcher = new PdfDispatcher(pastaTemp);
        this.printerService = new PrinterService(nomeDaImpressora, copias);
    }

    public void iniciar() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                logger.info("--- Iniciando ciclo de verificação de pastas ---");
                
                for (String path : pastasEntrada) {
                    CsvReader csvReader = new CsvReader(path, pastaProcessados);
                    List<File> csvs = csvReader.buscarCsvs();

                    if (csvs.isEmpty()) {
                        continue;
                    }

                    logger.info("Pasta com arquivos encontrados: {}", path);

                    for (File csv : csvs) {
                        logger.info("Processando: {}", csv.getName());

                        List<GolDespachoData> registros = csvReader.lerCsv(csv);

                        if (registros.isEmpty()) {
                            logger.warn("CSV vazio ou sem dados válidos: {}", csv.getName());
                            csvReader.moverParaProcessados(csv);
                            continue;
                        }

                        boolean todosOk = true;
                        for (GolDespachoData dados : registros) {
                            try {
                                logger.info("Gerando PDF - Companhia: {} | Destinatário: {}", 
                                        dados.companhia, dados.destinatarioNome);

                                File pdf = pdfDispatcher.gerar(dados);
                                printerService.imprimir(pdf);
                                pdf.delete();

                            } catch (IllegalArgumentException e) {
                                logger.error("Erro nos dados do CSV para {}: {}", dados.destinatarioNome, e.getMessage());
                            } catch (Exception e) {
                                todosOk = false;
                                logger.error("Erro no PDF/Impressão: {}", e.getMessage(), e);
                            }
                        }

                        if (todosOk) {
                            csvReader.moverParaProcessados(csv);
                            logger.info("Concluído: CSV movido para processados: {}", csv.getName());
                        } else {
                            logger.error("Mantido: CSV permanece na entrada por causa de erros: {}", csv.getName());
                        }
                    }
                }

            } catch (Throwable t) {
                logger.error("ERRO CRÍTICO NO CICLO DE AUTOMAÇÃO: {}", t.getMessage(), t);
            }

        }, 0, 30, TimeUnit.SECONDS);

        logger.info("============================================");
        logger.info("  Automação de Despacho iniciada!");
        logger.info("  Pastas monitoradas: {}", pastasEntrada.size());
        logger.info("  Verificando pastas a cada 30 segundos...");
        logger.info("============================================");
    }
}
