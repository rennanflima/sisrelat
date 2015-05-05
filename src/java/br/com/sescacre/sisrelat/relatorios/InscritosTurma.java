/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.relatorios;

import br.com.sescacre.sisrelat.util.Conexao;
import br.com.sescacre.sisrelat.dao.InscricaoDao;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author Rennan Francisco
 */
@ManagedBean
@RequestScoped
public class InscritosTurma {

    private InscricaoDao id;
    private Conexao con = new Conexao();

    public void gerarRelatorioWeb(Long cdprograma, Long cdconfig, Long sqocorrenc) {
        FacesContext msg = FacesContext.getCurrentInstance();
        //Pega caminho real do .jasper
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        ServletContext context = (ServletContext) externalContext.getContext();
        String arquivo = context.getRealPath("WEB-INF/relatorios/inscritosTurma.jasper");
        id = new InscricaoDao();
        //passa uma ResultSet por o relatório ter sido gerado .jasper usando JDBC datasource
        Connection conn = con.abreConexao();
        ResultSet rs = id.inscritosTurmaResultSet(conn, cdprograma, cdconfig, sqocorrenc);
        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
        //está passando null para a map pois o relatório não precisa de parametros
        Map<String, Object> par = new HashMap<String, Object>();
        par.put("logo", context.getRealPath("WEB-INF/relatorios/topo.jpg"));
        gerarPDF(jrRS, par, arquivo);
        con.fechaConexao();
    }

    private void gerarPDF(JRDataSource jrRS, Map<String, Object> parametros, String arquivo) {
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
