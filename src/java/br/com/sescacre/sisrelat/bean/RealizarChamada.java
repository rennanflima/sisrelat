/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.bean;

import br.com.sescacre.sisrelat.dao.ChamadaDao;
import br.com.sescacre.sisrelat.dao.InscricaoDao;
import br.com.sescacre.sisrelat.dao.PactoAcessoDao;
import br.com.sescacre.sisrelat.entidades.Acesso;
import br.com.sescacre.sisrelat.entidades.Chamada;
import br.com.sescacre.sisrelat.entidades.Horarios;
import br.com.sescacre.sisrelat.entidades.Inscritos;
import br.com.sescacre.sisrelat.entidades.PactoAcesso;
import br.com.sescacre.sisrelat.entidades.ProgramaCorrente;
import br.com.sescacre.sisrelat.entidades.Usuarios;
import br.com.sescacre.sisrelat.util.DateConverter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Rennan Francisco
 */
public class RealizarChamada implements Serializable {

    /**
     * <p> O Método pesquisa na tabela da Pacto, onde fica salvo os registros de entrada e saída da catraca e os inscritos da turma no dia. 
     * A lista de inscritos é percorrida e caso a carteira do Sesc do aluno seja encontrada na lista de acesso é lançada a presença, 
     * senão é lançada a falta.</p>
     *
     * <p>Ao final do processo de construção da lista contendo o lançamento das presenças e faltas de todos os inscritos do dia, ela é inserida
     * no DB2 na tabela CAFALTAS.</p>
     * 
     * @param progocor Turma a qual será feita a chamada
     * @param h Os horarios de inicio e fim do dia
     * @param data O dia a qual será feita a chamada
     * @param user O usuário do sistema que realizou a chamada
     */
    public static void turmasHorarioFixo(ProgramaCorrente progocor, Horarios h, LocalDate data, Usuarios user) {
        List<Chamada> chamada = new ArrayList<>();
        List<Inscritos> inscritos = new InscricaoDao().inscritosTurma(progocor.getPrograma(), progocor.getConfiguracaoPrograma(), progocor.getSequenciaOcorrencia());
        Date hi = DateConverter.convertLocalDateTimeToDate(h.getHoraInicio());
        Date hf = DateConverter.convertLocalDateTimeToDate(h.getHoraTermino());
        List<PactoAcesso> lista = new PactoAcessoDao().acessoDoDia(data, h.getHoraInicio().toLocalTime(), h.getHoraTermino().toLocalTime());
        for (Inscritos insc : inscritos) {
            Chamada presenca = new Chamada();
            presenca.setSqmatric(insc.getSqMatric());
            presenca.setCduop(insc.getCdUop());
            presenca.setCdprograma(insc.getCdPrograma());
            presenca.setCdconfig(insc.getCdConfig());
            presenca.setSqocorrenc(insc.getSqOcorrenc());
            presenca.setHriniaula(hi);
            presenca.setDtaula(DateConverter.convertLocalDateToDate(data));
            presenca.setLgatu(user.getLogin());
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
            chamada.stream().forEach((ch) -> {
                try {
                    new ChamadaDao().salvar(ch);
                } catch (Exception ex) {
                    System.out.println("Erro ao salvar a chamada: " + ex.getMessage());
                }
            });
        } else {
            System.out.println("Tamanho da lista de chamada é maior: " + chamada.size());
        }
    }
    /**
     * <p> O Método pesquisa na tabela da Pacto, onde fica salvo os registros de entrada e saída da catraca.
     * Com uma lista de acesso a qual entrada e saída são objetos distintos, é construída uma nova lista a qual entrada e saída são
     * único objeto da lista.</p>
     * 
     * <p>Com a lista de acesso primeiramente é lançada as presenças, com as seguintes regras:</p>
     * <ul>
     *  <li>Quando tiver Entrada e Saída, a presença será lançada a cada hora, começando na hora de entrada e terminando na hora de sáida;</li>
     *  <li>Quando tiver só a Entrada, a presença será lançada somente na hora de entrada;</li>
     *  <li>Quando tiver só a Saída, a presença será lançada somente na hora de saída.</li>
     * </ul>
     * 
     * <p>Como as aulas da turma tem duração maior que 1 hora, será dada presença conforme descrito acima e nos demais horários que não houverem
     * registros será lançada a falta; caso não tenha registros de acesso será lançada falta em todos os horários da turma.</p> 
     * 
     * <p>Ao final do processo de construção da lista contendo o lançamento das presenças e faltas de todos os inscritos do dia, ela é inserida
     * no DB2 na tabela CAFALTAS.</p>
     * 
     * @param progocor Turma a qual será feita a chamada
     * @param h Os horarios de inicio e fim do dia
     * @param data O dia a qual será feita a chamada
     * @param user O usuário do sistema que realizou a chamada
     */
    public static void turmaHorarioLivre(ProgramaCorrente progocor, Horarios h, LocalDate data, Usuarios user) {
        CopyOnWriteArrayList<Chamada> chamada = new CopyOnWriteArrayList<>();
        List<Inscritos> inscritos = new InscricaoDao().inscritosTurma(progocor.getPrograma(), progocor.getConfiguracaoPrograma(), progocor.getSequenciaOcorrencia());
        Date hi = DateConverter.convertLocalDateTimeToDate(h.getHoraInicio());
        Date hf = DateConverter.convertLocalDateTimeToDate(h.getHoraTermino());
        // percorre a lista de inscritos da turma
        inscritos.stream().forEach((insc) -> {
            List<PactoAcesso> lista = new PactoAcessoDao().acessoDoDiaAluno(data, h.getHoraInicio().toLocalTime(), h.getHoraTermino().toLocalTime(), insc.getSqMatric());
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
                            if (!listaAcessoAluno.contains(a)) {
                                listaAcessoAluno.add(a);
                            }
                        } else {
                            Acesso a = new Acesso();
                            a.setEntrada(null);
                            a.setSaida(pa);
                            if (!listaAcessoAluno.contains(a)) {
                                listaAcessoAluno.add(a);
                            }
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
                                        if (!listaAcessoAluno.contains(a)) {
                                            listaAcessoAluno.add(a);
                                        }
                                    }
                                }
                                // entrada igual a null
                            } else {
                                if (acesso.getSaida() == null) {
                                    Acesso a = new Acesso();
                                    a.setEntrada(pa);
                                    a.setSaida(null);
                                    if (!listaAcessoAluno.contains(a)) {
                                        listaAcessoAluno.add(a);
                                    }
                                    //saida diferente de null
                                } else {
                                    LocalDateTime saida = DateConverter.convertDateToLocalDateTime(acesso.getSaida().getDataHora());
                                    if (horaCatraca.isBefore(saida)) {
                                        acesso.setEntrada(pa);
                                    } else {
                                        Acesso a = new Acesso();
                                        a.setEntrada(pa);
                                        a.setSaida(null);
                                        if (!listaAcessoAluno.contains(a)) {
                                            listaAcessoAluno.add(a);
                                        }
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
                                if (!listaAcessoAluno.contains(a)) {
                                    listaAcessoAluno.add(a);
                                }
                            }
                            contaSaidaDia++;
                        }
                    }
                }
                listaAcessoAluno.stream().filter((acesso) -> (acesso.getEntrada() != null || acesso.getSaida() != null)).forEach((acesso) -> {
                    if (acesso.getEntrada() != null) {
                        LocalDateTime entrada = DateConverter.convertDateToLocalDateTime(acesso.getEntrada().getDataHora());
                        //tem entrada e saida
                        if (acesso.getSaida() != null) {
                            LocalDateTime saida = DateConverter.convertDateToLocalDateTime(acesso.getSaida().getDataHora());
                            LocalDateTime temp = h.getHoraInicio();
                            //Loop enquanto a hora da temp for menor ou igual a hora de termino da atividade
                            while (temp.getHour() <= h.getHoraTermino().getHour()) {
                                //temp entre a hora de entrada e a hora de saida
                                if (temp.getHour() >= entrada.getHour() && temp.getHour() <= saida.getHour()) {
                                    LocalDateTime tmp = entrada;
                                    //Loop enquanto a hora da tmp for menor ou igual a hora da saida
                                    while (tmp.getHour() <= saida.getHour()) {
                                        Chamada presenca = new Chamada(insc.getSqMatric(),
                                                insc.getCdUop(),
                                                insc.getCdPrograma(),
                                                insc.getCdConfig(),
                                                insc.getSqOcorrenc(),
                                                DateConverter.convertLocalDateToDate(data),
                                                (!tmp.isAfter(saida) ? DateConverter.convertLocalDateTimeToDate(tmp)
                                                    : DateConverter.convertLocalDateTimeToDate(saida)),
                                                true,
                                                user.getLogin(),
                                                new Date(),
                                                new Date());
                                        //se a tmp for depois que a hora de saida salva a hora da saida
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
                            LocalDateTime temp = h.getHoraInicio();
                            while (temp.getHour() <= h.getHoraTermino().getHour()) {
                                if (temp.getHour() == entrada.getHour()) {
                                    Chamada presenca = new Chamada(insc.getSqMatric(),
                                                insc.getCdUop(),
                                                insc.getCdPrograma(),
                                                insc.getCdConfig(),
                                                insc.getSqOcorrenc(),
                                                DateConverter.convertLocalDateToDate(data),
                                                DateConverter.convertLocalDateTimeToDate(entrada),
                                                true,
                                                user.getLogin(),
                                                new Date(),
                                                new Date());
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
                            LocalDateTime temp = h.getHoraInicio();
                            while (temp.getHour() <= h.getHoraTermino().getHour()) {
                                if (temp.getHour() == saida.getHour()) {
                                    Chamada presenca = new Chamada(insc.getSqMatric(),
                                                insc.getCdUop(),
                                                insc.getCdPrograma(),
                                                insc.getCdConfig(),
                                                insc.getSqOcorrenc(),
                                                DateConverter.convertLocalDateToDate(data),
                                                DateConverter.convertLocalDateTimeToDate(saida),
                                                true,
                                                user.getLogin(),
                                                new Date(),
                                                new Date());
                                    if (!chamada.contains(presenca)) {
                                        chamada.add(presenca);
                                    }
                                }
                                temp = temp.plusHours(1);
                            }
                        }
                    }
                });
                //falta
                LocalDateTime temp = h.getHoraInicio();
                while (temp.getHour() <= h.getHoraTermino().getHour()) {
                    for (Acesso acesso : listaAcessoAluno) {
                        if (acesso.getEntrada() != null) {
                            LocalDateTime entrada = DateConverter.convertDateToLocalDateTime(acesso.getEntrada().getDataHora());
                            //tem entrada e saida
                            if (acesso.getSaida() != null) {
                                LocalDateTime saida = DateConverter.convertDateToLocalDateTime(acesso.getSaida().getDataHora());
                                if (temp.getHour() < entrada.getHour() || temp.getHour() > saida.getHour()) {
                                    Chamada presenca = new Chamada(insc.getSqMatric(),
                                        insc.getCdUop(),
                                        insc.getCdPrograma(),
                                        insc.getCdConfig(),
                                        insc.getSqOcorrenc(),
                                        DateConverter.convertLocalDateToDate(data),
                                        (!temp.isAfter(h.getHoraTermino()) ? DateConverter.convertLocalDateTimeToDate(temp)
                                            : DateConverter.convertLocalDateTimeToDate(h.getHoraTermino())),
                                        false,
                                        user.getLogin(),
                                        new Date(),
                                        new Date());
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
                                    Chamada presenca = new Chamada(insc.getSqMatric(),
                                        insc.getCdUop(),
                                        insc.getCdPrograma(),
                                        insc.getCdConfig(),
                                        insc.getSqOcorrenc(),
                                        DateConverter.convertLocalDateToDate(data),
                                        (!temp.isAfter(h.getHoraTermino()) ? DateConverter.convertLocalDateTimeToDate(temp)
                                            : DateConverter.convertLocalDateTimeToDate(h.getHoraTermino())),
                                        false,
                                        user.getLogin(),
                                        new Date(),
                                        new Date());
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
                                    Chamada presenca = new Chamada(insc.getSqMatric(),
                                        insc.getCdUop(),
                                        insc.getCdPrograma(),
                                        insc.getCdConfig(),
                                        insc.getSqOcorrenc(),
                                        DateConverter.convertLocalDateToDate(data),
                                        (!temp.isAfter(h.getHoraTermino()) ? DateConverter.convertLocalDateTimeToDate(temp)
                                            : DateConverter.convertLocalDateTimeToDate(h.getHoraTermino())),
                                        false,
                                        user.getLogin(),
                                        new Date(),
                                        new Date());
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
                LocalDateTime temp = h.getHoraInicio();
                while (temp.getHour() <= h.getHoraTermino().getHour()) {
                    Chamada presenca = new Chamada(insc.getSqMatric(),
                        insc.getCdUop(),
                        insc.getCdPrograma(),
                        insc.getCdConfig(),
                        insc.getSqOcorrenc(),
                        DateConverter.convertLocalDateToDate(data),
                        (!temp.isAfter(h.getHoraTermino()) ? DateConverter.convertLocalDateTimeToDate(temp)
                            : DateConverter.convertLocalDateTimeToDate(h.getHoraTermino())),
                        false,
                        user.getLogin(),
                        new Date(),
                        new Date());
                    if (!chamada.contains(presenca)) {
                        chamada.add(presenca);
                    }
                    temp = temp.plusHours(1);
                }
            }
        });

        List<Chamada> listaC = new ArrayList<>(chamada);

        Collections.sort(listaC);

        listaC.stream().forEach((ch) -> {
            try {
                new ChamadaDao().salvar(ch);
            } catch (Exception ex) {
                System.out.println("Erro ao salvar a chamada: " + ex.getMessage());
            }
        });
    }
}
