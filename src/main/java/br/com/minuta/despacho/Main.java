package br.com.minuta.despacho;

import br.com.minuta.despacho.printer.PrinterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static List<String> pastasEntrada;
    private static String pastaProcessados;
    private static String pastaTemp;
    private static String nomeImpressora;

    public static void main(String[] args) {
        // Passo 0: carregar propriedades
        if (!carregarConfiguracoes()) {
            logger.error("Erro crítico: Não foi possível carregar as configurações.");
            return;
        }

        // Passo 1: garante que as pastas existem
        criarPastas();

        // Passo 2: Validar impressora
        PrinterService printerService = new PrinterService(nomeImpressora);
        if (!printerService.existeImpressora()) {
            logger.error("Nenhuma impressora encontrada (nem a configurada '{}' nem a padrão do Windows).", nomeImpressora);
            logger.error("A automação não será iniciada.");
            logger.info("Verifique a lista de impressoras disponíveis abaixo:");
            PrinterService.listarImpressoras();
            return;
        }

        logger.info("Impressora ativa: [{}]", printerService.getNomeImpressoraUtilizada());

        // Passo 3: inicia a automação
        AutomacaoScheduler scheduler = new AutomacaoScheduler(
                pastasEntrada,
                pastaProcessados,
                pastaTemp,
                nomeImpressora
        );

        scheduler.iniciar();

        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                logger.info("Automação de despacho finalizada.")
        ));
    }

    private static boolean carregarConfiguracoes() {
        Properties prop = new Properties();
        String fileName = "config.properties";

        try {
            // 1. Tenta carregar de um arquivo externo (na mesma pasta do JAR)
            File externalFile = new File(fileName);
            if (externalFile.exists()) {
                logger.info("Carregando configurações externas de: {}", externalFile.getAbsolutePath());
                try (InputStream input = new FileInputStream(externalFile)) {
                    prop.load(input);
                }
            } else {
                // 2. Fallback: Tenta carregar de dentro do JAR (recurso interno)
                logger.info("Arquivo externo {} não encontrado. Usando configurações internas.", fileName);
                try (InputStream input = Main.class.getClassLoader().getResourceAsStream(fileName)) {
                    if (input == null) {
                        logger.error("Arquivo {} não encontrado nem externamente nem internamente.", fileName);
                        return false;
                    }
                    prop.load(input);
                }
            }

            // Mapeamento das propriedades
            String inputPaths = prop.getProperty("pastas.entrada", "");
            if (!inputPaths.isEmpty()) {
                pastasEntrada = Arrays.asList(inputPaths.split(","));
            } else {
                pastasEntrada = Collections.emptyList();
            }

            pastaProcessados = prop.getProperty("pasta.processados", "");
            pastaTemp = prop.getProperty("pasta.temp", "");
            nomeImpressora = prop.getProperty("impressora.nome", "");

            return true;
        } catch (Exception e) {
            logger.error("Erro ao carregar propriedades: {}", e.getMessage());
            return false;
        }
    }

    private static void criarPastas() {
        if (pastasEntrada != null) {
            for (String path : pastasEntrada) {
                new File(path).mkdirs();
            }
        }
        if (pastaProcessados != null && !pastaProcessados.isEmpty()) new File(pastaProcessados).mkdirs();
        if (pastaTemp != null && !pastaTemp.isEmpty()) new File(pastaTemp).mkdirs();
        logger.info("Pastas verificadas/criadas.");
    }
}
