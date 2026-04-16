package br.com.minuta.despacho.model;

public class GolDespachoData {

    public String companhia = "GOL";

    // Campos dinâmicos do CSV para a GOL
    public String tipoEntrega;
    public String destinatarioNome;
    public String destinatarioCnpj;
    public String destinatarioEnd;
    public String destinatarioComplemento;
    public String destinatarioFone;
    public String destinatarioBairro;
    public String destinatarioEstado;
    public String destinatarioCep;
    public String destinatarioCidade;
    public String destinatarioEmail;
    public String qtdVolumes;
    public String peso;
    public String nfQde;

    public static GolDespachoData fromCsvLine(String[] campos) {
        GolDespachoData d = new GolDespachoData();

        // Mapeamento baseado no layout da Minuta da Gol
        d.tipoEntrega = get(campos, 0); // Tipo de Entrega

        // Destinatário
        d.destinatarioNome = get(campos, 1);
        d.destinatarioCnpj = get(campos, 2);
        d.destinatarioEnd = get(campos, 3);
        d.destinatarioComplemento = get(campos, 4);
        d.destinatarioFone = get(campos, 5);
        d.destinatarioBairro = get(campos, 6);

        if (campos.length == 13) {
            d.destinatarioCidade = get(campos, 6);
            d.destinatarioEstado = get(campos, 7);
            d.destinatarioCep = get(campos, 8);
            d.destinatarioEmail = get(campos, 9);
            d.qtdVolumes = get(campos, 10);
            d.peso = get(campos, 11);
            d.nfQde = get(campos, 12);
        } else if (campos.length >= 14) {
            d.destinatarioEstado = get(campos, 7);
            d.destinatarioCep = get(campos, 8);
            d.destinatarioCidade = get(campos, 9);
            d.destinatarioEmail = get(campos, 10);
            d.qtdVolumes = get(campos, 11);
            d.peso = get(campos, 12);
            d.nfQde = get(campos, 13);
        }

        return d;
    }

    private static String get(String[] arr, int idx) {
        if (arr == null || idx >= arr.length) return "";
        return arr[idx] == null ? "" : arr[idx].trim();
    }
}
