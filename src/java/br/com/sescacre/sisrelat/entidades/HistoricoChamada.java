/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sescacre.sisrelat.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Rennan Francisco
 */
@Entity
public class HistoricoChamada implements Serializable {
    
    @Id
    private Long cdprograma;
    @Id
    private Long cdconfig;
    @Id
    private Long sqocorrenc;
    @Id
    @Temporal(TemporalType.DATE)
    private Date mes;
    @OneToOne
    @JoinColumn(nullable = false)
    private Usuarios usuario = new Usuarios();
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_chamada")
    private Date datachamada;

    public Long getCdprograma() {
        return cdprograma;
    }

    public void setCdprograma(Long cdprograma) {
        this.cdprograma = cdprograma;
    }

    public Long getCdconfig() {
        return cdconfig;
    }

    public void setCdconfig(Long cdconfig) {
        this.cdconfig = cdconfig;
    }

    public Long getSqocorrenc() {
        return sqocorrenc;
    }

    public void setSqocorrenc(Long sqocorrenc) {
        this.sqocorrenc = sqocorrenc;
    }

    public Date getMes() {
        return mes;
    }

    public void setMes(Date mes) {
        this.mes = mes;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Date getDatachamada() {
        return datachamada;
    }

    public void setDatachamada(Date datachamada) {
        this.datachamada = datachamada;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.cdprograma);
        hash = 53 * hash + Objects.hashCode(this.cdconfig);
        hash = 53 * hash + Objects.hashCode(this.sqocorrenc);
        hash = 53 * hash + Objects.hashCode(this.mes);
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
        final HistoricoChamada other = (HistoricoChamada) obj;
        if (!Objects.equals(this.cdprograma, other.cdprograma)) {
            return false;
        }
        if (!Objects.equals(this.cdconfig, other.cdconfig)) {
            return false;
        }
        if (!Objects.equals(this.sqocorrenc, other.sqocorrenc)) {
            return false;
        }
        if (!Objects.equals(this.mes, other.mes)) {
            return false;
        }
        return true;
    }
    
    
}
