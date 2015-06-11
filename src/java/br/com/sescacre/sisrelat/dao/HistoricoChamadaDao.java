/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sescacre.sisrelat.dao;

import br.com.sescacre.sisrelat.entidades.HistoricoChamada;
import br.com.sescacre.sisrelat.util.HibernateUtil;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Rennan Francisco
 */
public class HistoricoChamadaDao {
    
    public void salvar(HistoricoChamada hc) throws Exception {
        Session s = HibernateUtil.getSession();
        try {
            Transaction t = s.beginTransaction();
            s.save(hc);
            t.commit();
        } catch (Exception ex) {
            throw new SQLIntegrityConstraintViolationException();
        } finally {
            s.close();
        }
    }

    public void alterar(HistoricoChamada hc) throws Exception {
        Session s = HibernateUtil.getSession();
        try {
            Transaction t = s.beginTransaction();
            s.update(hc);
            t.commit();
        } catch (Exception e) {
            throw new SQLIntegrityConstraintViolationException();
        } finally {
            s.close();
        }
    }

    public void excluir(HistoricoChamada hc) {
        Session s = HibernateUtil.getSession();
        Transaction t = s.beginTransaction();
        s.delete(hc);
        t.commit();
        s.close();
    }

    public List<HistoricoChamada> ListaTodos() {
        Session s = HibernateUtil.getSession();
        Query q = s.createQuery("from HistoricoChamada");
        List<HistoricoChamada> lista = q.list();
        s.close();
        return lista;
    }

    
    public boolean pesquisaChamada(Long programa, Long config, Long ocorrencia, Date mes){
        Session s = HibernateUtil.getSession();
        Query q = s.createQuery("from HistoricoChamada hc where hc.cdprograma = :programa "
                + "and hc.cdconfig = :config and hc.sqocorrenc = :ocorrencia and hc.mes = :mes");
        q.setParameter("programa", programa);
        q.setParameter("config", config);
        q.setParameter("ocorrencia", ocorrencia);
        q.setParameter("mes", mes);
        
        return q.uniqueResult() != null;
        
    }
}
