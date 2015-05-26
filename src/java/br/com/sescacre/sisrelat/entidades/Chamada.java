/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Rennan Francisco
 */
@Entity
@Table(name = "CAFALTAS")
public class Chamada implements Serializable, Comparable<Chamada>{

    @Id
    private Long sqmatric;
    @Id
    private Long cduop;
    @Id
    private Long cdprograma;
    @Id
    private Long cdconfig;
    @Id
    private Long sqocorrenc;
    @Id
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dtaula;
    @Id
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date hriniaula;
    private boolean vbfalta = false;
    private String lgatu;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dtatu;
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date hratu;

    public Long getSqmatric() {
        return sqmatric;
    }

    public void setSqmatric(Long sqmatric) {
        this.sqmatric = sqmatric;
    }

    public Long getCduop() {
        return cduop;
    }

    public void setCduop(Long cduop) {
        this.cduop = cduop;
    }

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

    public Date getDtaula() {
        return dtaula;
    }

    public void setDtaula(Date dtaula) {
        this.dtaula = dtaula;
    }

    public Date getHriniaula() {
        return hriniaula;
    }

    public void setHriniaula(Date hriniaula) {
        this.hriniaula = hriniaula;
    }

    public boolean isVbfalta() {
        return vbfalta;
    }

    public void setVbfalta(boolean vbfalta) {
        this.vbfalta = vbfalta;
    }

    public String getLgatu() {
        return lgatu;
    }

    public void setLgatu(String lgatu) {
        this.lgatu = lgatu;
    }

    public Date getDtatu() {
        return dtatu;
    }

    public void setDtatu(Date dtatu) {
        this.dtatu = dtatu;
    }

    public Date getHratu() {
        return hratu;
    }

    public void setHratu(Date hratu) {
        this.hratu = hratu;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.sqmatric);
        hash = 59 * hash + Objects.hashCode(this.cduop);
        hash = 59 * hash + Objects.hashCode(this.cdprograma);
        hash = 59 * hash + Objects.hashCode(this.cdconfig);
        hash = 59 * hash + Objects.hashCode(this.sqocorrenc);
        hash = 59 * hash + Objects.hashCode(this.dtaula);
        hash = 59 * hash + Objects.hashCode(this.hriniaula);
        hash = 59 * hash + (this.vbfalta ? 1 : 0);
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
        final Chamada other = (Chamada) obj;
        if (!Objects.equals(this.sqmatric, other.sqmatric)) {
            return false;
        }
        if (!Objects.equals(this.cduop, other.cduop)) {
            return false;
        }
        if (!Objects.equals(this.cdprograma, other.cdprograma)) {
            return false;
        }
        if (!Objects.equals(this.cdconfig, other.cdconfig)) {
            return false;
        }
        if (!Objects.equals(this.sqocorrenc, other.sqocorrenc)) {
            return false;
        }
        if (!Objects.equals(this.dtaula, other.dtaula)) {
            return false;
        }
        if (!Objects.equals(this.hriniaula, other.hriniaula)) {
            return false;
        }
        if (this.vbfalta != other.vbfalta) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Chamada c) {
        return this.dtatu.compareTo(c.getDtatu());
    }
    
}
