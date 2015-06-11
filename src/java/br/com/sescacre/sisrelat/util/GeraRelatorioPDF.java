/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author Rennan Francisco
 */
public class GeraRelatorioPDF {

    public static void gerarPDF(JRResultSetDataSource jrRS, Map<String, Object> parametros, String arquivo) {
        ServletOutputStream servletOutputStream = null;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        try {
            servletOutputStream = response.getOutputStream();
            //gera o relatório em pdf criando um relatório pdf temporário
            JasperRunManager.runReportToPdfStream(new FileInputStream(new File(arquivo)), response.getOutputStream(), parametros, jrRS);
            //diz ao navegador o tipo de documento da resposta, nesse caso pdf
            response.setContentType("application/pdf");
            //envia para o navegador o relatório
            servletOutputStream.flush();
            servletOutputStream.close();

            context.renderResponse();
            context.responseComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
