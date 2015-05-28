/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sescacre.sisrelat.dao;

import br.com.sescacre.sisrelat.entidades.Funcionarios;
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
public class FuncionariosDao {

    public void salvar(Funcionarios user) throws Exception {
        Session s = HibernateUtil.getSession();
        try {
            Transaction t = s.beginTransaction();
            s.save(user);
            t.commit();
        } catch (Exception ex) {
            throw new SQLIntegrityConstraintViolationException("Já existe um Funcionário cadastrado com essa matrícula!");
        } finally {
            s.close();
        }
    }

    public void excluir(Funcionarios user) {
        Session s = HibernateUtil.getSession();
        Transaction t = s.beginTransaction();
        s.delete(user);
        t.commit();
        s.close();

    }

    public void alterar(Funcionarios user) throws Exception {
        Session s = HibernateUtil.getSession();
        try {
            Transaction t = s.beginTransaction();
            s.update(user);
            t.commit();
        } catch (Exception ex) {
            throw new SQLIntegrityConstraintViolationException("Já existe um Funcionário cadastrado com essa matrícula!");
        } finally {
            s.close();
        }

    }

    public List<Funcionarios> ListaTodos() {
        Session s = HibernateUtil.getSession();
        Query q = s.createQuery("from Funcionarios");
        List<Funcionarios> lista = q.list();
        s.close();
        return lista;
    }

    public Funcionarios pesquisaPorId(Integer id) {
        Session s = HibernateUtil.getSession();
        return (Funcionarios) s.load(Funcionarios.class, id);
    }
    
    public Funcionarios pesquisaPorMat(Integer mat){
        Session s = HibernateUtil.getSession();
        Query q = s.createQuery("from Funcionarios f where f.matricula = :mat");
        q.setParameter("mat", mat);
        return (Funcionarios) q.uniqueResult();
    }
    
    public List<Funcionarios> pesquisaFuncionarioPorCargoDoSetor(Integer id) {//join
        Session s = HibernateUtil.getSession();
        Query q = s.createQuery("from Funcionarios f where f.cargo.id = :cargo order by f.id");
        q.setParameter("cargo", id);
        List<Funcionarios> lista = q.list();
        s.close();
        return lista;
    }
}

