/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
    
    public ResultSet acessoDoDia (Connection conn, Date data) {
        ResultSet rs = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT CF.MATFORMAT, "
                        + "C.NMCLIENTE, "
                        + "PA.TIPO, "
                        + "PA.DIRECAO, "
                        + "PA.DATAHORA "
                    + "FROM CLIENTELA C INNER JOIN PACTOACESSO PA ON C.CDUOP = PA.CDUOP AND C.SQMATRIC = PA.SQMATRIC "
                        + "AND PA.DATAHORA = ?"
                    + "INNER JOIN CLIFORMAT CF ON CF.CDUOP = PA.CDUOP AND CF.SQMATRIC = PA.SQMATRIC " + 
                    "ORDER BY PA.DATAHORA");
            ps.setDate(1, data);
            rs = ps.executeQuery();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }
}
