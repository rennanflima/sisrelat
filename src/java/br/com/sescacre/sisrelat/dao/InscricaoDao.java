/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sescacre.sisrelat.dao;

import br.com.sescacre.sisrelat.entidades.Inscritos;
import br.com.sescacre.sisrelat.util.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rennan Francisco
 */
public class InscricaoDao {
    
    public ResultSet inscritosTurmaResultSet(Connection conn, Long atividade, Long configuracao, Long sequenciaOcorrcencia) {
        ResultSet rs = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT UP.NMUOP, " +
                        "CF.MATFORMAT, " +
                        "C.NUDV, " +
                        "CAT.CDIMPRESS, " +
                        "C.NMCLIENTE, " +
                        "PO.DSUSUARIO, " +
                        "I.DTINSCRI " +
                    "FROM CLIENTELA C INNER JOIN INSCRICAO I ON C.SQMATRIC = I.SQMATRIC AND C.CDUOP = I.CDUOP AND I.STINSCRI = 0 " +
                    "INNER JOIN CLIFORMAT CF ON CF.CDUOP = C.CDUOP AND CF.SQMATRIC = C.SQMATRIC " +
                    "INNER JOIN CATEGORIA CAT ON CAT.CDCATEGORI = C.CDCATEGORI " +
                    "INNER JOIN PROGOCORR PO ON PO.CDPROGRAMA = I.CDPROGRAMA " +
                        "AND PO.CDCONFIG = I.CDCONFIG AND PO.SQOCORRENC = I.SQOCORRENC " +
                        "AND PO.CDPROGRAMA = ? AND PO.CDCONFIG = ? AND PO.SQOCORRENC = ? " +
                        //"AND PO.AAMODA = '2015' " +
                    "INNER JOIN UOP UP ON UP.CDUOP = PO.CDUOPCAD " +
                    "INNER JOIN CONFPROG CP ON CP.CDCONFIG = PO.CDCONFIG AND CP.CDPROGRAMA = PO.CDPROGRAMA " +
                    "ORDER BY C.NMCLIENTE");
            ps.setLong(1, atividade);
            ps.setLong(2, configuracao);
            ps.setLong(3, sequenciaOcorrcencia);
            rs = ps.executeQuery();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }
    
    public List<Inscritos> inscritosTurma(Long atividade, Long configuracao, Long sequenciaOcorrcencia) {
        Conexao con = new Conexao();
        List<Inscritos> lista = new ArrayList<Inscritos>();
        try {
            Connection conn = con.abreConexao();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT CF.MATFORMAT," + 
                        "C.SQMATRIC, " +
                        "C.CDUOP, " +
                        "PO.CDPROGRAMA, " + 
                        "PO.CDCONFIG, " +
                        "PO.SQOCORRENC, " +
                        "C.NMCLIENTE, " +
                        "CAT.CDIMPRESS " +
                    "FROM CLIENTELA C INNER JOIN INSCRICAO I ON C.SQMATRIC = I.SQMATRIC AND C.CDUOP = I.CDUOP AND I.STINSCRI = 0 " +
                    "INNER JOIN CLIFORMAT CF ON CF.CDUOP = C.CDUOP AND CF.SQMATRIC = C.SQMATRIC " +
                    "INNER JOIN CATEGORIA CAT ON CAT.CDCATEGORI = C.CDCATEGORI " +
                    "INNER JOIN PROGOCORR PO ON PO.CDPROGRAMA = I.CDPROGRAMA " +
                        "AND PO.CDCONFIG = I.CDCONFIG AND PO.SQOCORRENC = I.SQOCORRENC " +
                        "AND PO.CDPROGRAMA = ? AND PO.CDCONFIG = ? AND PO.SQOCORRENC = ? " +
                        //"AND PO.AAMODA = '2015' " +
                    "INNER JOIN UOP UP ON UP.CDUOP = PO.CDUOPCAD " +
                    "INNER JOIN CONFPROG CP ON CP.CDCONFIG = PO.CDCONFIG AND CP.CDPROGRAMA = PO.CDPROGRAMA " +
                    "ORDER BY C.NMCLIENTE");
            ps.setLong(1, atividade);
            ps.setLong(2, configuracao);
            ps.setLong(3, sequenciaOcorrcencia);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Inscritos inscrito = new Inscritos();
                inscrito.setMatFormat(rs.getString("MATFORMAT"));
                inscrito.setSqMatric(rs.getLong("SQMATRIC"));
                inscrito.setCdUop(rs.getLong("CDUOP"));
                inscrito.setCdPrograma(rs.getLong("CDPROGRAMA"));
                inscrito.setCdConfig(rs.getLong("CDCONFIG"));
                inscrito.setSqOcorrenc(rs.getLong("SQOCORRENC"));
                inscrito.setNmCliente(rs.getString("NMCLIENTE"));
                inscrito.setCdImpress(rs.getString("CDIMPRESS"));
                lista.add(inscrito);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar os inscritos da Turma: " + e.getMessage());
            return null;
        } finally {
            con.fechaConexao();
        }
        return lista;
    }
}
