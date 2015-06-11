/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.relatorios;

import br.com.sescacre.sisrelat.dao.ChamadaDao;
import br.com.sescacre.sisrelat.dao.ConfiguracaoProgramaDao;
import br.com.sescacre.sisrelat.dao.ProgramaDao;
import br.com.sescacre.sisrelat.dao.UnidadeOperacionalDao;
import br.com.sescacre.sisrelat.entidades.ConfiguracaoPrograma;
import br.com.sescacre.sisrelat.entidades.Programa;
import br.com.sescacre.sisrelat.entidades.UnidadeOperacional;
import br.com.sescacre.sisrelat.util.Conexao;
import br.com.sescacre.sisrelat.util.DateConverter;
import br.com.sescacre.sisrelat.util.GeraRelatorioPDF;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRResultSetDataSource;

/**
 *
 * @author Rennan Francisco
 */
@ManagedBean
@ViewScoped
public class AtendimentosDoMesPorTurma implements Serializable {

    private Long idUop;
    private Long idPrograma;
    private Long idModalidade;
    private Long idAtividade;
    private Long idConf;
    private Long idTurma;
    private List<UnidadeOperacional> listaUops = new ArrayList<>();
    private List<Programa> listaProgramas = new ArrayList<>();
    private List<Programa> listaModalidades = new ArrayList<>();
    private List<Programa> listaAtividades = new ArrayList<>();
    private List<ConfiguracaoPrograma> listaConfiguracoes = new ArrayList<>();
    private Date dataChamada;
    private Conexao con = new Conexao();

    @PostConstruct
    public void init() {
        setListaUops(new UnidadeOperacionalDao().ListaTodos());
    }

    public Long getIdUop() {
        return idUop;
    }

    public void setIdUop(Long idUop) {
        this.idUop = idUop;
    }

    public Long getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(Long idPrograma) {
        this.idPrograma = idPrograma;
    }

    public Long getIdModalidade() {
        return idModalidade;
    }

    public void setIdModalidade(Long idModalidade) {
        this.idModalidade = idModalidade;
    }

    public Long getIdAtividade() {
        return idAtividade;
    }

    public void setIdAtividade(Long idAtividade) {
        this.idAtividade = idAtividade;
    }

    public Long getIdConf() {
        return idConf;
    }

    public void setIdConf(Long idConf) {
        this.idConf = idConf;
    }

    public List<UnidadeOperacional> getListaUops() {
        return listaUops;
    }

    public void setListaUops(List<UnidadeOperacional> listaUops) {
        this.listaUops = listaUops;
    }

    public List<Programa> getListaProgramas() {
        if (idUop != null) {
            return listaProgramas = new ProgramaDao().listaPrograma(idUop);
        }
        return listaProgramas;
    }

    public void setListaProgramas(List<Programa> listaProgramas) {
        this.listaProgramas = listaProgramas;
    }

    public List<Programa> getListaModalidades() {
        if (idPrograma != null) {
            return listaModalidades = new ProgramaDao().listaProgramaFilho(idPrograma);
        }
        return listaModalidades;
    }

    public void setListaModalidades(List<Programa> listaModalidades) {
        this.listaModalidades = listaModalidades;
    }

    public List<Programa> getListaAtividades() {
        if (idModalidade != null) {
            return listaAtividades = new ProgramaDao().listaProgramaFilho(idModalidade);
        }
        return listaAtividades;
    }

    public void setListaAtividades(List<Programa> listaAtividades) {
        this.listaAtividades = listaAtividades;
    }

    public List<ConfiguracaoPrograma> getListaConfiguracoes() {
        if (idAtividade != null) {
            return listaConfiguracoes = new ConfiguracaoProgramaDao().listaConfiguracaoPrograma(idAtividade);
        }
        return listaConfiguracoes;
    }

    public void setListaConfiguracoes(List<ConfiguracaoPrograma> listaConfiguracoes) {
        this.listaConfiguracoes = listaConfiguracoes;
    }

    public Date getDataChamada() {
        return dataChamada;
    }

    public void setDataChamada(Date dataChamada) {
        this.dataChamada = dataChamada;
    }

    public void gerarRelatorioWeb() {
        FacesContext msg = FacesContext.getCurrentInstance();
        //Pega caminho real do .jasper
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        ServletContext context = (ServletContext) externalContext.getContext();
        String arquivo = context.getRealPath("WEB-INF/relatorios/atendimentosDoMesPorTurma.jasper");
        Programa programa = new ProgramaDao().pegaPorId(idAtividade);
        Calendar c = Calendar.getInstance();
        c.setTime(dataChamada);
        int mes = c.get(Calendar.MONTH) + 1;
        int ano = c.get(Calendar.YEAR);
        YearMonth anoMes = YearMonth.of(ano, mes);
        Date inicio = DateConverter.convertLocalDateToDate(anoMes.atDay(1));
        Date fim = DateConverter.convertLocalDateToDate(anoMes.atDay(anoMes.lengthOfMonth()));
        //passa uma ResultSet por o relatório ter sido gerado .jasper usando JDBC datasource
        Connection conn = con.abreConexao();
        ResultSet rs = new ChamadaDao().contaAtendimentoMes(conn, idAtividade, idConf, inicio, fim);
        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
        //está passando null para a map pois o relatório não precisa de parametros
        Map<String, Object> par = new HashMap<>();
        par.put("logo", context.getRealPath("WEB-INF/relatorios/topo.jpg"));
        par.put("atividade", programa.getNome());
        par.put("mes", anoMes.format(DateTimeFormatter.ofPattern("MM/yyyy")));
        GeraRelatorioPDF.gerarPDF(jrRS, par, arquivo);
        con.fechaConexao();
        limparCampos();
    }

    public String limparCampos() {
        idUop = null;
        idPrograma = null;
        idModalidade = null;
        idAtividade = null;
        idConf = null;
        dataChamada = null;
        idTurma = null;
        listaProgramas = new ArrayList<>();
        listaAtividades = new ArrayList<>();
        listaConfiguracoes = new ArrayList<>();
        listaModalidades = new ArrayList<>();
        init();
        return null;
    }
}
