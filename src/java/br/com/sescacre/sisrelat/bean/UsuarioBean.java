/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.bean;

import br.com.sescacre.sisrelat.dao.UsuariosDao;
import br.com.sescacre.sisrelat.entidades.Usuarios;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author Rennan Francisco
 */
@ManagedBean
@SessionScoped
public class UsuarioBean implements Serializable {

    private Usuarios usuario;
    private Integer unidade;

    public UsuarioBean() {
        usuario = new Usuarios();
        SecurityContext context = SecurityContextHolder.getContext();
        if (context instanceof SecurityContext) {
            Authentication authentication = context.getAuthentication();
            if (authentication instanceof Authentication) {
                usuario.setLogin(((User) authentication.getPrincipal()).getUsername());
            }
        }
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public String redirecionaUser() {
        FacesContext msg = FacesContext.getCurrentInstance();
        Usuarios us = new UsuariosDao().pesquisaPorId(usuario.getLogin());
        try {
            if (us.getAutorizacao().equals("ROLE_GER")) {
                msg.getExternalContext().redirect("/sisrelat/admin");
            } else if (us.getAutorizacao().equals("ROLE_TEC")) {
                msg.getExternalContext().redirect("/sisrelat/tecnico");
            }
        } catch (Exception e) {
            msg.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Ocorreu um erro ao redirecionar o usuário", null));
        }
        return null;
    }

}
