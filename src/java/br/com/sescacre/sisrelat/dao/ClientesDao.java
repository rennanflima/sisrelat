/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.dao;

import br.com.sescacre.sisrelat.entidades.Clientes;
import br.com.sescacre.sisrelat.util.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Rennan Francisco
 */
public class ClientesDao {

    public Clientes pesquisaCliente(String cduop, String sqmatric, String nudv) {
        Conexao con = new Conexao();
        Clientes cliente = new Clientes();
        try {
            Connection conn = con.abreConexao();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT CF.MATFORMAT, "
                        + "C.FOTO, "
                        + "C.SQMATRIC, "
                        + "C.NMCLIENTE, "
                        + "C.CDCATEGORI, "
                        + "C.DTVENCTO, "
                        + "C.STMATRIC "
                    + "FROM CLIENTELA C "
                    + "LEFT JOIN CLISERV ST ON ST.SQMATRIC = C.SQMATRIC AND ST.CDUOP = C.CDUOP "
                    + "LEFT JOIN CLIFORMAT CF ON CF.CDUOP = C.CDUOP AND CF.SQMATRIC = C.SQMATRIC "
                    + "LEFT JOIN SERVIDOR S ON ST.NUMATRIC = S.NUMATRIC "
                    + "WHERE C.CDUOP = ? AND C.SQMATRIC = ?");
            ps.setString(1, cduop);
            ps.setString(2, sqmatric);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cliente.setFoto(rs.getBytes("FOTO"));
                cliente.setMat(rs.getString("SQMATRIC"));
                cliente.setCarteira(rs.getString("MATFORMAT"));
                cliente.setNome(rs.getString("NMCLIENTE"));
                cliente.setDataVencimento(rs.getDate("DTVENCTO"));
                cliente.setCategoria(rs.getInt("CDCATEGORI"));
                cliente.setStatusCarteira(rs.getInt("STMATRIC"));
            }
            return cliente;
        } catch (Exception e) {
            System.out.println("Erro ao consultar: " + e.getMessage());
            return null;
        } finally {
            con.fechaConexao();
        }
    }
}
