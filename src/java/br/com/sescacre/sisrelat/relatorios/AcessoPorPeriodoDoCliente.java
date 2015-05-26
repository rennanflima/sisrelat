/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.relatorios;

import br.com.sescacre.sisrelat.bean.ControleAcessoBean;
import br.com.sescacre.sisrelat.dao.ClientesDao;
import br.com.sescacre.sisrelat.util.Conexao;
import br.com.sescacre.sisrelat.dao.PactoAcessoDao;
import br.com.sescacre.sisrelat.entidades.Clientes;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class AcessoPorPeriodoDoCliente {

    private PactoAcessoDao pactoDao;
    private Conexao con = new Conexao();
    private Date dtInicio;
    private Date dtTermino;
    private String carteira;

    public Date getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(Date dtInicio) {
        this.dtInicio = dtInicio;
    }

    public Date getDtTermino() {
        return dtTermino;
    }

    public void setDtTermino(Date dtTermino) {
        this.dtTermino = dtTermino;
    }

    public String getCarteira() {
        return carteira;
    }

    public void setCarteira(String carteira) {
        this.carteira = carteira;
    }

    public void gerarRelatorioWeb() {
        FacesContext msg = FacesContext.getCurrentInstance();
        //Pega caminho real do .jasper
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        ServletContext context = (ServletContext) externalContext.getContext();
        String arquivo = context.getRealPath("WEB-INF/relatorios/controleAcessoPorPeriodoDoCliente.jasper");
        pactoDao = new PactoAcessoDao();
        //passa uma ResultSet por o relatório ter sido gerado .jasper usando JDBC datasource
        Connection conn = con.abreConexao();
        String sqmatric = "";
        if (carteira.length() == 12) {
            sqmatric = carteira.substring(5, 11);
        } else {
            sqmatric = carteira.substring(4, 10);
        }
        ResultSet rs = pactoDao.acessoPorPeriodoDoCliente(conn, new java.sql.Date(dtInicio.getTime()), new java.sql.Date(dtTermino.getTime()), sqmatric);
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
