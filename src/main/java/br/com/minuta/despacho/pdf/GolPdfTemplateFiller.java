package br.com.minuta.despacho.pdf;

import br.com.minuta.despacho.model.GolDespachoData;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;

public class GolPdfTemplateFiller {

    private static final Logger logger = LoggerFactory.getLogger(GolPdfTemplateFiller.class);
    // Use o template com campos já criados
    private static final String TEMPLATE = "/templates/MINUTA_GOL_COM_CAMPOS.pdf";

    public File preencher(GolDespachoData d, String pastaTemp) throws Exception {
        String saida = pastaTemp + "/GOL_" + System.currentTimeMillis() + ".pdf";

        InputStream tpl  = getClass().getResourceAsStream(TEMPLATE);
        PdfReader reader = new PdfReader(tpl);
        PdfWriter writer = new PdfWriter(saida);
        PdfDocument pdf  = new PdfDocument(reader, writer);
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, false);

        // Habilitar quebra de linha nos campos de endereço
        configurarMultiline(form, "Endereço");
        configurarMultiline(form, "Endereço_2");

        set(form, "Serviço GOLLOG", "ECONOMICO");
        set(form, "Forma de Pagamento", "PGTO À VISTA");
        set(form, "Tipo de Entrega", d.tipoEntrega);
        set(form, "Aeroporto para Retirada", "");

        // ── REMETENTE ──────────────────────────────────────────────────────
        set(form, "Remetente", "SEAWAY CONFECÇÕES LTDA.");
        set(form, "CPF / CNPJ", "09.026.659/0001-91");
        set(form, "Endereço", "RUA PROFESSOR AURELIO DE CASTRO CAVALCANTE, 211");
        set(form, "Complemento", "");
        set(form, "Fone", "81 3462-2222");
        set(form, "Bairro", "BOA VIAGEM");
        set(form, "UF", "PE");
        set(form, "CEP", "51130-280");
        set(form, "Cidade", "RECIFE");
        set(form, "e-mail", "comercial@seaway.com.br");

        // ── DESTINATÁRIO ───────────────────────────────────────────────────
        set(form, "Destinatário", d.destinatarioNome);
        set(form, "CPF / CNPJ_2", d.destinatarioCnpj);
        set(form, "Endereço_2", d.destinatarioEnd);
        set(form, "Complemento_2", d.destinatarioComplemento);
        set(form, "Fone_2", d.destinatarioFone);
        set(form, "Bairro_2", d.destinatarioBairro);
        set(form, "UF_2", d.destinatarioEstado);
        set(form, "CEP_2", d.destinatarioCep);
        set(form, "Cidade_2", d.destinatarioCidade);
        set(form, "e-mail_2", d.destinatarioEmail);

        // ── TOMADOR DO FRETE ───────────────────────────────────────────────
        set(form, "Tomador do Frete", "SEAWAY CONFECÇÕES LTDA");
        set(form, "CNPJ", "09.026.659/0001-91");
        set(form, "Nº Conta Gollog", "");

        // ── VOLUMES / CARGA ────────────────────────────────────────────────
        set(form, "Nº de Volumes", d.qtdVolumes);
        set(form, "Peso Total", d.peso);

        set(form, "Medidas das Embalagens", calcularMedidas(d));

        set(form, "Produto Predominante", "CONFECÇÕES");
        set(form, "Tipo de Embalagem", "CAIXA DE PAPELÃO");

        // ── NOTAS FISCAIS ──────────────────────────────────────────────────
        set(form, "Notas Fiscais", d.nfQde);

        // ── SEGURO ─────────────────────────────────────────────────────────
        set(form, "Próprio", "Off");
        set(form, "Seguro GOL", "Yes");
        set(form, "Sem Seguro", "Off");

        set(form, "Nº de Apolice", "");
        set(form, "Seguradora", "");
        set(form, "Valor da Mercadoria", "");

        // ── AUTORIZAÇÃO ────────────────────────────────────────────────────
        set(form, "Autorização", "Yes");

        // ── RESPONSÁVEL ────────────────────────────────────────────────────
        set(form, "Local/Data", "RECIFE, " + LocalDate.now());
        set(form, "Nome/ Reponsável", "ALINE MELO");

        form.flattenFields();

        pdf.close();
        return new File(saida);
    }

    private String calcularMedidas(GolDespachoData d) {
        if (d.isAtacado) {
            return "54C x 34L x 37A";
        }

        // Lógica para E-commerce baseada na quantidade de peças (volumes no CSV)
        int qtd = 1;
        try {
            qtd = Integer.parseInt(d.qtdVolumes);
        } catch (Exception e) {
            logger.warn("Quantidade de volumes inválida para e-commerce: {}. Usando 1.", d.qtdVolumes);
        }

        if (qtd >= 4) {
            return "18,5C x 21,5L x 14,5A";
        }

        switch (qtd) {
            case 1:  return "18,5C x 8L x 8A";
            case 2:  return "18,5C x 14,5L x 8A";
            case 3:  return "18,5C x 21,5L x 8A";
            default:
                return "18,5C x 21,5L x 14,5A";
        }
    }

    private void configurarMultiline(PdfAcroForm form, String nomeCampo) {
        PdfFormField field = form.getField(nomeCampo);
        if (field instanceof PdfTextFormField) {
            ((PdfTextFormField) field).setMultiline(true);
        }
    }

    private void set(PdfAcroForm form, String campo, String valor) {
        try {
            PdfFormField field = form.getField(campo);
            if (field != null) {
                field.setValue(valor == null ? "" : valor);
            } else {
                logger.warn("Campo não encontrado: {}", campo);
            }
        } catch (Exception e) {
            logger.error("Erro no campo '{}': {}", campo, e.getMessage());
        }
    }
}
