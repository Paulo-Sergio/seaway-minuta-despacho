package br.com.minuta.despacho.model;

public class AzulDespachoData {

    // Identificação da companhia
    public String companhia; // GOL ou AZUL

    public String servicoGolLog;
    public String formaPagamento;
    public String tipoEntrega;
    public String aeroportoRetirada;

    // Remetente
    public String remetenteNome;
    public String remetenteCnpj;
    public String remetenteInscEst;
    public String remetenteEnd;
    public String remetenteNum;
    public String remetenteComplemento;
    public String remetenteBairro;
    public String remetenteCidade;
    public String remetenteEstado;
    public String remetenteCep;
    public String remetenteFone;
    public String remetenteFalarCom;
    public String remetenteEmail;

    // Destinatário
    public String destinatarioNome;
    public String destinatarioCnpj;
    public String destinatarioInscEst;
    public String destinatarioEnd;
    public String destinatarioNum;
    public String destinatarioComplemento;
    public String destinatarioBairro;
    public String destinatarioCidade;
    public String destinatarioEstado;
    public String destinatarioCep;
    public String destinatarioFone;
    public String destinatarioFalarCom;
    public String destinatarioEmail;

    // Expedidor
    public String expedidorNome;
    public String expedidorCnpj;
    public String expedidorInscEst;
    public String expedidorEnd;
    public String expedidorNum;
    public String expedidorComplemento;
    public String expedidorBairro;
    public String expedidorCidade;
    public String expedidorEstado;
    public String expedidorCep;
    public String expedidorFone;
    public String expedidorEmail;

    // Recebedor
    public String recebedorNome;
    public String recebedorCnpj;
    public String recebedorEnd;
    public String recebedorNum;
    public String recebedorComplemento;
    public String recebedorBairro;
    public String recebedorCidade;
    public String recebedorEstado;
    public String recebedorCep;
    public String recebedorFone;
    public String recebedorEmail;

    // Tomador
    public String tomadorNome;
    public String tomadorCnpj;
    public String tomadorEnd;
    public String tomadorNum;
    public String tomadorComplemento;
    public String tomadorBairro;
    public String tomadorCidade;
    public String tomadorEstado;
    public String tomadorCep;
    public String tomadorFone;
    public String tomadorEmail;

    // Carga
    public String qtdVolumes;
    public String peso;
    public String dimensoes;
    public String tipoEmbalagem;
    public String descricaoConteudo;
    public String valorDeclarado;

    // Serviço / Pagamento / Retirada
    public String servico;
    public String pagamento;
    public String retirada;

    // Fiscais / Seguro
    public String nfQde;
    public String nfValor;
    public String apolice;
    public String seguradora;

    // Extras
    public String observacao;
    public String minutaNum;

    public static AzulDespachoData fromCsvLine(String[] campos) {
        AzulDespachoData d = new AzulDespachoData();
        d.companhia = "AZUL"; 

        // Por enquanto, o layout do CSV para Azul não foi definido.
        // Este metod deve ser implementado quando o layout da Azul for conhecido.
        
        return d;
    }

    private static String get(String[] arr, int idx) {
        if (arr == null || idx >= arr.length) return "";
        return arr[idx] == null ? "" : arr[idx].trim();
    }
}
