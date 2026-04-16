package br.com.minuta.despacho.pdf;

import br.com.minuta.despacho.model.GolDespachoData;

import java.io.File;

public class PdfDispatcher {

    private final GolPdfTemplateFiller  golFiller;
    private final String pastaTemp;

    public PdfDispatcher(String pastaTemp) {
        this.pastaTemp = pastaTemp;
        this.golFiller  = new GolPdfTemplateFiller();
    }

    public File gerar(GolDespachoData dados) throws Exception {
        return golFiller.preencher(dados, pastaTemp);
    }
}
