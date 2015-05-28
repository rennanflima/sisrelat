/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.dao;

import br.com.sescacre.sisrelat.entidades.Cargos;
import br.com.sescacre.sisrelat.util.HibernateUtil;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Rennan Francisco
 */
public class CargosDAO {

    public void salvar(Cargos cargo) throws Exception {
        Session s = HibernateUtil.getSession();
        try {
            Transaction t = s.beginTransaction();
            s.save(cargo);
            t.commit();
        } catch (Exception ex) {
            throw new SQLIntegrityConstraintViolationException();
        } finally {
            s.close();
        }
    }

    public void alterar(Cargos cargo) throws Exception {
        Session s = HibernateUtil.getSession();
        try {
            Transaction t = s.beginTransaction();
            s.update(cargo);
            t.commit();
        } catch (Exception e) {
            throw new SQLIntegrityConstraintViolationException();
        } finally {
            s.close();
        }
    }

    public void excluir(Cargos cargo) {
        Session s = HibernateUtil.getSession();
        Transaction t = s.beginTransaction();
        s.delete(cargo);
        t.commit();
        s.close();
    }

    public List<Cargos> ListaTodos() {
        Session s = HibernateUtil.getSession();
        Query q = s.createQuery("from Cargos");
        List<Cargos> lista = q.list();
        s.close();
        return lista;
    }

    public Cargos pesquisaPorId(Integer id) {
        Session s = HibernateUtil.getSession();
        Cargos cargo = (Cargos) s.load(Cargos.class, id);
        s.close();
        return cargo;
    }
}
