package br.com.minuta.despacho;

import br.com.minuta.despacho.csv.CsvReader;
import br.com.minuta.despacho.model.DespachoData;
import br.com.minuta.despacho.pdf.PdfDispatcher;
import br.com.minuta.despacho.printer.PrinterService;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutomacaoScheduler {

    private final CsvReader csvReader;
    private final PdfDispatcher pdfDispatcher;
    private final PrinterService printerService;

    public AutomacaoScheduler(String pastaEntrada,
                              String pastaProcessados,
                              String pastaTemp,
                              String nomeDaImpressora) {
        this.csvReader = new CsvReader(pastaEntrada, pastaProcessados);
        this.pdfDispatcher = new PdfDispatcher(pastaTemp);
        this.printerService = new PrinterService(nomeDaImpressora);
    }

    public void iniciar() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                List<File> csvs = csvReader.buscarCsvs();

                if (csvs.isEmpty()) {
                    System.out.println("[AGUARDANDO] Nenhum CSV encontrado na pasta de entrada.");
                    return;
                }

                for (File csv : csvs) {
                    System.out.println("[PROCESSANDO] " + csv.getName());

                    List<DespachoData> registros = csvReader.lerCsv(csv);

                    if (registros.isEmpty()) {
                        System.out.println("[AVISO] CSV vazio ou sem dados válidos: " + csv.getName());
                        csvReader.moverParaProcessados(csv);
                        continue;
                    }

                    boolean todosOk = true;
                    for (DespachoData dados : registros) {
                        try {
                            System.out.println("[GERANDO PDF] Companhia: " + dados.companhia
                                    + " | Destinatário: " + dados.destinatarioNome);

                            File pdf = pdfDispatcher.gerar(dados);
                            printerService.imprimir(pdf);
                            pdf.delete();

                        } catch (IllegalArgumentException e) {
                            System.err.println("[ERRO CSV] " + e.getMessage());
                        } catch (Exception e) {
                            todosOk = false;
                            System.err.println("[ERRO PDF/IMPRESSÃO] " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    if (todosOk) {
                        csvReader.moverParaProcessados(csv);
                        System.out.println("[CONCLUÍDO] CSV movido para processados: " + csv.getName());
                    } else {
                        System.err.println("[MANTIDO] CSV permanece na entrada por causa de erros: "
                                + csv.getName());
                    }
                }

            } catch (Exception e) {
                System.err.println("[ERRO GERAL] " + e.getMessage());
                e.printStackTrace();
            }

        }, 0, 30, TimeUnit.SECONDS);

        System.out.println("============================================");
        System.out.println("  Automação de Despacho iniciada!");
        System.out.println("  Verificando pasta a cada 30 segundos...");
        System.out.println("============================================");
    }
}