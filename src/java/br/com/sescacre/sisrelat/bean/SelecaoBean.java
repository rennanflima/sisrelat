/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.bean;

import br.com.sescacre.sisrelat.dao.ChamadaDao;
import br.com.sescacre.sisrelat.dao.ConfiguracaoProgramaDao;
import br.com.sescacre.sisrelat.dao.HorariosDAO;
import br.com.sescacre.sisrelat.dao.InscricaoDao;
import br.com.sescacre.sisrelat.dao.ProgramaCorrenteDao;
import br.com.sescacre.sisrelat.dao.ProgramaDao;
import br.com.sescacre.sisrelat.dao.UnidadeOperacionalDao;
import br.com.sescacre.sisrelat.entidades.ConfiguracaoPrograma;
import br.com.sescacre.sisrelat.entidades.Horarios;
import br.com.sescacre.sisrelat.entidades.Inscritos;
import br.com.sescacre.sisrelat.entidades.Programa;
import br.com.sescacre.sisrelat.entidades.ProgramaCorrente;
import br.com.sescacre.sisrelat.entidades.UnidadeOperacional;
import br.com.sescacre.sisrelat.entidades.Usuarios;
import br.com.sescacre.sisrelat.util.DateConverter;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Rennan Francisco
 */
@ManagedBean
@SessionScoped
public class SelecaoBean implements Serializable {

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
    private List<ProgramaCorrente> listaTurmas = new ArrayList<>();
    private ProgramaCorrente progocor;
    private Date dataChamada;
    private Integer progress;
    private YearMonth anoMes;
    private Long atendimentos;
    private Long faltas;
    private Integer totalInscritos;

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

    public Integer getProgress() {
        if (progress == null) {
            progress = 0;
        } else {
            if (progress > 100) {
                progress = 100;
            }
        }
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public ProgramaCorrente getProgocor() {
        return progocor;
    }

    public void setProgocor(ProgramaCorrente progocor) {
        this.progocor = progocor;
    }

    public YearMonth getAnoMes() {
        return anoMes;
    }

    public void setAnoMes(YearMonth anoMes) {
        this.anoMes = anoMes;
    }

    public Long getAtendimentos() {
        return atendimentos;
    }

    public void setAtendimentos(Long atendimentos) {
        this.atendimentos = atendimentos;
    }

    public Long getFaltas() {
        return faltas;
    }

    public void setFaltas(Long faltas) {
        this.faltas = faltas;
    }

    public Integer getTotalInscritos() {
        return totalInscritos;
    }

    public void setTotalInscritos(Integer totalInscritos) {
        this.totalInscritos = totalInscritos;
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
        listaTurmas = new ArrayList<>();
        init();
        progress = null;
        return null;
    }

    public String realizaChamada() {
        FacesContext msg = FacesContext.getCurrentInstance();
        progocor = new ProgramaCorrenteDao().pegaPorId(idAtividade, idConf, idTurma);
        Map<DayOfWeek, Horarios> horarios = new HorariosDAO().pegaHorarioDaAtividade(progocor.getPrograma(), progocor.getConfiguracaoPrograma(), progocor.getSequenciaOcorrencia());
        Calendar c = Calendar.getInstance();
        c.setTime(dataChamada);
        int mes = c.get(Calendar.MONTH) + 1;
        int ano = c.get(Calendar.YEAR);
        anoMes = YearMonth.of(ano, mes);
        totalInscritos = (new InscricaoDao().inscritosTurma(progocor.getPrograma(), progocor.getConfiguracaoPrograma(), progocor.getSequenciaOcorrencia())).size();
        Usuarios user = new UsuarioBean().getUsuario();
        System.out.println("Turma: " + progocor.getDescricao());
        System.out.println();
        System.out.println("Início da chamada: " + new Date());
        System.out.println();
        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
            Horarios h = new Horarios();
            LocalDate data = anoMes.atDay(dia);
            if (dia == anoMes.lengthOfMonth()) {
                setProgress(99);
            } else {
                setProgress((dia * 100) / anoMes.lengthOfMonth());
            }
            if (data.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
                h = horarios.get(DayOfWeek.MONDAY);
            }
            if (data.getDayOfWeek().equals(DayOfWeek.TUESDAY)) {
                h = horarios.get(DayOfWeek.TUESDAY);
            }
            if (data.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)) {
                h = horarios.get(DayOfWeek.WEDNESDAY);
            }
            if (data.getDayOfWeek().equals(DayOfWeek.THURSDAY)) {
                h = horarios.get(DayOfWeek.THURSDAY);
            }
            if (data.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
                h = horarios.get(DayOfWeek.FRIDAY);
            }
            if (data.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                h = horarios.get(DayOfWeek.SATURDAY);
            }
            if (data.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                h = horarios.get(DayOfWeek.SUNDAY);
            }
            if (h != null) {
                LocalTime horaInicio = h.getHoraInicio().toLocalTime();
                LocalTime horaFim = h.getHoraTermino().toLocalTime();
                Duration duration = Duration.between(horaInicio, horaFim);
                //System.out.println("Horário da " + data.getDayOfWeek() + ": " + horaInicio + " às " + horaFim);
                //System.out.println("Duração: " + duration.toHours() + " - Minutos: " + duration.toMinutes());
                if (duration.toHours() <= 1) {
                    RealizarChamada.turmasHorarioFixo(progocor, h, data, user);
                } else {
                    RealizarChamada.turmaHorarioLivre(progocor, h, data, user);
                }
            }
        }
        setProgress(100);
        System.out.println("Termino da chamada: " + new Date());
        return "chamadaCompleta";
    }

    public String chamadaCompleta() {
        Date inicio = DateConverter.convertLocalDateToDate(anoMes.atDay(1));
        Date fim = DateConverter.convertLocalDateToDate(anoMes.atDay(anoMes.lengthOfMonth()));
        atendimentos = new ChamadaDao().contaPresencaDia(progocor.getPrograma(), progocor.getConfiguracaoPrograma(), progocor.getSequenciaOcorrencia(), 1, inicio, fim);
        faltas = new ChamadaDao().contaPresencaDia(progocor.getPrograma(), progocor.getConfiguracaoPrograma(), progocor.getSequenciaOcorrencia(), 0, inicio, fim);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "A chamada da turma '" + progocor.getDescricao() + "' do mês " + anoMes.format(formatter) + " foi realizada com sucesso.", null));
        limparCampos();
        return null;
    }

}
