/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.dao;

import br.com.sescacre.sisrelat.entidades.Setores;
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
public class SetoresDAO {

    public void salvar(Setores setor) throws Exception {
        Session s = HibernateUtil.getSession();
        try {
            Transaction t = s.beginTransaction();
            s.save(setor);
            t.commit();
        } catch (Exception ex) {
            throw new SQLIntegrityConstraintViolationException();
        } finally {
            s.close();
        }
    }

    public void alterar(Setores setor) throws Exception {
        Session s = HibernateUtil.getSession();
        try {
            Transaction t = s.beginTransaction();
            s.update(setor);
            t.commit();
        } catch (Exception e) {
            throw new SQLIntegrityConstraintViolationException();
        } finally {
            s.close();
        }
    }

    public void excluir(Setores setor) {
        Session s = HibernateUtil.getSession();
        Transaction t = s.beginTransaction();
        s.delete(setor);
        t.commit();
        s.close();
    }

    public List<Setores> ListaTodos() {
        Session s = HibernateUtil.getSession();
        Query q = s.createQuery("from Setores s order by s.sigla");
        List<Setores> lista = q.list();
        s.close();
        return lista;
    }

    public Setores pesquisaPorId(Integer id) {
        Session s = HibernateUtil.getSession();
        Setores setor = (Setores) s.load(Setores.class, id);
        s.close();
        return setor;
    }
}
