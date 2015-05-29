/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sescacre.sisrelat.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Rennan Francisco
 */
public class PactoAcesso implements Serializable {
    
    private String matFormat;
    private String nmCliente;
    private String tipo;
    private String direcao;
    private Date dataHora;

    public String getMatFormat() {
        return matFormat;
    }

    public void setMatFormat(String matFormat) {
        this.matFormat = matFormat;
    }

    public String getNmCliente() {
        return nmCliente;
    }

    public void setNmCliente(String nmCliente) {
        this.nmCliente = nmCliente;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDirecao() {
        return direcao;
    }

    public void setDirecao(String direcao) {
        this.direcao = direcao;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.matFormat);
        hash = 97 * hash + Objects.hashCode(this.nmCliente);
        hash = 97 * hash + Objects.hashCode(this.tipo);
        hash = 97 * hash + Objects.hashCode(this.direcao);
        hash = 97 * hash + Objects.hashCode(this.dataHora);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PactoAcesso other = (PactoAcesso) obj;
        if (!Objects.equals(this.matFormat, other.matFormat)) {
            return false;
        }
        if (!Objects.equals(this.nmCliente, other.nmCliente)) {
            return false;
        }
        if (!Objects.equals(this.tipo, other.tipo)) {
            return false;
        }
        if (!Objects.equals(this.direcao, other.direcao)) {
            return false;
        }
        if (!Objects.equals(this.dataHora, other.dataHora)) {
            return false;
        }
        return true;
    }
}
