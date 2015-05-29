/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sescacre.sisrelat.entidades;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Rennan Francisco
 */
public class Acesso implements Serializable {

    private PactoAcesso entrada = new PactoAcesso();
    private PactoAcesso saida = new PactoAcesso();

    public PactoAcesso getEntrada() {
        return entrada;
    }

    public void setEntrada(PactoAcesso entrada) {
        this.entrada = entrada;
    }

    public PactoAcesso getSaida() {
        return saida;
    }

    public void setSaida(PactoAcesso saida) {
        this.saida = saida;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.entrada);
        hash = 71 * hash + Objects.hashCode(this.saida);
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
        final Acesso other = (Acesso) obj;
        if (!Objects.equals(this.entrada, other.entrada)) {
            return false;
        }
        if (!Objects.equals(this.saida, other.saida)) {
            return false;
        }
        return true;
    }
    
}
