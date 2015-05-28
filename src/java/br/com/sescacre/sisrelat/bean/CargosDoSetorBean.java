/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.bean;

import br.com.sescacre.sisrelat.dao.CargosDoSetorDAO;
import br.com.sescacre.sisrelat.dao.FuncionariosDao;
import br.com.sescacre.sisrelat.entidades.Cargos;
import br.com.sescacre.sisrelat.entidades.CargosDoSetor;
import br.com.sescacre.sisrelat.entidades.Funcionarios;
import br.com.sescacre.sisrelat.entidades.Setores;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Rennan Francisco
 */
@ManagedBean
@ViewScoped
public class CargosDoSetorBean implements Serializable {

    private CargosDoSetor cDoSetor = new CargosDoSetor();
    private List<CargosDoSetor> cDoSetores = new ArrayList<CargosDoSetor>();
    private Integer idCargo;
    private Integer idSetor;
    private List<Funcionarios> func = new ArrayList<Funcionarios>();

    public CargosDoSetor getcDoSetor() {
        return cDoSetor;
    }

    public void setcDoSetor(CargosDoSetor cDoSetor) {
        this.cDoSetor = cDoSetor;
    }

    public List<CargosDoSetor> getcDoSetores() {
        return cDoSetores;
    }

    public void setcDoSetores(List<CargosDoSetor> cDoSetores) {
        this.cDoSetores = cDoSetores;
    }

    public Integer getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(Integer idCargo) {
        this.idCargo = idCargo;
    }

    public Integer getIdSetor() {
        return idSetor;
    }

    public void setIdSetor(Integer idSetor) {
        this.idSetor = idSetor;
    }

    public String salvar(Setores setor, Cargos cargo) throws Exception {
        CargosDoSetorDAO cdsd = new CargosDoSetorDAO();
        FacesContext msg = FacesContext.getCurrentInstance();
        try {
            cDoSetor.setCargo(cargo);
            cDoSetor.setSetor(setor);
            cdsd.salvar(cDoSetor);
            cDoSetor = new CargosDoSetor();
        } catch (Exception e) {
            throw new InterruptedException();
        }
        return null;
    }

    public String excluir(CargosDoSetor cds) throws Exception {
        CargosDoSetorDAO cdsd = new CargosDoSetorDAO();
        FacesContext msg = FacesContext.getCurrentInstance();
        try {
            func = new FuncionariosDao().pesquisaFuncionarioPorCargoDoSetor(cds.getIdCargoDoSetor());
            if (func.isEmpty()) {
                cdsd.excluir(cds);
                cDoSetor = new CargosDoSetor();
            } else {
                throw new IllegalArgumentException("O cargo '" + cds.getCargo().getNome() + "' "
                        + "não pode ser desvinculado do setor '" + cds.getSetor().getSigla() + "' , pois possui dependências com a tabela funcionários. É necessário corrigí-las.");
            }
        } catch (Exception e) {
            throw new InterruptedException();
        }
        return null;
    }

    public String limpar() {
        cDoSetor = new CargosDoSetor();
        return null;
    }
}
