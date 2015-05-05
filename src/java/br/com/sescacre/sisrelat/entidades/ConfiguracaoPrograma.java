/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sescacre.sisrelat.entidades;

/**
 *
 * @author Rennan Francisco
 */
public class ConfiguracaoPrograma {
    private Long codigo;
    private Long programa;
    private String nome;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Long getPrograma() {
        return programa;
    }

    public void setPrograma(Long programa) {
        this.programa = programa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
}
