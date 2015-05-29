/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sescacre.sisrelat.entidades;

import java.io.Serializable;


/**
 *
 * @author Rennan Francisco
 */
public class ProgramaCorrente implements Serializable{
    private Long configuracaoPrograma;
    private Long sequenciaOcorrencia;
    private Long programa;
    private String descricao;

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
}
