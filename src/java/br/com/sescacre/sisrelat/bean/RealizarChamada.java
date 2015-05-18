/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.bean;

import br.com.sescacre.sisrelat.dao.ChamadaDao;
import br.com.sescacre.sisrelat.dao.PactoAcessoDao;
import br.com.sescacre.sisrelat.entidades.Acesso;
import br.com.sescacre.sisrelat.entidades.Chamada;
import br.com.sescacre.sisrelat.entidades.Inscritos;
import br.com.sescacre.sisrelat.entidades.PactoAcesso;
import br.com.sescacre.sisrelat.entidades.ProgramaCorrente;
import br.com.sescacre.sisrelat.util.DateConverter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
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
                    presenca.setHriniaula(progocor.getHoraInicio());
                    presenca.setDtaula(DateConverter.convertLocalDateToDate(data));
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
        System.out.println("Termino da chamada: " + new Date());
        msg.addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "A chamada da turma de horário fixo '" + progocor.getDescricao() + "' do mês " + mes + "/" + ano + " foi realizada com sucesso.", null));
    }

    public static void turmaHorarioLivre(int ano, int mes, ProgramaCorrente progocor, List<Inscritos> inscritos, List<Chamada> chamada, FacesContext msg) {
        YearMonth anoMes = YearMonth.of(ano, mes);
        LocalDateTime horaInicio = DateConverter.convertDateToLocalDateTime(progocor.getHoraInicio());
        LocalDateTime horaFim = DateConverter.convertDateToLocalDateTime(progocor.getHoraFim());
        System.out.println(">>>>>>>>>>>> Entrada <<<<<<<<<<<<<<<<<<");
        System.out.println();
        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) { //percorre todos os dias do mes
            LocalDate data = anoMes.atDay(dia);
            if (!data.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !data.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {// verifica se é dia util
                for (Inscritos insc : inscritos) { // percorre a lista de inscritos da turma
                    //pesquisa o acesso de um aluno na catraca
                    List<PactoAcesso> lista = new PactoAcessoDao().acessoDoDiaAluno(data, progocor.getHoraInicio(), progocor.getHoraFim(), insc.getSqMatric());
                    LocalDateTime entrada = null;
                    LocalDateTime saida = null;
                    //List<Acesso> listaAcessoAluno = new ArrayList<Acesso>();
                    //situaçao pensada para apenas uma entrada no dia
                    int contaEntradaDia = 0;
                    // verifica se a lista de acesso está vazia 
                    if (!lista.isEmpty()) {
                        // percorre a lista de acesso na catraca de um aluno, sendo entrada e saida dois objetos
                        for (PactoAcesso pa : lista) {
                            System.out.println(pa.getMatFormat() + " - " + pa.getNmCliente() + " - " + pa.getDirecao() + " - " + pa.getDataHora());
                            if (pa.getDirecao().equals("Entrada")) {// verifica a direção de entrada do aluno
                                entrada = DateConverter.convertDateToLocalDateTime(pa.getDataHora());
                            } else {
                                saida = DateConverter.convertDateToLocalDateTime(pa.getDataHora());
                            }
                            /*
                             // percorre e monta a lista com a entrada e saida sendo um unico objeto
                             for (Acesso acesso : listaAcessoAluno) {
                             LocalDateTime horaCatraca = DateConverter.convertDateToLocalDateTime(pa.getDataHora());
                             // verifica a direção de entrada do aluno, entrada
                             if (pa.getDirecao().equals("Entrada")) {
                             LocalDateTime entrada = DateConverter.convertDateToLocalDateTime(acesso.getEntrada().getDataHora());
                             // se tiver outro registro de entrada com a mesma hora já salva desconsidera desconsidera
                             if (entrada.getHour() == horaCatraca.getHour()) {
                             break;
                             } else {
                             //se tiver outro registro de entrada que for uma hora ou mais do que a salva cria um novo acesso 
                             if (horaCatraca.getHour() >= entrada.plusHours(1).getHour()) {
                             Acesso a = new Acesso();
                             a.setEntrada(pa);
                             listaAcessoAluno.add(a);
                             }
                             }
                             contaEntradaDia++;
                             // verifica a direção de entrada do aluno, saida
                             } else {
                             LocalDateTime saida = DateConverter.convertDateToLocalDateTime(acesso.getSaida().getDataHora());
                             // verifica se nesse objeto já tem entrada
                             if (acesso.getEntrada() != null) {
                             // verifica se nesse objeto já tem saida
                             if (acesso.getSaida() == null) {
                             LocalDateTime entrada = DateConverter.convertDateToLocalDateTime(acesso.getEntrada().getDataHora());
                             // se a hora da saída for maior ou igual a entrada salva a saida
                             if (horaCatraca.getHour() >= entrada.getHour()) {
                             acesso.setSaida(pa);
                             }
                             } else {
                             break;
                             }
                             } else {
                             //se o objeto nao tiver entrada, cria o objeto somento com a saida
                             Acesso a = new Acesso();
                             a.setSaida(pa);
                             listaAcessoAluno.add(a);
                             }
                             }
                             }*/
                        }
                        // lançamento de presença
                        if (entrada != null || saida != null) {
                            if (entrada != null) { //tem a entrada
                                if (saida != null) { //tem entrada e saida
                                    LocalDateTime temp = horaInicio;
                                    while (temp.getHour() <= horaFim.getHour()) {
                                        if (temp.getHour() >= entrada.getHour() && temp.getHour() <= saida.getHour()) {//temp entre a hora de entrada e a hora de saida
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
                                            temp = temp.plusHours(1);
                                        } else { // temp fora do intevalo da entrada e saida
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
                                            presenca.setHriniaula(DateConverter.convertLocalDateTimeToDate(temp));
                                            chamada.add(presenca);
                                        }
                                        temp = temp.plusHours(1);
                                    }
                                } else { // tem só a entrada
                                    LocalDateTime temp = horaInicio;
                                    while (temp.getHour() <= horaFim.getHour()) {
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
                                        if (temp.getHour() == entrada.getHour()) {
                                            presenca.setHriniaula(DateConverter.convertLocalDateTimeToDate(entrada));
                                            presenca.setVbfalta(true);
                                        } else {
                                            presenca.setHriniaula(DateConverter.convertLocalDateTimeToDate(temp));
                                        }
                                        temp = temp.plusHours(1);
                                        chamada.add(presenca);
                                    }
                                }
                            } else { // nao tem a entrada
                                if (saida != null) { // só tem a saida
                                    LocalDateTime temp = horaInicio;
                                    while (temp.getHour() <= horaFim.getHour()) {
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
                                        if (temp.getHour() == saida.getHour()) {
                                            presenca.setHriniaula(DateConverter.convertLocalDateTimeToDate(saida));
                                            presenca.setVbfalta(true);
                                        } else {
                                            presenca.setHriniaula(DateConverter.convertLocalDateTimeToDate(temp));
                                        }
                                        temp = temp.plusHours(1);
                                        chamada.add(presenca);
                                    }
                                }
                            }
                        }
                    } else { // lançamento da falta quando a lista de acesso está vazia
                        LocalDateTime temp = horaInicio;
                        while (temp.getHour() <= horaFim.getHour()) {
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
                            if (!temp.isAfter(horaFim)) {
                                presenca.setHriniaula(DateConverter.convertLocalDateTimeToDate(temp));
                            } else {
                                presenca.setHriniaula(DateConverter.convertLocalDateTimeToDate(horaFim));
                            }
                            temp = temp.plusHours(1);
                            chamada.add(presenca);
                        }
                    }
                    break;
                }
            }
        }
        System.out.println();
        System.out.println(">>>>>>>>>>> Chamada <<<<<<<<<<<<<<");
        System.out.println();
        for (Chamada ch : chamada) {
            if (ch.isVbfalta()) {
                System.out.println(ch.getSqmatric() + " - " + ch.getDtaula() + " - " + ch.getHriniaula() + " - P");
            } else {
                System.out.println(ch.getSqmatric() + " - " + ch.getDtaula() + " - " + ch.getHriniaula() + " - F");
            }
        }
        System.out.println("Termino da chamada: " + new Date());
    }
}
