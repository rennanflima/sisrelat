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
import java.time.DayOfWeek;
import java.time.LocalDate;
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
    
    public static void turmasHorarioFechado(int ano, int mes, ProgramaCorrente progocor, List<Inscritos> inscritos, List<Chamada> chamada, FacesContext msg) {
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
                    presenca.setDtaula(Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant()));
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
    
}
