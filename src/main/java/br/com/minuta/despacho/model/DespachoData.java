package br.com.minuta.despacho.model;

public class DespachoData {

    // Identificação da companhia
    public String companhia; // GOL ou AZUL

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

    public static DespachoData fromCsvLine(String[] campos) {
        DespachoData d = new DespachoData();
        d.companhia = get(campos, 0);
        d.remetenteNome = get(campos, 1);
        d.remetenteCnpj = get(campos, 2);
        d.remetenteInscEst = get(campos, 3);
        d.remetenteEnd = get(campos, 4);
        d.remetenteNum = get(campos, 5);
        d.remetenteComplemento = get(campos, 6);
        d.remetenteBairro = get(campos, 7);
        d.remetenteCidade = get(campos, 8);
        d.remetenteEstado = get(campos, 9);
        d.remetenteCep = get(campos, 10);
        d.remetenteFone = get(campos, 11);
        d.remetenteFalarCom = get(campos, 12);
        d.remetenteEmail = get(campos, 13);
        d.destinatarioNome = get(campos, 14);
        d.destinatarioCnpj = get(campos, 15);
        d.destinatarioInscEst = get(campos, 16);
        d.destinatarioEnd = get(campos, 17);
        d.destinatarioNum = get(campos, 18);
        d.destinatarioComplemento = get(campos, 19);
        d.destinatarioBairro = get(campos, 20);
        d.destinatarioCidade = get(campos, 21);
        d.destinatarioEstado = get(campos, 22);
        d.destinatarioCep = get(campos, 23);
        d.destinatarioFone = get(campos, 24);
        d.destinatarioFalarCom = get(campos, 25);
        d.destinatarioEmail = get(campos, 26);
        d.expedidorNome = get(campos, 27);
        d.expedidorCnpj = get(campos, 28);
        d.expedidorEnd = get(campos, 29);
        d.expedidorNum = get(campos, 30);
        d.expedidorComplemento = get(campos, 31);
        d.expedidorBairro = get(campos, 32);
        d.expedidorCidade = get(campos, 33);
        d.expedidorEstado = get(campos, 34);
        d.expedidorCep = get(campos, 35);
        d.expedidorFone = get(campos, 36);
        d.expedidorEmail = get(campos, 37);
        d.recebedorNome = get(campos, 38);
        d.recebedorCnpj = get(campos, 39);
        d.recebedorEnd = get(campos, 40);
        d.recebedorNum = get(campos, 41);
        d.recebedorComplemento = get(campos, 42);
        d.recebedorBairro = get(campos, 43);
        d.recebedorCidade = get(campos, 44);
        d.recebedorEstado = get(campos, 45);
        d.recebedorCep = get(campos, 46);
        d.recebedorFone = get(campos, 47);
        d.recebedorEmail = get(campos, 48);
        d.tomadorNome = get(campos, 49);
        d.tomadorCnpj = get(campos, 50);
        d.tomadorEnd = get(campos, 51);
        d.tomadorNum = get(campos, 52);
        d.tomadorComplemento = get(campos, 53);
        d.tomadorBairro = get(campos, 54);
        d.tomadorCidade = get(campos, 55);
        d.tomadorEstado = get(campos, 56);
        d.tomadorCep = get(campos, 57);
        d.tomadorFone = get(campos, 58);
        d.tomadorEmail = get(campos, 59);
        d.qtdVolumes = get(campos, 60);
        d.peso = get(campos, 61);
        d.dimensoes = get(campos, 62);
        d.tipoEmbalagem = get(campos, 63);
        d.descricaoConteudo = get(campos, 64);
        d.valorDeclarado = get(campos, 65);
        d.servico = get(campos, 66);
        d.pagamento = get(campos, 67);
        d.retirada = get(campos, 68);
        d.nfQde = get(campos, 69);
        d.nfValor = get(campos, 70);
        d.apolice = get(campos, 71);
        d.seguradora = get(campos, 72);
        d.observacao = get(campos, 73);
        d.minutaNum = get(campos, 74);
        return d;
    }

    private static String get(String[] arr, int idx) {
        if (arr == null || idx >= arr.length) return "";
        return arr[idx] == null ? "" : arr[idx].trim();
    }
}