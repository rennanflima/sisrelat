/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.dao;

import br.com.sescacre.sisrelat.entidades.Chamada;
import br.com.sescacre.sisrelat.util.Conexao;
import br.com.sescacre.sisrelat.util.HibernateUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;

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

    public Long contaPresencaDia(Long atividade, Long configuracao, Long sequenciaOcorrcencia, boolean vbfalta) {
        //Conexao con = new Conexao();
        //Long qtd = 0L;
        Session s = HibernateUtil.getSession();
        Query q = s.createQuery("select count(*) from Chamada c where c.cdprograma = :programa and c.cdconfig = :conf and c.sqocorrenc = :oco and c.vbfalta = :falta");
        q.setParameter("programa", atividade);
        q.setParameter("conf", configuracao);
        q.setParameter("oco", sequenciaOcorrcencia);
        q.setParameter("falta", vbfalta);
        return (Long) q.uniqueResult();
        /*try {
            Connection conn = con.abreConexao();
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) as quantidade "
                    + "FROM cafaltas WHERE cdprograma = ? AND cdconfig = ? AND "
                    + "sqocorrenc = ? AND vbfalta = ?");
            ps.setLong(1, atividade);
            ps.setLong(2, configuracao);
            ps.setLong(3, sequenciaOcorrcencia);
            ps.setBoolean(4, vbfalta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                qtd = rs.getLong("quantidade");
            }
            return qtd;
        } catch (Exception e) {
            System.out.println("Erro ao pesquisar pelo código a Atividade: " + e.getMessage());
            return qtd;
        } finally {
            con.fechaConexao();
        }*/
    }
}
