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
public class Programa implements Serializable {
    private Long codigo;
    private String nome;
    private Long programaSuperior;
    private Long uop;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getProgramaSuperior() {
        return programaSuperior;
    }

    public void setProgramaSuperior(Long programaSuperior) {
        this.programaSuperior = programaSuperior;
    }

    public Long getUop() {
        return uop;
    }

    public void setUop(Long uop) {
        this.uop = uop;
    }

}
