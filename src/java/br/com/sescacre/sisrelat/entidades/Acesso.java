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
public class Acesso {

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
    
}
