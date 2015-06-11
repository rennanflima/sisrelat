/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.bean;

import br.com.sescacre.sisrelat.dao.CargosDAO;
import br.com.sescacre.sisrelat.dao.CargosDoSetorDAO;
import br.com.sescacre.sisrelat.dao.FuncionariosDao;
import br.com.sescacre.sisrelat.dao.SetoresDAO;
import br.com.sescacre.sisrelat.dao.UsuariosDao;
import br.com.sescacre.sisrelat.entidades.Cargos;
import br.com.sescacre.sisrelat.entidades.CargosDoSetor;
import br.com.sescacre.sisrelat.entidades.Funcionarios;
import br.com.sescacre.sisrelat.entidades.Setores;
import br.com.sescacre.sisrelat.entidades.Usuarios;
import br.com.sescacre.sisrelat.util.GeraSenha;
import br.com.sescacre.sisrelat.util.MailEnviaAuth;
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
public class FuncionariosBean implements Serializable {

    private Funcionarios funcionario = new Funcionarios();
    private List<Funcionarios> funcionarios = new ArrayList<Funcionarios>();
    private String login;
    private String permissao;
    private Integer idSetor;
    private Integer idCargo;
    private List<CargosDoSetor> cargosDoSetor = new ArrayList<CargosDoSetor>();

    @PostConstruct
    public void construct() {
        funcionarios = new FuncionariosDao().ListaTodos();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPermissao() {
        permissao = funcionario.getUser().getAutorizacao();
        return permissao;
    }

    public void setPermissao(String permissao) {
        this.permissao = permissao;
    }

    public Funcionarios getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionarios usuario) {
        this.funcionario = usuario;
    }

    public List<Funcionarios> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(List<Funcionarios> usuarios) {
        this.funcionarios = usuarios;
    }

    public Integer getIdSetor() {
        if (funcionario.getIdFuncionarios() == null) {
            return idSetor;
        } else {
            Integer temp = funcionario.getSetor().getIdSetor();
            if (idSetor == null) {
                return temp;
            } else {
                return idSetor;
            }
        }
    }

    public void setIdSetor(Integer idSetor) {
        this.idSetor = idSetor;
    }

    public Integer getIdCargo() {
        if (funcionario.getIdFuncionarios() == null) {
            return idCargo;
        } else {
            Integer temp = funcionario.getCargo().getIdCargo();
            if (idCargo == null) {
                return temp;
            } else {
                return idCargo;
            }
        }
    }

    public void setIdCargo(Integer idCargo) {
        this.idCargo = idCargo;
    }

    public List<CargosDoSetor> getCargosDoSetor() {
        return cargosDoSetor = new CargosDoSetorDAO().pesquisaCargosDoSetorPorSetor(idSetor);
    }

    public void setCargosDoSetor(List<CargosDoSetor> cargosDoSetor) {
        this.cargosDoSetor = cargosDoSetor;
    }

    public String salvar() {
        FuncionariosDao fd = new FuncionariosDao();
        MailEnviaAuth enviaEmail = new MailEnviaAuth();
        Usuarios us = new Usuarios();
        String pass = new GeraSenha().geraSenha();
        String senhaCript = new GeraSenha().ecripta(pass);
        UsuariosDao ud = new UsuariosDao();
        FacesContext msg = FacesContext.getCurrentInstance();
        boolean verificaEnvio;
        try {
            Setores setor = new SetoresDAO().pesquisaPorId(idSetor);
            Cargos cargo = new CargosDAO().pesquisaPorId(idCargo);
            funcionario.setSetor(setor);
            funcionario.setCargo(cargo);
            if (funcionario.getIdFuncionarios() == null) {
                us.setSenha(senhaCript);
                us.setLogin(login);
                us.setAutorizacao(permissao);
                ud.salvar(us);
                funcionario.setUser(us);
                fd.salvar(funcionario);
                msg.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "O funcionário " + funcionario.getNome() + " foi inserido com sucesso.", null));
                verificaEnvio = enviaEmail.enviaCad(funcionario.getEmail(), "Acesso ao Sistema de Relatórios e Faltas", funcionario.getUser().getLogin(), pass);
                if (verificaEnvio) {
                    msg.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "E-mail enviado com sucesso.", null));
                } else {
                    msg.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Ocorreu um erro ao enviar o e-mail", null));
                }
            } else {
                Usuarios user = ud.pesquisaPorId(funcionario.getUser().getLogin());
                user.setAutorizacao(permissao);
                ud.alterar(user);
                fd.alterar(funcionario);
                msg.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "O funcionário " + funcionario.getNome() + " foi alterado com sucesso.", null));
                RequestContext.getCurrentInstance().execute("inserir.hide()");
            }
            limpar();
        } catch (SQLIntegrityConstraintViolationException ex) {
            msg.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            ex.getMessage(), null));
        } catch (Exception e) {
            e.printStackTrace();
            msg.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Ocorreu um erro ao inserir o funcionário " + funcionario.getNome(), null));
        }
        construct();
        return null;
    }

    public String excluir() {
        FuncionariosDao fd = new FuncionariosDao();
        UsuariosDao ud = new UsuariosDao();
        FacesContext msg = FacesContext.getCurrentInstance();
        try {
            Usuarios user = ud.pesquisaPorId(funcionario.getUser().getLogin());
            fd.excluir(funcionario);
            ud.excluir(user);
            msg.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "O funcionário " + funcionario.getNome() + " foi excluído com sucesso.", null));
            funcionario = new Funcionarios();
        } catch (Exception e) {
            e.printStackTrace();
            msg.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Ocorreu um erro ao excluir o funcionário " + funcionario.getNome(), null));
        }
        construct();
        return null;
    }

    public String limpar() {
        funcionario = new Funcionarios();
        login = " ";
        permissao = " ";
        idSetor = null;
        idCargo = null;
        cargosDoSetor = null;
        return null;
    }

    public String listaPermissao() {
        if (funcionario.getUser().getAutorizacao() == null) {
            return "Sem Nível de Permissão";
        }
        if (funcionario.getUser().getAutorizacao().equals("ROLE_GER")) {
            return "Administrador";
        } /*else if (funcionario.getUser().getAutorizacao().equals("ROLE_TEC")) {
            return "Técnico";
        }*/ else {
            return "Técnico";
        }
    }
}
