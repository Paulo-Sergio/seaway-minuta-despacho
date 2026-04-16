package br.com.minuta.despacho.pdf;

import br.com.minuta.despacho.model.AzulDespachoData;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;
import java.io.InputStream;

public class AzulPdfTemplateFiller {

    private static final String TEMPLATE = "/templates/MINUTA_AZUL.pdf";

    public File preencher(AzulDespachoData d, String pastaTemp) throws Exception {
        String saida = pastaTemp + "/AZUL_" + System.currentTimeMillis() + ".pdf";

        InputStream tpl  = getClass().getResourceAsStream(TEMPLATE);
        PdfReader reader = new PdfReader(tpl);
        PdfWriter writer = new PdfWriter(saida);
        PdfDocument pdf  = new PdfDocument(reader, writer);
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, false);

        // ── REMETENTE ──────────────────────────────────────────────────────
        // O PDF tem dois blocos de Nome na linha do Remetente (Nome e Nome_2)
        // Nome   = Remetente lado esquerdo
        // Nome_2 = aparece duplicado no layout mas mapeia para o mesmo remetente
        set(form, "Nome",            d.remetenteNome);
        set(form, "CNPJCPF",         d.remetenteCnpj);
        set(form, "Insc Est",        d.remetenteInscEst);
        set(form, "End",             d.remetenteEnd);
        set(form, "N",               d.remetenteNum);
        set(form, "Complemento",     d.remetenteComplemento);
        set(form, "Bairro",          d.remetenteBairro);
        set(form, "Cidade",          d.remetenteCidade);
        set(form, "Estado",          d.remetenteEstado);
        set(form, "Texto1",          d.remetenteCep);   // CEP Remetente
        set(form, "Fone",            d.remetenteFone);
        set(form, "falar com",       d.remetenteFalarCom);
        set(form, "Texto3",          d.remetenteCep);   // CEP campo secundário
        set(form, "email",           d.remetenteEmail);

        // ── DESTINATÁRIO ───────────────────────────────────────────────────
        set(form, "Nome_2",          d.destinatarioNome);
        set(form, "CNPJCPF_2",       d.destinatarioCnpj);
        set(form, "Insc Est_2",      d.destinatarioInscEst);
        set(form, "End_2",           d.destinatarioEnd);
        set(form, "N_2",             d.destinatarioNum);
        set(form, "Complemento_2",   d.destinatarioComplemento);
        set(form, "Bairro_2",        d.destinatarioBairro);
        set(form, "Cidade_2",        d.destinatarioCidade);
        set(form, "Estado_2",        d.destinatarioEstado);
        set(form, "Texto9",          d.destinatarioCep);   // CEP Destinatário
        set(form, "Fone_2",          d.destinatarioFone);
        set(form, "falar com_2",     d.destinatarioFalarCom);
        set(form, "Texto5",          d.destinatarioCep);   // CEP campo secundário
        set(form, "email_2",         d.destinatarioEmail);

        // ── EXPEDIDOR ──────────────────────────────────────────────────────
        set(form, "Nome_3",          d.expedidorNome);
        set(form, "CNPJCPF_3",       d.expedidorCnpj);
        set(form, "Insc Est_3",      d.expedidorInscEst != null ? d.expedidorInscEst : "");
        set(form, "End_3",           d.expedidorEnd);
        set(form, "N_3",             d.expedidorNum);
        set(form, "Complemento_3",   d.expedidorComplemento);
        set(form, "Bairro_3",        d.expedidorBairro);
        set(form, "Cidade_3",        d.expedidorCidade);
        set(form, "Estado_3",        d.expedidorEstado);
        set(form, "Texto5",          d.expedidorCep);
        set(form, "Fone_3",          d.expedidorFone);
        set(form, "falar com_3",     "");
        set(form, "email_3",         d.expedidorEmail);

        // ── RECEBEDOR ──────────────────────────────────────────────────────
        set(form, "Nome_4",          d.recebedorNome);
        set(form, "CNPJCPF_4",       d.recebedorCnpj);
        set(form, "Insc Est_4",      "");
        set(form, "End_4",           d.recebedorEnd);
        set(form, "N_4",             d.recebedorNum);
        set(form, "Complemento_4",   d.recebedorComplemento);
        set(form, "Bairro_4",        d.recebedorBairro);
        set(form, "Cidade_4",        d.recebedorCidade);
        set(form, "Estado_4",        d.recebedorEstado);
        set(form, "Texto9",          d.recebedorCep);
        set(form, "Fone_4",          d.recebedorFone);
        set(form, "falar com_4",     "");
        set(form, "email_4",         d.recebedorEmail);

        // ── TOMADOR ────────────────────────────────────────────────────────
        set(form, "Nome_5",          d.tomadorNome);
        set(form, "CNPJCPF_5",       d.tomadorCnpj);
        set(form, "Insc Est_5",      "");
        set(form, "End_5",           d.tomadorEnd);
        set(form, "N_5",             d.tomadorNum);
        set(form, "Complemento_5",   d.tomadorComplemento);
        set(form, "Bairro_5",        d.tomadorBairro);
        set(form, "Cidade_5",        d.tomadorCidade);
        set(form, "Estado_5",        d.tomadorEstado);
        set(form, "Texto7",          d.tomadorCep);
        set(form, "Fone_5",          d.tomadorFone);
        set(form, "falar com_5",     "");
        set(form, "email_5",         d.tomadorEmail);

        // ── OBSERVAÇÃO ─────────────────────────────────────────────────────
        set(form, "OBSERVAÇÃO",      d.observacao);

        // ── VOLUMES ────────────────────────────────────────────────────────
        set(form, "Quantidade VolumesRow1", d.qtdVolumes);
        set(form, "PesoRow1",               d.peso);
        set(form, "Texto4",                 d.dimensoes);
        set(form, "Tipo EmbalagemRow1",     d.tipoEmbalagem);
        set(form, "Texto2",                 d.descricaoConteudo);

        // ── NOTAS FISCAIS / APÓLICE / SEGURADORA ───────────────────────────
        set(form, "Notas Fiscais Qde",      d.nfQde);
        set(form, "Valor",                  d.nfValor);
        set(form, "N Apólice",              d.apolice);
        set(form, "Seguradora",             d.seguradora);

        // ── VALOR DECLARADO ────────────────────────────────────────────────
        set(form, "Declaração Remessa Valor",
                d.valorDeclarado == null || d.valorDeclarado.isBlank()
                        ? "Sem valor declarado"
                        : "R$ " + d.valorDeclarado);

        // ── SERVIÇO — Caixas de Seleção (checkboxes) ───────────────────────
        // Caixa de Seleção1  = Azul Cargo 2 Horas
        // Caixa de Seleção2  = Azul Cargo Amanhã
        // Caixa de Seleção3  = Azul Cargo 10
        // Caixa de Seleção4  = Standard
        // Caixa de Seleção5  = Azul Cargo DOC
        // Caixa de Seleção6  = Azul Cargo Express
        // Caixa de Seleção7  = Perecível
        marcarCheckbox(form, d.servico, new String[]{
                "Azul Cargo 2 Horas", "Azul Cargo Amanhã", "Azul Cargo 10",
                "Standard", "Azul Cargo DOC", "Azul Cargo Express", "Perecível"
        }, new String[]{
                "Caixa de Seleção1", "Caixa de Seleção2", "Caixa de Seleção3",
                "Caixa de Seleção4", "Caixa de Seleção5", "Caixa de Seleção6",
                "Caixa de Seleção7"
        });

        // ── PAGAMENTO — Caixas de Seleção ──────────────────────────────────
        // Caixa de Seleção8  = Pago na origem
        // Caixa de Seleção9  = A pagar no destino
        // Caixa de Seleção10 = Debitar da C/C
        marcarCheckbox(form, d.pagamento, new String[]{
                "Pago na origem", "A pagar no destino", "Debitar da C/C"
        }, new String[]{
                "Caixa de Seleção8", "Caixa de Seleção9", "Caixa de Seleção10"
        });

        // ── RETIRADA — Caixas de Seleção ───────────────────────────────────
        // Caixa de Seleção11 = Retira no aeroporto
        // Caixa de Seleção12 = Entrega em domicílio
        // Caixa de Seleção13 = Retira na loja ou no CD
        marcarCheckbox(form, d.retirada, new String[]{
                "Retira no aeroporto", "Entrega em domicílio", "Retira na loja ou no CD"
        }, new String[]{
                "Caixa de Seleção11", "Caixa de Seleção12", "Caixa de Seleção13"
        });

        // Achata os campos — transforma em texto fixo, não editável
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
                        // Pega o valor "on" do checkbox (pode variar por PDF)
                        String[] states = field.getAppearanceStates();
                        String onValue = "Yes";
                        for (String s : states) {
                            if (!s.equals("Off")) { onValue = s; break; }
                        }
                        field.setValue(onValue);
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