/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.relatorios;

import br.com.sescacre.sisrelat.util.Conexao;
import br.com.sescacre.sisrelat.dao.PactoAcessoDao;
import br.com.sescacre.sisrelat.util.GeraRelatorioPDF;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRResultSetDataSource;

/**
 *
 * @author Rennan Francisco
 */
@ManagedBean
@RequestScoped
public class AcessoPorPeriodoETipo implements Serializable {

    private PactoAcessoDao pactoDao;
    private Conexao con = new Conexao();
    private Date dtInicio;
    private Date dtTermino;
    private String tipo;

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void gerarRelatorioWeb() {
        FacesContext msg = FacesContext.getCurrentInstance();
        //Pega caminho real do .jasper
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        ServletContext context = (ServletContext) externalContext.getContext();
        String arquivo = context.getRealPath("WEB-INF/relatorios/controleAcessoPorPeriodoETipo.jasper");
        pactoDao = new PactoAcessoDao();
        //passa uma ResultSet por o relatório ter sido gerado .jasper usando JDBC datasource
        Connection conn = con.abreConexao();
        ResultSet rs = pactoDao.acessoPorPeriodoETipo(conn, tipo, new java.sql.Date(dtInicio.getTime()), new java.sql.Date(dtTermino.getTime()));
        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
        //está passando null para a map pois o relatório não precisa de parametros
        Map<String, Object> par = new HashMap<>();
        par.put("logo", context.getRealPath("WEB-INF/relatorios/topo.jpg"));
        GeraRelatorioPDF.gerarPDF(jrRS, par, arquivo);
        con.fechaConexao();
    }
}
