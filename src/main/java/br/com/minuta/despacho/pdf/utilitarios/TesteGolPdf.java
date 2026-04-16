package br.com.minuta.despacho.pdf.utilitarios;

import br.com.minuta.despacho.model.DespachoData;
import br.com.minuta.despacho.pdf.GolPdfTemplateFiller;

import java.io.File;

/**
 * Teste rápido: gera um PDF GOL preenchido e abre no explorador.
 * Executar com:
 *   mvn compile exec:java -Dexec.mainClass="br.com.minuta.despacho.pdf.utilitarios.TesteGolPdf"
 */
public class TesteGolPdf {

    public static void main(String[] args) throws Exception {
        DespachoData d = new DespachoData();

        // Remetente
        d.remetenteNome        = "Iguatemi Express Ltda";
        d.remetenteCnpj        = "11.222.333/0001-44";
        d.remetenteEnd         = "Rua Augusta";
        d.remetenteComplemento = "500";
        d.remetenteBairro      = "Consolação";
        d.remetenteCidade      = "São Paulo";
        d.remetenteEstado      = "SP";
        d.remetenteCep         = "01305-000";
        d.remetenteFone        = "(11) 2222-3333";
        d.remetenteEmail       = "pedro@iguatemi.com.br";

        // Destinatário
        d.destinatarioNome        = "Carlos Souza ME";
        d.destinatarioCnpj        = "55.666.777/0001-88";
        d.destinatarioEnd         = "Rua Oscar Freire";
        d.destinatarioComplemento = "300 - Casa";
        d.destinatarioBairro      = "Jardins";
        d.destinatarioCidade      = "São Paulo";
        d.destinatarioEstado      = "SP";
        d.destinatarioCep         = "01426-001";
        d.destinatarioFone        = "(11) 8888-7777";
        d.destinatarioEmail       = "carlos@souza.com.br";

        // Tomador
        d.tomadorNome  = "REMETENTE";
        d.tomadorCnpj  = "11.222.333/0001-44";

        // Carga
        d.qtdVolumes       = "3";
        d.peso             = "8,0 kg";
        d.tipoEmbalagem    = "Saco plástico";
        d.descricaoConteudo = "Roupas e acessórios diversos";
        d.dimensoes        = "40x30x25 cm";

        // Financeiro
        d.nfQde      = "2";
        d.nfValor    = "800,00";
        d.apolice    = "AP-2024-003";
        d.seguradora = "Allianz Seguros";

        // Operação
        d.pagamento = "Pago na origem";
        d.retirada  = "";
        d.servico   = "GOLLOG EXPRESS";

        String pastaTemp = "massa-de-testes/temp";
        new File(pastaTemp).mkdirs();

        GolPdfTemplateFiller filler = new GolPdfTemplateFiller();
        File pdf = filler.preencher(d, pastaTemp);

        System.out.println("✓ PDF gerado: " + pdf.getAbsolutePath());
        System.out.println("  Tamanho: " + pdf.length() + " bytes");

        // Abre automaticamente no visualizador padrão (Windows)
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            Runtime.getRuntime().exec("cmd /c start \"\" \"" + pdf.getAbsolutePath() + "\"");
            System.out.println("  → Abrindo no visualizador de PDF...");
        }
    }
}
