/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.bean;

import br.com.sescacre.sisrelat.dao.ClientesDao;
import br.com.sescacre.sisrelat.dao.ProgramaCorrenteDao;
import br.com.sescacre.sisrelat.entidades.Clientes;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;

/**
 *
 * @author Rennan Francisco
 */
@ManagedBean
@ViewScoped
public class ControleAcessoBean implements Serializable {

    private Clientes cliente = new Clientes();
    private String carteira;
    private List<String> atividades = new ArrayList<>();

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }

    public String getCarteira() {
        return carteira;
    }

    public void setCarteira(String carteira) {
        this.carteira = carteira;
    }

    public List<String> getAtividades() {
        return atividades;
    }

    public void setAtividades(List<String> atividades) {
        this.atividades = atividades;
    }

    public void pesquisaCliente() {
        ClientesDao clienteDao = new ClientesDao();
        FacesContext msg = FacesContext.getCurrentInstance();
        String nrviacart = "";
        String cduop = "";
        String sqmatric = "";
        String nudv = "";
        if (carteira.length() == 12) {
            nrviacart = carteira.substring(0, 1);
            cduop = carteira.substring(1, 5);
            sqmatric = carteira.substring(5, 11);
            nudv = carteira.substring(11);
        } else {
            cduop = carteira.substring(0, 4);
            sqmatric = carteira.substring(4, 10);
            nudv = carteira.substring(10);
        }
        cliente = clienteDao.pesquisaCliente(cduop, sqmatric, nudv);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        ServletContext context = (ServletContext) externalContext.getContext();
        String nomeArquivo = cliente.getMat() + ".jpg";
        String arquivo = context.getRealPath("/resources/imgs/tmp/" + nomeArquivo);
        criaArquivo(cliente.getFoto(), arquivo);
        atividades = new ProgramaCorrenteDao().buscaAtivadesIncritasCliente(sqmatric);
        carteira = null;
    }

    public void criaArquivo(byte[] bytes, String arquivo) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(arquivo);
            fos.write(bytes);
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ControleAcessoBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ControleAcessoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void limpa() {
        carteira = null;
        cliente = new Clientes();
    }
}
