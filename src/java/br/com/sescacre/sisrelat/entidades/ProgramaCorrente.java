/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sescacre.sisrelat.entidades;

import java.util.Date;


/**
 *
 * @author Rennan Francisco
 */
public class ProgramaCorrente {
    private Long configuracaoPrograma;
    private Long sequenciaOcorrencia;
    private Long programa;
    private String descricao;
    private Date horaInicio;
    private Date horaFim;

    public Long getConfiguracaoPrograma() {
        return configuracaoPrograma;
    }

    public void setConfiguracaoPrograma(Long configuracaoPrograma) {
        this.configuracaoPrograma = configuracaoPrograma;
    }

    public Long getSequenciaOcorrencia() {
        return sequenciaOcorrencia;
    }

    public void setSequenciaOcorrencia(Long sequenciaOcorrencia) {
        this.sequenciaOcorrencia = sequenciaOcorrencia;
    }

    public Long getPrograma() {
        return programa;
    }

    public void setPrograma(Long programa) {
        this.programa = programa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(Date horaFim) {
        this.horaFim = horaFim;
    }
    
    
}
