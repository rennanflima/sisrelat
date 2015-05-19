/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.bean;

import br.com.sescacre.sisrelat.dao.ConfiguracaoProgramaDao;
import br.com.sescacre.sisrelat.dao.InscricaoDao;
import br.com.sescacre.sisrelat.dao.ProgramaCorrenteDao;
import br.com.sescacre.sisrelat.dao.ProgramaDao;
import br.com.sescacre.sisrelat.dao.UnidadeOperacionalDao;
import br.com.sescacre.sisrelat.entidades.Chamada;
import br.com.sescacre.sisrelat.entidades.ConfiguracaoPrograma;
import br.com.sescacre.sisrelat.entidades.Inscritos;
import br.com.sescacre.sisrelat.entidades.Programa;
import br.com.sescacre.sisrelat.entidades.ProgramaCorrente;
import br.com.sescacre.sisrelat.entidades.UnidadeOperacional;
import br.com.sescacre.sisrelat.relatorios.InscritosTurma;
import br.com.sescacre.sisrelat.util.DateConverter;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Rennan Francisco
 */
@ManagedBean
@ViewScoped
public class SelecaoBean implements Serializable {

    private Long idUop;
    private Long idPrograma;
    private Long idModalidade;
    private Long idAtividade;
    private Long idConf;
    private Long idTurma;
    private List<UnidadeOperacional> listaUops = new ArrayList<UnidadeOperacional>();
    private List<Programa> listaProgramas = new ArrayList<Programa>();
    private List<Programa> listaModalidades = new ArrayList<Programa>();
    private List<Programa> listaAtividades = new ArrayList<Programa>();
    private List<ConfiguracaoPrograma> listaConfiguracoes = new ArrayList<ConfiguracaoPrograma>();
    private List<ProgramaCorrente> listaTurmas = new ArrayList<ProgramaCorrente>();
    private Date dataChamada;

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

    public Long getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(Long idTurma) {
        this.idTurma = idTurma;
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

    public List<ProgramaCorrente> getListaTurmas() {
        if (idConf != null && idAtividade != null) {
            return listaTurmas = new ProgramaCorrenteDao().listaTurma(idAtividade, idConf);
        }
        return listaTurmas;
    }

    public void setListaTurmas(List<ProgramaCorrente> listaTurmas) {
        this.listaTurmas = listaTurmas;
    }

    public Date getDataChamada() {
        return dataChamada;
    }

    public void setDataChamada(Date dataChamada) {
        this.dataChamada = dataChamada;
    }

    public void geraRelatório() {
        InscritosTurma it = new InscritosTurma();
        it.gerarRelatorioWeb(idAtividade, idConf, idTurma);
    }

    public String limparCampos() {
        idUop = null;
        idPrograma = null;
        idModalidade = null;
        idAtividade = null;
        idConf = null;
        dataChamada = null;
        idTurma = null;
        listaProgramas = new ArrayList<Programa>();
        listaAtividades = new ArrayList<Programa>();
        listaConfiguracoes = new ArrayList<ConfiguracaoPrograma>();
        listaModalidades = new ArrayList<Programa>();
        listaTurmas = new ArrayList<ProgramaCorrente>();
        init();
        return null;
    }

    public String realizaChamada() {
        List<Chamada> chamada = new ArrayList<Chamada>();
        FacesContext msg = FacesContext.getCurrentInstance();
        List<Inscritos> inscritos = new InscricaoDao().inscritosTurma(idAtividade, idConf, idTurma);
        ProgramaCorrente progocor = new ProgramaCorrenteDao().pegaPorId(idAtividade, idConf, idTurma);
        Calendar c = Calendar.getInstance();
        c.setTime(dataChamada);
        int mes = c.get(Calendar.MONTH) + 1;
        int ano = c.get(Calendar.YEAR);
        LocalTime horaInicio = DateConverter.convertDateToLocalTime(progocor.getHoraInicio());
        LocalTime horaFim = DateConverter.convertDateToLocalTime(progocor.getHoraFim());
        Duration duration = Duration.between(horaInicio, horaFim);
        System.out.println("Turma: " + progocor.getDescricao());
        System.out.println("Horário: " + progocor.getHoraInicio() + " às " + progocor.getHoraFim());
        System.out.println("Duração: " + duration.toHours() + " - Minutos: " + duration.toMinutes());
        System.out.println();
        System.out.println("Inicio da chamada: "+ new Date());
        if (duration.toHours() <= 1) {
            RealizarChamada.turmasHorarioFixo(ano, mes, progocor, inscritos, chamada, msg);
        } else {
            /*msg.addMessage(null,
             new FacesMessage(FacesMessage.SEVERITY_INFO,
             "Turmas de horário livre!", null));*/
            RealizarChamada.turmaHorarioLivre(ano, mes, progocor, inscritos, chamada, msg);
        }
        limparCampos();
        return null;
    }

}
