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

    public boolean excluiLixoMes(Long atividade, Long configuracao, Long sequenciaOcorrcencia, Date inicio, Date fim) {
        Conexao con = new Conexao();
        try {
            Connection conn = con.abreConexao();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM cafaltas "
                    + "WHERE cdprograma = ? and cdconfig = ? and sqocorrenc = ? "
                    + "and dtaula BETWEEN ? and ?");
            ps.setLong(1, atividade);
            ps.setLong(2, configuracao);
            ps.setLong(3, sequenciaOcorrcencia);
            ps.setDate(4, new java.sql.Date(inicio.getTime()));
            ps.setDate(5, new java.sql.Date(fim.getTime()));
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao excluir o lixo da chamada: " + e.getMessage());
            return false;
        } finally {
            con.fechaConexao();
        }
    }

    public ResultSet contaAtendimentoMes(Connection conn, Long atividade, Long configuracao, Date inicio, Date fim) {
        ResultSet rs = null;
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT po.dsusuario, HOUR(c.hriniaula) AS Hora, count(*) AS Atendimentos FROM cafaltas c INNER JOIN progocorr po ON "
                    + "c.cdprograma = po.cdprograma AND c.cdconfig = po.cdconfig AND c.sqocorrenc = po.sqocorrenc "
                    + "AND c.cdprograma = ? AND c.vbfalta = 1 AND c.cdconfig = ? AND c.dtaula between ? AND ? INNER JOIN programas p ON c.cdprograma = p.cdprograma "
                    + "GROUP BY dsusuario, HOUR(c.hriniaula)");
            ps.setLong(1, atividade);
            ps.setLong(2, configuracao);
            ps.setDate(3, new java.sql.Date(inicio.getTime()));
            ps.setDate(4, new java.sql.Date(fim.getTime()));
            rs = ps.executeQuery();
        } catch (Exception e) {
            System.out.println("Erro ao contar as presencas: " + e.getMessage());
        }
        return rs;
    }
}
