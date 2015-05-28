/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.bean;

import br.com.sescacre.sisrelat.dao.CargosDAO;
import br.com.sescacre.sisrelat.dao.CargosDoSetorDAO;
import br.com.sescacre.sisrelat.dao.SetoresDAO;
import br.com.sescacre.sisrelat.entidades.Cargos;
import br.com.sescacre.sisrelat.entidades.CargosDoSetor;
import br.com.sescacre.sisrelat.entidades.Setores;
import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Iterator;
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
public class CargosBean implements Serializable {

    private Cargos cargo = new Cargos();
    private List<Cargos> cargos = new ArrayList<Cargos>();
    private List<String> setores = new ArrayList<String>();
    private List<CargosDoSetor> cds = new ArrayList<CargosDoSetor>();
    private String msgSetores = "";

    @PostConstruct
    public void construct() {
        cargos = new CargosDAO().ListaTodos();
    }

    public Cargos getCargo() {
        return cargo;
    }

    public void setCargo(Cargos cargo) {
        this.cargo = cargo;
    }

    public List<Cargos> getCargos() {
        return cargos;
    }

    public void setCargos(List<Cargos> cargos) {
        this.cargos = cargos;
    }

    public List<String> getSetores() {
        return setores;
    }

    public void setSetores(List<String> setores) {
        this.setores = setores;
    }

    public List<CargosDoSetor> getCds() {
        return cds = new CargosDoSetorDAO().pesquisaCargosDoSetorPorCargo(cargo.getIdCargo());
    }

    public void setCds(List<CargosDoSetor> cds) {
        this.cds = cds;
    }

    public String carregaListaSetores() {
        getCds();
        for (CargosDoSetor cdsd : cds) {
            setores.add(cdsd.getSetor().getIdSetor().toString());
        }
        return null;
    }

    public String getMsgSetores() {
        msgSetores = "";
        getCds();
        int qtd = cds.size();
        if (qtd >= 2) {
            Iterator i = cds.iterator();
            while (i.hasNext()) {
                CargosDoSetor cs = (CargosDoSetor) i.next();
                if (i.hasNext()) {
                    msgSetores += cs.getSetor().getSigla() + ", ";
                } else {
                    msgSetores += cs.getSetor().getSigla();
                }
            }
            return msgSetores;
        } else {
            for (CargosDoSetor c : cds) {
                msgSetores += c.getSetor().getSigla();
            }
            return msgSetores;
        }
    }

    public void setMsgSetores(String msgSetores) {
        this.msgSetores = msgSetores;
    }

    public String salvar() {
        CargosDAO cargoD = new CargosDAO();
        FacesContext msg = FacesContext.getCurrentInstance();
        CargosDoSetorBean cdsb = new CargosDoSetorBean();
        try {
            if (cargo.getIdCargo() == null) {
                cargoD.salvar(cargo);
                for (String i : setores) {
                    Setores s = new SetoresDAO().pesquisaPorId(Integer.parseInt(i));
                    cdsb.salvar(s, cargo);
                }
                msg.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "O cargo '" + cargo.getNome() + "' foi inserido com sucesso.", null));
            } else {
                List<CargosDoSetor> cargoSetor = getCds();
                cargoD.alterar(cargo);
                if (setores.size() >= cargoSetor.size()) {
                    for (String i : setores) {
                        for (CargosDoSetor cs : cargoSetor) {
                            if (cs.getSetor().getIdSetor() != Integer.parseInt(i)) {
                                Setores s = new SetoresDAO().pesquisaPorId(Integer.parseInt(i));
                                cdsb.salvar(s, cargo);
                            }
                        }
                    }
                } else {
                    List<CargosDoSetor> tmp = new ArrayList<>();
                    for (String i : setores) {
                        for (CargosDoSetor cs : cargoSetor) {
                            if (cs.getSetor().getIdSetor() != Integer.parseInt(i)) {
                                tmp.add(cs);
                            }
                        }
                    }
                    for (CargosDoSetor cargosDoSetor : tmp) {
                        cdsb.excluir(cargosDoSetor);
                    }
                    /*for (CargosDoSetor cargosDoSetor : cs) {
                     cdsb.excluir(cargosDoSetor);
                     }
                     cargoD.alterar(cargo);
                     for (String i : setores) {
                     Setores s = new SetoresDAO().pesquisaPorId(Integer.parseInt(i));
                     cdsb.alterar(s, cargo);
                     }*/
                }
                msg.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "O cargo '" + cargo.getNome() + "' foi alterado com sucesso.", null));
                RequestContext.getCurrentInstance().execute("inserir.hide()");
            }
            limpar();
        } catch (IllegalArgumentException ex) {
            msg.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            ex.getMessage(), null));
        } catch (InterruptedException ex) {
            msg.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Ocorreu um erro ao vincular o(s) setore(s) ao cargo '" + cargo.getNome() + "'", null));
        } catch (SQLIntegrityConstraintViolationException ex) {
            msg.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Já existe um Cargo cadastrado com esse nome!", null));
        } catch (Exception e) {
            System.out.println(e);
            msg.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Ocorreu um erro ao inserir o setor '" + cargo.getNome() + "'", null));
        }
        construct();
        return null;
    }

    public String excluir() {
        CargosDAO cargoD = new CargosDAO();
        FacesContext msg = FacesContext.getCurrentInstance();
        CargosDoSetorBean cdsb = new CargosDoSetorBean();
        try {
            List<CargosDoSetor> cargoSetor = getCds();
            for (CargosDoSetor cargosDoSetor : cargoSetor) {
                cdsb.excluir(cargosDoSetor);
            }
            cargoD.excluir(cargo);
            msg.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "O cargo '" + cargo.getNome() + "' foi excluído com sucesso.", null));
            limpar();
        } catch (Exception e) {
            msg.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Ocorreu um erro ao excluir o pilar '" + cargo.getNome() + "'", null));
        }
        construct();
        return null;
    }

    public String limpar() {
        cargo = new Cargos();
        setores = new ArrayList<String>();
        cds = new ArrayList<CargosDoSetor>();
        return null;
    }
}
