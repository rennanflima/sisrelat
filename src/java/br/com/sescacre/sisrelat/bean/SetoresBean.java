/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.bean;

import br.com.sescacre.sisrelat.dao.CargosDoSetorDAO;
import br.com.sescacre.sisrelat.dao.SetoresDAO;
import br.com.sescacre.sisrelat.entidades.CargosDoSetor;
import br.com.sescacre.sisrelat.entidades.Setores;
import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Rennan Francisco
 */
@ManagedBean
@ViewScoped
public class SetoresBean implements Serializable {

    private Setores setor = new Setores();
    private List<Setores> setores = new ArrayList<Setores>();
    private List<CargosDoSetor> cds = new ArrayList<CargosDoSetor>();

    @PostConstruct
    public void construct() {
        setores = new SetoresDAO().ListaTodos();
    }

    public Setores getSetor() {
        return setor;
    }

    public void setSetor(Setores setor) {
        this.setor = setor;
    }

    public List<Setores> getSetores() {
        return setores;
    }

    public void setSetores(List<Setores> setores) {
        this.setores = setores;
    }

    public String salvar() {
        SetoresDAO setorD = new SetoresDAO();
        FacesContext msg = FacesContext.getCurrentInstance();
        try {
            if (setor.getIdSetor() == null) {
                setorD.salvar(setor);
                msg.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "O setor '" + setor.getNome() + "' foi inserido com sucesso.", null));
            } else {
                setorD.alterar(setor);
                msg.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "O setor '" + setor.getNome() + "' foi alterado com sucesso.", null));
                RequestContext.getCurrentInstance().execute("inserir.hide()");
            }
            limpar();
        } catch (SQLIntegrityConstraintViolationException ex) {
            msg.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Já existe um Setor cadastrado com esse nome!", null));
        } catch (Exception e) {
            msg.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Ocorreu um erro ao inserir o setor '" + setor.getNome() + "'", null));
        }
        construct();
        return null;
    }

    public String excluir() {
        SetoresDAO setorD = new SetoresDAO();
        FacesContext msg = FacesContext.getCurrentInstance();
        try {
            cds = new CargosDoSetorDAO().pesquisaCargosDoSetorPorSetor(setor.getIdSetor());
            if (cds.isEmpty()) {
                setorD.excluir(setor);
                msg.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "O setor '" + setor.getNome() + "' foi excluído com sucesso.", null));
                limpar();
            } else {
                msg.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "O setor '" + setor.getNome() + "' possui dependências com a tabela cargos. É necessário corrigí-las.", null));
            }
        } catch (Exception e) {
            msg.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Ocorreu um erro ao excluir o pilar '" + setor.getNome() + "'", null));
        }
        construct();
        return null;
    }

    public String limpar() {
        setor = new Setores();
        return null;
    }
}
