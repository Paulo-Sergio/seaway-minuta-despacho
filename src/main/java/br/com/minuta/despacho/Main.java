package br.com.minuta.despacho;

import br.com.minuta.despacho.printer.PrinterService;

import java.io.File;

public class Main {

    // ============================================================
    //  CONFIGURAÇÕES — ajuste para o seu ambiente
    // ============================================================
    private static final String PASTA_ENTRADA = "C:/projetos/seaway-minuta-despacho/massa-de-testes/entrada";
    private static final String PASTA_PROCESSADOS = "C:/projetos/seaway-minuta-despacho/massa-de-testes/processados";
    private static final String PASTA_TEMP = "C:/projetos/seaway-minuta-despacho/massa-de-testes/temp";

    // Cole aqui o nome EXATO que aparecer ao rodar listarImpressoras()
    private static final String NOME_IMPRESSORA = "NOME_DA_IMPRESSORA_AQUI";
    // ============================================================

    public static void main(String[] args) {

        // Passo 1: garante que as pastas existem
        criarPastas();

        // Passo 2: lista as impressoras disponíveis para você copiar o nome correto acima
        System.out.println("\n[DICA] Impressoras disponíveis nesta máquina:");
        PrinterService.listarImpressoras();

        // Passo 3: inicia a automação
        AutomacaoScheduler scheduler = new AutomacaoScheduler(
                PASTA_ENTRADA,
                PASTA_PROCESSADOS,
                PASTA_TEMP,
                NOME_IMPRESSORA
        );

        scheduler.iniciar();

        // Mantém a JVM viva indefinidamente
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                System.out.println("\n[ENCERRADO] Automação de despacho finalizada.")
        ));
    }

    private static void criarPastas() {
        new File(PASTA_ENTRADA).mkdirs();
        new File(PASTA_PROCESSADOS).mkdirs();
        new File(PASTA_TEMP).mkdirs();
        System.out.println("[OK] Pastas verificadas/criadas.");
    }
}