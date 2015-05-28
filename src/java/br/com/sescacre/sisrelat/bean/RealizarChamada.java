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
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Rennan Francisco
 */
public class RealizarChamada implements Serializable {

    public static void turmasHorarioFixo(int ano, int mes, ProgramaCorrente progocor, List<Inscritos> inscritos, FacesContext msg) {
        List<Chamada> chamada = new ArrayList<>();
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
                chamada = new ArrayList<>();
            }
        }
        System.out.println("Termino da chamada: " + new Date());
        msg.addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "A chamada da turma de horário fixo '" + progocor.getDescricao() + "' do mês " + mes + "/" + ano + " foi realizada com sucesso.", null));
    }

    public static void turmaHorarioLivre(int ano, int mes, ProgramaCorrente progocor, List<Inscritos> inscritos, FacesContext msg) {
        CopyOnWriteArrayList<Chamada> chamada = new CopyOnWriteArrayList<>();
        YearMonth anoMes = YearMonth.of(ano, mes);
        LocalDateTime horaInicio = DateConverter.convertDateToLocalDateTime(progocor.getHoraInicio());
        LocalDateTime horaFim = DateConverter.convertDateToLocalDateTime(progocor.getHoraFim());
        System.out.println();
        System.out.println("--------------------------------------------");
        System.out.println();
        System.out.println(">>>>>>>>>>>>>>>> Entrada <<<<<<<<<<<<<<<<<<<");
        System.out.println();
        System.out.println("--------------------------------------------");
        System.out.println();
        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) { //percorre todos os dias do mes
            LocalDate data = anoMes.atDay(dia);
            if (!data.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !data.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {// verifica se é dia util
                for (Inscritos insc : inscritos) { // percorre a lista de inscritos da turma
                    if (insc.getSqMatric() == 10828) {//20860(ok) e 10828 
                        //pesquisa o acesso de um aluno na catraca
                        List<PactoAcesso> lista = new PactoAcessoDao().acessoDoDiaAluno(data, progocor.getHoraInicio(), progocor.getHoraFim(), insc.getSqMatric());
                        CopyOnWriteArrayList<Acesso> listaAcessoAluno = new CopyOnWriteArrayList<>();
                        //situaçao pensada para apenas uma entrada no dia
                        int contaEntradaDia = 0;
                        int contaSaidaDia = 0;
                        // verifica se a lista de acesso está vazia 
                        if (!lista.isEmpty()) {
                            // percorre a lista de acesso na catraca de um aluno, sendo entrada e saida dois objetos
                            for (PactoAcesso pa : lista) {
                                //inicializa a listaAcessoAluno
                                if (listaAcessoAluno.isEmpty()) {
                                    if (pa.getDirecao().equals("Entrada")) {
                                        Acesso a = new Acesso();
                                        a.setEntrada(pa);
                                        a.setSaida(null);
                                        listaAcessoAluno.add(a);
                                    } else {
                                        Acesso a = new Acesso();
                                        a.setEntrada(null);
                                        a.setSaida(pa);
                                        listaAcessoAluno.add(a);
                                    }
                                }
                                LocalDateTime horaCatraca = DateConverter.convertDateToLocalDateTime(pa.getDataHora());
                                // percorre e monta a lista com a entrada e saida sendo um unico objeto
                                for (Acesso acesso : listaAcessoAluno) {
                                    // verifica a direção de entrada do aluno, entrada
                                    if (pa.getDirecao().equals("Entrada")) {
                                        //entrada diferente de null
                                        if (acesso.getEntrada() != null) {
                                            LocalDateTime entrada = DateConverter.convertDateToLocalDateTime(acesso.getEntrada().getDataHora());
                                            // se tiver outro registro de entrada com a mesma hora já salva desconsidera desconsidera
                                            if (!entrada.isEqual(horaCatraca) || !(horaCatraca.getHour() == entrada.getHour())) {
                                                //se tiver outro registro de entrada que for uma hora ou mais do que a salva cria um novo acesso 
                                                if (horaCatraca.getHour() >= entrada.plusHours(1).getHour()) {
                                                    Acesso a = new Acesso();
                                                    a.setEntrada(pa);
                                                    a.setSaida(null);
                                                    listaAcessoAluno.add(a);
                                                }
                                            }
                                            // entrada igual a null
                                        } else {
                                            if (acesso.getSaida() == null) {
                                                Acesso a = new Acesso();
                                                a.setEntrada(pa);
                                                a.setSaida(null);
                                                listaAcessoAluno.add(a);
                                                //saida diferente de null
                                            } else {
                                                LocalDateTime saida = DateConverter.convertDateToLocalDateTime(acesso.getSaida().getDataHora());
                                                if (horaCatraca.isBefore(saida)) {
                                                    acesso.setEntrada(pa);
                                                } else {
                                                    Acesso a = new Acesso();
                                                    a.setEntrada(pa);
                                                    a.setSaida(null);
                                                    listaAcessoAluno.add(a);
                                                }
                                            }
                                        }
                                        contaEntradaDia++;
                                        // verifica a direção de entrada do aluno, saida
                                    } else {
                                        // verifica se nesse objeto já tem entrada
                                        if (acesso.getEntrada() != null) {
                                            // verifica se nesse objeto já tem saida
                                            if (acesso.getSaida() == null) {
                                                LocalDateTime entrada = DateConverter.convertDateToLocalDateTime(acesso.getEntrada().getDataHora());
                                                // se a hora da saída for maior ou igual a entrada salva a saida
                                                if (horaCatraca.isAfter(entrada)) {
                                                    acesso.setSaida(pa);
                                                }
                                            }
                                        } else {
                                            //se o objeto nao tiver entrada, cria o objeto somento com a saida
                                            Acesso a = new Acesso();
                                            a.setEntrada(null);
                                            a.setSaida(pa);
                                            listaAcessoAluno.add(a);
                                        }
                                        contaSaidaDia++;
                                    }
                                }
                            }
                            listaAcessoAluno.stream().forEach((acesso) -> {
                                if (acesso.getEntrada() != null) {
                                    if (acesso.getSaida() != null) {
                                        System.out.println(acesso.getEntrada().getDirecao() + " - " + acesso.getEntrada().getDataHora() + " / "
                                                + acesso.getSaida().getDirecao() + " - " + acesso.getSaida().getDataHora());
                                    } else {
                                        System.out.println(acesso.getEntrada().getDirecao() + " - " + acesso.getEntrada().getDataHora() + " / Sem Saída");
                                    }
                                } else {
                                    if (acesso.getSaida() != null) {
                                        System.out.println("Sem Entrada / " + acesso.getSaida().getDirecao() + " - " + acesso.getSaida().getDataHora());
                                    }
                                }
                            });
                            System.out.println();
                            System.out.println("--------------------------------------------");
                            System.out.println();
                            List<Chamada> falta = new ArrayList<>();
                            listaAcessoAluno.stream().filter((acesso) -> (acesso.getEntrada() != null || acesso.getSaida() != null)).forEach((acesso) -> {
                                if (acesso.getEntrada() != null) {
                                    LocalDateTime entrada = DateConverter.convertDateToLocalDateTime(acesso.getEntrada().getDataHora());
                                    //tem entrada e saida
                                    if (acesso.getSaida() != null) {
                                        LocalDateTime saida = DateConverter.convertDateToLocalDateTime(acesso.getSaida().getDataHora());
                                        LocalDateTime temp = horaInicio;
                                        //Loop enquanto a hora da temp for menor ou igual a hora de termino da atividade
                                        while (temp.getHour() <= horaFim.getHour()) {
                                            //temp entre a hora de entrada e a hora de saida
                                            if (temp.getHour() >= entrada.getHour() && temp.getHour() <= saida.getHour()) {
                                                LocalDateTime tmp = entrada;
                                                //Loop enquanto a hora da tmp for menor ou igual a hora da saida
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
                                                    //se a tmp for depois que a hora de saida salva a hora da saida
                                                    if (!tmp.isAfter(saida)) {
                                                        presenca.setHriniaula(DateConverter.convertLocalDateTimeToDate(tmp));
                                                    } else {
                                                        presenca.setHriniaula(DateConverter.convertLocalDateTimeToDate(saida));
                                                    }
                                                    presenca.setVbfalta(true);
                                                    if (!chamada.contains(presenca)) {
                                                        chamada.add(presenca);
                                                    }
                                                    tmp = tmp.plusHours(1); 
                                                    temp = temp.plusHours(1);
                                                }
                                                // temp fora do intevalo da entrada e saida, falta
                                            } else {
                                                temp = temp.plusHours(1);
                                            }
                                        }
                                        // tem só a entrada
                                    } else {
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
                                                if (!chamada.contains(presenca)) {
                                                    chamada.add(presenca);
                                                }
                                            }
                                            temp = temp.plusHours(1);
                                        }
                                    }
                                    // nao tem a entrada
                                } else {
                                    // só tem a saida
                                    if (acesso.getSaida() != null) {
                                        LocalDateTime saida = DateConverter.convertDateToLocalDateTime(acesso.getSaida().getDataHora());
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
                                                if (!chamada.contains(presenca)) {
                                                    chamada.add(presenca);
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                            //falta
                            LocalDateTime temp = horaInicio;
                            while (temp.getHour() <= horaFim.getHour()) {
                                for (Acesso acesso : listaAcessoAluno) {
                                    if (acesso.getEntrada() != null) {
                                        LocalDateTime entrada = DateConverter.convertDateToLocalDateTime(acesso.getEntrada().getDataHora());
                                        //tem entrada e saida
                                        if (acesso.getSaida() != null) {
                                            LocalDateTime saida = DateConverter.convertDateToLocalDateTime(acesso.getSaida().getDataHora());
                                            if (temp.getHour() < entrada.getHour() || temp.getHour() > saida.getHour()) {
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
                                                if (!chamada.contains(presenca)) {
                                                    chamada.add(presenca);
                                                }
                                                temp = temp.plusHours(1);
                                            } else {
                                                temp = temp.plusHours(1);
                                            }
                                            //tem só a entrada
                                        } else {
                                            if (temp.getHour() < entrada.getHour() || temp.getHour() > entrada.getHour()) {
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
                                                if (!chamada.contains(presenca)) {
                                                    chamada.add(presenca);
                                                }
                                                temp = temp.plusHours(1);
                                            } else {
                                                temp = temp.plusHours(1);
                                            }
                                        }
                                        //tem só a saída
                                    } else {
                                        if (acesso.getSaida() != null) {
                                            LocalDateTime saida = DateConverter.convertDateToLocalDateTime(acesso.getSaida().getDataHora());

                                            if (temp.getHour() < saida.getHour() || temp.getHour() > saida.getHour()) {
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
                                                if (!chamada.contains(presenca)) {
                                                    chamada.add(presenca);
                                                }
                                                temp = temp.plusHours(1);
                                            } else {
                                                temp = temp.plusHours(1);
                                            }
                                        }
                                    }
                                }

                            }
                            // lançamento da falta quando a lista de acesso está vazia
                        } else {
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
                                if (!chamada.contains(presenca)) {
                                    chamada.add(presenca);
                                }
                                temp = temp.plusHours(1);
                            }
                        }
                        // break;
                    }// fim if matricula
                }// fim for inscritos
            }
        }

        System.out.println();
        System.out.println();
        System.out.println(">>>>>>>>>>>>>>>> Chamada <<<<<<<<<<<<<<<<<<<");
        System.out.println();
        System.out.println("--------------------------------------------");
        System.out.println();

        List<Chamada> listaC = new ArrayList<>(chamada);

        Collections.sort(listaC);

        listaC.stream().forEach((ch) -> {
            if (ch.isVbfalta()) {
                System.out.println(ch.getSqmatric() + " - " + ch.getDtaula() + " - " + ch.getHriniaula() + " - P");
            } else {
                System.out.println(ch.getSqmatric() + " - " + ch.getDtaula() + " - " + ch.getHriniaula() + " - F");
            }
        });

        System.out.println();
        System.out.println("--------------------------------------------");
        System.out.println();
        System.out.println("Termino da chamada: " + new Date());
    }
}
