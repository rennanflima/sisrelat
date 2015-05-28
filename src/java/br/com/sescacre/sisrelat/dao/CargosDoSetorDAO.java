/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.dao;

import br.com.sescacre.sisrelat.entidades.CargosDoSetor;
import br.com.sescacre.sisrelat.util.HibernateUtil;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Rennan Francisco
 */
public class CargosDoSetorDAO {

    public void salvar(CargosDoSetor cds) {
        Session s = HibernateUtil.getSession();
        Transaction t = s.beginTransaction();
        s.merge(cds);
        t.commit();
        s.close();
    }

    public void alterar(CargosDoSetor cds) {
        Session s = HibernateUtil.getSession();
        Transaction t = s.beginTransaction();
        s.update(cds);
        t.commit();
        s.close();
    }

    public void excluir(CargosDoSetor cds) {
        Session s = HibernateUtil.getSession();
        Transaction t = s.beginTransaction();
        s.delete(cds);
        t.commit();
        s.close();
    }

    public List<CargosDoSetor> ListaTodos() {
        Session s = HibernateUtil.getSession();
        Query q = s.createQuery("from CargosDoSetor");
        List<CargosDoSetor> lista = q.list();
        s.close();
        return lista;
    }

    public CargosDoSetor pesquisaPorId(Integer id) {
        Session s = HibernateUtil.getSession();
        CargosDoSetor cds = (CargosDoSetor) s.load(CargosDoSetor.class, id);
        s.close();
        return cds;
    }

    public List<CargosDoSetor> pesquisaCargosDoSetorPorSetor(Integer id) {//join
        Session s = HibernateUtil.getSession();
        Query q = s.createQuery("from CargosDoSetor cds where cds.setor.id = :setor order by cds.cargo.nome");
        q.setParameter("setor", id);
        List<CargosDoSetor> lista = q.list();
        s.close();
        return lista;
    }
    
    public List<CargosDoSetor> pesquisaCargosDoSetorPorCargo(Integer id) {//join
        Session s = HibernateUtil.getSession();
        Query q = s.createQuery("from CargosDoSetor cds where cds.cargo.id = :cargo order by cds.id");
        q.setParameter("cargo", id);
        List<CargosDoSetor> lista = q.list();
        s.close();
        return lista;
    }
}
