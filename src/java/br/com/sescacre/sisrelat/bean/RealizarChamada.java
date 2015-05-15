/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.bean;

import br.com.sescacre.sisrelat.dao.ChamadaDao;
import br.com.sescacre.sisrelat.dao.PactoAcessoDao;
import br.com.sescacre.sisrelat.entidades.Chamada;
import br.com.sescacre.sisrelat.entidades.Inscritos;
import br.com.sescacre.sisrelat.entidades.PactoAcesso;
import br.com.sescacre.sisrelat.entidades.ProgramaCorrente;
import br.com.sescacre.sisrelat.util.DateConverter;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Rennan Francisco
 */
public class RealizarChamada {

    public static void turmasHorarioFixo(int ano, int mes, ProgramaCorrente progocor, List<Inscritos> inscritos, List<Chamada> chamada, FacesContext msg) {
        YearMonth anoMes = YearMonth.of(ano, mes);
        for (int dia = 1; dia < anoMes.lengthOfMonth(); dia++) {
            LocalDate data = anoMes.atDay(dia);
            if (!data.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !data.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                List<PactoAcesso> lista = new PactoAcessoDao().acessoDoDia(data, progocor.getHoraInicio(), progocor.getHoraFim());
                for (Inscritos insc : inscritos) {
                    Chamada presenca = new Chamada();
                    presenca.setSqmatric(insc.getSqMatric());
                    presenca.setCduop(insc.getCdUop());
                    presenca.setCdprograma(insc.getCdPrograma());
                    presenca.setCdconfig(insc.getCdConfig());
                    presenca.setSqocorrenc(insc.getSqOcorrenc());
                    presenca.setDtaula(DateConverter.convertLocalDateToDate(data));
                    presenca.setHriniaula(progocor.getHoraInicio());
                    presenca.setLgatu("jcavalcant");
                    presenca.setDtatu(new Date());
                    presenca.setHratu(new Date());
                    for (PactoAcesso acesso : lista) {
                        if (acesso.getMatFormat().equals(insc.getMatFormat())) {
                            presenca.setVbfalta(true);
                            break;
                        }
                    }
                    chamada.add(presenca);
                }
                if (chamada.size() <= inscritos.size()) {
                    for (Chamada ch : chamada) {
                        try {
                            new ChamadaDao().salvar(ch);
                        } catch (Exception ex) {
                            System.out.println("Erro ao salvar a chamada: " + ex.getMessage());
                        }
                    }
                } else {
                    System.out.println("Tamanho da lista de chamada é maior: " + chamada.size());
                }
                chamada = new ArrayList<Chamada>();
            }
        }
        msg.addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "A chamada da turma '" + progocor.getDescricao() + "' do mês " + mes + "/" + ano + " foi realizada com sucesso.", null));
    }

    public static void turmaHorarioLivre(int ano, int mes, ProgramaCorrente progocor, List<Inscritos> inscritos, List<Chamada> chamada, FacesContext msg) {
        YearMonth anoMes = YearMonth.of(ano, mes);
        LocalTime horaInicio = DateConverter.convertDateToLocalTime(progocor.getHoraInicio());
        LocalTime horaFim = DateConverter.convertDateToLocalTime(progocor.getHoraFim());
        System.out.println(">>>>>>>>>>>> Entrada <<<<<<<<<<<<<<<<<<");
        System.out.println();
        for (int dia = 1; dia < anoMes.lengthOfMonth(); dia++) {
            LocalDate data = anoMes.atDay(dia);
            if (!data.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !data.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                for (Inscritos insc : inscritos) {

                    List<PactoAcesso> lista = new PactoAcessoDao().acessoDoDiaAluno(data, progocor.getHoraInicio(), progocor.getHoraFim(), insc.getSqMatric());
                    LocalDateTime entrada = null;
                    LocalDateTime saida = null;
                    for (PactoAcesso pa : lista) {
                        System.out.println(pa.getMatFormat() + " - " + pa.getNmCliente() + " - " + pa.getDirecao() + " - " + pa.getDataHora());
                        if (pa.getDirecao().equals("Entrada")) {
                            entrada = DateConverter.convertDateToLocalDateTime(pa.getDataHora());
                        } else {
                            saida = DateConverter.convertDateToLocalDateTime(pa.getDataHora());
                        }
                    }
                    if (entrada != null || saida != null) { // lançamento de presença
                        if (entrada != null) { //tem a entrada
                            if (saida != null) { //tem entrada e saida
                                int count = 0;
                                LocalDateTime tmp = entrada;
                                while (tmp.getHour() <= saida.getHour()) {
                                    Chamada presenca = new Chamada();
                                    presenca.setSqmatric(insc.getSqMatric());
                                    presenca.setCduop(insc.getCdUop());
                                    presenca.setCdprograma(insc.getCdPrograma());
                                    presenca.setCdconfig(insc.getCdConfig());
                                    presenca.setSqocorrenc(insc.getSqOcorrenc());
                                    presenca.setDtaula(DateConverter.convertLocalDateToDate(data));
                                    presenca.setLgatu("jcavalcant");
                                    presenca.setDtatu(new Date());
                                    presenca.setHratu(new Date());
                                    if (!tmp.isAfter(saida)) {
                                        presenca.setHriniaula(DateConverter.convertLocalDateTimeToDate(tmp));
                                    } else {
                                        presenca.setHriniaula(DateConverter.convertLocalDateTimeToDate(saida));
                                    }
                                    presenca.setVbfalta(true);
                                    chamada.add(presenca);
                                    tmp = tmp.plusHours(1);
                                    count++;
                                }
                            } else { // tem só a entrada
                                Chamada presenca = new Chamada();
                                presenca.setSqmatric(insc.getSqMatric());
                                presenca.setCduop(insc.getCdUop());
                                presenca.setCdprograma(insc.getCdPrograma());
                                presenca.setCdconfig(insc.getCdConfig());
                                presenca.setSqocorrenc(insc.getSqOcorrenc());
                                presenca.setDtaula(DateConverter.convertLocalDateToDate(data));
                                presenca.setLgatu("jcavalcant");
                                presenca.setDtatu(new Date());
                                presenca.setHratu(new Date());
                                presenca.setHriniaula(DateConverter.convertLocalDateTimeToDate(entrada));
                                presenca.setVbfalta(true);
                                chamada.add(presenca);
                            }
                        } else { // nao tem a entrada
                            if (saida != null) { // só tem a saida
                                Chamada presenca = new Chamada();
                                presenca.setSqmatric(insc.getSqMatric());
                                presenca.setCduop(insc.getCdUop());
                                presenca.setCdprograma(insc.getCdPrograma());
                                presenca.setCdconfig(insc.getCdConfig());
                                presenca.setSqocorrenc(insc.getSqOcorrenc());
                                presenca.setDtaula(DateConverter.convertLocalDateToDate(data));
                                presenca.setLgatu("jcavalcant");
                                presenca.setDtatu(new Date());
                                presenca.setHratu(new Date());
                                presenca.setHriniaula(DateConverter.convertLocalDateTimeToDate(saida));
                                presenca.setVbfalta(true);
                                chamada.add(presenca);
                            }
                        }
                    } else{
                        // TODO: lançar faltas
                    }
                    break;
                }
            }
        }
        System.out.println();
        System.out.println(">>>>>>>>>>> Chamada <<<<<<<<<<<<<<");
        System.out.println();
        for (Chamada ch : chamada) {
            System.out.println(ch.getSqmatric() + " - " + ch.getDtaula() + " - " + ch.getHriniaula());
        }
    }
}
