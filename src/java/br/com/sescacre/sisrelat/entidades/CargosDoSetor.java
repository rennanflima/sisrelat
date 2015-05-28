/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sescacre.sisrelat.entidades;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Rennan Francisco
 */
@Entity
public class CargosDoSetor implements Serializable {
    
    @Id
    @GeneratedValue
    private Integer idCargoDoSetor;
    @ManyToOne
    @JoinColumn
    private Setores setor = new Setores();
    @ManyToOne
    @JoinColumn
    private Cargos cargo = new Cargos();

    public Integer getIdCargoDoSetor() {
        return idCargoDoSetor;
    }

    public void setIdCargoDoSetor(Integer idCargoDoSetor) {
        this.idCargoDoSetor = idCargoDoSetor;
    }

    public Setores getSetor() {
        return setor;
    }

    public void setSetor(Setores setor) {
        this.setor = setor;
    }

    public Cargos getCargo() {
        return cargo;
    }

    public void setCargo(Cargos cargo) {
        this.cargo = cargo;
    }
    
    
}
