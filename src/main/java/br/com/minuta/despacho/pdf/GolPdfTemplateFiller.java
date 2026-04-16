package br.com.minuta.despacho.pdf;

import br.com.minuta.despacho.model.DespachoData;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;
import java.io.InputStream;

public class GolPdfTemplateFiller {

    // Use o template com campos já criados
    private static final String TEMPLATE = "/templates/MINUTA_GOL_COM_CAMPOS.pdf";

    public File preencher(DespachoData d, String pastaTemp) throws Exception {
        String saida = pastaTemp + "/GOL_" + System.currentTimeMillis() + ".pdf";

        InputStream tpl  = getClass().getResourceAsStream(TEMPLATE);
        PdfReader reader = new PdfReader(tpl);
        PdfWriter writer = new PdfWriter(saida);
        PdfDocument pdf  = new PdfDocument(reader, writer);
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, false);

        // ── REMETENTE ──────────────────────────────────────────────────────
        set(form, "Remetente", d.remetenteNome);
        set(form, "CPF / CNPJ", d.remetenteCnpj);
        set(form, "Endereço", d.remetenteEnd);
        set(form, "Complemento", d.remetenteComplemento);
        set(form, "Bairro", d.remetenteBairro);
        set(form, "CEP", d.remetenteCep);
        set(form, "Cidade", d.remetenteCidade);
        set(form, "UF", d.remetenteEstado);
        set(form, "Fone", d.remetenteFone);
        set(form, "e-mail", d.remetenteEmail);

        // ── DESTINATÁRIO ───────────────────────────────────────────────────
        set(form, "Destinatário", d.destinatarioNome);
        set(form, "CPF / CNPJ_2", d.destinatarioCnpj);
        set(form, "Endereço_2", d.destinatarioEnd);
        set(form, "Complemento_2", d.destinatarioComplemento);
        set(form, "Bairro_2", d.destinatarioBairro);
        set(form, "CEP_2", d.destinatarioCep);
        set(form, "Cidade_2", d.destinatarioCidade);
        set(form, "UF_2", d.destinatarioEstado);
        set(form, "Fone_2", d.destinatarioFone);
        set(form, "e-mail_2", d.destinatarioEmail);

        // ── TOMADOR DO FRETE ───────────────────────────────────────────────
        set(form, "Tomador do Frete", d.tomadorNome);
        set(form, "CNPJ", d.tomadorCnpj);
        set(form, "Nº Conta Gollog", "");

        // ── VOLUMES / CARGA ────────────────────────────────────────────────
        set(form, "Nº de Volumes", d.qtdVolumes);
        set(form, "Peso Total", d.peso);
        set(form, "Tipo de Embalagem", d.tipoEmbalagem);
        set(form, "Produto Predominante", d.descricaoConteudo);
        set(form, "Medidas das Embalagens", d.dimensoes);

        // ── NOTAS FISCAIS ──────────────────────────────────────────────────
        set(form, "Notas Fiscais", d.nfQde);

        // ── SEGURO ─────────────────────────────────────────────────────────
        marcarCheckbox(form, d.seguradora, new String[]{
                "Próprio", "Seguro GOL", "Sem Seguro"
        }, new String[]{
                "Próprio", "Seguro GOL", "Sem Seguro"
        });

        set(form, "Nº de Apolice", d.apolice);
        set(form, "Seguradora", d.seguradora);
        set(form, "Valor da Mercadoria", d.nfValor);

        // ── FORMA DE PAGAMENTO ─────────────────────────────────────────────
        marcarCheckbox(form, d.pagamento, new String[]{
                "Pago na origem", "A pagar no destino"
        }, new String[]{
                "Pagamento Remetente", "Pagamento Destinatário"
        });

        // ── TIPO DE ENTREGA ────────────────────────────────────────────────
        marcarCheckbox(form, d.retirada, new String[]{
                "Aeroporto para Retirada"
        }, new String[]{
                "Aeroporto para Retirada"
        });

        // ── SERVIÇO ────────────────────────────────────────────────────────
        set(form, "Serviço GOLLOG", d.servico);

        // ── RESPONSÁVEL ────────────────────────────────────────────────────
        set(form, "Local/Data", "");
        set(form, "Nome/ Reponsável", d.remetenteNome);
        set(form, "e-mail_3", d.remetenteEmail);

        form.flattenFields();

        pdf.close();
        return new File(saida);
    }

    private void set(PdfAcroForm form, String campo, String valor) {
        try {
            PdfFormField field = form.getField(campo);
            if (field != null) {
                field.setValue(valor == null ? "" : valor);
            } else {
                System.out.println("[AVISO] Campo não encontrado: " + campo);
            }
        } catch (Exception e) {
            System.err.println("[ERRO] Campo '" + campo + "': " + e.getMessage());
        }
    }

    private void marcarCheckbox(PdfAcroForm form, String valorCsv,
                                String[] opcoes, String[] nomeCampos) {
        if (valorCsv == null || valorCsv.isBlank()) return;
        for (int i = 0; i < opcoes.length; i++) {
            if (valorCsv.toLowerCase().contains(opcoes[i].toLowerCase())) {
                try {
                    PdfFormField field = form.getField(nomeCampos[i]);
                    if (field != null) {
                        field.setValue("Yes");
                    }
                } catch (Exception e) {
                    System.err.println("[ERRO] Checkbox '" + nomeCampos[i]
                            + "': " + e.getMessage());
                }
                break;
            }
        }
    }
}