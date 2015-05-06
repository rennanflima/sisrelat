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
public class Inscritos {

    private String matFormat;
    private Long sqMatric;
    private Long cdUop;
    private Long cdPrograma;
    private Long cdConfig;
    private Long sqOcorrenc;
    private String nmCliente;
    private String cdImpress;

    public String getMatFormat() {
        return matFormat;
    }

    public void setMatFormat(String matFormat) {
        this.matFormat = matFormat;
    }

    public Long getSqMatric() {
        return sqMatric;
    }

    public void setSqMatric(Long sqMatric) {
        this.sqMatric = sqMatric;
    }

    public Long getCdUop() {
        return cdUop;
    }

    public void setCdUop(Long cdUop) {
        this.cdUop = cdUop;
    }

    public Long getCdPrograma() {
        return cdPrograma;
    }

    public void setCdPrograma(Long cdPrograma) {
        this.cdPrograma = cdPrograma;
    }

    public Long getCdConfig() {
        return cdConfig;
    }

    public void setCdConfig(Long cdConfig) {
        this.cdConfig = cdConfig;
    }

    public Long getSqOcorrenc() {
        return sqOcorrenc;
    }

    public void setSqOcorrenc(Long sqOcorrenc) {
        this.sqOcorrenc = sqOcorrenc;
    }

    public String getNmCliente() {
        return nmCliente;
    }

    public void setNmCliente(String nmCliente) {
        this.nmCliente = nmCliente;
    }

    public String getCdImpress() {
        return cdImpress;
    }

    public void setCdImpress(String cdImpress) {
        this.cdImpress = cdImpress;
    }
    
}
