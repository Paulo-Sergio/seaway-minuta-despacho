package br.com.minuta.despacho.pdf;

import br.com.minuta.despacho.model.DespachoData;

import java.io.File;

public class PdfDispatcher {

    private final AzulPdfTemplateFiller azulFiller;
    private final GolPdfTemplateFiller  golFiller;
    private final String pastaTemp;

    public PdfDispatcher(String pastaTemp) {
        this.pastaTemp = pastaTemp;
        this.azulFiller = new AzulPdfTemplateFiller();
        this.golFiller  = new GolPdfTemplateFiller();
    }

    public File gerar(DespachoData dados) throws Exception {
        if ("AZUL".equalsIgnoreCase(dados.companhia)) {
            return azulFiller.preencher(dados, pastaTemp);
        } else if ("GOL".equalsIgnoreCase(dados.companhia)) {
            return golFiller.preencher(dados, pastaTemp);
        } else {
            throw new IllegalArgumentException(
                    "Companhia desconhecida: " + dados.companhia +
                            ". Use GOL ou AZUL na primeira coluna do CSV.");
        }
    }
}