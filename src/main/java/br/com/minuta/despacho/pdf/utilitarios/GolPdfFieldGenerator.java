package br.com.minuta.despacho.pdf.utilitarios;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Utilitário de uso único: lê o MINUTA_GOL.pdf (sem AcroForm) e gera
 * o MINUTA_GOL_COM_CAMPOS.pdf com todos os campos AcroForm posicionados
 * e com Default Appearance (DA) configurada — condição obrigatória para
 * que o iText consiga renderizar o texto ao chamar flattenFields().
 * <p>
 * Como executar:
 * mvn compile exec:java
 * -Dexec.mainClass="br.com.minuta.despacho.pdf.utilitarios.GolPdfFieldGenerator"
 * <p>
 * Após a execução, copie o arquivo gerado para:
 * src/main/resources/templates/MINUTA_GOL_COM_CAMPOS.pdf
 */
public class GolPdfFieldGenerator {

    private static final Logger logger = LoggerFactory.getLogger(GolPdfFieldGenerator.class);
    private static final String TEMPLATE = "/templates/MINUTA_GOL.pdf";
    /**
     * Tamanho de fonte padrão para todos os campos de texto
     */
    private static final float FONT_SIZE = 10f;

    public static void gerarTemplateComCampos(String caminhoSaida) throws Exception {
        InputStream tpl = GolPdfFieldGenerator.class.getResourceAsStream(TEMPLATE);
        if (tpl == null) {
            throw new IllegalStateException("Template não encontrado: " + TEMPLATE);
        }

        PdfReader reader = new PdfReader(tpl);
        PdfWriter writer = new PdfWriter(caminhoSaida);
        PdfDocument pdf = new PdfDocument(reader, writer);
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);

        // Fonte built-in — não precisa de arquivo externo.
        // O iText a incorpora automaticamente no dicionário de recursos do formulário.
        PdfFont helv = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // ── Topo ────────────────────────────────────
        texto(form, pdf, helv, "Serviço GOLLOG",            20, 561, 120, 15, false);
        texto(form, pdf, helv, "Forma de Pagamento",        150, 562, 115, 15, false);
        texto(form, pdf, helv, "Tipo de Entrega",           285, 562, 130, 15, false);
        texto(form, pdf, helv, "Aeroporto para Retirada",   545, 562, 43, 18, false);

        // ── REMETENTE (coluna esquerda) ────────────────────────────────────
        texto(form, pdf, helv, "Remetente", 100, 535, 200, 18, false);
        texto(form, pdf, helv, "CPF / CNPJ", 100, 508, 200, 18, false);
        texto(form, pdf, helv, "Endereço", 100, 472, 200, 28, true);
        texto(form, pdf, helv, "Complemento", 100, 445, 75, 18, false);
        texto(form, pdf, helv, "Fone", 225, 445, 75, 18, false);
        texto(form, pdf, helv, "Bairro", 55, 418, 150, 18, false);
        texto(form, pdf, helv, "UF", 255, 418, 45, 18, false);
        texto(form, pdf, helv, "CEP", 55, 392, 65, 18, false);
        texto(form, pdf, helv, "Cidade", 170, 392, 130, 18, false);
        texto(form, pdf, helv, "e-mail", 65, 370, 230, 16, false);

        // ── DESTINATÁRIO (coluna direita) ──────────────────────────────────
        texto(form, pdf, helv, "Destinatário",  387, 535, 200, 18, false);
        texto(form, pdf, helv, "CPF / CNPJ_2",  387, 508, 200, 18, false);
        texto(form, pdf, helv, "Endereço_2",    387, 472, 200, 28, true);
        texto(form, pdf, helv, "Complemento_2", 387, 445, 75, 18, false);
        texto(form, pdf, helv, "Fone_2",        512, 445, 75, 18, false);
        texto(form, pdf, helv, "Bairro_2",      342, 418, 150, 18, false);
        texto(form, pdf, helv, "UF_2",          540, 418, 45, 18, false);
        texto(form, pdf, helv, "CEP_2",         342, 392, 65, 18, false);
        texto(form, pdf, helv, "Cidade_2",      457, 392, 130, 18, false);
        texto(form, pdf, helv, "e-mail_2",      352, 370, 230, 18, false);

        // ── TOMADOR DO FRETE ───────────────────────────────────────────────
        texto(form, pdf, helv, "Tomador do Frete",  100, 345, 200, 18, false);
        texto(form, pdf, helv, "CNPJ",              345, 345, 120, 18, false);
        texto(form, pdf, helv, "Nº Conta Gollog",   545, 345, 45, 18, false);

        // ── VOLUMES / CARGA ────────────────────────────────────────────────
        texto(form, pdf, helv, "Nº de Volumes",             109, 305, 53, 22, false);
        texto(form, pdf, helv, "Peso Total",                217, 305, 61, 22, false);
        texto(form, pdf, helv, "Medidas das Embalagens",    408, 305, 185, 22, false);
        texto(form, pdf, helv, "Produto Predominante",      126, 268, 158, 22, false);
        texto(form, pdf, helv, "Artigo Perigoso UN",        293, 265, 135, 15, false);
        texto(form, pdf, helv, "Tipo de Embalagem",         438, 265, 135, 15, false);

        // ── NOTAS FISCAIS ──────────────────────────────────────────────────
        texto(form, pdf, helv, "Notas Fiscais", 126, 238, 460, 18, false);

        // ── TIPO DE SEGURO (checkboxes) ────────────────────────────────────
        // Assumindo as posições originais baseadas no modelo (X: Próprio=60, Seguro GOL=140, Sem Seguro=240)
        checkbox(form, pdf, "Próprio",      101, 216, 11, 11);
        checkbox(form, pdf, "Seguro GOL",   101, 188, 11, 11);
        checkbox(form, pdf, "Sem Seguro",   101, 165, 11, 11);

        // ── DADOS DE SEGURO ────────────────────────────────────────────────
        texto(form, pdf, helv, "Nº de Apolice",         296, 212, 87, 18, false);
        texto(form, pdf, helv, "Seguradora",            448, 212, 142, 18, false);
        texto(form, pdf, helv, "Valor da Mercadoria",   296, 185, 87, 18, false);

        // ── AUTORIZACAO ────────────────────────────────────────────────────
        checkbox(form, pdf, "Autorização",      92, 148, 11, 11);

        // ── RESPONSÁVEL ────────────────────────────────────────────────────
        texto(form, pdf, helv, "Local/Data",        127, 122, 210, 18, false);
        texto(form, pdf, helv, "Nome/ Reponsável",  451, 122, 135, 18, false);

        pdf.close();
        logger.info("✓ Template GOL com campos gerado em: {}", caminhoSaida);
        logger.info("  → Copie o arquivo para: src/main/resources/templates/MINUTA_GOL_COM_CAMPOS.pdf");
    }

    /**
     * Cria um campo de texto com Default Appearance definida.
     * Sem a DA, o iText não sabe qual fonte usar ao achatar o campo
     * e o texto fica invisível no PDF final.
     */
    private static void texto(PdfAcroForm form, PdfDocument pdf, PdfFont font,
                              String nome, float x, float y, float w, float h, boolean multiline) {
        PdfTextFormField campo = PdfFormField.createText(
                pdf, new Rectangle(x, y, w, h), nome, "");
        campo.setFont(font).setFontSize(FONT_SIZE);
        if (multiline) {
            campo.setMultiline(true);
        }
        form.addField(campo);
    }

    /**
     * Cria um campo checkbox com valor On = "Yes".
     */
    private static void checkbox(PdfAcroForm form, PdfDocument pdf,
                                 String nome, float x, float y, float w, float h) {
        PdfButtonFormField cb = PdfFormField.createCheckBox(
                pdf, new Rectangle(x, y, w, h), nome, "Yes");
        form.addField(cb);
    }

    public static void main(String[] args) throws Exception {
        // Gera o template na raiz do projeto; copie manualmente para resources.
        String saida = "MINUTA_GOL_COM_CAMPOS.pdf";
        gerarTemplateComCampos(saida);
    }
}
