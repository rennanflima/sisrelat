/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.dao;

import br.com.sescacre.sisrelat.entidades.Chamada;
import br.com.sescacre.sisrelat.util.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;

/**
 *
 * @author Rennan Francisco
 */
public class ChamadaDao {

    public void salvar(Chamada c) throws Exception {
        Conexao con = new Conexao();
        try {
            Connection conn = con.abreConexao();
            PreparedStatement ps = conn.prepareStatement("insert into cafaltas(sqmatric, cduop, cdprograma, cdconfig, sqocorrenc, dtaula, hriniaula, vbfalta, lgatu, dtatu, hratu) "
                    + "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setLong(1, c.getSqmatric());
            ps.setLong(2, c.getCduop());
            ps.setLong(3, c.getCdprograma());
            ps.setLong(4, c.getCdconfig());
            ps.setLong(5, c.getSqocorrenc());
            ps.setDate(6, new java.sql.Date(c.getDtaula().getTime()));
            ps.setTime(7, new java.sql.Time(c.getHriniaula().getTime()));
            ps.setBoolean(8, c.isVbfalta());
            ps.setString(9, c.getLgatu());
            ps.setDate(10, new java.sql.Date(c.getDtatu().getTime()));
            ps.setTime(11, new java.sql.Time(c.getHratu().getTime()));
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar. " + e.getMessage());
            throw new SQLIntegrityConstraintViolationException();
        } finally {
            con.fechaConexao();
        }

        /*Session s = HibernateUtil.getSession();
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
         }*/
    }

    public Long contaPresencaDia(Long atividade, Long configuracao, Long sequenciaOcorrcencia, int vbfalta, Date inicio, Date fim) {
        Conexao con = new Conexao();
        Long qtd = 0L;
        /*Session s = HibernateUtil.getSession();
         Query q = s.createQuery("select count(*) from Chamada c where c.cdprograma = :programa and c.cdconfig = :conf and c.sqocorrenc = :oco and c.vbfalta = :falta and c.dtaula BETWEEN :inicio and :fim");
         q.setParameter("programa", atividade);
         q.setParameter("conf", configuracao);
         q.setParameter("oco", sequenciaOcorrcencia);
         q.setParameter("falta", vbfalta);
         q.setParameter("inicio", inicio);
         q.setParameter("fim", fim);
         return (Long) q.uniqueResult();*/
        try {
            Connection conn = con.abreConexao();
            PreparedStatement ps = conn.prepareStatement("SELECT count(*) as qtd FROM cafaltas "
                    + "WHERE cdprograma = ? and cdconfig = ? and sqocorrenc = ? "
                    + "and vbfalta = ? and dtaula BETWEEN ? and ?");
            ps.setLong(1, atividade);
            ps.setLong(2, configuracao);
            ps.setLong(3, sequenciaOcorrcencia);
            ps.setInt(4, vbfalta);
            ps.setDate(5, new java.sql.Date(inicio.getTime()));
            ps.setDate(6, new java.sql.Date(fim.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                qtd = rs.getLong("qtd");
            }
            return qtd;
        } catch (Exception e) {
            System.out.println("Erro ao contar as presencas: " + e.getMessage());
            return qtd;
        } finally {
            con.fechaConexao();
        }
    }
}
