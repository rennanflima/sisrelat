/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sescacre.sisrelat.dao;

import br.com.sescacre.sisrelat.util.Conexao;
import br.com.sescacre.sisrelat.entidades.ProgramaCorrente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rennan Francisco
 */
public class ProgramaCorrenteDao {
    
    public List<ProgramaCorrente> listaTurma(Long prog, Long conf) {
        Conexao con = new Conexao();
        List<ProgramaCorrente> lista = new ArrayList<ProgramaCorrente>();
        try {
            Connection conn = con.abreConexao();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT cdprograma, cdconfig, sqocorrenc, dsusuario FROM PROGOCORR WHERE cdprograma = ? AND cdconfig = ? ORDER BY dsusuario");
            ps.setLong(1, prog);
            ps.setLong(2, conf);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProgramaCorrente config = new ProgramaCorrente();
                config.setPrograma(rs.getLong("CDPROGRAMA"));
                config.setSequenciaOcorrencia(rs.getLong("SQOCORRENC"));
                config.setConfiguracaoPrograma(rs.getLong("CDCONFIG"));
                config.setDescricao(rs.getString("DSUSUARIO"));
                lista.add(config);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar a Turma: " + e.getMessage());
            return null;
        } finally {
            con.fechaConexao();
        }
        return lista;
    }
    
    public ProgramaCorrente pegaPorId(Long atividade, Long configuracao, Long sequenciaOcorrcencia) {
        Conexao con = new Conexao();
        ProgramaCorrente programaCorrente = new ProgramaCorrente();
        try {
            Connection conn = con.abreConexao();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT PO.CDPROGRAMA, PO.CDCONFIG, PO.SQOCORRENC, PO.DSUSUARIO, H.HRINICIO, H.HRFIM "
                    + "FROM PROGOCORR PO INNER JOIN HORARIOS H ON H.CDELEMENT = SUBSTR(CAST(100000000 + PO.CDPROGRAMA AS CHAR(9)), 2, 8) || "
                        + "SUBSTR(CAST(100000000 + PO.CDCONFIG AS CHAR(9)), 2, 8) || SUBSTR(CAST(100000000 + PO.SQOCORRENC AS CHAR(9)), 2, 8) "
                    + "WHERE PO.CDPROGRAMA = ? AND PO.CDCONFIG = ? AND PO.SQOCORRENC = ?");
            ps.setLong(1, atividade);
            ps.setLong(2, configuracao);
            ps.setLong(3, sequenciaOcorrcencia);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                programaCorrente.setPrograma(rs.getLong("CDPROGRAMA"));
                programaCorrente.setSequenciaOcorrencia(rs.getLong("SQOCORRENC"));
                programaCorrente.setConfiguracaoPrograma(rs.getLong("CDCONFIG"));
                programaCorrente.setDescricao(rs.getString("DSUSUARIO"));
                programaCorrente.setHoraInicio(rs.getTime("HRINICIO"));
                programaCorrente.setHoraFim(rs.getTime("HRFIM"));
            }
            return programaCorrente;
        } catch (Exception e) {
            System.out.println("Erro ao pesquisar pelo código a Atividade: "+e.getMessage());
            return null;
        }finally{
            con.fechaConexao();
        }
    }
}
