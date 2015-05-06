/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sescacre.sisrelat.dao;

import br.com.sescacre.sisrelat.entidades.Chamada;
import br.com.sescacre.sisrelat.util.HibernateUtil;
import java.sql.SQLIntegrityConstraintViolationException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Rennan Francisco
 */
public class ChamadaDao {
    
    public void salvar(Chamada c) throws Exception {
        Session s = HibernateUtil.getSession();
        Transaction t = null;
        try {
            t = s.beginTransaction();
            s.save(c);
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new SQLIntegrityConstraintViolationException();
        } finally {
            s.close();
        }
    }
}
