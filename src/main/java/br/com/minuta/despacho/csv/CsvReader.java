package br.com.minuta.despacho.csv;

import br.com.minuta.despacho.model.GolDespachoData;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class CsvReader {

    private final String pastaEntrada;
    private final String pastaProcessados;

    public CsvReader(String pastaEntrada, String pastaProcessados) {
        this.pastaEntrada = pastaEntrada;
        this.pastaProcessados = pastaProcessados;
    }

    public List<File> buscarCsvs() {
        File pasta = new File(pastaEntrada);
        File[] arquivos = pasta.listFiles((dir, nome) -> nome.toLowerCase().endsWith(".csv"));
        return arquivos != null ? Arrays.asList(arquivos) : Collections.emptyList();
    }

    public List<GolDespachoData> lerCsv(File arquivo) throws IOException {
        List<GolDespachoData> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8))) {

            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                // Pula cabeçalho apenas se parecer um cabeçalho de verdade
                if (primeiraLinha) {
                    primeiraLinha = false;
                    String upper = linha.toUpperCase();
                    if (upper.contains("TIPO DE ENTREGA") || upper.contains("RAZÃO SOCIAL")) continue;
                }

                String[] campos = linha.split(";", -1);
                if (campos.length < 2) continue; // linha vazia ou inválida

                // Automação GOL
                lista.add(GolDespachoData.fromCsvLine(campos));
            }
        }
        return lista;
    }

    public void moverParaProcessados(File arquivo) throws IOException {
        Path destino = Paths.get(pastaProcessados, arquivo.getName());
        Files.move(arquivo.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
    }
}
