/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.dao;

import br.com.sescacre.sisrelat.entidades.PactoAcesso;
import br.com.sescacre.sisrelat.util.Conexao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rennan Francisco
 */
public class PactoAcessoDao {

    public ResultSet acessoPorPeriodo(Connection conn, Date dtInicio, Date dtTermino) {
        ResultSet rs = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT CF.MATFORMAT, "
                        + "C.NMCLIENTE, "
                        + "PA.TIPO, "
                        + "PA.DIRECAO, "
                        + "PA.DATAHORA "
                    + "FROM CLIENTELA C INNER JOIN PACTOACESSO PA ON C.CDUOP = PA.CDUOP AND C.SQMATRIC = PA.SQMATRIC "
                        + "AND PA.DATAHORA BETWEEN ? AND ?"
                    + "INNER JOIN CLIFORMAT CF ON CF.CDUOP = PA.CDUOP AND CF.SQMATRIC = PA.SQMATRIC ");
            ps.setDate(1, dtInicio);
            ps.setDate(2, dtTermino);
            rs = ps.executeQuery();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }

    public ResultSet acessoPorPeriodoETipo(Connection conn, String tipo, Date dtInicio, Date dtTermino) {
        ResultSet rs = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT 	CF.MATFORMAT, " +
                        "C.NMCLIENTE, " +
                        "PA.TIPO, " +
                        "PA.DIRECAO, " +
                        "PA.DATAHORA " +
                    "FROM CLIENTELA C INNER JOIN PACTOACESSO PA ON C.CDUOP = PA.CDUOP AND C.SQMATRIC = PA.SQMATRIC " +
                            "AND PA.TIPO = ? AND PA.DATAHORA BETWEEN ? AND ? " +
                    "INNER JOIN CLIFORMAT CF ON CF.CDUOP = PA.CDUOP AND CF.SQMATRIC = PA.SQMATRIC " +
                    "ORDER BY PA.DATAHORA");
            ps.setString(1, tipo);
            ps.setDate(2, dtInicio);
            ps.setDate(3, dtTermino);
            rs = ps.executeQuery();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }
    
    public List<PactoAcesso> acessoDoDia (LocalDate dia, java.util.Date hrinicio, java.util.Date hrfim) {
        Conexao con = new Conexao();
        List<PactoAcesso> lista = new ArrayList<PactoAcesso>();
        try {
            Connection conn = con.abreConexao();
            String sql = "SELECT DISTINCT(CF.MATFORMAT), "
                        + "C.NMCLIENTE, "
                        + "PA.TIPO, "
                        + "PA.DIRECAO, "
                        + "PA.DATAHORA "
                    + "FROM CLIENTELA C INNER JOIN PACTOACESSO PA ON C.CDUOP = PA.CDUOP AND C.SQMATRIC = PA.SQMATRIC "
                        + "AND DATE(PA.DATAHORA) = '" + dia + "' AND TIME(PA.DATAHORA) BETWEEN '" + hrinicio + "' AND '" + hrfim + "' "
                        + "AND PA.TIPO = 'CLIENTE' AND PA.DIRECAO = 'Entrada' "
                    + "INNER JOIN CLIFORMAT CF ON CF.CDUOP = PA.CDUOP AND CF.SQMATRIC = PA.SQMATRIC " + 
                    "ORDER BY PA.DATAHORA";
            PreparedStatement ps = conn.prepareStatement(sql);
            //ps.setDate(1, java.sql.Date.valueOf(dia));
            //ps.setDate(2, hrinicio);
            //ps.setDate(3, hrfim);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                PactoAcesso pacto = new PactoAcesso();
                pacto.setMatFormat(rs.getString("MATFORMAT"));
                pacto.setNmCliente(rs.getString("NMCLIENTE"));
                pacto.setTipo(rs.getString("TIPO"));
                pacto.setDirecao(rs.getString("DIRECAO"));
                pacto.setDataHora(rs.getTimestamp("DATAHORA"));
                lista.add(pacto);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar o Acesso da Pacto: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            con.fechaConexao();
        }
        return lista;
    }
    
    public List<PactoAcesso> acessoDoDiaAluno (LocalDate dia, java.util.Date hrinicio, java.util.Date hrfim, Long sqmatric) {
        Conexao con = new Conexao();
        List<PactoAcesso> lista = new ArrayList<PactoAcesso>();
        try {
            Connection conn = con.abreConexao();
            String sql = "SELECT CF.MATFORMAT, "
                        + "C.NMCLIENTE, "
                        + "PA.TIPO, "
                        + "PA.DIRECAO, "
                        + "PA.DATAHORA "
                    + "FROM CLIENTELA C INNER JOIN PACTOACESSO PA ON C.CDUOP = PA.CDUOP AND C.SQMATRIC = PA.SQMATRIC "
                        + "AND DATE(PA.DATAHORA) = '" + dia + "' AND TIME(PA.DATAHORA) BETWEEN '" + hrinicio + "' AND '" + hrfim + "' "
                        + "AND PA.TIPO = 'CLIENTE' AND PA.SQMATRIC = ? "
                    + "INNER JOIN CLIFORMAT CF ON CF.CDUOP = PA.CDUOP AND CF.SQMATRIC = PA.SQMATRIC " + 
                    "ORDER BY PA.DATAHORA";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, sqmatric);
            //ps.setDate(1, java.sql.Date.valueOf(dia));
            //ps.setDate(2, hrinicio);
            //ps.setDate(3, hrfim);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                PactoAcesso pacto = new PactoAcesso();
                pacto.setMatFormat(rs.getString("MATFORMAT"));
                pacto.setNmCliente(rs.getString("NMCLIENTE"));
                pacto.setTipo(rs.getString("TIPO"));
                pacto.setDirecao(rs.getString("DIRECAO"));
                pacto.setDataHora(rs.getTimestamp("DATAHORA"));
                lista.add(pacto);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar o Acesso da Pacto: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            con.fechaConexao();
        }
        return lista;
    }
}
